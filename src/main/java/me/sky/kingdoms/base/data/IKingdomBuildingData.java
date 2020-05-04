package me.sky.kingdoms.base.data;

import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.utils.SerializableLocation;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IKingdomBuildingData {
    String getId();
    KingdomBuildingType getType();
    List<SerializableLocation> getPlacedBlocks();
    boolean isOwned();
    UUID getOwnedBy();
    int getAngle();
    void setOwned(boolean owned);
    void setOwnedBy(Player player);
    void setOwnedBy(UUID uuid);
    void setAngle(int angle);
}
