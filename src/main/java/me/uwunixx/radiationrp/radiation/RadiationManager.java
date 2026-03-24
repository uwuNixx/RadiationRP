package me.uwunixx.radiationrp.radiation;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

public class RadiationManager {

    private final RadiationRP plugin;

    private final Map<Integer, RadiationZone> zones = new HashMap<>();
    private final Map<Location, RadiationBlock> infected = new HashMap<>();

    private int nextId = 1;

    private BukkitTask damageTask;
    private BukkitTask spreadTask;

    public RadiationManager(RadiationRP plugin) {
        this.plugin = plugin;
    }

    // ------------------ ЗОНЫ ------------------

    public RadiationZone createZone(Location center, double radius, int power) {
        RadiationZone zone = new RadiationZone(nextId++, center, radius, power);
        zones.put(zone.getId(), zone);

        infect(center, zone.getId(), power);
        return zone;
    }

    public RadiationZone getZone(int id) {
        return zones.get(id);
    }

    public Collection<RadiationZone> getZones() {
        return zones.values();
    }

    public void removeZone(int id) {
        RadiationZone zone = zones.remove(id);
        if (zone == null) return;

        // Удаляем заражённые блоки этой зоны
        infected.entrySet().removeIf(e -> e.getValue().getZoneId() == id);
    }

    public void clearAllZones() {
        zones.clear();
        infected.clear();
    }


    // ------------------ ЗАРАЖЕНИЕ ------------------

    public void infect(Location loc, int zoneId, int power) {
        infected.put(loc, new RadiationBlock(loc, zoneId, power));
    }

    public boolean isInfected(Location loc) {
        return infected.containsKey(loc);
    }

    public Collection<RadiationBlock> getInfected() {
        return infected.values();
    }

    // ------------------ РАДИАЦИЯ ВОКРУГ ------------------

    public int getRadiationAround(Location loc) {
        int max = 0;

        for (RadiationBlock block : infected.values()) {
            if (!block.getLocation().getWorld().equals(loc.getWorld())) continue;

            double dist = block.getLocation().distance(loc);
            if (dist <= 3) {
                max = Math.max(max, block.getPower());
            }
        }

        return max;
    }

    // ------------------ ТАСКИ ------------------

    public void startTasks() {
        int interval = plugin.getConfig().getInt("spread-interval", 40);

        damageTask = new RadiationDamageTask(plugin).runTaskTimer(plugin, 20, 20);
        spreadTask = new RadiationSpreadTask(plugin).runTaskTimer(plugin, interval, interval);
    }

    public void stopTasks() {
        if (damageTask != null) damageTask.cancel();
        if (spreadTask != null) spreadTask.cancel();
    }

    // ------------------ СОХРАНЕНИЕ ------------------

    public void saveData() {
        saveZones();
        saveInfected();
    }

    public void loadData() {
        loadZones();
        loadInfected();
    }

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

            String world = cfg.getString(path + ".world");
            double x = cfg.getDouble(path + ".x");
            double y = cfg.getDouble(path + ".y");
            double z = cfg.getDouble(path + ".z");
            double radius = cfg.getDouble(path + ".radius");
            int power = cfg.getInt(path + ".power");

            Location loc = new Location(plugin.getServer().getWorld(world), x, y, z);

            RadiationZone zone = new RadiationZone(id, loc, radius, power);
            zones.put(id, zone);

            nextId = Math.max(nextId, id + 1);
        }
    }

    private void saveInfected() {
        File file = new File(plugin.getDataFolder(), "infected.yml");
        YamlConfiguration cfg = new YamlConfiguration();

        int i = 0;
        for (RadiationBlock block : infected.values()) {
            String key = "blocks." + i++;

            cfg.set(key + ".world", block.getLocation().getWorld().getName());
            cfg.set(key + ".x", block.getLocation().getX());
            cfg.set(key + ".y", block.getLocation().getY());
            cfg.set(key + ".z", block.getLocation().getZ());
            cfg.set(key + ".zone", block.getZoneId());
            cfg.set(key + ".power", block.getPower());
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

            String world = cfg.getString(path + ".world");
            double x = cfg.getDouble(path + ".x");
            double y = cfg.getDouble(path + ".y");
            double z = cfg.getDouble(path + ".z");
            int zone = cfg.getInt(path + ".zone");
            int power = cfg.getInt(path + ".power");

            Location loc = new Location(plugin.getServer().getWorld(world), x, y, z);

            infected.put(loc, new RadiationBlock(loc, zone, power));
        }
    }
}
