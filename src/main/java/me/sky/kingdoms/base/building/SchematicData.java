package me.sky.kingdoms.base.building;

import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.utils.SerializableVector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SchematicData {

    private String id;
    private String schematic;
    private String direction;
    private SerializableVector offset;
    private List<SerializableVector[]> buildingAreas = new ArrayList<>();

    public SchematicData(String id, File schematic, Direction direction) {
        this.id = id;
        this.schematic = schematic.getPath();
        this.direction = direction.name();
    }

    public String getId() {
        return id;
    }

    public File getSchematic() {
        return new File(schematic);
    }

    public Direction getDirection() {
        return Direction.valueOf(direction);
    }

    public SerializableVector getOffset() {
        return offset;
    }

    public List<SerializableVector[]> getBuildingAreas() {
        return buildingAreas;
    }

    public void setSchematic(String schematic) {
        this.schematic = schematic;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setOffset(SerializableVector offset) {
        this.offset = offset;
    }
}
