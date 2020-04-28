package me.sky.kingdoms.base.building.data;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.utils.JsonItemStack;
import me.sky.kingdoms.utils.SerializableLocation;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseData extends KingdomBuildingData {

    private final Map<SerializableLocation, List<String>> storage = new HashMap<>();

    public HouseData(IKingdomBuilding building) {
        super(building);
    }

    public Map<SerializableLocation, List<ItemStack>> getStorage() {
        Map<SerializableLocation, List<ItemStack>> itemMap = new HashMap<>();
        this.storage.forEach((serializableLocation, strings) -> {
            List<ItemStack> items = new ArrayList<>();
            strings.forEach(s -> {
                try {
                    items.add(JsonItemStack.itemFrom64(s));
                } catch (IOException | ClassNotFoundException ignored) {
                }
            });
            itemMap.put(serializableLocation, items);
        });
        return itemMap;
    }
}
