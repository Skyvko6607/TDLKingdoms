package me.sky.kingdoms.commands;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public interface ICommandCategory extends ICommandArgument {
    List<ICommandArgument> getCommandArguments();

    @Override
    default void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        if (strings.length == 0) {
            sendArgumentsList(player, command.getName());
            return;
        }
        for (ICommandArgument argument : getCommandArguments()) {
            if (strings[0].equalsIgnoreCase(argument.getArgument().split(" ")[0])) {
                if (!(argument instanceof ICommandCategory)) {
                    if (strings.length != argument.getArgument().split(" ").length) {
                        player.sendMessage(KingdomUtils.PREFIX + "§7Correct usage: §f/" + command.getName() + " " + argument.getArgument());
                        return;
                    }
                    if (!player.isOp() && (argument.getPermission() != null && !player.hasPermission(argument.getPermission()))) {
                        player.sendMessage(KingdomUtils.PREFIX + "You don't have enough permissions to use this!");
                        return;
                    }
                }
                argument.onCommand(player, Arrays.copyOfRange(strings, 1, strings.length), command, plugin);
                return;
            }
        }
        sendArgumentsList(player, command.getName());
    }

    default ICommandArgument getCommandArgumentById(String id) {
        for (ICommandArgument argument : getCommandArguments()) {
            if (argument.getArgument().split(" ")[0].equalsIgnoreCase(id)) {
                return argument;
            } else if (argument instanceof ICommandCategory) {
                if (argument.getArgument().split(" ")[0].equalsIgnoreCase(id)) {
                    return argument;
                }
            }
        }
        return null;
    }

    default void sendArgumentsList(Player player, String command) {
        for (ICommandArgument argument : getCommandArguments()) {
            if (getPermission() == null || player.hasPermission(getPermission()) || player.isOp()) {
                player.sendMessage("  §f- §b/" + command + (getArgument() != null ? " " + getArgument() : "") + " " + argument.getArgument());
            }
        }
    }
}
