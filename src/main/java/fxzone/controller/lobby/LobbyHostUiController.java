package fxzone.controller.lobby;

import fxzone.controller.ingame.InGameHostUiController;
import fxzone.controller.menu.PlayMenuUiController;
import fxzone.controller.ServerHostController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Game;
import fxzone.game.logic.Map;
import fxzone.game.logic.Player;
import fxzone.game.logic.Unit;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.net.packet.LobbyPlayerListPacket;
import fxzone.net.server.Server;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
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

        /*
        START Creating map.
        */
        Map map = new Map(5, 3, null);
        Unit tank1 = new Unit("tank", 1, 1, map.getTileRenderSize(), null);
        map.addUnit(tank1);
        Unit tank2 = new Unit("hunter_tank", 2, 1, 0, null);
        map.addUnit(tank2);
        Unit tank3 = new Unit("artillery", 3, 1, 0, null);
        map.addUnit(tank3);
        map.addUnit(new Unit("tank", 5, 3, 0, null));
        /*
        END Creating map.
        */

        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(hostingPlayer);
        playerList.addAll(server.getPlayers());

        //Set unit ownership for debug
        tank1.setOwner(hostingPlayer);
        tank2.setOwner(playerList.size() > 1 ? playerList.get(1): null);
        tank3.setOwner(playerList.size() > 2 ? playerList.get(2): null);

        /*
        * START Creating game.
        * */
        Game game = new Game(playerList, map);
        GameSerializable gameSerializable = new GameSerializable(game);
        /*
         * END Creating game.
         * */

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


    public boolean playerJoinedLobby(Player player) {
        playerListUpdateFlag = true;
        return true;
    }

    @Override
    public void unitMoveCommandByClient(Point unitPosition, ArrayDeque<Point> path) {
        System.err.println("[LOBBY-HOST-UI-CONTROLLER] Received unit move command while still in lobby.");
    }

    @Override
    public void endTurnByClient(){
        System.err.println("[LOBBY-HOST-UI-CONTROLLER] Received end turn while still in lobby.");
    }
}