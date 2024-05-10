package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.serializable.GameSerializable;

public class InGameLocalUiController extends InGameUiController{

    public InGameLocalUiController(AbstractGameController gameController, GameSerializable initialGame) {
        super(gameController, initialGame, 1);

    }

    @Override
    protected void initializeGameSpecifics(){
        this.thisPlayer = game.getPlayers().get(0);
        super.initializeGameSpecifics();
    }

    @Override
    protected void onPlayerEndTurn(){
        turnStateToNoTurn();
    }
}
