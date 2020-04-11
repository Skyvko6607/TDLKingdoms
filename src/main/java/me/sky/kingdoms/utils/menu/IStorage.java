package me.sky.kingdoms.utils.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface IStorage extends IMenu {
    void OnGUI(Player player, int slot, ItemStack item, ClickType clickType, InventoryClickEvent event);
    void OnGUIClose(Player player);
}
