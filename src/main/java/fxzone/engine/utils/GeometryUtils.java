package fxzone.engine.utils;

import java.awt.Point;

public class GeometryUtils {

    public static final int NORTH_WEST = 0, NORTH = 1, NORTH_EAST = 2, WEST = 3, EAST = 4, SOUTH_WEST = 5, SOUTH = 6, SOUTH_EAST = 7;

    public static Direction getPointToPointDirection(Point a, Point b){
        if(a.x < b.x){
            return Direction.RIGHT;
        } else if(a.y < b.y) {
            return Direction.DOWN;
        } else if(a.x > b.x){
            return Direction.LEFT;
        } else if(a.y > b.y){
            return Direction.UP;
        } else {
            System.err.println("[GEOMETRY-UTILS] ERROR! Could not determine direction from Point "+a+" to Point "+b);
            return Direction.NONE;
        }
    }

    public static boolean isPointNeighborOf(Point a, Point b){
        return Math.abs(a.x-b.x) + Math.abs(a.y-b.y) == 1;
    }

    public static Point getNeighborsPosition(int x, int y, int neighborDirection){
        if(neighborDirection == NORTH){
            return new Point(x, y-1);
        }
        if(neighborDirection == WEST){
            return new Point(x-1, y);
        }
        if(neighborDirection == SOUTH){
            return new Point(x, y+1);
        }
        if(neighborDirection == EAST){
            return new Point(x+1, y);
        }
        if(neighborDirection == NORTH_WEST){
            return new Point(x-1, y-1);
        }
        if(neighborDirection == NORTH_EAST){
            return new Point(x+1, y-1);
        }
        if(neighborDirection == SOUTH_WEST){
            return new Point(x-1, y+1);
        }
        if(neighborDirection == SOUTH_EAST){
            return new Point(x+1, y+1);
        }
        System.err.println("[GEOMETRY-UTILS] ERROR! Bad neighbor direction");
        return null;
    }
}
