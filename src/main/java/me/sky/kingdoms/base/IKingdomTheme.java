package me.sky.kingdoms.base;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.SortedMap;

public interface IKingdomTheme {
    String getId();
    String getName();
    ItemStack getIcon();
    SortedMap<Integer, IKingdomTemplate> getMainTemplates();
    List<IKingdomBuilding> getBuildings();
}
