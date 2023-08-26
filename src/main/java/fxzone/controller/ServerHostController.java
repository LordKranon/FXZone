package fxzone.controller;

import fxzone.game.logic.Player;

public interface ServerHostController extends NetworkController{
    boolean playerJoinedLobby(Player player);
}
