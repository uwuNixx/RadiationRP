package me.uwunixx.radiationrp.listeners;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.filters.FilterManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final FilterManager filterManager;

    public BlockBreakListener(RadiationRP plugin) {
        this.filterManager = plugin.getFilterManager();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (filterManager.isFilter(event.getBlock())) {
            filterManager.removeFilter(event.getBlock());
        }
    }
}
