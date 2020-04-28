package me.sky.kingdoms.commands;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.commands.arguments.CreateKingdomArgument;
import me.sky.kingdoms.commands.arguments.JoinKingdomArgument;
import me.sky.kingdoms.commands.arguments.LeaveKingdomArgument;
import me.sky.kingdoms.commands.categories.SchematicCategory;
import me.sky.kingdoms.commands.categories.TemplateCategory;
import me.sky.kingdoms.commands.categories.ThemeCategory;
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
        player.sendMessage("§7§m-+--+--+--+--+--+--+--+-");
        player.sendMessage("");
        onCommand(player, strings, command, plugin);
        player.sendMessage("");
        player.sendMessage("§7§m-+--+--+--+--+--+--+--+-");
        return false;
    }

    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new CreateKingdomArgument(), new JoinKingdomArgument(), new LeaveKingdomArgument(), new ThemeCategory(), new TemplateCategory(), new SchematicCategory());
    }

    @Override
    public String getArgument() {
        return null;
    }

    @Override
    public String getPermission() {
        return "kingdoms.base";
    }
}
