package me.sky.kingdoms.base.data.buildings;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.utils.JsonLocation;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.builder.ToStringStyle;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class House implements IKingdomBuildingData {

    private final String id;

    private List<JsonLocation> placedBlocks = new ArrayList<>();
    private boolean isOwned = false;
    private UUID owner = null;
    private Map<Block, Inventory> storage = new HashMap<>();

    public House(IKingdomBuilding building) {
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
    public List<JsonLocation> getPlacedBlocks() {
        return placedBlocks;
    }

    @Override
    public boolean isOwned() {
        return isOwned;
    }

    @Override
    public UUID getOwnedBy() {
        return owner;
    }

    public Map<Block, Inventory> getStorage() {
        return storage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("placedBlocks", placedBlocks)
                .append("isOwned", isOwned)
                .append("owner", owner)
                .append("storage", storage)
                .toString();
    }
}
