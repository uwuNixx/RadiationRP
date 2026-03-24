package me.uwunixx.radiationrp.listeners;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.items.NBTUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMoveListener implements Listener {

    private final RadiationRP plugin;

    public PlayerMoveListener(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!NBTUtils.isGeiger(item)) return;

        int rad = plugin.getRadiationManager().getRadiationAt(player.getLocation());

        if (rad > 0) {
            player.sendActionBar("§e☢ Уровень радиации: §c" + rad);
        }
    }
}
