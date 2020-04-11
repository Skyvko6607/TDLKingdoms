package me.sky.kingdoms.utils.nbt;

import me.sky.kingdoms.utils.nbt.mob_1_15.NBTMobData_1_15;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class NBTMobData {

    private static final ServerVersion version = ServerVersion.getVersion();

    public static Entity addTag(Entity e, String s) {
        switch (version) {
            case VERSION_1_15:
                return NBTMobData_1_15.addTag(e, s);
        }
        return e;
    }

    public static boolean hasTag(Entity e, String s) {
        switch (version) {
            case VERSION_1_15:
                return NBTMobData_1_15.hasTag(e, s);
        }
        return false;
    }

    public static List<String> getTags(Entity e) {
        switch (version) {
            case VERSION_1_15:
                return NBTMobData_1_15.getTags(e);
        }
        return new ArrayList<>();
    }

}
