package fxzone.controller.ingame;

import fxzone.controller.NetworkController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Unit;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.net.packet.GameActionPacket;
import fxzone.net.packet.UnitCreatedPacket;
import fxzone.net.packet.UnitMoveCommandPacket;
import java.awt.Point;
import java.util.ArrayDeque;

public abstract class InGameNetworkUiController extends InGameUiController implements NetworkController {

    public InGameNetworkUiController(AbstractGameController gameController, GameSerializable gameSerializable, int thisPlayerId) {
        super(gameController, gameSerializable, thisPlayerId);
    }

    /**
     * Upon receiving info about a move command over the network.
     */
    void onNetworkPlayerUnitMoveCommandReceived(UnitMoveCommandPacket unitMoveCommandPacket){
        //TODO Handle desync
        /*
        There may be a conflict about unit positions. Some clients might not have received some
        previous unit move commands so unit positions and their states might be desynced.
         */
        Point unitPosition = unitMoveCommandPacket.getUnitPosition();
        if(verbose) System.out.println("[IN-GAME-NETWORK-UI-CONTROLLER] Received unit move command for unit at X="+unitPosition.x+" Y="+unitPosition.y);
        ArrayDeque<Point> path = unitMoveCommandPacket.getPath();

        Unit unit = null;
        for(Unit u : map.getUnits()){
            if(u.getUnitId() == unitMoveCommandPacket.getUnitId()){
                unit = u;
            }
        }
        if(unit == null){
            System.err.println("[IN-GAME-NETWORK-UI-CONTROLLER] ERROR onNetworkPlayerUnitMoveCommandReceived No Unit with such ID found");
            return;
        }


        /*
        Also implement wait-for-attack for network games
        Also implement enter transport for network games
         */
        commandUnitToMove(unit, path, unitMoveCommandPacket.getPointToAttack(), unitMoveCommandPacket.getWaitForAttack(), unitMoveCommandPacket.getEnterTransport());
    }

    /**
     * Upon receiving info that the network player, whose turn it currently is, has ended their turn.
     */
    private void onNetworkPlayerEndTurn(){
        //TODO Handle desync
        /*
        There may be a conflict about what players turn is next, since for some clients the previous
        turn might have already ended, and "endTurn" might be executed too many times.
         */
        endTurn();
    }

    private void onNetworkPlayerCreatesUnit(UnitCreatedPacket unitCreatedPacket){
        /*
        Same desync problem?
         */
        // Since unit graphics can't be created outside of FX application thread, listen in FX app thread for new units created
        unitsToBeCreated.add(
            new PendingUnitCreation(unitCreatedPacket.getUnitSerializable(), unitCreatedPacket.getStatPurchasingPrice(),
                unitCreatedPacket.getInTransport()));
        if(unitCreatedPacket.getUnitSerializable().unitId != runningUnitId++){
            System.err.println("[IN-GAME-NETWORK-UI-CONTROLLER] ERROR Unit ID desync");
        }
        offThreadGraphicsNeedHandling = true;
    }

    @Override
    protected void beginTurn(){
        super.beginTurn();
        startOfTurnEffectFlag = true;
        offThreadGraphicsNeedHandling = true;
    }

    protected void onNetworkPlayerGameAction(GameActionPacket gameActionPacket){
        switch (gameActionPacket.getGameActionSpecification()){
            case UNIT_MOVE_COMMAND: onNetworkPlayerUnitMoveCommandReceived((UnitMoveCommandPacket) gameActionPacket); break;
            case UNIT_CREATED: onNetworkPlayerCreatesUnit((UnitCreatedPacket) gameActionPacket); break;
            case END_TURN: onNetworkPlayerEndTurn(); break;
            default: break;
        }
    }

    @Override
    void returnToMenuButtonClicked(){
        quitGame();
    }

    boolean isPlayerUnitMoveCommandAllowed(){
        return unitsMoving.isEmpty() && unitsAttacking.isEmpty();
    }

    @Override
    protected void goToEndOfTurnGraphicalEffects(){
        /*
        Skip this phase in network games.
         */
        onPlayerEndTurn();
    }
}
