package me.sky.kingdoms.commands.arguments;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.main.objects.KingdomPrivacy;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class JoinKingdomArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "join <name>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByName(strings[0]);
        if (kingdom == null) {
            player.sendMessage(KingdomUtils.PREFIX + "This kingdom doesn't exist!");
            return;
        }
        if (kingdom.getPrivacy() == KingdomPrivacy.PRIVATE) {
            player.sendMessage(KingdomUtils.PREFIX + "This kingdom is private.");
            return;
        }
        if (kingdom.getMembers().size() >= plugin.getThemeManager().getThemeFromId(kingdom.getThemeName()).getTemplate(kingdom.getLevel()).getMaxMembers()) {
            player.sendMessage(KingdomUtils.PREFIX + "The kingdom is currently full! Try again later.");
            return;
        }
        kingdom.addMember(player);
        plugin.getKingdomManager().saveKingdom(kingdom);
        player.sendMessage(KingdomUtils.PREFIX + "You have successfully joined the kingdom!");
    }
}
