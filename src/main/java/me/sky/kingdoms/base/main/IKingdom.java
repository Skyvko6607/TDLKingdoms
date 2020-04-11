package me.sky.kingdoms.base.main;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.data.IMemberData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IKingdom {
    String getUuid();
    String getName();
    Map<UUID, IMemberData> getMembers();
    KingdomPrivacy getPrivacy();
    String getThemeName();
    List<IKingdomBuilding> getBuildings();
    int getLevel();
    int getMaxMembers();
}
