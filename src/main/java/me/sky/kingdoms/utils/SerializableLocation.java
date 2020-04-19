package me.sky.kingdoms.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SerializableLocation extends SerializableVector {

    private String world;
    private float pitch;
    private float yaw;

    public SerializableLocation(Location loc) {
        super(loc);
        setLocation(loc);
    }

    public void setLocation(Location loc) {
        this.world = loc.getWorld().getName();
        this.pitch = loc.getPitch();
        this.yaw = loc.getYaw();
    }

    public String getWorld() {
        return world;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), getX(), getY(), getZ(), yaw, pitch);
    }
}
