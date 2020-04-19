package me.sky.kingdoms.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SerializableVector {

    private final double x, y, z;

    public SerializableVector(Location loc) {
        this(loc.getX(), loc.getY(), loc.getZ());
    }

    public SerializableVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
