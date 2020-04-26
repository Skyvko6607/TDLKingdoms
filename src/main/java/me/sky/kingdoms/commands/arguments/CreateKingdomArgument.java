package me.sky.kingdoms.commands.arguments;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.gui.creator.KingdomCreator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CreateKingdomArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "create <name>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        String name = strings[0];
        if (plugin.getKingdomManager().getKingdomByName(name) != null) {
            player.sendMessage(KingdomUtils.PREFIX + "A kingdom with this name already exists!");
            return;
        }
        new KingdomCreator(player, ChatColor.stripColor(name), plugin);
    }
}
