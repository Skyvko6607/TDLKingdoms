package me.sky.kingdoms.base.data.buildings.data;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.utils.SerializableLocation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HouseData implements IKingdomBuildingData {

    private final String id;
    private final List<SerializableLocation> placedBlocks = new ArrayList<>();
    private final Map<SerializableLocation, List<ItemStack>> storage = new HashMap<>();

    private boolean owned = false;
    private UUID owner = null;

    public HouseData(IKingdomBuilding building) {
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

    public Map<SerializableLocation, List<ItemStack>> getStorage() {
        return storage;
    }
}
