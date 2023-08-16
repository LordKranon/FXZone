package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.handler.AssetHandler;
import fxzone.game.logic.Player;
import fxzone.net.packet.LobbyPlayerListPacket;
import fxzone.net.server.Server;
import java.util.Collection;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LobbyHostUiController extends LobbyUiController {

    private Server server;



    public LobbyHostUiController(AbstractGameController gameController, Server server) {
        super(gameController);
        this.server = server;
        this.server.setLobbyHostUiController(this);
    }

    @Override
    public void update(AbstractGameController gameController, double delta){
        if(playerListUpdateFlag){
            Collection<Player> players = server.getPlayers();
            updatePlayerList(players);
            playerListUpdateFlag = false;
            server.sendPacketToAll(new LobbyPlayerListPacket(players));
        }
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

    }

    @Override
    protected void sendTestMessageOuter() {
        server.sendTestMessageToAll("[MESSAGE] (Server): Kappa 123");
    }



    public void playerJoinedLobby(Player player){
        playerListUpdateFlag = true;
    }
}