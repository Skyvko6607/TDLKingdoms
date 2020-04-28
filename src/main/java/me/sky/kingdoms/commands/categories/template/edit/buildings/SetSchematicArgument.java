package me.sky.kingdoms.commands.categories.template.edit.buildings;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SetSchematicArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "setschematic <theme> <level> <building_id> <schematic_id>";
    }

    @SuppressWarnings("deprecation")
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
        IKingdomBuilding building = plugin.getBuildingManager().getBuilding(strings[2], template);
        if (building == null) {
            player.sendMessage(KingdomUtils.PREFIX + "A building with that ID doesn't exist!");
            return;
        }
        String schematicData = strings[3];
        if (plugin.getBuildingManager().getSchematicData(schematicData) == null) {
            player.sendMessage(KingdomUtils.PREFIX + "Schematic data with that ID doesn't exist!");
            return;
        }
        building.setSchematicData(schematicData);
        player.sendMessage(KingdomUtils.PREFIX + "Schematic data set!");
        theme.save(plugin);
    }
}
