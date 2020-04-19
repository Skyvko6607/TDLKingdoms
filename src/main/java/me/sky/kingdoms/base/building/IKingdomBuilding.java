package me.sky.kingdoms.base.building;

import com.sk89q.worldedit.math.Vector3;
import me.sky.kingdoms.base.data.objects.Direction;

import java.io.File;
import java.util.List;

public interface IKingdomBuilding {
    String getId();
    KingdomBuildingType getType();
    Vector3 getOffset();
    Direction getDirection();
    List<Vector3[]> getBuildingAreas();
    File getSchematicFile();
}
