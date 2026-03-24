package me.uwunixx.radiationrp.radiation;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class RadiationSpreadTask extends BukkitRunnable {

    private final RadiationRP plugin;

    public RadiationSpreadTask(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        for (RadiationBlock block : new ArrayList<>(plugin.getRadiationManager().getInfected())) {

            Location loc = block.getLocation();
            RadiationZone zone = plugin.getRadiationManager().getZone(block.getZoneId());
            if (zone == null) continue;

            // Распространение
            for (Location near : getNeighbors(loc)) {

                if (plugin.getRadiationManager().isInfected(near)) continue;

                double dist = near.distance(zone.getCenter());
                if (dist > zone.getMaxRadius()) continue;

                plugin.getRadiationManager().infect(
                        near,
                        block.getZoneId(),
                        Math.max(1, block.getPower() - 1)
                );
            }
        }
    }

    private Location[] getNeighbors(Location loc) {
        return new Location[]{
                loc.clone().add(1, 0, 0),
                loc.clone().add(-1, 0, 0),
                loc.clone().add(0, 0, 1),
                loc.clone().add(0, 0, -1),
                loc.clone().add(0, 1, 0),
                loc.clone().add(0, -1, 0)
        };
    }
}
