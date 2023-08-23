package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.net.server.Server;

public class InGameHostUiController extends InGameNetworkUiController implements ServerHostController{

    private final Server server;

    public InGameHostUiController(AbstractGameController gameController, Server server) {
        super(gameController);
        this.server = server;
        this.server.setInGameHostUiController(this);
    }

    @Override
    public void lobbyPlayerListChanged() {
        System.out.println("[IN-GAME-HOST-UI-CONTROLLER] Lobby player list changed but game is already running.");
    }

    @Override
    public void playerJoinedLobby(Player player) {

    }
}
