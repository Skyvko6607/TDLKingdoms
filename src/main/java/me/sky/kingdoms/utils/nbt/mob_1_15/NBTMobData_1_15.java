package me.sky.kingdoms.utils.nbt.mob_1_15;

import me.sky.kingdoms.utils.nbt.NBTTagString;
import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class NBTMobData_1_15 {

    public static Entity addTag(Entity e, String s) {
        net.minecraft.server.v1_15_R1.Entity entity = ((CraftEntity) e).getHandle();
        NBTTagCompound compound = new NBTTagCompound();
        entity.c(compound);
        NBTTagList l = (compound.getList("Tags", 8) == null) ? new NBTTagList() : compound.getList("Tags", 8);
        l.add(new NBTTagString(s));
        compound.set("Tags", l);
        entity.f(compound);
        return e;
    }

    public static boolean hasTag(Entity e, String s) {
        try {
            net.minecraft.server.v1_15_R1.Entity entity = ((CraftEntity) e).getHandle();
            NBTTagCompound compound = new NBTTagCompound();
            entity.c(compound);
            for (int i = 0; i < (compound.getList("Tags", 8)).size(); i++) {
                NBTBase b = (compound.getList("Tags", 8)).get(i);
                if (b.toString().replace("\"", "").equalsIgnoreCase(s)) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public static List<String> getTags(Entity e) {
        List<String> list = new ArrayList<>();
        try {
            net.minecraft.server.v1_15_R1.Entity entity = ((CraftEntity) e).getHandle();
            NBTTagCompound compound = new NBTTagCompound();
            entity.c(compound);
            for (int i = 0; i < (compound.getList("Tags", 8)).size(); i++) {
                NBTBase b = (compound.getList("Tags", 8)).get(i);
                list.add(b.toString().replace("\"", ""));
            }
        } catch (Exception ignored) {
        }
        return list;
    }

}
