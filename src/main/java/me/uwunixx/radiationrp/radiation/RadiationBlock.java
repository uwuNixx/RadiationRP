package me.uwunixx.radiationrp.radiation;

import org.bukkit.Location;

public class RadiationBlock {

    private final Location location;
    private final int zoneId;
    private int power;

    public RadiationBlock(Location location, int zoneId, int power) {
        this.location = location;
        this.zoneId = zoneId;
        this.power = power;
    }

    public Location getLocation() {
        return location;
    }

    public int getZoneId() {
        return zoneId;
    }

    public int getPower() {
        return power;
    }

    public void weaken(int amount) {
        power = Math.max(0, power - amount);
    }

    public void strengthen(int amount) {
        power += amount;
    }
}
