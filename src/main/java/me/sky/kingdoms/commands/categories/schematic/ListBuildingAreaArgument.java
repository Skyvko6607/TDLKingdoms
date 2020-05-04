package me.sky.kingdoms.commands.categories.schematic;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.SchematicData;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class ListBuildingAreaArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "listbuildingarea <schematic_id>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        SchematicData data = plugin.getBuildingManager().getSchematicData(strings[0]);
        if (data == null) {
            player.sendMessage(KingdomUtils.PREFIX + "Schematic data with this ID doesn't exist!");
            return;
        }
        player.sendMessage(KingdomUtils.PREFIX + "Building areas by index:");
        int i = 0;
        for (SerializableVector[] area : data.getBuildingAreas()) {
            player.sendMessage("§f" + i + ": §7(" + area[0].toString() + "; " + area[1].toString() + ")");
            i++;
        }
    }
}
