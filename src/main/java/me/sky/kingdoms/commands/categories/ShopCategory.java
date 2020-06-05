package me.sky.kingdoms.commands.categories;

import me.sky.kingdoms.commands.ICommandArgument;
import me.sky.kingdoms.commands.ICommandCategory;
import me.sky.kingdoms.commands.categories.shop.AddItemArgument;
import me.sky.kingdoms.commands.categories.shop.RemoveItemArgument;

import java.util.Arrays;
import java.util.List;

public class ShopCategory implements ICommandCategory {
    @Override
    public List<ICommandArgument> getCommandArguments() {
        return Arrays.asList(new AddItemArgument(), new RemoveItemArgument());
    }

    @Override
    public String getArgument() {
        return "shop";
    }
}
