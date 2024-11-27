package fxzone.engine.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GeometryUtils {

    public static final int TOTAL_AMOUNT_NEIGHBOR_DIRECTIONS = 4;
    public static final int NORTH_WEST = 4, NORTH = 0, NORTH_EAST = 5, WEST = 1, EAST = 2, SOUTH_WEST = 6, SOUTH = 3, SOUTH_EAST = 7;

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
        return getPointToPointDistance(a, b) == 1;
    }
    public static int getPointToPointDistance(Point a, Point b){
        return Math.abs(a.x-b.x) + Math.abs(a.y-b.y);
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

    public static ArrayList<Point> getPointsInRange(int range){
        ArrayList<Point> pointsInRange = new ArrayList<>();
        pointsInRange.add(new Point(0, 0));
        for(int i = 1; i <= range; i++){
            for(int j = 0; j < i; j++){
                pointsInRange.add(new Point(i-j, j));
            }
            for(int j = 0; j < i; j++){
                pointsInRange.add(new Point(j, -i+j));
            }
            for(int j = 0; j < i; j++){
                pointsInRange.add(new Point(-i+j, -j));
            }
            for(int j = 0; j < i; j++){
                pointsInRange.add(new Point(-j, i-j));
            }
        }
        return pointsInRange;
    }

    public static class DoublePoint{
        public double x, y;
        public DoublePoint(double x, double y){
            this.x = x;
            this.y = y;
        }
    }
    public static DoublePoint getVectorFromAngle(double angle){
        return new DoublePoint(Math.cos(angle), Math.sin(angle));
    }
}
