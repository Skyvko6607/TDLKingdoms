package me.sky.kingdoms.commands;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.commands.arguments.CreateKingdomArgument;
import me.sky.kingdoms.commands.arguments.TemplateCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class KingdomCommand implements CommandExecutor, ICommandCategory {

    private final IKingdomsPlugin plugin;

    public KingdomCommand(IKingdomsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player player = (Player) commandSender;
        onCommand(player, strings, command, plugin);
        return false;
    }

    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new CreateKingdomArgument(), new TemplateCategory());
    }

    @Override
    public String getArgument() {
        return null;
    }
}
