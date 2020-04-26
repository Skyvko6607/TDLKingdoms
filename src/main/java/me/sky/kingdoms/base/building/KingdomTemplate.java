package me.sky.kingdoms.base.building;

import com.sk89q.worldedit.math.BlockVector3;
import me.sky.kingdoms.base.data.buildings.types.KingdomBuilding;
import me.sky.kingdoms.utils.SerializableVector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KingdomTemplate implements IKingdomTemplate {

    private final String id;
    private String schematic;
    private SerializableVector offset;
    private int maxMembers;

    private final List<KingdomBuilding> buildings = new ArrayList<>();

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
    public BlockVector3 getCenterOffset() {
        return BlockVector3.at(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public List<KingdomBuilding> getBuildings() {
        return buildings;
    }

    @Override
    public int getMaxMembers() {
        return maxMembers;
    }

    @Override
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    @Override
    public void setCenterOffset(BlockVector3 vector) {
        this.offset = new SerializableVector(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void setSchematic(File schematic) {
        this.schematic = schematic.getPath();
    }
}
