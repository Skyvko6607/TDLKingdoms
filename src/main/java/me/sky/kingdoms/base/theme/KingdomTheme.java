package me.sky.kingdoms.base.theme;

import me.sky.kingdoms.base.building.IKingdomTemplate;
import me.sky.kingdoms.base.building.KingdomTemplate;
import me.sky.kingdoms.utils.JsonItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

public class KingdomTheme implements IKingdomTheme {

    private String id, name, icon;
    private SortedMap<Integer, IKingdomTemplate> templates = new TreeMap<>();

    public KingdomTheme(String id) {
        this.id = id;
        this.name = id;
        this.icon = null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getIcon() {
        try {
            return JsonItemStack.itemFrom64(icon);
        } catch (IOException | ClassNotFoundException e) {
            icon = null;
        }
        return null;
    }

    @Override
    public SortedMap<Integer, IKingdomTemplate> getMainTemplates() {
        return templates;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setIcon(ItemStack icon) {
        try {
            this.icon = JsonItemStack.itemTo64(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTemplate(int level) {
        this.templates.put(level, new KingdomTemplate());
    }

    @Override
    public IKingdomTemplate getTemplate(int level) {
        return this.templates.getOrDefault(level, null);
    }

    @Override
    public void removeTemplate(int level) {
        this.templates.remove(level);
    }

    @Override
    public void onDelete() {

    }
}
