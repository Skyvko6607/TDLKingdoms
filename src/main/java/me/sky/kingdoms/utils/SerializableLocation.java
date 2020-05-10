package me.sky.kingdoms.utils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SerializableLocation extends SerializableVector {

    private final String world;
    private final float pitch;
    private final float yaw;

    public SerializableLocation(String s) {
        super(s);
        this.world = s.split(",")[3];
        this.pitch = Float.parseFloat(s.split(",")[4]);
        this.yaw = Float.parseFloat(s.split(",")[5]);
    }

    public SerializableLocation(Location loc) {
        super(loc);
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("x", getX())
                .append("y", getY())
                .append("z", getZ())
                .append("world", getWorld())
                .append("pitch", getPitch())
                .append("yaw", getYaw())
                .toString();
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), getX(), getY(), getZ(), yaw, pitch);
    }
}
