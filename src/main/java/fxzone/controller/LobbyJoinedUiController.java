package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.net.client.Client;
import javafx.scene.layout.AnchorPane;

public class LobbyJoinedUiController extends LobbyUiController {

    private Client client;

    public LobbyJoinedUiController(AbstractGameController gameController, Client client) {
        super(gameController);
        this.client = client;
    }

    @Override
    protected void initializeOuter(AnchorPane anchorPane) {
        super.initializeOuter(anchorPane);
    }

    @Override
    protected void startOuter(AbstractGameController gameController) {

    }

    @Override
    protected void sendTestMessageOuter() {
        client.sendClientConnectPacket();
    }
}
