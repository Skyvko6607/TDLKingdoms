package me.sky.kingdoms.base.building.enums;

public enum KingdomBuildingType {
    HOUSE("§aHouse"),
    SHOP("§eShop");

    private final String name;

    KingdomBuildingType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
