package fxzone.net.packet;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

public class UnitMoveCommandPacket extends GameActionPacket{

    private final Point unitPosition;
    private final ArrayDeque<Point> path;

    public UnitMoveCommandPacket(Point unitPosition, ArrayDeque<Point> path) {
        super(PacketType.UNIT_MOVE_COMMAND);
        this.unitPosition = unitPosition;
        this.path = path;
    }

    public Point getUnitPosition(){
        return unitPosition;
    }
    public ArrayDeque<Point> getPath(){
        return path;
    }
}
