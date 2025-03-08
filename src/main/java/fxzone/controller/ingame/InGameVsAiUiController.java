package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.serializable.GameSerializable;

public class InGameVsAiUiController extends InGameUiController{

    public InGameVsAiUiController(AbstractGameController gameController, GameSerializable initialGame) {
        super(gameController, initialGame, 1);
    }

    @Override
    protected void initializeGameSpecifics(){
        this.thisPlayer = game.getPlayers().get(0);
        super.initializeGameSpecifics();
    }

    @Override
    public void update(AbstractGameController gameController, double delta){
        super.update(gameController, delta);

        if(turnState == TurnState.AI_TURN){
            handleAiTurn();
        }

    }
    private void handleAiTurn(){
        if(unitsMoving.isEmpty() && unitsAttacking.isEmpty()){
            System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI decided to end turn.");
            endTurn();
        }
    }

    @Override
    protected void endTurn(){
        if(turnState == TurnState.AI_TURN){
            System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [endTurn] AI ended turn.");
            turnState = TurnState.ENDING_TURN;
            super.endTurn();
        } else {
            System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [endTurn] Player ended turn.");
            super.endTurn();
        }
    }

    @Override
    protected void beginTurn(){
        if(thisPlayer.equals(game.getPlayers().get(game.whoseTurn()))){
            System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [beginTurn] Player begins turn.");
            map.handleStartOfTurnEffects(game, particleHandler);
            setLabelToPlayer(thisPlayer);
            super.beginTurn();
        } else {
            System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [beginTurn] AI begins turn.");
            super.beginTurn();
            map.handleStartOfTurnEffects(game, particleHandler);
            setLabelToPlayer(thisPlayer);
            turnState = TurnState.AI_TURN;
        }
    }
}
