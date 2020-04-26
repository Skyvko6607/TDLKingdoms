package me.sky.kingdoms.commands.categories.template.edit.buildings;

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
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddBuildingArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "add <theme> <level> <id> <house/shop> <name>";
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
        Region selection = Fawe.get().getWorldEdit().getSessionManager().findByName(player.getName()).getSelection(FaweAPI.getWorld(player.getWorld().getName()));
        if (selection == null) {
            player.sendMessage(KingdomUtils.PREFIX + "No selection!");
            return;
        }
        Clipboard clipboard = new BlockArrayClipboard(selection);
        clipboard.setOrigin(BlockVector3.at(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
        File file = new File("plugins/" + plugin.getName() + "/schematics/" + theme.getName() + "-" + level + "-" + id + ".schematic");
        try (ClipboardWriter writer = BuiltInClipboardFormat.MINECRAFT_STRUCTURE.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        KingdomBuilding building = null;
        if (type == KingdomBuildingType.HOUSE) {
            building = new House(id, file);
        } else {
            building = new KingdomBuilding(id, file);
        }
        building.setOffset(clipboard.getOrigin()
                .subtract(clipboard.getMinimumPoint()).toVector3());
        building.setDirection(Direction.getDirection(player.getLocation().getYaw()));
        building.setName(ChatColor.translateAlternateColorCodes('&', strings[4]));
        template.getBuildings().add(building);
        player.sendMessage(KingdomUtils.PREFIX + "Building added!");
    }
}
