package me.sky.kingdoms.commands.categories;

import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.ICommandCategory;
import me.sky.kingdoms.commands.categories.theme.CreateThemeArgument;
import me.sky.kingdoms.commands.categories.theme.DescriptionArgument;
import me.sky.kingdoms.commands.categories.theme.IconArgument;
import me.sky.kingdoms.commands.categories.theme.NameArgument;

import java.util.Arrays;
import java.util.List;

public class ThemeCategory implements ICommandCategory {
    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new CreateThemeArgument(), new NameArgument(), new DescriptionArgument(), new IconArgument(), new SaveThemeArgument());
    }

    @Override
    public String getArgument() {
        return "theme";
    }
}
