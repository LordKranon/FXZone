package fxzone.controller.ingame;

import fxzone.controller.ClientJoinedController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import java.util.ArrayList;

public class InGameJoinedUiController extends InGameNetworkUiController implements ClientJoinedController {

    public InGameJoinedUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void setLatestPlayerList(ArrayList<Player> players) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void lobbyPlayerListChanged() {

    }
}
