package me.sky.kingdoms.base.data;

import me.sky.kingdoms.base.building.data.KingdomBuildingData;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface IMemberData {
    UUID getUniqueId();
    String getName();
    KingdomRank getRank();
    List<String> getOwningBuildings();
    List<ItemStack> getDumpItems();
    List<String> getJsonDumpItems();
    void setName(String name);
    void setRank(KingdomRank rank);
    void addDumpItems(ItemStack... items);
}
