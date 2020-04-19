package me.sky.kingdoms.base.data.member;

import me.sky.kingdoms.base.data.IMemberData;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MemberData implements IMemberData {

    private final String uuid;
    private KingdomRank rank;
    private final List<String> owningBuildings;

    public MemberData(Player player, KingdomRank rank) {
        this.uuid = player.getUniqueId().toString();
        this.rank = rank;
        this.owningBuildings = new ArrayList<>();
    }

    @Override
    public KingdomRank getRank() {
        return rank;
    }

    @Override
    public List<String> getOwningBuildings() {
        return owningBuildings;
    }

    @Override
    public void setRank(KingdomRank rank) {
        this.rank = rank;
    }
}
