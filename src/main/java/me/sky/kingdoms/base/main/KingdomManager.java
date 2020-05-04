package me.sky.kingdoms.base.main;

import com.sk89q.worldedit.math.BlockVector3;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.base.building.data.HouseData;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.base.data.objects.IValuedBuilding;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.utils.Language;
import me.sky.kingdoms.utils.Options;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KingdomManager implements IKingdomManager {

    private final IKingdomsPlugin plugin;
    private final List<IKingdom> kingdoms;

    public KingdomManager(IKingdomsPlugin plugin) {
        this.plugin = plugin;
        this.kingdoms = new ArrayList<>();
        loadKingdoms();
        new BukkitRunnable() {
            @Override
            public void run() {
                List<IKingdom> toRemove = new ArrayList<>();
                kingdoms.iterator().forEachRemaining(kingdom -> {
                    if (kingdom.getMembers().isEmpty()) {
                        toRemove.add(kingdom);
                        return;
                    }
                    for (String bId : kingdom.getBuildings().keySet()) {
                        IKingdomBuilding building = plugin.getBuildingManager().getBuilding(bId, plugin.getThemeManager().getThemeFromId(kingdom.getThemeName()).getTemplate(kingdom.getLevel()));
                        if (building == null) {
                            kingdom.removeBuildingData(bId);
                            continue;
                        }
                        updateSign(kingdom, building);
                    }
                });
                toRemove.forEach(kingdom -> removeKingdom(kingdom));
            }
        }.runTaskTimer(plugin, 20, 100);
    }

    @Override
    public List<IKingdom> getKingdoms() {
        return kingdoms;
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
    public IKingdom getKingdomByName(String name) {
        for (IKingdom kingdom : kingdoms) {
            if (kingdom.getName().equalsIgnoreCase(name)) {
                return kingdom;
            }
        }
        return null;
    }

    @Override
    public void loadKingdoms() {
        File dir = new File("plugins/" + getPlugin().getName() + "/data");
        if (!dir.exists()) {
            dir.mkdir();
            return;
        }
        for (File file : dir.listFiles()) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine()) {
                    kingdoms.add(plugin.getGson().fromJson(scanner.nextLine(), Kingdom.class));
                }
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Error loading kingdom data: " + file.getName());
            }
        }
        plugin.getLogger().info(String.format("Loaded %d Kingdoms!", kingdoms.size()));
    }

    private void updateSign(IKingdom kingdom, IKingdomBuilding building) {
        if (kingdom.getBuildingOwner(building) != null) {
            return;
        }
        BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
        vec = vec.subtract(building.getOffset().toBlockPoint());
        Block block = new Location(kingdom.getLocation().getWorld(), vec.getX(), vec.getY(), vec.getZ()).getBlock();
        if (!block.getChunk().isLoaded()) {
            return;
        }
        if (!block.getType().name().contains("SIGN")) {
            new Location(kingdom.getLocation().getWorld(), vec.getX(), vec.getY() - 1, vec.getZ()).getBlock().setType(Material.BEDROCK);
            block.setType(Material.OAK_SIGN);
        }
        Sign sign = (Sign) block.getState();
        List<String> signLore = new ArrayList<>(Language.get().getMessageList(StringUtils.capitalize(building.getType().name().toLowerCase()) + "Property"));
        signLore.replaceAll(s -> s
                .replace("%price%", (building instanceof IValuedBuilding ? String.valueOf(((IValuedBuilding) building).getBuyPrice()) : "0"))
                .replace("%name%", building.getName())
        );
        for (int i = 0; i < 4; i++) {
            if (i >= signLore.size()) {
                break;
            }
            sign.setLine(i, signLore.get(i));
        }
        sign.update();
        if (building.getDirection() != null) {
            org.bukkit.block.data.type.Sign sign1 = (org.bukkit.block.data.type.Sign) block.getBlockData();
            Direction direction = building.getDirection();
            sign1.setRotation(BlockFace.valueOf(Direction.getSubDirection(direction, 2).name()));
            block.setBlockData(sign1);
            block.getState().update();
        }
    }

    @Override
    public IKingdom createKingdom(Player owner, String name, IKingdomTheme theme) {
        IKingdom nearest = getNearestKingdom(owner.getLocation());
        if (nearest != null && owner.getLocation().distance(nearest.getLocation()) <= Options.get().getInt("MinimumDistanceBetween")) {
            return null;
        }
        IKingdom kingdom = new Kingdom(owner, name, theme);
        try {
            plugin.getBuildingManager().placeTemplate(kingdom, theme.getTemplate(kingdom.getLevel()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        theme.getTemplate(kingdom.getLevel()).getBuildings().forEach(kingdomBuilding -> kingdom.getBuildings().put(kingdomBuilding.getId(), kingdomBuilding.getType() == KingdomBuildingType.HOUSE ? new HouseData(kingdomBuilding) : null));
        for (String bId : kingdom.getBuildings().keySet()) {
            IKingdomBuilding building = plugin.getBuildingManager().getBuilding(bId, theme.getTemplate(kingdom.getLevel()));
            updateSign(kingdom, building);
        }
        owner.closeInventory();
        plugin.getKingdomManager().saveKingdom(kingdom);
        kingdoms.add(kingdom);
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
