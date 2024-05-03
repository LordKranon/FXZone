package fxzone.net.packet;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

public class UnitMoveCommandPacket extends GameActionPacket{

    private final Point unitPosition;
    private final ArrayDeque<Point> path;
    private final Point pointToAttack;

    public UnitMoveCommandPacket(Point unitPosition, ArrayDeque<Point> path, Point pointToAttack) {
        super(PacketType.UNIT_MOVE_COMMAND);
        this.unitPosition = unitPosition;
        this.path = path;
        this.pointToAttack = pointToAttack;
    }

    public Point getUnitPosition(){
        return unitPosition;
    }
    public ArrayDeque<Point> getPath(){
        return path;
    }
    public Point getPointToAttack(){
        return pointToAttack;
    }
}
