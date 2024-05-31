package fxzone.controller.ingame;

import fxzone.controller.ServerHostController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.net.packet.EndTurnPacket;
import fxzone.net.packet.GameActionPacket;
import fxzone.net.packet.UnitCreatedPacket;
import fxzone.net.packet.UnitMoveCommandPacket;
import fxzone.net.server.Server;
import java.awt.Point;
import java.util.ArrayDeque;

public class InGameHostUiController extends InGameNetworkUiController implements ServerHostController {

    private final Server server;

    public InGameHostUiController(AbstractGameController gameController, Server server, GameSerializable gameSerializable) {
        super(gameController, gameSerializable, 1);
        this.server = server;

        //this.thisPlayer = game.getPlayers().get(0);
    }

    @Override
    public void lobbyPlayerListChanged() {
        if(verbose) System.err.println("[IN-GAME-HOST-UI-CONTROLLER] Lobby player list changed but game is already running.");
    }

    @Override
    public boolean playerJoinedLobby(Player player) {
        if(verbose) System.err.println("[IN-GAME-HOST-UI-CONTROLLER] Player tried to join lobby but game is already running.");
        return false;
    }

    @Override
    public void gameActionByClient(GameActionPacket gameActionPacket){
        onNetworkPlayerGameAction(gameActionPacket);
        server.sendPacketToAllVerifiedPlayers(gameActionPacket);
    }

    @Override
    protected void quitGame(){
        server.stopServerRaw();
        super.quitGame();
    }

    @Override
    protected void onPlayerUnitMoveCommand(ArrayDeque<Point> path, Point pointToAttack){
        super.onPlayerUnitMoveCommand(path, pointToAttack);
        server.sendPacketToAllVerifiedPlayers(new UnitMoveCommandPacket(new Point(selectedUnit.getVisualTileX(), selectedUnit.getVisualTileY()), path, pointToAttack));
    }

    @Override
    protected void onPlayerCreatesUnit(UnitSerializable unitSerializable){
        super.onPlayerCreatesUnit(unitSerializable);
        server.sendPacketToAllVerifiedPlayers(new UnitCreatedPacket(unitSerializable));
    }

    @Override
    protected void onPlayerEndTurn(){
        super.onPlayerEndTurn();
        server.sendPacketToAllVerifiedPlayers(new EndTurnPacket());
    }

    @Override
    protected void initializeGameSpecifics(){
        this.thisPlayer = game.getPlayers().get(0);
        super.initializeGameSpecifics();
    }
}
