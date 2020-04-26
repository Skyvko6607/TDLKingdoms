package me.sky.kingdoms.commands.categories.theme;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.base.theme.KingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CreateThemeArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "create <theme> <name>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        if (plugin.getThemeManager().getThemeFromId(strings[0]) != null) {
            player.sendMessage(KingdomUtils.PREFIX + "A theme with this ID already exists!");
            return;
        }
        IKingdomTheme theme = new KingdomTheme(strings[0]);
        theme.setName(ChatColor.translateAlternateColorCodes('&', strings[1]));
        theme.setSlot(10);
        theme.setIcon(new ItemStack(Material.PAPER));
        plugin.getThemeManager().getThemes().add(theme);
        theme.save(plugin);
        player.sendMessage(KingdomUtils.PREFIX + "Successfully created theme!");
    }
}
