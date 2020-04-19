package me.sky.kingdoms.base.building;

import com.sk89q.worldedit.math.Vector3;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.utils.SerializableVector;

import java.io.File;
import java.util.List;

public interface IKingdomBuilding {
    String getId();
    String getName();
    KingdomBuildingType getType();
    Vector3 getOffset();
    Direction getDirection();
    List<SerializableVector[]> getBuildingAreas();
    File getSchematicFile();

    void setName(String name);
    void setOffset(Vector3 offset);
    void setType(KingdomBuildingType type);
    void setDirection(Direction direction);
    void addBuildingArea(SerializableVector[] area);
}
