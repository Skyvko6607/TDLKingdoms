package me.sky.kingdoms.base.building;

import com.sk89q.worldedit.math.Vector3;
import me.sky.kingdoms.utils.SerializableVector;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class KingdomTemplate implements IKingdomTemplate {

    private final String id;
    private String schematic;
    private SerializableVector offset;
    private int maxMembers;

    public KingdomTemplate() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public File getSchematicFile() {
        return new File(schematic);
    }

    @Override
    public Vector3 getCenterOffset() {
        return Vector3.at(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public List<IKingdomBuilding> getBuildings() {
        return null;
    }

    @Override
    public int getMaxMembers() {
        return maxMembers;
    }

    @Override
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }
}
