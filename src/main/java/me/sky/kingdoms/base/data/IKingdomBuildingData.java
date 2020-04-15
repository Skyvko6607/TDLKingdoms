package me.sky.kingdoms.base.data;

import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.utils.JsonLocation;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IKingdomBuildingData {
    String getId();
    KingdomBuildingType getType();
    List<JsonLocation> getPlacedBlocks();
    boolean isOwned();
    UUID getOwnedBy();
    void setOwned(boolean owned);
    void setOwnedBy(Player player);
}
