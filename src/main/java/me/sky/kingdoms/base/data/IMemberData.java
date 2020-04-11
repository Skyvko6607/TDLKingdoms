package me.sky.kingdoms.base.data;

import me.sky.kingdoms.base.main.KingdomRank;

import java.util.List;

public interface IMemberData {
    KingdomRank getRank();
    List<String> getOwningBuildings();
}
