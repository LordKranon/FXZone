package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.utils.FxUtils;
import fxzone.game.logic.Player;
import fxzone.game.logic.Unit;
import fxzone.game.logic.serializable.GameSerializable;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class InGameLocalUiController extends InGameUiController{

    public InGameLocalUiController(AbstractGameController gameController, GameSerializable initialGame) {
        super(gameController, initialGame, 1);
        //Begin the first turn
        beginTurn();
        endTurnButton.setVisible(true);
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
        globalMessageText.setStyle("-fx-fill: #ffffff");

        globalMessageName.setText("\n"+thisPlayer.getName());
        globalMessageName.setStyle("-fx-fill: "+ FxUtils.toRGBCode(thisPlayer.getTextColor()));

        globalMessageTextFlow.setVisible(true);

        endTurnButton.setText("Begin Turn");
    }

    @Override
    protected void beginTurn(){
        map.handleStartOfTurnEffects(game, particleHandler);
        setLabelToPlayer(thisPlayer);
        map.setVisible(true);
        thisPlayerFowVision = map.getVisionOfPlayer(thisPlayer.getId());
        map.setFogOfWarToVision(thisPlayerFowVision);
        turnStateToNeutral();

        globalMessageTextFlow.setVisible(false);

        endTurnButton.setText("End Turn");
        onBeginTurnDoVisualEffect();
    }

    @Override
    protected void commandUnitToMove(Unit unit, ArrayDeque<Point> path, Point pointToAttack, boolean waitForAttack, boolean enterTransport){
        super.commandUnitToMove(unit, path, pointToAttack, waitForAttack, enterTransport);
        setHoveredTileInfoLabel(tileHovered);
    }
}
