package me.sky.kingdoms.commands.arguments;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.main.objects.KingdomPrivacy;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.Player;

public class SetPrivacyArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "setprivacy <privacy>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            player.sendMessage(KingdomUtils.PREFIX + "You are not part of a kingdom!");
            return;
        }
        if (kingdom.getMembers().get(player.getUniqueId()).getRank() != KingdomRank.CO_RULER) {
            player.sendMessage(KingdomUtils.PREFIX + "Only the co-ruler can set the privacy of the kingdom!");
            return;
        }
        if (!EnumUtils.isValidEnum(KingdomPrivacy.class, strings[0].toUpperCase())) {
            player.sendMessage(KingdomUtils.PREFIX + "This type of privacy doesn't exist. Please choose between these two: PUBLIC - PRIVATE");
            return;
        }
        kingdom.setPrivacy(KingdomPrivacy.valueOf(strings[0].toUpperCase()));
        plugin.getKingdomManager().saveKingdom(kingdom);
    }
}
