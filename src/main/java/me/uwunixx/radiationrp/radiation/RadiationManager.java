package me.uwunixx.radiationrp.radiation;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RadiationManager {

    private final RadiationRP plugin;
    private final Map<Integer, RadiationZone> zones = new HashMap<>();
    private int nextId = 1;

    private BukkitTask damageTask;

    public RadiationManager(RadiationRP plugin) {
        this.plugin = plugin;
    }

    public RadiationZone createZone(Location center, double radius, int power) {
        RadiationZone zone = new RadiationZone(nextId++, center, radius, power);
        zones.put(zone.getId(), zone);
        return zone;
    }

    public Collection<RadiationZone> getZones() {
        return zones.values();
    }

    /**
     * Возвращает уровень радиации в точке.
     * Пока что — просто проверка расстояния до зон.
     */
    public int getRadiationAt(Location loc) {
        int max = 0;

        for (RadiationZone zone : zones.values()) {
            if (!zone.getCenter().getWorld().equals(loc.getWorld())) continue;

            double dist = loc.distance(zone.getCenter());
            if (dist <= zone.getMaxRadius()) {
                max = Math.max(max, zone.getPower());
            }
        }

        return max;
    }

    public void startTasks() {
        // Урон от радиации — каждые 20 тиков (1 секунда)
        damageTask = new RadiationDamageTask(plugin).runTaskTimer(plugin, 20, 20);

        // Spread и Growth добавим позже
    }

    public void stopTasks() {
        if (damageTask != null) damageTask.cancel();
    }
}
