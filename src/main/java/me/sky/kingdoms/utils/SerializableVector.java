package me.sky.kingdoms.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SerializableVector {

    private double x;
    private double y;
    private double z;

    public SerializableVector(Location loc) {
        setLocation(loc);
    }

    public void setLocation(Location loc) {
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
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

    public Vector getVector() {
        return new Vector(x, y, z);
    }
}
