package me.sky.kingdoms.gui.creator;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.main.Kingdom;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.utils.ItemUtils;
import me.sky.kingdoms.utils.menu.IMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KingdomCreator implements IMenu {

    private final IKingdomsPlugin plugin;
    private final Inventory inv;
    private final String name;
    private final Map<Integer, IKingdomTheme> themeMap = new HashMap<>();

    public KingdomCreator(Player player, String name, IKingdomsPlugin plugin) {
        this.plugin = plugin;
        this.name = name;
        this.inv = Bukkit.createInventory(this, 27, "§l" + name);
        inv.setItem(4, ItemUtils.constructItem(Material.PAPER, "§aSelect a kingdom theme", new ArrayList<>()));
        for (IKingdomTheme theme : plugin.getThemeManager().getThemes()) {
            inv.setItem(theme.getSlot(), getThemeIcon(theme));
            themeMap.put(theme.getSlot(), theme);
        }
        player.openInventory(inv);
    }

    private ItemStack getThemeIcon(IKingdomTheme theme) {
        ItemStack itemStack = theme.getIcon().clone();
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(theme.getName());
        meta.setLore(theme.getDescription());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void OnGUI(Player player, int slot, ItemStack item, ClickType clickType) {
        if (!(themeMap.containsKey(slot))) {
            return;
        }
        IKingdomTheme theme = themeMap.get(slot);
        player.openInventory(new IMenu() {
            private Inventory inv;
            @Override
            public void OnGUI(Player player, int slot, ItemStack item, ClickType clickType) {
                if (slot == 2) {
                    plugin.getKingdomManager().createKingdom(player, name, theme);
                } else if (slot == 6) {
                    player.closeInventory();
                }
            }
            @NotNull
            @Override
            public Inventory getInventory() {
                if (inv == null) {
                    inv = Bukkit.createInventory(this, 9, "§lAre you sure?");
                    inv.setItem(2, ItemUtils.constructItem(Material.LIME_WOOL, "§a§lConfirm", new ArrayList<>()));
                    inv.setItem(6, ItemUtils.constructItem(Material.RED_WOOL, "§4§lCancel", new ArrayList<>()));
                }
                return inv;
            }
        }.getInventory());
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
