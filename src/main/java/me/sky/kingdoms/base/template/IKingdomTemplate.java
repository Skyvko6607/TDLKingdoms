package me.sky.kingdoms.base.template;

import com.sk89q.worldedit.math.BlockVector3;
import me.sky.kingdoms.base.building.KingdomBuilding;
import me.sky.kingdoms.base.building.types.Mine;

import java.io.File;
import java.util.List;

public interface IKingdomTemplate {
    String getId();
    File getSchematicFile();
    BlockVector3 getCenterOffset();
    List<KingdomBuilding> getBuildings();
    int getMaxMembers();
    BlockVector3 getLocation();
    int getUpgradePrice();
    Mine getMine();
    void setMaxMembers(int maxMembers);
    void setCenterOffset(BlockVector3 vector);
    void setSchematic(File schematic);
    void setLocation(BlockVector3 location);
    void setUpgradePrice(int price);
    void setMine(Mine mine);
}
