package me.sky.kingdoms.commands.arguments.template.edit;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.commands.ICommandArgument;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class MaxMembersTemplateArgument implements ICommandArgument {
    @Override
    public String getArgument() {
        return "setmaxmembers <theme> <level> <value>";
    }

    @Override
    public void onCommand(Player player, String[] strings, Command command, IKingdomsPlugin plugin) {
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(strings[0]);
        int level = Integer.parseInt(strings[1]);
        if (!theme.getMainTemplates().containsKey(level)) {
            player.sendMessage(KingdomUtils.PREFIX + "This template doesn't exist!");
            return;
        }
        IKingdomTemplate template = theme.getTemplate(level);
        int value = Integer.parseInt(strings[2]);
        template.setMaxMembers(value);
    }
}
