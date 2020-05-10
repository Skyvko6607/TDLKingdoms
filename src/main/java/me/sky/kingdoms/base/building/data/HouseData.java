package me.sky.kingdoms.base.building.data;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.utils.JsonItemStack;
import me.sky.kingdoms.utils.SerializableLocation;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseData extends KingdomBuildingData {

    private final Map<String, List<String>> storage = new HashMap<>();

    public HouseData(IKingdomBuilding building) {
        super(building);
    }

    public Map<Location, List<ItemStack>> getStorage() {
        Map<Location, List<ItemStack>> itemMap = new HashMap<>();
        this.storage.forEach((serializableLocation, strings) -> {
            List<ItemStack> items = new ArrayList<>();
            strings.forEach(s -> {
                try {
                    items.add(JsonItemStack.itemFrom64(s));
                } catch (IOException | ClassNotFoundException ignored) {
                }
            });
            itemMap.put(new SerializableLocation(serializableLocation).getLocation(), items);
        });
        return itemMap;
    }

    public Map<String, List<String>> getJsonStorage() {
        return storage;
    }

    public void setItems(SerializableLocation location, List<ItemStack> items) {
        List<String> i = new ArrayList<>();
        items.forEach(itemStack -> {
            try {
                i.add(JsonItemStack.itemTo64(itemStack));
            } catch (IOException ignored) {
            }
        });
        for (String locString : storage.keySet()) {
            SerializableLocation loc = new SerializableLocation(locString);
            if (loc.getLocation().equals(location.getLocation())) {
                storage.replace(locString, i);
                return;
            }
        }
        storage.put(location.toString(), i);
    }
}
