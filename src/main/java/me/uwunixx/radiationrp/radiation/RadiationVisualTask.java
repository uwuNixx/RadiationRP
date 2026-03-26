package me.uwunixx.radiationrp.radiation;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RadiationVisualTask extends BukkitRunnable {

    private final RadiationRP plugin;

    public RadiationVisualTask(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        for (RadiationBlock block : plugin.getRadiationManager().getInfected()) {

            Location loc = block.getLocation();
            World world = loc.getWorld();
            if (world == null) continue;

            for (Player player : world.getPlayers()) {

                // если игрок рядом — показываем частицу
                if (player.getLocation().distance(loc) <= 32) {
                    player.spawnParticle(
                            Particle.PORTAL,
                            loc.getX()+ 0.5,
                            loc.getY() + 0.5,
                            loc.getZ()+ 0.5,
                            1,
                            0, 0, 0,
                            0
                    );
                }
            }
        }
    }
}
