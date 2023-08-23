package fxzone.controller;

import fxzone.game.logic.Player;

public interface ServerHostController extends NetworkController{
    void playerJoinedLobby(Player player);
}
