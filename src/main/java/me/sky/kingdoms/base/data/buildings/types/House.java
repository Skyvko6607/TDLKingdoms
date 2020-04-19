package me.sky.kingdoms.base.data.buildings.types;

import me.sky.kingdoms.base.data.objects.IValuedBuilding;

import java.io.File;

public class House extends KingdomBuilding implements IValuedBuilding {

    private int buyPrice, sellPrice;

    public House(String id, File schematic) {
        super(id, schematic);
    }

    @Override
    public int getBuyPrice() {
        return buyPrice;
    }

    @Override
    public int getSellPrice() {
        return sellPrice;
    }
}
