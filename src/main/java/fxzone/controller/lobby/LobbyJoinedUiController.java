package fxzone.controller.lobby;

import fxzone.controller.ClientJoinedController;
import fxzone.controller.ingame.InGameJoinedUiController;
import fxzone.controller.menu.PlayMenuUiController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.net.client.Client;
import fxzone.net.packet.GameActionPacket;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;

public class LobbyJoinedUiController extends LobbyUiController implements ClientJoinedController {

    private final Client client;

    private ArrayList<Player> latestPlayerList;

    private boolean exitFlag;

    private boolean gameStartFlag;

    private GameSerializable latestGameStartMap;

    private int thisPlayerId;

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
        if(gameStartFlag){
            goIntoGame(gameController);
        }
    }

    /**
     * Go into game because the host has started the game.
     */
    private void goIntoGame(AbstractGameController gameController){
        InGameJoinedUiController inGameJoinedUiController = new InGameJoinedUiController(gameController, client, latestGameStartMap, thisPlayerId);
        client.setInGameJoinedUiController(inGameJoinedUiController);
        gameController.setActiveUiController(inGameJoinedUiController);
    }

    @Override
    protected void startOuter(AbstractGameController gameController, String mapName) {

    }

    @Override
    protected void quitOuter(AbstractGameController gameController) {
        client.closeConnectionRaw();
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

    @Override
    public void gameStart(GameSerializable gameSerializable, int playerId) {
        this.thisPlayerId = playerId;
        this.latestGameStartMap = gameSerializable;
        gameStartFlag = true;
    }

    @Override
    public void gameActionReceived(GameActionPacket gameActionPacket){
        System.err.println("[LOBBY-JOINED-UI-CONTROLLER] Received in-game action while still in lobby.");
    }
}
