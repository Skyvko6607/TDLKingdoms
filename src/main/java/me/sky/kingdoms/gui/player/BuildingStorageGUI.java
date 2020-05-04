package me.sky.kingdoms.gui.player;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.data.HouseData;
import me.sky.kingdoms.base.building.types.House;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.main.Kingdom;
import me.sky.kingdoms.utils.SerializableLocation;
import me.sky.kingdoms.utils.menu.IMenu;
import me.sky.kingdoms.utils.menu.IStorage;
import me.sky.kingdoms.utils.nbt.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildingStorageGUI implements IStorage {

    private final IKingdom kingdom;
    private final HouseData data;
    private final SerializableLocation location;
    private final IKingdomsPlugin plugin;
    private final Inventory inv;

    public BuildingStorageGUI(Player player, IKingdom kingdom, HouseData data, SerializableLocation location, InventoryHolder holder, IKingdomsPlugin plugin) {
        this.kingdom = kingdom;
        this.data = data;
        this.location = location;
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(this, holder.getInventory().getSize(), "§lStorage");
        List<ItemStack> items = data.getStorage().get(location.getLocation());
        int i = 0;
        for (ItemStack item : items) {
            if (item != null) {
                inv.setItem(i, item);
            }
            i++;
        }
        player.openInventory(inv);
    }

    @Override
    public void OnGUI(Player player, int slot, ItemStack item, ClickType clickType, InventoryClickEvent event) {

    }

    @Override
    public void OnGUIClose(Player player) {
        List<ItemStack> items = Arrays.asList(inv.getContents());
        data.setItems(location, items);
        plugin.getKingdomManager().saveKingdom(kingdom);
    }

    @Override
    public void OnGUI(Player player, int slot, ItemStack item, ClickType clickType) {

    }
    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
