package me.sky.kingdoms.base.building.data;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.utils.SerializableLocation;
import org.bukkit.entity.Player;

import java.util.*;

public class KingdomBuildingData implements IKingdomBuildingData {

    private final String id;
    private final List<SerializableLocation> placedBlocks = new ArrayList<>();

    private boolean owned = false;
    private String owner = null;

    public KingdomBuildingData(IKingdomBuilding building) {
        this.id = building.getId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public KingdomBuildingType getType() {
        return KingdomBuildingType.HOUSE;
    }

    @Override
    public List<SerializableLocation> getPlacedBlocks() {
        return placedBlocks;
    }

    @Override
    public boolean isOwned() {
        return owned;
    }

    @Override
    public UUID getOwnedBy() {
        return UUID.fromString(owner);
    }

    @Override
    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    @Override
    public void setOwnedBy(Player player) {
        this.owner = player.getUniqueId().toString();
    }
}
