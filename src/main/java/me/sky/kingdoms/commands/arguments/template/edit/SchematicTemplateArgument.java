package me.sky.kingdoms.commands.arguments.template.edit;

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
import me.sky.kingdoms.base.building.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SchematicTemplateArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "setschematic <theme> <level>";
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
        Region selection = Fawe.get().getWorldEdit().getSessionManager().findByName(player.getName()).getSelection(FaweAPI.getWorld(player.getWorld().getName()));
        if (selection == null) {
            player.sendMessage(KingdomUtils.PREFIX + "No selection!");
            return;
        }
        Clipboard clipboard = new BlockArrayClipboard(selection);
        clipboard.setOrigin(BlockVector3.at(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
        File file = new File("plugins/" + plugin.getName() + "/schematics/" + theme.getName() + "-" + level + ".schematic");
        try (ClipboardWriter writer = BuiltInClipboardFormat.MINECRAFT_STRUCTURE.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (IOException e) {
            e.printStackTrace();
            theme.getMainTemplates().remove(level);
            return;
        }
        template.setSchematic(file);
        template.setCenterOffset(clipboard.getOrigin());
    }
}
