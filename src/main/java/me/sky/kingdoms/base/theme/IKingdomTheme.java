package me.sky.kingdoms.base.theme;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.IKingdomTemplate;
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