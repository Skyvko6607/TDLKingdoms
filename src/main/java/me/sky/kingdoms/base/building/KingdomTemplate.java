package me.sky.kingdoms.base.building;

import com.sk89q.worldedit.math.Vector3;

import java.io.File;
import java.util.List;

public class KingdomTemplate implements IKingdomTemplate {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public File getSchematicFile() {
        return null;
    }

    @Override
    public Vector3 getCenterOffset() {
        return null;
    }

    @Override
    public List<IKingdomBuilding> getBuildings() {
        return null;
    }
}
