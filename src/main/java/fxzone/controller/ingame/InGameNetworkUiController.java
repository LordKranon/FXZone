package fxzone.controller.ingame;

import fxzone.controller.NetworkController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.TurnState;
import fxzone.game.logic.Unit;
import fxzone.game.logic.serializable.MapSerializable;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

public abstract class InGameNetworkUiController extends InGameUiController implements NetworkController {

    public InGameNetworkUiController(AbstractGameController gameController, MapSerializable mapSerializable) {
        super(gameController, mapSerializable);
    }

    @Override
    protected void onPlayerUnitMoveCommand(ArrayDeque<Point> path){
        super.onPlayerUnitMoveCommand(path);
    }

    /**
     * Upon receiving info about a move command over the network.
     */
    protected void onNetworkPlayerUnitMoveCommandReceived(Point unitPosition, ArrayDeque<Point> path){
        Unit unit = map.getTiles()[unitPosition.x][unitPosition.y].getUnitOnTile();
        commandUnitToMove(unit, path);
        turnState = TurnState.NEUTRAL;
    }

}
