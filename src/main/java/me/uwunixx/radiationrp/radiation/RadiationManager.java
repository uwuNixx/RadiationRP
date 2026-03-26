package me.uwunixx.radiationrp.radiation;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

public class RadiationManager {

    private final RadiationRP plugin;

    // Заражение по чанкам: chunkKey -> (Location -> RadiationBlock)
    private final Map<Long, Map<Location, RadiationBlock>> infectedChunks = new HashMap<>();
    private final Map<Integer, RadiationZone> zones = new HashMap<>();
    private int nextId = 1;

    private BukkitTask damageTask;
    private BukkitTask spreadTask;
    private BukkitTask visualTask;

    public RadiationManager(RadiationRP plugin) {
        this.plugin = plugin;
    }

    // ------------------ ЧАНКИ (Оптимизация) ------------------

    private long getChunkKey(Location loc) {
        return (((long) (loc.getBlockX() >> 4)) << 32) ^ ((loc.getBlockZ() >> 4) & 0xffffffffL);
    }

    // Вспомогательный метод для получения мапы чанка без создания лишних объектов
    private Map<Location, RadiationBlock> getChunkMap(Location loc) {
        return infectedChunks.computeIfAbsent(getChunkKey(loc), k -> new HashMap<>());
    }

    // ------------------ ЗАРАЖЕНИЕ ------------------

    public void infect(Location loc, int zoneId, int power, boolean core) {
        // Всегда сохраняем "чистую" локацию блока без дробных чисел
        Location blockLoc = loc.getBlock().getLocation();
        Map<Location, RadiationBlock> chunk = getChunkMap(blockLoc);
        chunk.put(blockLoc, new RadiationBlock(blockLoc, zoneId, power, core));
    }

    public void infect(Location loc, int zoneId, int power) {
        infect(loc, zoneId, power, false);
    }

    public RadiationBlock getBlock(Location loc) {
        return getChunkMap(loc).get(loc.getBlock().getLocation());
    }

    public void removeInfectedAt(Location loc) {
        getChunkMap(loc).remove(loc.getBlock().getLocation());
    }

    public Collection<RadiationBlock> getInfected() {
        List<RadiationBlock> all = new ArrayList<>();
        for (Map<Location, RadiationBlock> map : infectedChunks.values()) {
            all.addAll(map.values());
        }
        return all;
    }

    public Collection<Map<Location, RadiationBlock>> getChunkMaps() {
        return infectedChunks.values();
    }

    // ------------------ РАДИАЦИЯ В ТОЧКЕ (ИСПРАВЛЕНО) ------------------

    /**
     * Теперь метод проверяет только те блоки, в которых физически стоит игрок.
     * Это убирает "прострелы" радиации сквозь стены.
     */
    public int getRadiationAround(Location loc) {
        // Проверяем блок на уровне ног
        RadiationBlock legs = getBlock(loc);
        int powerLegs = (legs != null) ? legs.getPower() : 0;

        // Проверяем блок на уровне головы (на случай если радиация сверху)
        RadiationBlock head = getBlock(loc.clone().add(0, 1, 0));
        int powerHead = (head != null) ? head.getPower() : 0;

        // Возвращаем максимальное значение из двух точек
        return Math.max(powerLegs, powerHead);
    }

    // ------------------ ОСТАЛЬНАЯ ЛОГИКА (БЕЗ ИЗМЕНЕНИЙ) ------------------

    public RadiationZone createZone(Location center, double radius, int power) {
        RadiationZone zone = new RadiationZone(nextId++, center.getBlock().getLocation(), radius, power);
        zones.put(zone.getId(), zone);
        infect(center, zone.getId(), power, true);
        return zone;
    }

    public RadiationZone getZone(int id) { return zones.get(id); }

    public void removeZone(int id) {
        zones.remove(id);
        for (Map<Location, RadiationBlock> map : infectedChunks.values()) {
            map.values().removeIf(b -> b.getZoneId() == id);
        }
    }

    public void clearAllZones() {
        zones.clear();
        infectedChunks.clear();
    }

    public void startTasks() {
        int interval = plugin.getConfig().getInt("spread-interval", 60);
        damageTask = new RadiationDamageTask(plugin).runTaskTimer(plugin, 20, 20);
        spreadTask = new RadiationSpreadTask(plugin).runTaskTimer(plugin, interval, interval);
        visualTask = new RadiationVisualTask(plugin).runTaskTimer(plugin, 10, 10);
    }

    public void stopTasks() {
        if (damageTask != null) damageTask.cancel();
        if (spreadTask != null) spreadTask.cancel();
        if (visualTask != null) visualTask.cancel();
    }

    public void saveData() { saveZones(); saveInfected(); }
    public void loadData() { loadZones(); loadInfected(); }

    private void saveZones() {
        File file = new File(plugin.getDataFolder(), "zones.yml");
        YamlConfiguration cfg = new YamlConfiguration();
        for (RadiationZone zone : zones.values()) {
            String key = "zones." + zone.getId();
            cfg.set(key + ".world", zone.getCenter().getWorld().getName());
            cfg.set(key + ".x", zone.getCenter().getX());
            cfg.set(key + ".y", zone.getCenter().getY());
            cfg.set(key + ".z", zone.getCenter().getZ());
            cfg.set(key + ".radius", zone.getMaxRadius());
            cfg.set(key + ".power", zone.getPower());
        }
        try { cfg.save(file); } catch (Exception ignored) {}
    }

    private void loadZones() {
        File file = new File(plugin.getDataFolder(), "zones.yml");
        if (!file.exists()) return;
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (!cfg.contains("zones")) return;
        for (String idStr : cfg.getConfigurationSection("zones").getKeys(false)) {
            int id = Integer.parseInt(idStr);
            String path = "zones." + idStr;
            Location loc = new Location(
                    plugin.getServer().getWorld(cfg.getString(path + ".world")),
                    cfg.getDouble(path + ".x"), cfg.getDouble(path + ".y"), cfg.getDouble(path + ".z")
            );
            zones.put(id, new RadiationZone(id, loc, cfg.getDouble(path + ".radius"), cfg.getInt(path + ".power")));
            nextId = Math.max(nextId, id + 1);
        }
    }

    private void saveInfected() {
        File file = new File(plugin.getDataFolder(), "infected.yml");
        YamlConfiguration cfg = new YamlConfiguration();
        int i = 0;
        for (RadiationBlock block : getInfected()) {
            String key = "blocks." + i++;
            cfg.set(key + ".world", block.getLocation().getWorld().getName());
            cfg.set(key + ".x", block.getLocation().getX());
            cfg.set(key + ".y", block.getLocation().getY());
            cfg.set(key + ".z", block.getLocation().getZ());
            cfg.set(key + ".zone", block.getZoneId());
            cfg.set(key + ".power", block.getPower());
            cfg.set(key + ".core", block.isCore());
        }
        try { cfg.save(file); } catch (Exception ignored) {}
    }

    private void loadInfected() {
        File file = new File(plugin.getDataFolder(), "infected.yml");
        if (!file.exists()) return;
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (!cfg.contains("blocks")) return;
        for (String key : cfg.getConfigurationSection("blocks").getKeys(false)) {
            String path = "blocks." + key;
            Location loc = new Location(
                    plugin.getServer().getWorld(cfg.getString(path + ".world")),
                    cfg.getDouble(path + ".x"), cfg.getDouble(path + ".y"), cfg.getDouble(path + ".z")
            );
            infect(loc, cfg.getInt(path + ".zone"), cfg.getInt(path + ".power"), cfg.getBoolean(path + ".core"));
        }
    }
}