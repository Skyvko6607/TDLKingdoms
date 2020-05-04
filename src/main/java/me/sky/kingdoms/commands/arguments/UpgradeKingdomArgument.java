package me.sky.kingdoms.commands.arguments;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class UpgradeKingdomArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "upgrade";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdom kingdom = plugin.getKingdomManager().getKingdomByPlayer(player);
        if (kingdom == null) {
            player.sendMessage(KingdomUtils.PREFIX + "You are not part of a kingdom!");
            return;
        }
        if (kingdom.getMembers().get(player.getUniqueId()).getRank() != KingdomRank.CO_RULER) {
            player.sendMessage(KingdomUtils.PREFIX + "Only the co-ruler can upgrade the kingdom!");
            return;
        }
        plugin.getBuildingManager().upgradeKingdom(kingdom, player);
    }
}
