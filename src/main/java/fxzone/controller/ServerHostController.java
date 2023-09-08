package fxzone.controller;

import fxzone.game.logic.Player;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

public interface ServerHostController extends NetworkController{
    boolean playerJoinedLobby(Player player);
    void unitMoveCommandByClient(Point unitPosition, ArrayDeque<Point> path);
}
