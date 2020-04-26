package me.sky.kingdoms.commands;

import me.sky.kingdoms.IKingdomsPlugin;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface ICommandArgument {
    String getArgument();

    void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin);

    default String getPermission() {
        return null;
    }
}
