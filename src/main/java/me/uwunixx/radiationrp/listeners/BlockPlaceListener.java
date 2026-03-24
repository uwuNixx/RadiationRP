package me.uwunixx.radiationrp.listeners;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.filters.FilterBlock;
import me.uwunixx.radiationrp.filters.FilterManager;
import me.uwunixx.radiationrp.items.NBTUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    private final FilterManager filterManager;

    public BlockPlaceListener(RadiationRP plugin) {
        this.filterManager = plugin.getFilterManager();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (!NBTUtils.isFilterItem(event.getItemInHand())) return;

        int level = NBTUtils.getFilterLevel(event.getItemInHand());

        FilterBlock filter = new FilterBlock(
                event.getBlockPlaced().getLocation(),
                level,
                6 // радиус фильтра — потом можно вынести в конфиг
        );

        filterManager.addFilter(filter);
    }
}
