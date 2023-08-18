package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.net.client.Client;
import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.layout.AnchorPane;

public class LobbyJoinedUiController extends LobbyUiController {

    private Client client;

    private ArrayList<Player> latestPlayerList;

    private boolean exitFlag;

    public LobbyJoinedUiController(AbstractGameController gameController, Client client) {
        super(gameController);
        this.client = client;
        this.client.setLobbyJoinedUiController(this);
        this.client.sendClientConnectPacket();
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
        if(exitFlag){
            quitOuter(gameController);
        }
    }

    @Override
    protected void startOuter(AbstractGameController gameController) {

    }

    @Override
    protected void quitOuter(AbstractGameController gameController) {
        gameController.setActiveUiController(new PlayMenuUiController(gameController));
    }

    @Override
    protected void sendTestMessageOuter() {
        //client.sendClientConnectPacket();
    }

    public void setLatestPlayerList(ArrayList<Player> latestPlayerList){
        this.latestPlayerList = latestPlayerList;
    }

    /**
     * Called by client when the connection to server is closed.
     * Set the exit-flag to stop displaying the lobby UI and go back to menu asap.
     */
    public void connectionClosed(){
        System.out.println("[LOBBY-JOINED-UI-CONTROLLER] Connection closed. Exiting lobby.");
        exitFlag = true;
    }
}
