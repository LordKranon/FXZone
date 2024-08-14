package fxzone.controller.lobby;

import fxzone.controller.ServerHostController;
import fxzone.controller.ingame.InGameHostUiController;
import fxzone.controller.menu.PlayMenuUiController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Building;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Game;
import fxzone.game.logic.Map;
import fxzone.game.logic.Player;
import fxzone.game.logic.Unit;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.net.packet.GameActionPacket;
import fxzone.net.packet.LobbyPlayerListPacket;
import fxzone.net.server.Server;
import fxzone.save.Save;
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
        this.hostingPlayer = new Player("Alpha", Color.web("#ff0000"), 1);
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
    protected void startOuter(AbstractGameController gameController, String mapName) {


        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(hostingPlayer);
        playerList.addAll(server.getPlayers());


        GameSerializable gameSerializable = new GameSerializable(Save.loadMap("map"), playerList);


        InGameHostUiController inGameHostUiController = new InGameHostUiController(gameController, server, gameSerializable);
        if(server.startGameForAll(inGameHostUiController, gameSerializable)){
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

    @Override
    public boolean playerJoinedLobby(Player player) {
        playerListUpdateFlag = true;
        return true;
    }

    @Override
    public void gameActionByClient(GameActionPacket gameActionPacket){
        System.err.println("[LOBBY-HOST-UI-CONTROLLER] Received in-game action by a client while still in lobby.");
    }
}