package me.sky.kingdoms.base.building;

import com.sk89q.worldedit.math.Vector3;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.enums.KingdomBuildingType;
import me.sky.kingdoms.base.building.schematic.SchematicData;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.utils.SerializableVector;

import java.io.File;
import java.util.List;

public interface IKingdomBuilding {
    String getId();
    String getName();
    KingdomBuildingType getType();
    Vector3 getOffset();
    Vector3 getBuildingOffset(IKingdomsPlugin plugin);
    Direction getDirection();
    List<SerializableVector[]> getBuildingAreas(IKingdomsPlugin plugin);
    List<SerializableVector[]> getBuildingAreas(SerializableVector center, int angle, IKingdomsPlugin plugin);
    SchematicData getSchematicData(IKingdomsPlugin plugin);
    File getSchematicFile(IKingdomsPlugin plugin);

    void setOffset(Vector3 offset);
    void setType(KingdomBuildingType type);
    void setDirection(Direction direction);
    void setSchematicData(String data);
}
