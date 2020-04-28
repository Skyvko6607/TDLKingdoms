package me.sky.kingdoms.commands.categories;

import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.ICommandCategory;
import me.sky.kingdoms.commands.categories.schematic.AddSchematicArgument;
import me.sky.kingdoms.commands.categories.template.CreateTemplateArgument;
import me.sky.kingdoms.commands.categories.template.EditTemplateCategory;

import java.util.Arrays;
import java.util.List;

public class SchematicCategory implements ICommandCategory {
    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new AddSchematicArgument());
    }

    @Override
    public String getArgument() {
        return "schematic";
    }
}
