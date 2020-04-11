package me.sky.kingdoms.utils.particleeffect;

import org.bukkit.Location;

public class ColorEffect {
    public static void display(int r, int g, int b, Location loc) {
        ParticleEffect.REDSTONE.display(getColor(r, g, b), loc, 16);
    }
    public static void displayNote(int r, int g, int b, Location loc) {
        ParticleEffect.NOTE.display(getColor(r, g, b), loc, 16);
    }

    public static ParticleEffect.OrdinaryColor getColor(int i, int i2, int i3) {
        return new ParticleEffect.OrdinaryColor(i, i2, i3);
    }
}
