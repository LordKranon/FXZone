package fxzone.controller.ingame;

import fxzone.controller.ClientJoinedController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.net.client.Client;
import java.util.ArrayList;

public class InGameJoinedUiController extends InGameNetworkUiController implements ClientJoinedController {

    private final Client client;

    private boolean exitFlag;

    public InGameJoinedUiController(AbstractGameController gameController, Client client, MapSerializable mapSerializable) {
        super(gameController, mapSerializable);
        this.client = client;
    }

    @Override
    public void update(AbstractGameController gameController, double delta){
        super.update(gameController, delta);
        if(exitFlag){
            quitGame();
        }
    }

    @Override
    protected void quitGame(){
        client.closeConnectionRaw();
        super.quitGame();
    }

    @Override
    public void setLatestPlayerList(ArrayList<Player> players) {

    }

    @Override
    public void connectionClosed() {
        System.out.println("[IN-GAME-JOINED-UI-CONTROLLER] Connection closed. Exiting game.");
        exitFlag = true;
    }

    @Override
    public void gameStart(MapSerializable mapSerializable) {

    }

    @Override
    public void lobbyPlayerListChanged() {

    }
}
