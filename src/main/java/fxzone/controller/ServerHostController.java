package fxzone.controller;

import fxzone.game.logic.Player;
import fxzone.net.packet.GameActionPacket;

/**
 * Handle info and commands received from a client and distribute that to other clients.
 */
public interface ServerHostController extends NetworkController{
    boolean playerJoinedLobby(Player player);
    void gameActionByClient(GameActionPacket gameActionPacket);
}
