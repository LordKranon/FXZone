package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.net.client.Client;
import java.util.Collection;
import javafx.scene.layout.AnchorPane;

public class LobbyJoinedUiController extends LobbyUiController {

    private Client client;

    private Collection<Player> latestPlayerList;

    public LobbyJoinedUiController(AbstractGameController gameController, Client client) {
        super(gameController);
        this.client = client;
        this.client.setLobbyJoinedUiController(this);
    }

    @Override
    protected void initializeOuter(AnchorPane anchorPane) {
        super.initializeOuter(anchorPane);
    }

    @Override
    public void update(AbstractGameController gameController, double delta){
        if(playerListUpdateFlag){
            updatePlayerList(latestPlayerList);
            playerListUpdateFlag = false;
        }
    }

    @Override
    protected void startOuter(AbstractGameController gameController) {

    }

    @Override
    protected void sendTestMessageOuter() {
        client.sendClientConnectPacket();
    }

    public void setLatestPlayerList(Collection<Player> latestPlayerList){
        this.latestPlayerList = latestPlayerList;
    }
}
