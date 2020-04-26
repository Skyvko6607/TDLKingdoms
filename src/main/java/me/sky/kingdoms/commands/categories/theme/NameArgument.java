package me.sky.kingdoms.commands.categories.theme;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class NameArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "setname <theme> <name>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(strings[0]);
        if (theme == null) {
            player.sendMessage(KingdomUtils.PREFIX + "This theme doesn't exist!");
            return;
        }
        theme.setName(ChatColor.translateAlternateColorCodes('&', strings[1]));
        theme.save(plugin);
        player.sendMessage(KingdomUtils.PREFIX + "Successfully set the name of the theme!");
    }
}
