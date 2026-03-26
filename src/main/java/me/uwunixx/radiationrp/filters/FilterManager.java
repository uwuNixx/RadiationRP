package me.uwunixx.radiationrp.filters;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.radiation.RadiationBlock;
import me.uwunixx.radiationrp.radiation.RadiationManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class FilterManager {

    private final RadiationRP plugin;
    private final Map<Location, FilterBlock> filters = new HashMap<>();

    public FilterManager(RadiationRP plugin) {
        this.plugin = plugin;
    }

    public void addFilter(FilterBlock filter) {
        filters.put(filter.getLocation(), filter);
    }

    public void removeFilter(Block block) {
        filters.remove(block.getLocation());
    }

    public boolean isFilter(Block block) {
        return filters.containsKey(block.getLocation());
    }

    public boolean isNearFilter(Location loc, int radius) {
        for (FilterBlock filter : filters.values()) {
            if (filter.getLocation().getWorld().equals(loc.getWorld()) &&
                    filter.getLocation().distance(loc) <= radius) {
                return true;
            }
        }
        return false;
    }

    public void cleanRadiation(RadiationManager rad) {
        for (RadiationBlock block : new ArrayList<>(rad.getInfected())) {
            // ядро зоны фильтрами не трогаем
            if (block.isCore()) continue;

            for (FilterBlock filter : filters.values()) {
                if (!filter.getLocation().getWorld().equals(block.getLocation().getWorld())) continue;

                if (block.getLocation().distance(filter.getLocation()) <= filter.getRadius()) {
                    block.weaken(2);
                    if (block.getPower() <= 0) {
                        rad.removeInfectedAt(block.getLocation());
                    }
                }
            }
        }
    }

    public void startTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanRadiation(plugin.getRadiationManager());
            }
        }.runTaskTimer(plugin, 40, 40);
    }

    public void stopTasks() {
        // Пока пусто
    }
}
