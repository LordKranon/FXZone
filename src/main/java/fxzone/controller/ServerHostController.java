package fxzone.controller;

import fxzone.game.logic.Player;
import java.awt.Point;
import java.util.Queue;

public interface ServerHostController extends NetworkController{
    boolean playerJoinedLobby(Player player);
    void unitMoveCommandByClient(Point unitPosition, Queue<Point> path);
}
