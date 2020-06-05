package me.sky.kingdoms.events;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.data.ShopData;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.gui.player.ShopGUI;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class KingdomEvents implements Listener {

    private final IKingdomsPlugin plugin;

    public KingdomEvents(IKingdomsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getKingdomManager().getKingdoms().forEach(kingdom -> {
                    IKingdomTemplate template = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName()).getTemplate(kingdom.getLevel());
                    kingdom.getBuildings().values().forEach(kingdomBuildingData -> {
                        if (kingdomBuildingData.isOwned() && kingdomBuildingData instanceof ShopData) {
                            IKingdomBuilding building = plugin.getBuildingManager().getBuilding(kingdomBuildingData.getId(), template);
                            if (building != null) {
                                BlockVector3 weVec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ()).subtract(building.getOffset().toBlockPoint());
                                SerializableVector vec = new SerializableVector(weVec.getX(), weVec.getY(), weVec.getZ());
                                for (SerializableVector[] area : building.getBuildingAreas(vec, kingdomBuildingData.getAngle(), plugin)) {
                                    Location loc = area[1].getVector().subtract(area[0].getVector()).toLocation(kingdom.getLocation().getWorld());
                                    double length = area[0].getVector().distance(area[1].getVector()) / 2;
                                    for (Entity e : loc.getWorld().getNearbyEntities(loc, length, length, length, entity -> entity instanceof Player)) {
                                        Player player = (Player) e;
                                        if (kingdom.getMembers().containsKey(player.getUniqueId())) {
                                            player.sendTitle("§aPress F to", "§aopen the shop!", 20, 40, 20);
                                        }
                                    }
                                }
                            }
                        }
                    });
                });
            }
        }.runTaskTimerAsynchronously(plugin, 0, 60);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void teamDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) && !(event.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(damager);
        if (kingdom != null && kingdom.getMembers().containsKey(target.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    private final List<Player> pressCooldown = new ArrayList<>();
    @EventHandler(ignoreCancelled = true)
    public void swapPress(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (pressCooldown.contains(player)) {
            return;
        }
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            return;
        }
        pressCooldown.add(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                pressCooldown.remove(player);
            }
        }.runTaskLater(plugin, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                IKingdomTemplate template = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName()).getTemplate(kingdom.getLevel());
                for (ShopData shopData : kingdom.getShops().values()) {
                    if (!shopData.isOwned()) {
                        continue;
                    }
                    IKingdomBuilding building = plugin.getBuildingManager().getBuilding(shopData.getId(), template);
                    if (building != null) {
                        BlockVector3 weVec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ()).subtract(building.getOffset().toBlockPoint());
                        SerializableVector vec = new SerializableVector(weVec.getX(), weVec.getY(), weVec.getZ());
                        for (SerializableVector[] area : building.getBuildingAreas(vec, shopData.getAngle(), plugin)) {
                            Region region = new CuboidRegion(BlockVector3.at(area[0].getX(), area[0].getY(), area[0].getZ()), BlockVector3.at(area[1].getX(), area[1].getY(), area[1].getZ()));
                            if (region.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())) {
                                new ShopGUI(player, shopData, plugin);
                                return;
                            }
                        }
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
