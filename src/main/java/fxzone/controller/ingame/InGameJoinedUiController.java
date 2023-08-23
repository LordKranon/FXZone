package fxzone.controller.ingame;

import fxzone.controller.ClientJoinedController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.net.client.Client;
import java.util.ArrayList;

public class InGameJoinedUiController extends InGameNetworkUiController implements ClientJoinedController {

    private final Client client;

    public InGameJoinedUiController(AbstractGameController gameController, Client client) {
        super(gameController);
        this.client = client;
    }

    @Override
    public void setLatestPlayerList(ArrayList<Player> players) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void gameStart() {

    }

    @Override
    public void lobbyPlayerListChanged() {

    }
}
