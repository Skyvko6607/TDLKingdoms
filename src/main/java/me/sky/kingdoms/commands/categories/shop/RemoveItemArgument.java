package me.sky.kingdoms.commands.categories.shop;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.data.ShopData;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RemoveItemArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "removeitem <slot>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            player.sendMessage(KingdomUtils.PREFIX + "You are not part of a kingdom!");
            return;
        }
        int slot;
        try {
            slot = Integer.parseInt(strings[0]) - 1;
        } catch (Exception e) {
            player.sendMessage(KingdomUtils.PREFIX + "Number is required!");
            return;
        }
        List<ShopData> shops = kingdom.getShopDatasByOwner(player);
        if (shops.isEmpty()) {
            player.sendMessage(KingdomUtils.PREFIX + "You do not own any shops!");
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                IKingdomTemplate template = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName()).getTemplate(kingdom.getLevel());
                for (ShopData shopData : shops) {
                    IKingdomBuilding building = plugin.getBuildingManager().getBuilding(shopData.getId(), template);
                    if (building != null) {
                        BlockVector3 weVec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ()).subtract(building.getOffset().toBlockPoint());
                        SerializableVector vec = new SerializableVector(weVec.getX(), weVec.getY(), weVec.getZ());
                        for (SerializableVector[] area : building.getBuildingAreas(vec, shopData.getAngle(), plugin)) {
                            Region region = new CuboidRegion(BlockVector3.at(area[0].getX(), area[0].getY(), area[0].getZ()), BlockVector3.at(area[1].getX(), area[1].getY(), area[1].getZ()));
                            if (region.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())) {
                                if (slot >= shopData.getSaleItems().size()) {
                                    player.sendMessage(KingdomUtils.PREFIX + "An item in that slot does not exist!");
                                    return;
                                }
                                shopData.getSaleItems().remove(shopData.getSaleItems().keySet().toArray(new String[0])[slot]);
                                return;
                            }
                        }
                    }
                }
                player.sendMessage(KingdomUtils.PREFIX + "You have to be standing in a shop that you own!");
            }
        }.runTaskAsynchronously(plugin);
    }
}
