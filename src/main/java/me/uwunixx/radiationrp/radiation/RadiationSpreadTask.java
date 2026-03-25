package me.uwunixx.radiationrp.radiation;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
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

            // Спавним партиклы для игроков в радиусе
            spawnPortalParticlesForNearbyPlayers(loc, 8);
        }
    }

    // Спавн партиклов только для игроков в радиусе
    private void spawnPortalParticlesForNearbyPlayers(Location blockLocation, double radius) {
        World world = blockLocation.getWorld();
        if (world == null) return;

        // Получаем всех игроков в радиусе
        for (Player player : world.getPlayers()) {
            if (player.getLocation().distance(blockLocation) <= radius) {
                // Спавним партиклы только для этого игрока
                spawnPortalParticles(player, blockLocation);
            }
        }
    }

    // Спавн партиклов портала для конкретного игрока
    private void spawnPortalParticles(Player player, Location location) {
        World world = location.getWorld();
        if (world == null) return;

        // Создаем партиклы вокруг блока
        for (int i = 0; i < 8; i++) {
            double angle = 2 * Math.PI * i / 8;
            double x = location.getX() + 0.5 + Math.cos(angle) * 0.6;
            double y = location.getY() + 0.5 + Math.sin(angle) * 0.6;
            double z = location.getZ() + 0.5 + Math.sin(angle) * 0.6;

            player.spawnParticle(
                    Particle.PORTAL,
                    x, y, z,
                    1, // количество
                    0, 0, 0, // смещение
                    0 // скорость
            );
        }

        // Дополнительные партиклы сверху блока
        player.spawnParticle(
                Particle.PORTAL,
                location.getX() + 0.5,
                location.getY() + 1.2,
                location.getZ() + 0.5,
                5, // количество
                0.2, 0.1, 0.2, // случайное смещение
                0.05 // скорость
        );
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
