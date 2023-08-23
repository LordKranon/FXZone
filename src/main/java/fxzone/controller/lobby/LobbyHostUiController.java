package fxzone.controller.lobby;

import fxzone.controller.ingame.InGameHostUiController;
import fxzone.controller.menu.PlayMenuUiController;
import fxzone.controller.ServerHostController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.net.packet.LobbyPlayerListPacket;
import fxzone.net.server.Server;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class LobbyHostUiController extends LobbyUiController implements ServerHostController {

    private final Server server;

    private final Player hostingPlayer;

    public LobbyHostUiController(AbstractGameController gameController, Server server) {
        super(gameController);
        this.server = server;
        this.server.setLobbyHostUiController(this);
        this.hostingPlayer = new Player("Hosting Player", Color.web("#ff0000"));
        updatePlayerListOfHostLobby();
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {
        if (playerListUpdateFlag) {
            ArrayList<Player> players = updatePlayerListOfHostLobby();
            playerListUpdateFlag = false;
            server.sendPacketToAllVerifiedPlayers(new LobbyPlayerListPacket(players));
        }
    }

    private ArrayList<Player> updatePlayerListOfHostLobby(){
        ArrayList<Player> players = new ArrayList<>();
        players.add(hostingPlayer);
        players.addAll(server.getPlayers());
        updatePlayerList(players);
        return players;
    }

    @Override
    protected void initializeOuter(AnchorPane anchorPane) {
        super.initializeOuter(anchorPane);
        GridPane gridPane = (GridPane) anchorPane.getChildren().get(0);
        Label label = (Label) gridPane.getChildren().get(1);
        label.setText("HOST LOBBY");
    }

    @Override
    protected void startOuter(AbstractGameController gameController) {
        InGameHostUiController inGameHostUiController = new InGameHostUiController(gameController, server);
        if(server.startGameForAll(inGameHostUiController)){
            gameController.setActiveUiController(inGameHostUiController);
        }
    }

    @Override
    protected void quitOuter(AbstractGameController gameController){
        server.stopServerRaw();
        gameController.setActiveUiController(new PlayMenuUiController(gameController));
    }

    @Override
    protected void sendTestMessageOuter() {
        server.sendTestMessageToAll("[MESSAGE] (Server): Kappa 123");
    }


    public void playerJoinedLobby(Player player) {
        playerListUpdateFlag = true;
    }
}