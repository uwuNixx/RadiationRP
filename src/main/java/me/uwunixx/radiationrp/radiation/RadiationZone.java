package me.uwunixx.radiationrp.radiation;

import org.bukkit.Location;
import java.util.HashSet;
import java.util.Set;

public class RadiationZone {

    private final int id;
    private final Location center;
    private final double maxRadius;
    private int power;

    private final Set<RadiationBlock> blocks = new HashSet<>();

    public RadiationZone(int id, Location center, double maxRadius, int power) {
        this.id = id;
        this.center = center;
        this.maxRadius = maxRadius;
        this.power = power;
    }

    public int getId() {
        return id;
    }

    public Location getCenter() {
        return center;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Set<RadiationBlock> getBlocks() {
        return blocks;
    }
}
