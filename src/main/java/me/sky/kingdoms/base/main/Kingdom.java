package me.sky.kingdoms.base.main;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.data.IMemberData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Kingdom implements IKingdom {
    @Override
    public String getUuid() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Map<UUID, IMemberData> getMembers() {
        return null;
    }

    @Override
    public KingdomPrivacy getPrivacy() {
        return null;
    }

    @Override
    public String getThemeName() {
        return null;
    }

    @Override
    public List<IKingdomBuilding> getBuildings() {
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getMaxMembers() {
        return 0;
    }
}
