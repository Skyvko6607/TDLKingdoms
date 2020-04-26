package me.sky.kingdoms.commands.arguments;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.gui.creator.KingdomCreator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class LeaveKingdomArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "leave";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            player.sendMessage(KingdomUtils.PREFIX + "You are not part of a kingdom!");
            return;
        }
        kingdom.removeMember(player);
        plugin.getKingdomManager().saveKingdom(kingdom);
        player.sendMessage(KingdomUtils.PREFIX + "Successfully left kingdom!");
    }
}
