package me.sky.kingdoms.utils.nbt;

import me.sky.kingdoms.utils.nbt.item_1_15.NBTItemData_1_15;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class NBTItemData {

    private static final ServerVersion version = ServerVersion.getVersion();

    public static ItemStack set(String base, String path, String i, ItemStack itemStack) {
        switch (version) {
            case VERSION_1_15:
                return NBTItemData_1_15.set(base, path, i, itemStack);
        }
        return itemStack;
    }

    public static String get(String base, String path, ItemStack itemStack) {
        switch (version) {
            case VERSION_1_15:
                return NBTItemData_1_15.get(base, path, itemStack);
        }
        return null;
    }

    public static Set<String> getList(String base, ItemStack itemStack) {
        switch (version) {
            case VERSION_1_15:
                return NBTItemData_1_15.getList(base, itemStack);
        }
        return new HashSet<>();
    }

    public static boolean isDefault(String base, ItemStack itemStack) {
        switch (version) {
            case VERSION_1_15:
                return NBTItemData_1_15.isDefault(base, itemStack);
        }
        return false;
    }

}
