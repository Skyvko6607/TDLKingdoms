package me.sky.kingdoms.base.main;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.bukkit.wrapper.AsyncWorld;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.world.FastModeExtent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.base.data.objects.IValuedBuilding;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.utils.Language;
import me.sky.kingdoms.utils.Options;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

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
        kingdom.getBuildings().addAll(theme.getTemplate(kingdom.getLevel()).getBuildings());
        for (IKingdomBuilding building : kingdom.getBuildings()) {
            BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
            vec.add(building.getOffset().toBlockPoint());
            Block block = new Location(kingdom.getLocation().getWorld(), vec.getX(), vec.getY(), vec.getZ()).getBlock();
            block.setType(Material.OAK_SIGN);
        }
        TaskManager.IMP.async(() -> {
            AsyncWorld world = AsyncWorld.wrap(kingdom.getLocation().getWorld());
            for (IKingdomBuilding building : kingdom.getBuildings()) {
                BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
                vec.add(building.getOffset().toBlockPoint());
                BlockState b = vec.getBlock(new FastModeExtent(FaweAPI.getWorld(world.getName())));
                world.setBlock(vec.getX(), vec.getY(), vec.getZ(), b);
            }
            world.commit();
//            for (IKingdomBuilding building : theme.getTemplate(kingdom.getLevel()).getBuildings()) {
//                Vector3 offset = building.getOffset();
//                Sign sign = (Sign) kingdom.getLocation().add(offset.getX(), offset.getY(), offset.getZ()).getBlock().getState();
//                List<String> signLore = Language.get().getMessageList(StringUtils.capitalize(building.getType().name()) + "Property");
//                signLore.replaceAll(s -> s
//                        .replace("%price%", (building instanceof IValuedBuilding ? String.valueOf(((IValuedBuilding) building).getBuyPrice()) : "0"))
//                        .replace("%name%", building.getName())
//                );
//                for (int i = 0; i < 4; i++) {
//                    if (i >= signLore.size()) {
//                        break;
//                    }
//                    sign.setLine(i, signLore.get(i));
//                }
//                sign.update();
//            }
        });
        owner.closeInventory();
        plugin.getKingdomManager().saveKingdom(kingdom);
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
