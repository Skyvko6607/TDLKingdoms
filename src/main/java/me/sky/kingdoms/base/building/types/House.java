package me.sky.kingdoms.base.building.types;

import me.sky.kingdoms.base.building.KingdomBuilding;
import me.sky.kingdoms.base.building.enums.KingdomBuildingType;
import me.sky.kingdoms.base.data.objects.IValuedBuilding;

public class House extends KingdomBuilding implements IValuedBuilding {

    private int buyPrice, sellPrice;

    public House(String id) {
        super(id);
        setType(KingdomBuildingType.HOUSE);
    }

    @Override
    public int getBuyPrice() {
        return buyPrice;
    }

    @Override
    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public void setBuyPrice(int price) {
        this.buyPrice = price;
    }

    @Override
    public void setSellPrice(int price) {
        this.sellPrice = price;
    }
}
