package me.sky.kingdoms.events;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.gui.creator.building.BuildingClaimGUI;
import me.sky.kingdoms.utils.Options;
import me.sky.kingdoms.utils.SerializableLocation;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
    public void blockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            return;
        }
        final Material type = event.getBlockPlaced().getType();
        new BukkitRunnable() {
            @Override
            public void run() {
                IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName());
                IKingdomTemplate template = theme.getTemplate(kingdom.getLevel());
                for (String bId : kingdom.getBuildings().keySet()) {
                    IKingdomBuilding building = plugin.getBuildingManager().getBuilding(bId, template);
                    if (building == null) {
                        continue;
                    }
                    if (!kingdom.getLocation().getWorld().equals(event.getBlockPlaced().getWorld()) ||
                            kingdom.getLocation().distance(event.getBlockPlaced().getLocation()) > Options.get().getInt("MinimumDistanceBetween")) {
                        return;
                    }
                    for (SerializableVector[] area : building.getBuildingAreas()) {
                        Region region = new CuboidRegion(BlockVector3.at(area[0].getX(), area[0].getY(), area[0].getZ()), BlockVector3.at(area[1].getX(), area[1].getY(), area[1].getZ()));
                        if (region.contains(event.getBlockPlaced().getX(), event.getBlockPlaced().getY(), event.getBlockPlaced().getZ())) {
                            IKingdomBuildingData data = kingdom.getBuildings().get(building.getId());
                            if (!data.isOwned() || !data.getOwnedBy().equals(player.getUniqueId())) {
                                player.sendMessage(KingdomUtils.PREFIX + "You are not allowed to build here!");
                                return;
                            }
                            data.getPlacedBlocks().add(new SerializableLocation(event.getBlockPlaced().getLocation()));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    event.getBlockPlaced().setType(type);
                                }
                            }.runTaskLater(plugin, 1);
                            return;
                        }
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
        event.setCancelled(true);
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
