package me.sky.kingdoms.base.data;

import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.utils.JsonLocation;

import java.util.List;
import java.util.UUID;

public interface IKingdomBuildingData {
    String getId();
    KingdomBuildingType getType();
    List<JsonLocation> getPlacedBlocks();
    boolean isOwned();
    UUID getOwnedBy();
}
