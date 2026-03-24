package me.uwunixx.radiationrp.radiation;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RadiationDamageTask extends BukkitRunnable {

    private final RadiationRP plugin;

    public RadiationDamageTask(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            int rad = plugin.getRadiationManager().getRadiationAt(player.getLocation());
            if (rad <= 0) continue;

            int suit = plugin.getSuitManager().getPlayerSuitLevel(player);

            int damage = Math.max(0, rad - suit * 2);

            if (damage > 0) {
                player.damage(damage * 0.5); // можно настроить
                player.sendActionBar("§cРадиация: §f" + rad + " §7(защита: " + suit + ")");
            }
        }
    }
}
