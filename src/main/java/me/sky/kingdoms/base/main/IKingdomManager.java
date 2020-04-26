package me.sky.kingdoms.base.main;

import me.sky.kingdoms.base.IManager;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface IKingdomManager extends IManager {
    List<IKingdom> getKingdoms();
    IKingdom getKingdomByPlayer(Player player);
    IKingdom getKingdomByUniqueId(String uniqueId);
    IKingdom getKingdomByName(String name);
    void loadKingdoms();
    IKingdom createKingdom(Player owner, String name, IKingdomTheme theme);
    IKingdom getNearestKingdom(Location location);

    default void saveKingdom(IKingdom kingdom) {
        File file = new File("plugins/" + getPlugin().getName() + "/data/" + kingdom.getUuid() + ".json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                getPlugin().getLogger().severe("Error creating file for kingdom: " + kingdom.getUuid());
                return;
            }
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(getPlugin().getGson().toJson(kingdom));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            getPlugin().getLogger().severe("Error saving data for kingdom: " + kingdom.getUuid());
        }
    }

    default void removeKingdom(IKingdom kingdom) {
        getKingdoms().remove(kingdom);
        new File("plugins/" + getPlugin().getName() + "/data/" + kingdom.getUuid() + ".json").delete();
    }
}
