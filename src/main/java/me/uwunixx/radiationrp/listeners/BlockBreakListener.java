package me.uwunixx.radiationrp.listeners;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final RadiationRP plugin;

    public BlockBreakListener(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        if (e.getBlock().getType() != Material.FURNACE) return;

        if (plugin.getFilterManager().isFilter(e.getBlock())) {
            plugin.getFilterManager().removeFilter(e.getBlock());
            e.getPlayer().sendMessage("§cФильтр удалён.");
        }
    }
}
