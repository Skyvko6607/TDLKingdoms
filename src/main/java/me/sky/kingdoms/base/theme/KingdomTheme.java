package me.sky.kingdoms.base.theme;

import me.sky.kingdoms.base.building.IKingdomTemplate;
import me.sky.kingdoms.base.building.KingdomTemplate;
import me.sky.kingdoms.utils.JsonItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class KingdomTheme implements IKingdomTheme {

    private String id, name, icon;
    private int slot;
    private List<String> description = new ArrayList<>();
    private final SortedMap<Integer, KingdomTemplate> templates = new TreeMap<>();

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
    public List<String> getDescription() {
        return description;
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
    public int getSlot() {
        return slot;
    }

    @Override
    public SortedMap<Integer, KingdomTemplate> getMainTemplates() {
        return templates;
    }

    @Override
    public IKingdomTemplate getTemplate(int level) {
        return this.templates.getOrDefault(level, null);
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
    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public void setDescription(List<String> description) {
        this.description = description;
    }

    @Override
    public IKingdomTemplate createTemplate(int level) {
        return this.templates.put(level, new KingdomTemplate());
    }

    @Override
    public void removeTemplate(int level) {
        this.templates.remove(level);
    }
}
