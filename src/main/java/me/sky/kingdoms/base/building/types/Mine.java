package me.sky.kingdoms.base.building.types;

import me.sky.kingdoms.base.building.KingdomBuilding;
import me.sky.kingdoms.utils.SerializableVector;

import java.util.HashMap;
import java.util.Map;

public class Mine extends KingdomBuilding {

    private final Map<String, Double> blocks = new HashMap<>();
    private SerializableVector[] mineOffset;

    public Mine(String id) {
        super(id);
    }

    public SerializableVector[] getMineOffset() {
        return mineOffset;
    }

    public void setMineOffset(SerializableVector[] mineOffset) {
        this.mineOffset = mineOffset;
    }

    public Map<String, Double> getBlocks() {
        return blocks;
    }
}
