package me.sky.kingdoms.base.building;

import com.sk89q.worldedit.math.Vector3;

import java.io.File;

public interface IKingdomTemplate {
    String getId();
    File getSchematicFile();
    Vector3 getCenterOffset();
}
