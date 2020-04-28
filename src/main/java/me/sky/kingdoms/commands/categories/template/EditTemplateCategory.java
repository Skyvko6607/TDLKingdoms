package me.sky.kingdoms.commands.categories.template;

import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.ICommandCategory;
import me.sky.kingdoms.commands.categories.template.edit.BuildingsCategory;
import me.sky.kingdoms.commands.categories.template.edit.MaxMembersTemplateArgument;
import me.sky.kingdoms.commands.categories.template.edit.SchematicTemplateArgument;

import java.util.Arrays;
import java.util.List;

public class EditTemplateCategory implements ICommandCategory {
    @Override
    public String getArgument() {
        return "edit";
    }

    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new SchematicTemplateArgument(), new MaxMembersTemplateArgument(), new BuildingsCategory());
    }
}