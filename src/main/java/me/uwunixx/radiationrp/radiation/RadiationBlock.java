package me.uwunixx.radiationrp.radiation;

import org.bukkit.Location;

public class RadiationBlock {

    private final Location location;
    private int level; // 1..zone.power
    private long infectedAt;

    public RadiationBlock(Location location, int level, long infectedAt) {
        this.location = location;
        this.level = level;
        this.infectedAt = infectedAt;
    }

    public Location getLocation() {
        return location;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getInfectedAt() {
        return infectedAt;
    }

    public void setInfectedAt(long infectedAt) {
        this.infectedAt = infectedAt;
    }
}
