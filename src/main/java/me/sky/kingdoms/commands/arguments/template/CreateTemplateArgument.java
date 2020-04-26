package me.sky.kingdoms.commands.arguments.template;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.ICommandCategory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CreateTemplateArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "create <theme> <level>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(strings[0]);
        int level = Integer.parseInt(strings[1]);
        if (!theme.getMainTemplates().containsKey(level)) {
            theme.createTemplate(level);
        }
        player.sendMessage(KingdomUtils.PREFIX + "Successfully created a template!");
        ((ICommandCategory) plugin.getServer().getPluginCommand("kingdom").getExecutor()).getCommandArgumentById("setschematic").onCommand(player, strings, command, plugin);
    }
}
