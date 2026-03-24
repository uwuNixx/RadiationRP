package me.uwunixx.radiationrp.listeners;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.filters.FilterManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.BlockInventoryHolder;

public class InventoryOpenListener implements Listener {

    private final FilterManager filterManager;

    public InventoryOpenListener(RadiationRP plugin) {
        this.filterManager = plugin.getFilterManager();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {

        if (!(event.getInventory().getHolder() instanceof BlockInventoryHolder holder)) return;

        Block block = holder.getBlock();

        if (filterManager.isFilter(block)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§eЭто фильтр, а не печь.");
        }
    }
}
