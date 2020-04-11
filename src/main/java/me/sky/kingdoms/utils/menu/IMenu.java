package me.sky.kingdoms.utils.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface IMenu extends InventoryHolder {
    void OnGUI(Player player, int slot, ItemStack item, ClickType clickType);
}
