package me.uwunixx.radiationrp.listeners;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.radiation.RadiationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final RadiationManager radiationManager;

    public PlayerMoveListener(RadiationRP plugin) {
        this.radiationManager = plugin.getRadiationManager();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        // Пока что просто каркас — позже добавим логику
        // radiationManager.applyRadiation(player);
    }
}
