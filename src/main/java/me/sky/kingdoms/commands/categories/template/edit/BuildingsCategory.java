package me.sky.kingdoms.commands.categories.template.edit;

import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.ICommandCategory;
import me.sky.kingdoms.commands.categories.template.edit.buildings.*;

import java.util.Arrays;
import java.util.List;

public class BuildingsCategory implements ICommandCategory {
    @Override
    public String getArgument() {
        return "buildings";
    }

    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new AddBuildingArgument(), new RemoveBuildingArgument(), new ListBuildingArgument(), new AddBuildingAreaArgument(), new RemoveBuildingAreaArgument(), new ListBuildingAreaArgument());
    }
}
