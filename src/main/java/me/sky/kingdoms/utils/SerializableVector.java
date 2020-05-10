package me.sky.kingdoms.utils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SerializableVector {

    private final double x, y, z;

    public SerializableVector(String s) {
        this.x = Double.parseDouble(s.split(",")[0]);
        this.y = Double.parseDouble(s.split(",")[1]);
        this.z = Double.parseDouble(s.split(",")[2]);
    }

    public SerializableVector(Location loc) {
        this(loc.getX(), loc.getY(), loc.getZ());
    }

    public SerializableVector(Vector vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("x", x)
                .append("y", y)
                .append("z", z)
                .toString();
    }
}
