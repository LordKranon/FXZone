package fxzone.controller.ingame;

import fxzone.controller.ServerHostController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Game;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.net.packet.UnitMoveCommandPacket;
import fxzone.net.server.Server;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class InGameHostUiController extends InGameNetworkUiController implements ServerHostController {

    private final Server server;

    public InGameHostUiController(AbstractGameController gameController, Server server, GameSerializable gameSerializable) {
        super(gameController, gameSerializable);
        this.server = server;

        //this.game = new Game(playerList);
        this.thisPlayer = game.getPlayers().get(0);
    }

    @Override
    public void lobbyPlayerListChanged() {
        System.err.println("[IN-GAME-HOST-UI-CONTROLLER] Lobby player list changed but game is already running.");
    }

    @Override
    public boolean playerJoinedLobby(Player player) {
        System.err.println("[IN-GAME-HOST-UI-CONTROLLER] Player tried to join lobby but game is already running.");
        return false;
    }

    @Override
    public void unitMoveCommandByClient(Point unitPosition, ArrayDeque<Point> path) {
        onNetworkPlayerUnitMoveCommandReceived(unitPosition, path);
        server.sendPacketToAllVerifiedPlayers(new UnitMoveCommandPacket(unitPosition, path));
    }

    protected void quitGame(){
        server.stopServerRaw();
        super.quitGame();
    }

    @Override
    protected void onPlayerUnitMoveCommand(ArrayDeque<Point> path){
        super.onPlayerUnitMoveCommand(path);
        server.sendPacketToAllVerifiedPlayers(new UnitMoveCommandPacket(new Point(selectedUnit.getX(), selectedUnit.getY()), path));
    }
}
