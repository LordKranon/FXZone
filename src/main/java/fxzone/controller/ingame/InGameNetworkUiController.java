package fxzone.controller.ingame;

import fxzone.controller.NetworkController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.TurnState;
import fxzone.game.logic.Unit;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

public abstract class InGameNetworkUiController extends InGameUiController implements NetworkController {

    public InGameNetworkUiController(AbstractGameController gameController, GameSerializable gameSerializable) {
        super(gameController, gameSerializable);
    }

    /**
     * Upon receiving info about a move command over the network.
     */
    protected void onNetworkPlayerUnitMoveCommandReceived(Point unitPosition, ArrayDeque<Point> path){
        //TODO Handle desync
        /*
        There may be a conflict about unit positions. Some clients might not have received some
        previous unit move commands so unit positions and their states might be desynced.
         */
        Unit unit = map.getTiles()[unitPosition.x][unitPosition.y].getUnitOnTile();
        commandUnitToMove(unit, path);
        turnState = TurnState.NEUTRAL;
    }

    /**
     * Upon receiving info that the network player, whose turn it currently is, has ended their turn.
     */
    protected void onNetworkPlayerEndTurn(){
        //TODO Handle desync
        /*
        There may be a conflict about what players turn is next, since for some clients the previous
        turn might have already ended, and "endTurn" might be executed too many times.
         */
        endTurn();
    }

}
