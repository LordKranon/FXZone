package fxzone.controller.ingame;

import fxzone.controller.ServerHostController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.net.server.Server;

public class InGameHostUiController extends InGameNetworkUiController implements ServerHostController {

    private final Server server;

    public InGameHostUiController(AbstractGameController gameController, Server server, MapSerializable mapSerializable) {
        super(gameController, mapSerializable);
        this.server = server;
        //this.server.setInGameHostUiController(this);
        //selectedUnit = map.getUnits().get(0);
        //selectUnit(map.getUnits().get(0));
    }

    @Override
    public void lobbyPlayerListChanged() {
        System.out.println("[IN-GAME-HOST-UI-CONTROLLER] Lobby player list changed but game is already running.");
    }

    @Override
    public boolean playerJoinedLobby(Player player) {
        System.out.println("[IN-GAME-HOST-UI-CONTROLLER] Player joined lobby but game is already running.");
        return false;
    }

    protected void quitGame(){
        server.stopServerRaw();
        super.quitGame();
    }
}
