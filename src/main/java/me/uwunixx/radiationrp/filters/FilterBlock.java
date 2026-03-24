package me.uwunixx.radiationrp.filters;

import org.bukkit.Location;

public class FilterBlock {

    private final Location location;
    private final int level;
    private final int radius;

    public FilterBlock(Location location, int level, int radius) {
        this.location = location;
        this.level = level;
        this.radius = radius;
    }

    public Location getLocation() {
        return location;
    }

    public int getLevel() {
        return level;
    }

    public int getRadius() {
        return radius;
    }
}
