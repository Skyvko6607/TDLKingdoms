package me.sky.kingdoms.commands.categories.schematic;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.schematic.SchematicData;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class RemoveBuildingAreaArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "removebuildingarea <schematic_id> <index>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        SchematicData data = plugin.getBuildingManager().getSchematicData(strings[0]);
        if (data == null) {
            player.sendMessage(KingdomUtils.PREFIX + "Schematic data with this ID doesn't exist!");
            return;
        }
        int index = Integer.parseInt(strings[1]);
        if (index >= data.getBuildingAreas().size()) {
            player.sendMessage(KingdomUtils.PREFIX + "The index exceeds the size of the building areas list!");
            return;
        }
        data.getBuildingAreas().remove(index);
        plugin.getBuildingManager().saveSchematicData();
        player.sendMessage(KingdomUtils.PREFIX + "Building area removed!");
    }
}
