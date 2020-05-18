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

public class ShopData extends KingdomBuildingData {

    private final Map<String, Long> saleItems = new HashMap<>();

    public ShopData(IKingdomBuilding building) {
        super(building);
    }

    public Map<ItemStack, Long> getItems() {
        Map<ItemStack, Long> items = new HashMap<>();
        saleItems.forEach((s, price) -> {
            try {
                items.put(JsonItemStack.itemFrom64(s), price);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return items;
    }

    public void addItem(ItemStack itemStack, Long price) {
        try {
            saleItems.put(JsonItemStack.itemTo64(itemStack), price);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Long> getSaleItems() {
        return saleItems;
    }
}
