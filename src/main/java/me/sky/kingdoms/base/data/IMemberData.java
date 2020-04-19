package me.sky.kingdoms.base.data;

import me.sky.kingdoms.base.main.objects.KingdomRank;

import java.util.List;

public interface IMemberData {
    KingdomRank getRank();
    List<String> getOwningBuildings();
    void setRank(KingdomRank rank);
}
