package me.sky.kingdoms.commands;

import me.sky.kingdoms.IKingdomsPlugin;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public interface ICommandCategory extends ICommandArgument {
    List<ICommandArgument> getCommandArguments();

    @Override
    default void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        for (ICommandArgument argument : getCommandArguments()) {
            if (strings[0].equalsIgnoreCase(argument.getArgument().split(" ")[0])) {
                if (strings.length != argument.getArgument().split(" ").length) {
                    player.sendMessage("/" + command.getName() + " " + argument.getArgument());
                    return;
                }
                if (!player.isOp() && (argument.getPermission() != null && !player.hasPermission(argument.getPermission()))) {
                    return;
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
            player.sendMessage("§7/" + command + (getArgument() != null ? " " + getArgument() : "") + " " + argument.getArgument());
        }
    }
}
