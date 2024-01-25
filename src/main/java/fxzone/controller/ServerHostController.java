package fxzone.controller;

import fxzone.game.logic.Player;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Handle info and commands received from a client and distribute that to other clients.
 */
public interface ServerHostController extends NetworkController{
    boolean playerJoinedLobby(Player player);
    void unitMoveCommandByClient(Point unitPosition, ArrayDeque<Point> path);
    void endTurnByClient();
}
