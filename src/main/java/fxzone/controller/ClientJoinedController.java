package fxzone.controller;

import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.MapSerializable;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public interface ClientJoinedController extends NetworkController{

    void setLatestPlayerList(ArrayList<Player> players);
    void connectionClosed();
    void gameStart(MapSerializable mapSerializable);
    void unitMoveCommandReceived(Point unitPosition, ArrayDeque<Point> path);
}
