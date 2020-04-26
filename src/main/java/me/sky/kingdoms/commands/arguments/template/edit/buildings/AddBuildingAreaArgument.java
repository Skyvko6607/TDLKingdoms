package me.sky.kingdoms.commands.arguments.template.edit.buildings;

import com.boydti.fawe.Fawe;
import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.IKingdomTemplate;
import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.base.data.buildings.types.House;
import me.sky.kingdoms.base.data.buildings.types.KingdomBuilding;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddBuildingAreaArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "addbuildingarea <theme> <level> <id>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(strings[0]);
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
        Region selection = Fawe.get().getWorldEdit().getSessionManager().findByName(player.getName()).getSelection(FaweAPI.getWorld(player.getWorld().getName()));
        if (selection == null) {
            player.sendMessage(KingdomUtils.PREFIX + "No selection!");
            return;
        }
        building.getBuildingAreas().add(new SerializableVector[] {
                new SerializableVector(selection.getMinimumPoint().getX(), selection.getMinimumPoint().getY(), selection.getMinimumPoint().getZ()),
                new SerializableVector(selection.getMaximumPoint().getX(), selection.getMaximumPoint().getY(), selection.getMaximumPoint().getZ())
        });
        player.sendMessage(KingdomUtils.PREFIX + "Building area added to the building!");
    }
}
