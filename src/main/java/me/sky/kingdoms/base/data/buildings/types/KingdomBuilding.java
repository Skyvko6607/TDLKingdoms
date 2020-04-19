package me.sky.kingdoms.base.data.buildings.types;

import com.sk89q.worldedit.math.Vector3;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.utils.SerializableVector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KingdomBuilding implements IKingdomBuilding {

    private final String id, schematic;
    private final List<SerializableVector[]> buildingAreas;
    private String name = "New Building";
    private KingdomBuildingType type;
    private SerializableVector offset;
    private Direction direction;

    public KingdomBuilding(String id, File schematic) {
        this.id = id;
        this.schematic = schematic.getPath();
        this.buildingAreas = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public KingdomBuildingType getType() {
        return type;
    }

    @Override
    public Vector3 getOffset() {
        return Vector3.at(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public List<SerializableVector[]> getBuildingAreas() {
        return buildingAreas;
    }

    @Override
    public File getSchematicFile() {
        return new File(schematic);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setOffset(Vector3 offset) {
        this.offset = new SerializableVector(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public void setType(KingdomBuildingType type) {
        this.type = type;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void addBuildingArea(SerializableVector[] area) {
        this.buildingAreas.add(area);
    }
}
