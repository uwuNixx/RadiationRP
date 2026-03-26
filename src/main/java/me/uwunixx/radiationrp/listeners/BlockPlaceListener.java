package me.uwunixx.radiationrp.listeners;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.filters.FilterBlock;
import me.uwunixx.radiationrp.items.NBTUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    private final RadiationRP plugin;

    public BlockPlaceListener(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        if (e.getBlock().getType() != Material.FURNACE) return;

        ItemStack item = e.getItemInHand();
        if (!NBTUtils.isFilterItem(item)) return; // только специальные печи-фильтры

        plugin.getFilterManager().addFilter(new FilterBlock(
                e.getBlock().getLocation(),
                1,
                6
        ));
        e.getPlayer().sendMessage("§aФильтр установлен.");
    }
}
