package fxzone.net.packet;

import java.awt.Point;
import java.util.Queue;

public class UnitMoveCommandPacket extends Packet{

    private final Point unitPosition;
    private final Queue<Point> path;

    public UnitMoveCommandPacket(Point unitPosition, Queue<Point> path) {
        super(PacketType.UNIT_MOVE_COMMAND);
        this.unitPosition = unitPosition;
        this.path = path;
    }

    public Point getUnitPosition(){
        return unitPosition;
    }
    public Queue<Point> getPath(){
        return path;
    }
}
