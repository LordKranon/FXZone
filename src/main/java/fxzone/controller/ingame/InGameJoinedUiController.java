package fxzone.controller.ingame;

import fxzone.controller.ClientJoinedController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.net.client.Client;
import fxzone.net.packet.EndTurnPacket;
import fxzone.net.packet.UnitMoveCommandPacket;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class InGameJoinedUiController extends InGameNetworkUiController implements ClientJoinedController {

    private final Client client;

    private boolean exitFlag;

    public InGameJoinedUiController(AbstractGameController gameController, Client client, GameSerializable gameSerializable, String thisPlayerName) {
        super(gameController, gameSerializable);
        this.client = client;

        Player thisPlayerToBe = null;
        for(Player player : this.game.getPlayers()){
            if(player.getName().equals(thisPlayerName)){
                thisPlayerToBe = player;
                break;
            }
        }
        this.thisPlayer = thisPlayerToBe;

        if(verbose && this.thisPlayer == null) System.err.println("[IN-GAME-JOINED-UI-CONTROLLER");
    }

    @Override
    public void update(AbstractGameController gameController, double delta){
        super.update(gameController, delta);
        if(exitFlag){
            quitGame();
        }
    }

    @Override
    protected void quitGame(){
        client.closeConnectionRaw();
        super.quitGame();
    }

    @Override
    public void setLatestPlayerList(ArrayList<Player> players) {

    }

    @Override
    public void connectionClosed() {
        if(verbose) System.out.println("[IN-GAME-JOINED-UI-CONTROLLER] Connection closed. Exiting game.");
        exitFlag = true;
    }

    @Override
    public void gameStart(GameSerializable gameSerializable, String playerName) {

    }

    @Override
    public void unitMoveCommandReceived(Point unitPosition, ArrayDeque<Point> path) {
        onNetworkPlayerUnitMoveCommandReceived(unitPosition, path);
    }

    @Override
    public void endTurnReceived(){
        onNetworkPlayerEndTurn();
    }

    @Override
    public void lobbyPlayerListChanged() {

    }

    @Override
    protected void onPlayerUnitMoveCommand(ArrayDeque<Point> path){
        client.sendPacket(new UnitMoveCommandPacket(new Point(selectedUnit.getX(), selectedUnit.getY()), path));
    }

    @Override
    protected void onPlayerEndTurn(){
        /*
        The case that a client hits the end turn button multiple times, thus potentially ending the turn of the next player,
        is prevented via not
         */
        client.sendPacket(new EndTurnPacket());
    }
}
