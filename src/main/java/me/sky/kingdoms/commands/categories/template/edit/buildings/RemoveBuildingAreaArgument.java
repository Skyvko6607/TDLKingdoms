package me.sky.kingdoms.commands.categories.template.edit.buildings;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class RemoveBuildingAreaArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "removebuildingarea <theme> <level> <id> <index>";
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
        IKingdomBuilding building = null;
        for (IKingdomBuilding b : template.getBuildings()) {
            if (b.getId().equalsIgnoreCase(id)) {
                building = b;
                break;
            }
        }
        if (building == null) {
            player.sendMessage(KingdomUtils.PREFIX + "Building with this ID doesn't exist!");
            return;
        }
        int index = Integer.parseInt(strings[3]);
        if (index >= building.getBuildingAreas().size()) {
            player.sendMessage(KingdomUtils.PREFIX + "The index exceeds the size of the building areas list!");
            return;
        }
        building.getBuildingAreas().remove(index);
        player.sendMessage(KingdomUtils.PREFIX + "Building area removed!");
    }
}
