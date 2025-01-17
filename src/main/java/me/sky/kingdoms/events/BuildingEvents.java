package me.sky.kingdoms.events;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.enums.KingdomBuildingType;
import me.sky.kingdoms.base.building.data.HouseData;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.gui.creator.building.BuildingClaimGUI;
import me.sky.kingdoms.gui.player.BuildingStorageGUI;
import me.sky.kingdoms.utils.Options;
import me.sky.kingdoms.utils.SerializableLocation;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BuildingEvents implements Listener {

    private final IKingdomsPlugin plugin;

    public BuildingEvents(IKingdomsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private final List<String> clickDelay = new ArrayList<>();

    @EventHandler
    public void signClick(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!event.getClickedBlock().getType().equals(Material.OAK_SIGN)) {
            return;
        }
        Player player = event.getPlayer();
        if (clickDelay.contains(player.getUniqueId().toString())) {
            return;
        }
        clickDelay.add(player.getUniqueId().toString());
        new BukkitRunnable() {
            @Override
            public void run() {
                clickDelay.remove(player.getUniqueId().toString());
            }
        }.runTaskLater(plugin, 20 * 2);
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            return;
        }
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName());
        IKingdomTemplate template = theme.getTemplate(kingdom.getLevel());
        for (String bId : kingdom.getBuildings().keySet()) {
            IKingdomBuilding building = plugin.getBuildingManager().getBuilding(bId, template);
            if (building == null) {
                continue;
            }
            BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
            vec = vec.subtract(building.getOffset().toBlockPoint());
            Block block = new Location(kingdom.getLocation().getWorld(), vec.getX(), vec.getY(), vec.getZ()).getBlock();
            if (event.getClickedBlock().equals(block)) {
                new BuildingClaimGUI(player, kingdom, building, plugin);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void chestOpen(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!event.getClickedBlock().getType().name().contains("CHEST")) {
            return;
        }
        Player player = event.getPlayer();
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            return;
        }
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName());
        IKingdomTemplate template = theme.getTemplate(kingdom.getLevel());
        Region kingdomRegion = new CuboidRegion(
                BlockVector3.at(kingdom.getPoints()[0].getX(), kingdom.getPoints()[0].getY(), kingdom.getPoints()[0].getZ()),
                BlockVector3.at(kingdom.getPoints()[1].getX(), kingdom.getPoints()[1].getY(), kingdom.getPoints()[1].getZ())
        );
        if (!kingdomRegion.contains(event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ())) {
            return;
        }
        event.setCancelled(true);
        Chest chest = (Chest) event.getClickedBlock().getState();
        if (chest.getInventory().getHolder() instanceof DoubleChest) {
            chest = (Chest) ((DoubleChest) chest.getInventory().getHolder()).getLeftSide();
        }
        for (String bId : kingdom.getBuildings().keySet()) {
            IKingdomBuilding building = plugin.getBuildingManager().getBuilding(bId, template);
            if (building == null) {
                break;
            }
            if (!building.getType().equals(KingdomBuildingType.HOUSE)) {
                break;
            }
            HouseData data = (HouseData) kingdom.getBuildings().get(bId);
            BlockVector3 weVec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ()).subtract(building.getOffset().toBlockPoint());
            SerializableVector vec = new SerializableVector(weVec.getX(), weVec.getY(), weVec.getZ());
            for (SerializableVector[] area : building.getBuildingAreas(vec, data.getAngle(), plugin)) {
                Region region = new CuboidRegion(BlockVector3.at(area[0].getX(), area[0].getY(), area[0].getZ()), BlockVector3.at(area[1].getX(), area[1].getY(), area[1].getZ()));
                if (region.contains(event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ())) {
                    if (!data.isOwned() || !data.getOwnedBy().equals(player.getUniqueId())) {
                        break;
                    }
                    if (!data.getStorage().containsKey(event.getClickedBlock().getLocation())) {
                        data.setItems(new SerializableLocation(event.getClickedBlock().getLocation()), new ArrayList<>());
                        plugin.getKingdomManager().saveKingdom(kingdom);
                    }
                    new BuildingStorageGUI(player, kingdom, data, new SerializableLocation(event.getClickedBlock().getLocation()), chest.getBlockInventory().getHolder(), plugin);
                    return;
                }
            }
        }
        player.sendMessage(KingdomUtils.PREFIX + "You are not allowed to open this chest!");
    }

    @EventHandler(ignoreCancelled = true)
    public void blockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            return;
        }
        if (!kingdom.getLocation().getWorld().equals(event.getBlockPlaced().getWorld()) ||
                kingdom.getLocation().distance(event.getBlockPlaced().getLocation()) > Options.get().getInt("MinimumDistanceBetween")) {
            return;
        }
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName());
        IKingdomTemplate template = theme.getTemplate(kingdom.getLevel());
        for (String bId : kingdom.getBuildings().keySet()) {
            IKingdomBuilding building = plugin.getBuildingManager().getBuilding(bId, template);
            if (building == null) {
                continue;
            }
            BlockVector3 weVec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ()).subtract(building.getOffset().toBlockPoint());
            SerializableVector vec = new SerializableVector(weVec.getX(), weVec.getY(), weVec.getZ());
            for (SerializableVector[] area : building.getBuildingAreas(vec, plugin.getBuildingManager().getAngle(building), plugin)) {
                Region region = new CuboidRegion(BlockVector3.at(area[0].getX(), area[0].getY(), area[0].getZ()), BlockVector3.at(area[1].getX(), area[1].getY(), area[1].getZ()));
                if (region.contains(event.getBlockPlaced().getX(), event.getBlockPlaced().getY(), event.getBlockPlaced().getZ())) {
                    IKingdomBuildingData data = kingdom.getBuildings().get(building.getId());
                    if (!data.isOwned() || !data.getOwnedBy().equals(player.getUniqueId())) {
                        player.sendMessage(KingdomUtils.PREFIX + "You are not allowed to build here!");
                        event.setCancelled(true);
                        return;
                    }
                    data.getPlacedBlocks().add(new SerializableLocation(event.getBlockPlaced().getLocation()));
                    return;
                }
            }
        }
        Region kingdomRegion = new CuboidRegion(
                BlockVector3.at(kingdom.getPoints()[0].getX(), kingdom.getPoints()[0].getY(), kingdom.getPoints()[0].getZ()),
                BlockVector3.at(kingdom.getPoints()[1].getX(), kingdom.getPoints()[1].getY(), kingdom.getPoints()[1].getZ())
        );
        if (kingdomRegion.contains(event.getBlockPlaced().getX(), event.getBlockPlaced().getY(), event.getBlockPlaced().getZ())) {
            event.setCancelled(true);
            player.sendMessage(KingdomUtils.PREFIX + "You are not allowed to build here!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void blockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            return;
        }
        Region kingdomRegion = new CuboidRegion(
                BlockVector3.at(kingdom.getPoints()[0].getX(), kingdom.getPoints()[0].getY(), kingdom.getPoints()[0].getZ()),
                BlockVector3.at(kingdom.getPoints()[1].getX(), kingdom.getPoints()[1].getY(), kingdom.getPoints()[1].getZ())
        );
        if (!kingdomRegion.contains(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ())) {
            return;
        }
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName());
        IKingdomTemplate template = theme.getTemplate(kingdom.getLevel());
        for (String bId : kingdom.getBuildings().keySet()) {
            IKingdomBuilding building = plugin.getBuildingManager().getBuilding(bId, template);
            if (building == null) {
                continue;
            }
            if (!kingdom.getLocation().getWorld().equals(event.getBlock().getWorld()) ||
                    kingdom.getLocation().distance(event.getBlock().getLocation()) > Options.get().getInt("MinimumDistanceBetween")) {
                return;
            }
            IKingdomBuildingData data = kingdom.getBuildings().get(building.getId());
            for (SerializableLocation location : data.getPlacedBlocks()) {
                if (location.getLocation().equals(event.getBlock().getLocation())) {
                    return;
                }
            }
        }
        event.setCancelled(true);
    }

}
