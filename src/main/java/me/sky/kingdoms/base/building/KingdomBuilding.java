package me.sky.kingdoms.base.building;

import com.sk89q.worldedit.math.Vector3;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.enums.KingdomBuildingType;
import me.sky.kingdoms.base.building.schematic.SchematicData;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.utils.SerializableVector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KingdomBuilding implements IKingdomBuilding {

    private final String id;
    private String type;
    private SerializableVector offset;
    private String direction;
    private String data;

    public KingdomBuilding(String id) {
        this.id = id;
        this.data = null;
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
    public List<SerializableVector[]> getBuildingAreas(IKingdomsPlugin plugin) {
        return getSchematicData(plugin).getBuildingAreas();
    }

    @Override
    public List<SerializableVector[]> getBuildingAreas(SerializableVector center, int angle, IKingdomsPlugin plugin) {
        List<SerializableVector[]> areas = new ArrayList<>();
        getSchematicData(plugin).getBuildingAreas().forEach(vectors -> {
            SerializableVector[] vecs = new SerializableVector[2];
            Vector3 c = Vector3.at(center.getX(), center.getY(), center.getZ());
            for (int i = 0; i < vectors.length; i++) {
                Vector3 vec = Vector3.at(vectors[i].getX(), vectors[i].getY(), vectors[i].getZ());
                vec = c.subtract(vec);
                vec = vec.transform2D(-angle, center.getX(), center.getZ(), 0, 0);
                vecs[i] = new SerializableVector(vec.getX(), vec.getY(), vec.getZ());
            }
            areas.add(vecs);
        });
        return areas;
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
    public void setSchematicData(String data) {
        this.data = data;
    }
}
