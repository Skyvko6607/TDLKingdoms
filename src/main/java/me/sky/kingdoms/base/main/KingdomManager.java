package me.sky.kingdoms.base.main;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.utils.Options;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KingdomManager implements IKingdomManager {

    private final IKingdomsPlugin plugin;
    private final List<IKingdom> kingdoms;

    public KingdomManager(IKingdomsPlugin plugin) {
        this.plugin = plugin;
        this.kingdoms = new ArrayList<>();
        loadKingdoms();
    }


    @Override
    public List<IKingdom> getKingdoms() {
        return null;
    }

    @Override
    public IKingdom getKingdomByPlayer(Player player) {
        for (IKingdom kingdom : kingdoms) {
            if (kingdom.getMembers().containsKey(player.getUniqueId())) {
                return kingdom;
            }
        }
        return null;
    }

    @Override
    public IKingdom getKingdomByUniqueId(String uniqueId) {
        for (IKingdom kingdom : kingdoms) {
            if (kingdom.getUuid().equalsIgnoreCase(uniqueId)) {
                return kingdom;
            }
        }
        return null;
    }

    @Override
    public void loadKingdoms() {

    }

    @Override
    public IKingdom createKingdom(Player owner, String name, IKingdomTheme theme) {
        IKingdom nearest = getNearestKingdom(owner.getLocation());
        if (owner.getLocation().distance(nearest.getLocation()) <= Options.get().getInt("MinimumDistanceBetween")) {
            return null;
        }
        IKingdom kingdom = new Kingdom(owner, name, theme);
        try {
            plugin.getBuildingManager().placeTemplate(kingdom, theme.getTemplate(kingdom.getLevel()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return kingdom;
    }

    @Override
    public IKingdom getNearestKingdom(Location location) {
        IKingdom nearest = null;
        double lastDistance = 0;
        for (IKingdom kingdom : kingdoms) {
            if (kingdom.getLocation().getWorld().equals(location.getWorld())) {
                double distance = kingdom.getLocation().distance(location);
                if (nearest == null) {
                    nearest = kingdom;
                    lastDistance = distance;
                } else if (distance <= lastDistance) {
                    nearest = kingdom;
                    lastDistance = distance;
                }
            }
        }
        return nearest;
    }

    @Override
    public IKingdomsPlugin getPlugin() {
        return plugin;
    }
}
