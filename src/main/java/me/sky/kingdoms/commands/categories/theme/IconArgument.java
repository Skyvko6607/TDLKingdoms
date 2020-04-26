package me.sky.kingdoms.commands.categories.theme;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IconArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "seticon <theme>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(strings[0]);
        if (theme == null) {
            player.sendMessage(KingdomUtils.PREFIX + "This theme doesn't exist!");
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage(KingdomUtils.PREFIX + "You are not holding any item!");
            return;
        }
        theme.setIcon(item.clone());
        theme.save(plugin);
        player.sendMessage(KingdomUtils.PREFIX + "Successfully set the description of the theme!");
    }
}
