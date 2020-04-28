package me.sky.kingdoms.base.building.types;

import com.sk89q.worldedit.math.Vector3;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.KingdomBuildingType;
import me.sky.kingdoms.base.building.SchematicData;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.utils.SerializableVector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KingdomBuilding implements IKingdomBuilding {

    private final String id;
    private final List<SerializableVector[]> buildingAreas;
    private String type;
    private SerializableVector offset;
    private String direction;
    private String data;

    public KingdomBuilding(String id) {
        this.id = id;
        this.data = null;
        this.buildingAreas = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return getType().getName();
    }

    @Override
    public KingdomBuildingType getType() {
        return KingdomBuildingType.valueOf(type);
    }

    @Override
    public Vector3 getOffset() {
        return Vector3.at(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public Vector3 getBuildingOffset(IKingdomsPlugin plugin) {
        SchematicData data = getSchematicData(plugin);
        return Vector3.at(data.getOffset().getX(), data.getOffset().getY(), data.getOffset().getZ());
    }

    @Override
    public Direction getDirection() {
        return Direction.valueOf(direction);
    }

    @Override
    public List<SerializableVector[]> getBuildingAreas() {
        return buildingAreas;
    }

    @Override
    public SchematicData getSchematicData(IKingdomsPlugin plugin) {
        return plugin.getBuildingManager().getSchematicData(data);
    }

    @Override
    public File getSchematicFile(IKingdomsPlugin plugin) {
        return plugin.getBuildingManager().getSchematicData(data).getSchematic();
    }

    @Override
    public void setOffset(Vector3 offset) {
        this.offset = new SerializableVector(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public void setType(KingdomBuildingType type) {
        this.type = type.name();
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction.name();
    }

    @Override
    public void addBuildingArea(SerializableVector[] area) {
        this.buildingAreas.add(area);
    }

    @Override
    public void setSchematicData(String data) {
        this.data = data;
    }
}
