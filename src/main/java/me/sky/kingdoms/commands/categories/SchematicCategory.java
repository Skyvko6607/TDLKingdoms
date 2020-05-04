package me.sky.kingdoms.commands.categories;

import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.ICommandCategory;
import me.sky.kingdoms.commands.categories.schematic.AddSchematicArgument;
import me.sky.kingdoms.commands.categories.schematic.AddBuildingAreaArgument;
import me.sky.kingdoms.commands.categories.schematic.ListBuildingAreaArgument;
import me.sky.kingdoms.commands.categories.schematic.RemoveBuildingAreaArgument;

import java.util.Arrays;
import java.util.List;

public class SchematicCategory implements ICommandCategory {
    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new AddSchematicArgument(), new AddBuildingAreaArgument(), new RemoveBuildingAreaArgument(), new ListBuildingAreaArgument());
    }

    @Override
    public String getArgument() {
        return "schematic";
    }
}
