package me.uwunixx.radiationrp.radiation;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RadiationManager {

    private final RadiationRP plugin;
    private final Map<Integer, RadiationZone> zones = new HashMap<>();
    private int nextId = 1;

    private BukkitTask spreadTask;
    private BukkitTask growthTask;
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

    public void startTasks() {
        // TODO: запустить spread, growth, damage
    }

    public void stopTasks() {
        if (spreadTask != null) spreadTask.cancel();
        if (growthTask != null) growthTask.cancel();
        if (damageTask != null) damageTask.cancel();
    }
}
