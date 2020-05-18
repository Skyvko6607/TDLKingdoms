package me.sky.kingdoms.commands.categories.template.edit.buildings;

import com.sk89q.worldedit.math.BlockVector3;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.enums.KingdomBuildingType;
import me.sky.kingdoms.base.building.types.House;
import me.sky.kingdoms.base.building.KingdomBuilding;
import me.sky.kingdoms.base.building.types.Shop;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class AddBuildingArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "add <theme> <level> <building_id> <house/shop>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(strings[0]);
        if (theme == null) {
            player.sendMessage(KingdomUtils.PREFIX + "This theme doesn't exist!");
            return;
        }
        int level = Integer.parseInt(strings[1]);
        if (!theme.getMainTemplates().containsKey(level)) {
            player.sendMessage(KingdomUtils.PREFIX + "This template doesn't exist!");
            return;
        }
        IKingdomTemplate template = theme.getTemplate(level);
        String id = strings[2];
        for (IKingdomBuilding building : template.getBuildings()) {
            if (building.getId().equalsIgnoreCase(id)) {
                player.sendMessage(KingdomUtils.PREFIX + "A building with this ID already exists!");
                return;
            }
        }
        KingdomBuildingType type = KingdomBuildingType.valueOf(strings[3].toUpperCase());
        KingdomBuilding building = null;
        if (type == KingdomBuildingType.HOUSE) {
            building = new House(id);
        } else if (type == KingdomBuildingType.SHOP) {
            building = new Shop(id);
        }
        BlockVector3 offset = BlockVector3.at(template.getLocation().getX(), template.getLocation().getY(), template.getLocation().getZ());
        building.setOffset(offset.subtract(BlockVector3.at(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())).toVector3());
        building.setDirection(Direction.getDirection(player.getLocation().getYaw()));
//        building.setName(ChatColor.translateAlternateColorCodes('&', strings[4]));
        template.getBuildings().add(building);
        player.sendMessage(KingdomUtils.PREFIX + "Building added!");
        player.sendMessage(KingdomUtils.PREFIX + "Use /kingdom template edit buildings setschematic <theme> <level> <building_id> <schematic_id> to set the schematic for the house!");
    }
}
