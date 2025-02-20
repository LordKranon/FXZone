package fxzone.net.packet;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

public class UnitMoveCommandPacket extends GameActionPacket{

    private final int unitId;
    private final Point unitPosition;
    private final ArrayDeque<Point> path;
    private final Point pointToAttack;

    private final boolean waitForAttack;
    private final boolean enterTransport;

    public UnitMoveCommandPacket(int unitId, Point unitPosition, ArrayDeque<Point> path, Point pointToAttack, boolean waitForAttack, boolean enterTransport) {
        super(PacketType.UNIT_MOVE_COMMAND);

        this.unitId = unitId;
        this.unitPosition = unitPosition;
        this.path = path;
        this.pointToAttack = pointToAttack;
        this.waitForAttack = waitForAttack;
        this.enterTransport = enterTransport;
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

    public boolean getWaitForAttack(){
        return waitForAttack;
    }
    public boolean getEnterTransport(){
        return enterTransport;
    }

    public int getUnitId(){
        return unitId;
    }
}
