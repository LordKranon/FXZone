package fxzone.controller.ingame;

import fxzone.controller.ClientJoinedController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.net.client.Client;
import fxzone.net.packet.EndTurnPacket;
import fxzone.net.packet.GameActionPacket;
import fxzone.net.packet.UnitCreatedPacket;
import fxzone.net.packet.UnitMoveCommandPacket;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class InGameJoinedUiController extends InGameNetworkUiController implements ClientJoinedController {

    private final Client client;

    private boolean exitFlag;

    public InGameJoinedUiController(AbstractGameController gameController, Client client, GameSerializable gameSerializable, int thisPlayerId) {
        super(gameController, gameSerializable, thisPlayerId);
        this.client = client;

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
    public void gameStart(GameSerializable gameSerializable, int playerId) {

    }

    @Override
    public void gameActionReceived(GameActionPacket gameActionPacket){
        onNetworkPlayerGameAction(gameActionPacket);
    }

    @Override
    public void lobbyPlayerListChanged() {

    }

    @Override
    protected boolean onPlayerUnitMoveCommand(ArrayDeque<Point> path, Point pointToAttack){
        turnStateToNeutral();
        boolean stoppedOnFow = verifyPathOnMoveCommand(path);
        boolean enterTransport = checkEnterTransportOnMoveCommand(path);
        boolean waitForAttack = checkWaitForAttackOnMoveCommand(path, pointToAttack, stoppedOnFow, enterTransport);
        client.sendPacket(new UnitMoveCommandPacket(selectedUnit.getUnitId(), new Point(selectedUnit.getX(), selectedUnit.getY()), path, stoppedOnFow?null:pointToAttack, waitForAttack, enterTransport));
        return stoppedOnFow;
    }

    @Override
    protected void onPlayerCreatesUnit(UnitSerializable unitSerializable, int statPurchasingPrice){
        client.sendPacket(new UnitCreatedPacket(unitSerializable, statPurchasingPrice));
    }

    @Override
    protected void onPlayerEndTurn(){
        /*
        The case that a client hits the end turn button multiple times, thus potentially ending the turn of the next player,
        is prevented via not
         */
        client.sendPacket(new EndTurnPacket());
    }

    @Override
    protected void initializeGameSpecifics(){
        Player thisPlayerToBe = null;
        for(Player player : this.game.getPlayers()){
            if(player.getId() == thisPlayerIdTemp){
                thisPlayerToBe = player;
                break;
            }
        }
        this.thisPlayer = thisPlayerToBe;
        super.initializeGameSpecifics();
    }
}
