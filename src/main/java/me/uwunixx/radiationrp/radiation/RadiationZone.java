package me.uwunixx.radiationrp.radiation;

import org.bukkit.Location;

public class RadiationZone {

    private final int id;
    private final Location center;
    private final double maxRadius;
    private int power;

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
}
