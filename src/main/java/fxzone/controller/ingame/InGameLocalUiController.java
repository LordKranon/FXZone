package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.utils.FxUtils;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import java.util.ArrayList;

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
    protected void endTurn(){
        turnStateToNoTurn();
        game.handleEndOfTurnEffects();
        if(game.eliminationCheckup()){
            if(game.getPlayers().size() < 2){
                turnStateToGameOver(true, game.getPlayers().get(0).getId());
                return;
            }
        }
        game.goNextTurn();
        thisPlayer = game.getPlayers().get(game.whoseTurn());
        setLabelToPlayer(thisPlayer);


        globalMessageText.setText("TURN "+game.getTurnCount());

        globalMessageName.setText("\n"+thisPlayer.getName());
        globalMessageName.setStyle("-fx-fill: "+ FxUtils.toRGBCode(thisPlayer.getTextColor()));

        globalMessageTextFlow.setVisible(true);

        endTurnButton.setText("Begin Turn");
    }

    @Override
    protected void beginTurn(){
        map.handleStartOfTurnEffects(game);
        setLabelToPlayer(thisPlayer);
        map.setVisible(true);
        thisPlayerFowVision = map.getVisionOfPlayer(thisPlayer.getId());
        map.setFogOfWarToVision(thisPlayerFowVision);
        turnStateToNeutral();

        globalMessageTextFlow.setVisible(false);

        endTurnButton.setText("End Turn");
    }
}
