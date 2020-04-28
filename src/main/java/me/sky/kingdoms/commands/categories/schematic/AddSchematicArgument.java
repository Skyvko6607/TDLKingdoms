package me.sky.kingdoms.commands.categories.schematic;

import com.boydti.fawe.Fawe;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.SchematicData;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddSchematicArgument implements ICommandArgument {

    @Override
    public String getArgument() {
        return "add <id>";
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        ClipboardHolder holder = Fawe.get().getWorldEdit().getSessionManager().get(new BukkitPlayer(player)).getExistingClipboard();
        if (holder == null || holder.getClipboard() == null) {
            player.sendMessage(KingdomUtils.PREFIX + "The clipboard is empty! Do //copy to create a clipboard.");
            return;
        }
        Clipboard clipboard = holder.getClipboard();
        File file = new File("plugins/" + plugin.getName() + "/schematics/" + strings[0] + ".schematic");
        try (ClipboardWriter writer = BuiltInClipboardFormat.MINECRAFT_STRUCTURE.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        BlockVector3 origin = clipboard.getOrigin();
        SchematicData data = new SchematicData(strings[0], file, Direction.getDirection(player.getLocation().getYaw()));
        Vector3 vec = origin.subtract(clipboard.getMinimumPoint()).toVector3();
        data.setOffset(new SerializableVector(vec.getX(), vec.getY(), vec.getZ()));
        plugin.getBuildingManager().getSchematicData().add(data);
        plugin.getBuildingManager().saveSchematicData();
        player.sendMessage(KingdomUtils.PREFIX + "Schematic data successfully added!");
    }
}
