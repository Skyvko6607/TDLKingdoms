package me.sky.kingdoms.utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class Items {

    private Config config;
    private Plugin plugin;

    private static Items instance;

    public Items(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
        config = new Config("plugins/" + plugin.getName(), "items.yml", plugin);
        config.create();
        if (!config.exists()) {
            config.setDefault("items.yml");
            config.getConfig().options().copyDefaults(true);
            config.saveConfig();
            config.reloadConfig();
        } else {
            Config c = new Config("plugins/" + plugin.getName(), "temp.yml", plugin);
            c.create();
            c.setDefault("items.yml");
            c.getConfig().options().copyDefaults(true);
            for (String key : c.getConfig().getKeys(false)) {
                if (!config.getConfig().contains(key)) {
                    config.getConfig().set(key, c.getConfig().get(key));
                }
            }
            config.saveConfig();
            config.reloadConfig();
        }
    }

    public static Items get() {
        return instance;
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItem(String name) {
        try {
            if (!config.getConfig().contains(name)) {
                throw new ItemNotFoundException();
            }
            return new ConfigItem(config.getConfig().getConfigurationSection(name));
        } catch (ItemNotFoundException e) {
            plugin.getLogger().info("§4The item " + name + " couldn't be found in items.yml!");
        }
        ItemStack item = getExampleItem();
        ConfigurationSection sec = config.getConfig().createSection(name);
        sec.set("ID", item.getType().name());
        sec.set("Amount", item.getAmount());
        sec.set("Name", item.getItemMeta().getDisplayName().replace("§", "&"));
        List<String> lore = item.getItemMeta().getLore();
        lore.replaceAll(s -> s.replace("§", "&"));
        sec.set("Lore", lore);
        String enchant = Enchantment.DURABILITY.getName();
        sec.set("Enchants", Collections.singletonList(enchant + " " + 1));
        config.saveConfig();
        config.reloadConfig();
        return item;
    }

    private ItemStack getExampleItem() {
        return ItemUtils.constructItem(Material.DIRT, "Example Item", Collections.singletonList("Example item description"));
    }
}
