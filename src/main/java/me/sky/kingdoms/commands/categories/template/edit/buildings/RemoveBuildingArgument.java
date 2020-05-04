package me.sky.kingdoms.commands.categories.template.edit.buildings;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class RemoveBuildingArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "remove <theme> <level> <id>";
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
                template.getBuildings().remove(building);
                player.sendMessage(KingdomUtils.PREFIX + "Building successfully removed!");
                return;
            }
        }
        player.sendMessage(KingdomUtils.PREFIX + "Building with this ID doesn't exist!");
    }
}
