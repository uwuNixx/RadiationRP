package me.uwunixx.radiationrp.radiation;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class RadiationSpreadTask extends BukkitRunnable {

    private final RadiationRP plugin;

    public RadiationSpreadTask(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // 1. Загружаем изоляторы из конфига (железо, обсидиан и т.д.)
        Set<Material> insulators = plugin.getConfig().getStringList("blocked-blocks").stream()
                .map(name -> {
                    try { return Material.valueOf(name.toUpperCase()); }
                    catch (Exception e) { return null; }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 2. Итерируемся по копии данных
        for (Map<Location, RadiationBlock> chunkMap : new ArrayList<>(plugin.getRadiationManager().getChunkMaps())) {
            for (RadiationBlock radBlock : new ArrayList<>(chunkMap.values())) {

                // ПРИНУДИТЕЛЬНОЕ ВЫРАВНИВАНИЕ: берем ровные координаты текущего блока
                Location currentLoc = radBlock.getLocation().getBlock().getLocation();

                // Защита от бага: если источник вдруг оказался внутри железа, он не фонит
                if (insulators.contains(currentLoc.getBlock().getType())) continue;

                RadiationZone zone = plugin.getRadiationManager().getZone(radBlock.getZoneId());
                if (zone == null) continue;

                for (Location neighbor : getNeighbors(currentLoc)) {

                    // Проверка загрузки чанка соседа (чтобы не было ошибок)
                    if (!neighbor.getWorld().isChunkLoaded(neighbor.getBlockX() >> 4, neighbor.getBlockZ() >> 4)) continue;

                    // Если сосед уже заражен — пропускаем
                    if (plugin.getRadiationManager().getBlock(neighbor) != null) continue;

                    Material targetMat = neighbor.getBlock().getType();

                    // ГЛАВНАЯ ПРОВЕРКА: Если сосед — железо/обсидиан, мы его НЕ заражаем
                    // Цикл просто идет к следующему соседу (continue), не прерывая общее распространение
                    if (insulators.contains(targetMat)) continue;

                    // Проверка максимального радиуса зоны
                    if (neighbor.distance(zone.getCenter()) > zone.getMaxRadius()) continue;

                    // ЗАРАЖАЕМ (Оригинальная логика силы радиации возвращена!)
                    plugin.getRadiationManager().infect(
                            neighbor, // Ровные координаты от getNeighbors
                            radBlock.getZoneId(),
                            Math.max(1, radBlock.getPower() - 1), // Всегда минимум 1, чтобы дошло до края радиуса
                            false
                    );
                }

                // Оригинальная логика затухания
                if (!radBlock.isCore() && radBlock.getPower() > zone.getPower()) {
                    radBlock.weaken(1);
                }
            }
        }
    }

    private List<Location> getNeighbors(Location loc) {
        // Мы используем BlockX/Y/Z, чтобы гарантировать отсутствие дробных чисел типа 0.5
        return Arrays.asList(
                new Location(loc.getWorld(), loc.getBlockX() + 1, loc.getBlockY(), loc.getBlockZ()),
                new Location(loc.getWorld(), loc.getBlockX() - 1, loc.getBlockY(), loc.getBlockZ()),
                new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ()),
                new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()),
                new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + 1),
                new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 1)
        );
    }
}