package me.sky.kingdoms.commands.arguments;

import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.ICommandCategory;
import me.sky.kingdoms.commands.arguments.template.CreateTemplateArgument;
import me.sky.kingdoms.commands.arguments.template.EditTemplateCategory;

import java.util.Arrays;
import java.util.List;

public class TemplateCategory implements ICommandCategory {
    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new CreateTemplateArgument(), new EditTemplateCategory());
    }

    @Override
    public String getArgument() {
        return "template";
    }
}
