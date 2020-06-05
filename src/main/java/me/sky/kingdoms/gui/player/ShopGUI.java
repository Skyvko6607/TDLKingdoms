package me.sky.kingdoms.gui.player;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.data.ShopData;
import me.sky.kingdoms.utils.JsonItemStack;
import me.sky.kingdoms.utils.Language;
import me.sky.kingdoms.utils.menu.IMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopGUI implements IMenu {

    private final ShopData shop;
    private final Inventory inv;
    private final IKingdomsPlugin plugin;
    private final Map<Integer, String> items = new HashMap<>();

    public ShopGUI(Player player, ShopData shop, IKingdomsPlugin plugin) {
        this.shop = shop;
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(this, 27, Language.get().getMessage("ShopGUI"));
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        int i = 0;
        for (Map.Entry<String, Long> entry : shop.getSaleItems().entrySet()) {
            if (i >= 45) {
                break;
            }
            ItemStack itemStack = null;
            try {
                itemStack = JsonItemStack.itemFrom64(entry.getKey());
            } catch (IOException | ClassNotFoundException e) {
                continue;
            }
            ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
            lore.add("");
            lore.add(Language.get().getMessage("BuyItem").replace("%price%", String.valueOf(entry.getValue())));
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            inv.setItem(i, itemStack);
            items.put(i, entry.getKey());
            i++;
        }
        player.openInventory(this.inv);
    }

    @Override
    public void OnGUI(Player player, int slot, ItemStack item, ClickType clickType) {
        if (!items.containsKey(slot)) {
            return;
        }
        if (plugin.getEconomy().getBalance(player) < shop.getSaleItems().get(items.get(slot))) {
            player.sendMessage(Language.get().getMessage("NotEnoughMoney"));
            return;
        }
        try {
            player.getInventory().addItem(JsonItemStack.itemFrom64(items.get(slot)));
        } catch (IOException | ClassNotFoundException e) {
            new ShopGUI(player, shop, plugin);
            return;
        }
        plugin.getEconomy().withdrawPlayer(player, shop.getSaleItems().get(items.get(slot)));
        shop.getSaleItems().remove(items.get(slot));
        new ShopGUI(player, shop, plugin);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
