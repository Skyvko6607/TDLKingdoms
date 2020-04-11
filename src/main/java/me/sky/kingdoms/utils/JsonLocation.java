package me.sky.kingdoms.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class JsonLocation {

    private String world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public JsonLocation(Location loc) {
        setLocation(loc);
    }

    public void setLocation(Location loc) {
        this.world = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.pitch = loc.getPitch();
        this.yaw = loc.getYaw();
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}
