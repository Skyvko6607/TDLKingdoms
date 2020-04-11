package me.sky.kingdoms.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;

public class ConfigItem extends ItemStack {

    private final List<Color> colors = Arrays.asList(
            Color.AQUA,
            Color.FUCHSIA,
            Color.BLACK,
            Color.BLUE,
            Color.GREEN,
            Color.GRAY,
            Color.LIME,
            Color.RED,
            Color.MAROON,
            Color.ORANGE,
            Color.OLIVE,
            Color.WHITE,
            Color.YELLOW,
            Color.SILVER,
            Color.PURPLE,
            Color.NAVY,
            Color.TEAL
    );

    public ConfigItem(ConfigurationSection section) {
        super(Material.valueOf(section.getString("ID")), section.contains("Amount") ? section.getInt("Amount") : 1);
        ItemMeta meta = this.getItemMeta();
        if (section.contains("Name")) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("Name")));
        }
        if (section.contains("Lore")) {
            List<String> lore = section.getStringList("Lore");
            lore.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
            meta.setLore(lore);
        }
        if (meta instanceof LeatherArmorMeta && section.contains("Color")) {
            ((LeatherArmorMeta) meta).setColor(getColor(section.getString("Color")));
        }
        this.setItemMeta(meta);
        section.getStringList("Enchants").forEach(s -> addUnsafeEnchantment(Enchantment.getByName(s.split(" ")[0]), Integer.parseInt(s.split(" ")[1])));
    }

    private Color getColor(String name) {
        for (Color color : colors) {
            if (color.toString().equalsIgnoreCase(name)) {
                return color;
            }
        }
        return null;
    }

}
