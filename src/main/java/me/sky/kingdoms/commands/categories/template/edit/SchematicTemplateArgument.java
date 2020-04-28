package me.sky.kingdoms.commands.categories.template.edit;

import com.boydti.fawe.Fawe;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.template.IKingdomTemplate;
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
        ClipboardHolder holder = Fawe.get().getWorldEdit().getSessionManager().get(new BukkitPlayer(player)).getExistingClipboard();
        if (holder == null || holder.getClipboard() == null) {
            player.sendMessage(KingdomUtils.PREFIX + "The clipboard is empty! Do //copy to create a clipboard.");
            return;
        }
        Clipboard clipboard = holder.getClipboard();
        File dir = new File("plugins/" + plugin.getName() + "/schematics");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File("plugins/" + plugin.getName() + "/schematics/" + theme.getId() + "-" + level + ".schematic");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            theme.getMainTemplates().remove(level);
            player.sendMessage(KingdomUtils.PREFIX + "Failed to create file!");
            return;
        }
        try (ClipboardWriter writer = BuiltInClipboardFormat.MINECRAFT_STRUCTURE.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (IOException e) {
            e.printStackTrace();
            theme.getMainTemplates().remove(level);
            player.sendMessage(KingdomUtils.PREFIX + "Failed to save schematic!");
            return;
        }
        template.setLocation(clipboard.getOrigin());
        template.setSchematic(file);
        template.setCenterOffset(clipboard.getOrigin().subtract(clipboard.getMinimumPoint()));
        player.sendMessage(KingdomUtils.PREFIX + "Schematic successfully set!");
    }
}
