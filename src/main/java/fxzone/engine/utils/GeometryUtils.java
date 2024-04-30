package fxzone.engine.utils;

import java.awt.Point;

public class GeometryUtils {

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
}
