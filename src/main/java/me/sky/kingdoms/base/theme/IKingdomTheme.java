package me.sky.kingdoms.base.theme;

import com.google.gson.Gson;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.IKingdomTemplate;
import me.sky.kingdoms.base.building.KingdomTemplate;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.SortedMap;

public interface IKingdomTheme {
    String getId();
    String getName();
    List<String> getDescription();
    ItemStack getIcon();
    int getSlot();
    SortedMap<Integer, KingdomTemplate> getMainTemplates();
    IKingdomTemplate getTemplate(int level);
    void setId(String id);
    void setName(String name);
    void setIcon(ItemStack icon);
    void setSlot(int slot);
    void setDescription(List<String> description);
    IKingdomTemplate createTemplate(int level);
    void removeTemplate(int level);

    Gson gson = new Gson();

    default void save(IKingdomsPlugin plugin) {
        File file = new File("plugins/" + plugin.getName() + "/themes/" + getId() + ".json");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(this));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void delete(IKingdomsPlugin plugin) {
        plugin.getThemeManager().getThemes().remove(this);
        new File("plugins/" + plugin.getName() + "/themes/" + getId() + ".json").delete();
    }
}
