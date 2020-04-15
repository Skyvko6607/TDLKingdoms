package me.sky.kingdoms.base.data.buildings;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.utils.JsonLocation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class House implements IKingdomBuildingData {

    private final String id;

    private List<JsonLocation> placedBlocks = new ArrayList<>();
    private boolean owned = false;
    private UUID owner = null;
    private Map<JsonLocation, List<ItemStack>> storage = new HashMap<>();

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
        return owned;
    }

    @Override
    public UUID getOwnedBy() {
        return owner;
    }

    @Override
    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    @Override
    public void setOwnedBy(Player player) {
        this.owner = player.getUniqueId();
    }

    public Map<JsonLocation, List<ItemStack>> getStorage() {
        return storage;
    }
}
