package me.uwunixx.radiationrp.filters;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public class FilterManager {

    private final RadiationRP plugin;
    private final Set<FilterBlock> filters = new HashSet<>();
    private BukkitTask cleanTask;

    public FilterManager(RadiationRP plugin) {
        this.plugin = plugin;
    }

    public void addFilter(FilterBlock filter) {
        filters.add(filter);
    }

    public void removeFilter(Block block) {
        filters.removeIf(f -> f.getLocation().equals(block.getLocation()));
    }

    public boolean isFilter(Block block) {
        return filters.stream().anyMatch(f -> f.getLocation().equals(block.getLocation()));
    }

    public void startTasks() {
        // TODO: периодическая очистка заражённых блоков
    }

    public void stopTasks() {
        if (cleanTask != null) cleanTask.cancel();
    }
}
