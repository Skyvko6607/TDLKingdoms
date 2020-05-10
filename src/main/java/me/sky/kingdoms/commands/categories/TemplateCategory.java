package me.sky.kingdoms.commands.categories;

import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.ICommandCategory;
import me.sky.kingdoms.commands.categories.template.CreateTemplateArgument;
import me.sky.kingdoms.commands.categories.template.edit.BuildingsCategory;
import me.sky.kingdoms.commands.categories.template.edit.MaxMembersTemplateArgument;
import me.sky.kingdoms.commands.categories.template.edit.SchematicTemplateArgument;

import java.util.Arrays;
import java.util.List;

public class TemplateCategory implements ICommandCategory {
    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new CreateTemplateArgument(), new SchematicTemplateArgument(), new MaxMembersTemplateArgument(), new BuildingsCategory(), new SaveThemeArgument());
    }

    @Override
    public String getArgument() {
        return "template";
    }
}
