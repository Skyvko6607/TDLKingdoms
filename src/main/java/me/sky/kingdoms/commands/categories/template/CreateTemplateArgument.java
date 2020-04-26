package me.sky.kingdoms.commands.categories.template;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.categories.template.edit.SchematicTemplateArgument;
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
        if (theme == null) {
            player.sendMessage(KingdomUtils.PREFIX + "This theme doesn't exist!");
            return;
        }
        int level = Integer.parseInt(strings[1]);
        if (!theme.getMainTemplates().containsKey(level)) {
            theme.createTemplate(level);
        }
        player.sendMessage(KingdomUtils.PREFIX + "Successfully created a template!");
        new SchematicTemplateArgument().onCommand(player, strings, command, plugin);
    }
}
