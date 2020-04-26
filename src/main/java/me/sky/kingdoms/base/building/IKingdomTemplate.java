package me.sky.kingdoms.base.building;

import com.sk89q.worldedit.math.BlockVector3;
import me.sky.kingdoms.base.data.buildings.types.KingdomBuilding;

import java.io.File;
import java.util.List;

public interface IKingdomTemplate {
    String getId();
    File getSchematicFile();
    BlockVector3 getCenterOffset();
    List<KingdomBuilding> getBuildings();
    int getMaxMembers();
    void setMaxMembers(int maxMembers);
    void setCenterOffset(BlockVector3 vector);
    void setSchematic(File schematic);
}
