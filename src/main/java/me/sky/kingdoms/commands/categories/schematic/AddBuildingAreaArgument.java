package me.sky.kingdoms.commands.categories.schematic;

import com.boydti.fawe.Fawe;
import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.schematic.SchematicData;
import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class AddBuildingAreaArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "addbuildingarea <schematic_id>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        SchematicData data = plugin.getBuildingManager().getSchematicData(strings[0]);
        if (data == null) {
            player.sendMessage(KingdomUtils.PREFIX + "Schematic data with this ID doesn't exist!");
            return;
        }
        Region selection = Fawe.get().getWorldEdit().getSessionManager().findByName(player.getName()).getSelection(FaweAPI.getWorld(player.getWorld().getName()));
        if (selection == null) {
            player.sendMessage(KingdomUtils.PREFIX + "No selection!");
            return;
        }
        Vector3 offset = Vector3.at(data.getLocation().getX(), data.getLocation().getY(), data.getLocation().getZ());
        Vector3 min = offset.subtract(selection.getMinimumPoint().toVector3());
        Vector3 max = offset.subtract(selection.getMaximumPoint().toVector3());
        data.getBuildingAreas().add(new SerializableVector[] {
                new SerializableVector(min.getX(), min.getY(), min.getZ()),
                new SerializableVector(max.getX(), max.getY(), max.getZ())
        });
        plugin.getBuildingManager().saveSchematicData();
        player.sendMessage(KingdomUtils.PREFIX + "Building area added to the building!");
    }
}
