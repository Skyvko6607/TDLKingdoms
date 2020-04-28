package me.sky.kingdoms.base.data.member;

import me.sky.kingdoms.base.data.IMemberData;
import me.sky.kingdoms.base.building.data.KingdomBuildingData;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemberData implements IMemberData {

    private final String uuid;
    private String name;
    private KingdomRank rank;
    private final List<KingdomBuildingData> owningBuildings;

    public MemberData(Player player, KingdomRank rank) {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
        this.rank = rank;
        this.owningBuildings = new ArrayList<>();
    }

    @Override
    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public KingdomRank getRank() {
        return rank;
    }

    @Override
    public List<KingdomBuildingData> getOwningBuildings() {
        return owningBuildings;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRank(KingdomRank rank) {
        this.rank = rank;
    }
}
