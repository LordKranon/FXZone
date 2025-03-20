package fxzone.controller.ingame;

import fxzone.controller.ClientJoinedController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Codex;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.Player;
import fxzone.game.logic.Unit;
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

    /**
     * Upon issuing a move command to the server, block any other move commands until a move command is received back from the server.
     */
    private boolean awaitingUnitCommandFlag;

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
        if(!isPlayerUnitMoveCommandAllowed()){
            System.err.println("[IN-GAME-JOINED-UI-CONTROLLER] ERROR Cannot give a command in network game while another unit is already in action.");
            return false;
        }
        turnStateToNeutral();
        boolean stoppedOnFow = verifyPathOnMoveCommand(path);
        boolean enterTransport = checkEnterTransportOnMoveCommand(path);
        boolean waitForAttack = checkWaitForAttackOnMoveCommand(path, pointToAttack, stoppedOnFow, enterTransport);
        client.sendPacket(new UnitMoveCommandPacket(selectedUnit.getUnitId(), new Point(selectedUnit.getX(), selectedUnit.getY()), path, stoppedOnFow?null:pointToAttack, waitForAttack, enterTransport));
        awaitingUnitCommandFlag = true;
        return stoppedOnFow;
    }

    @Override
    protected void onPlayerCreatesUnit(UnitSerializable unitSerializable, int statPurchasingPrice, boolean inTransport){
        client.sendPacket(new UnitCreatedPacket(unitSerializable, statPurchasingPrice, inTransport));
    }

    @Override
    void onBuyUnitCreateUnit(UnitType unitType, int x, int y, int ownerId, boolean inTransport){
        // The only difference here is that runningUnitId is not raised, because it will be raised when unit creation packet is
        // received here. Otherwise when a client creates a unit the runningUnitId is raised twice.
        Unit createdUnit = new Unit(unitType, x, y, runningUnitId);
        createdUnit.setOwnerId(ownerId);
        UnitSerializable createdUnitSerializable = new UnitSerializable(createdUnit);
        onPlayerCreatesUnit(createdUnitSerializable, Codex.getUnitProfile(unitType).COST, inTransport);
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

    @Override
    boolean isPlayerUnitMoveCommandAllowed(){
        return super.isPlayerUnitMoveCommandAllowed() && !awaitingUnitCommandFlag;
    }

    @Override
    void onNetworkPlayerUnitMoveCommandReceived(UnitMoveCommandPacket unitMoveCommandPacket){
        awaitingUnitCommandFlag = false;
        super.onNetworkPlayerUnitMoveCommandReceived(unitMoveCommandPacket);
    }
}
