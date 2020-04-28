package me.sky.kingdoms.base.data.objects;

public enum Direction {
    NORTH(180),
    EAST(-90),
    SOUTH(0),
    WEST(90);

    private final int angle;

    Direction(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public static int indexOf(Direction direction) {
        for (int i = 0; i < Direction.values().length; i++) {
            if (Direction.values()[i] == direction) {
                return i;
            }
        }
        return -1;
    }

    public static Direction getSubDirection(Direction start, int index) {
        int i = indexOf(start) + index;
        if (i >= Direction.values().length) {
            i -= Direction.values().length;
        }
        return Direction.values()[i];
    }

    public static Direction getDirection(float rotation) {
        rotation -= 360;
        if (rotation >= -45 && 45 >= rotation) {
            return Direction.SOUTH;
        } else if (rotation >= 45 && 135 >= rotation) {
            return Direction.WEST;
        } else if (rotation >= -135 && -45 >= rotation) {
            return Direction.EAST;
        } else if (rotation >= 135 || -135 >= rotation) {
            return Direction.NORTH;
        }
        return Direction.NORTH;
    }
}
