package me.sky.kingdoms.base.data.objects;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public enum Direction {
    NORTH(0),
    EAST(90),
    SOUTH(180),
    WEST(-90);

    private final int angle;

    Direction(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public static Direction getDirection(float rotation) {
        if (rotation >= -45 && 45 >= rotation) {
            return Direction.NORTH;
        } else if ((rotation >= 45 && 135 >= rotation) || (rotation <= -315 && -210 >= rotation)) {
            return Direction.EAST;
        } else if ((rotation >= 135 && 210 >= rotation) || (rotation <= -210 && -135 >= rotation)) {
            return Direction.SOUTH;
        } else if ((rotation >= 210 && 315 >= rotation) || (rotation <= -135 && -45 >= rotation)) {
            return Direction.WEST;
        }
        return Direction.NORTH;
    }
}
