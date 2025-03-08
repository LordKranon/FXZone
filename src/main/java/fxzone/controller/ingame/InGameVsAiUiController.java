package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Unit;
import fxzone.game.logic.Unit.UnitState;
import fxzone.game.logic.serializable.GameSerializable;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class InGameVsAiUiController extends InGameUiController{

    final static boolean verbose = true;

    protected boolean[][] currentAiPlayerFowVision;

    /*
    AI COMMAND
     */
    /**
     * Units that the AI has yet to decide what to do with.
     */
    private ArrayList<Unit> unitsToHandle = new ArrayList<>();

    //temporary test
    private boolean aiCommandGivenThisTurn = false;

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

            // Find actionable unit
            if(!unitsToHandle.isEmpty()){
                Unit u = unitsToHandle.get(0);
                handleAiCommandToUnit(u);
                return;
            }

            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI decided to end turn.");
            endTurn();
        }
    }

    private void handleAiCommandToUnit(Unit unit){
        selectedUnit = unit;
        unitsToHandle.remove(unit);

        //Calculate options
        onSelectUnitCalculateMoveCommandGrid(currentAiPlayerFowVision);

        //If any attack is possible, do attack
        ArrayList<Point> attackablePoints = new ArrayList<>();
        for(int i = 0; i < moveCommandGridAttackableSquares.length; i++){
            for (int j = 0; j < moveCommandGridAttackableSquares[i].length; j++){
                if(moveCommandGridAttackableSquares[i][j]){
                    attackablePoints.add(new Point(i, j));
                }
            }
        }
        if(!attackablePoints.isEmpty()){

            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI decided to give a move&attack command.");

            Point pointToAttack = attackablePoints.get(0);
            handleSelectedUnitPathQueueNewPointToAttack(pointToAttack);

            boolean wasStoppedOnFow = verifyPathOnMoveCommand(selectedUnitQueuedPath);
            //boolean enterTransport = checkEnterTransportOnMoveCommand(selectedUnitQueuedPath);
            //boolean waitForAttack = checkWaitForAttackOnMoveCommand(selectedUnitQueuedPath, pointToAttack, wasStoppedOnFow, enterTransport);
            commandUnitToMove(selectedUnit, selectedUnitQueuedPath, wasStoppedOnFow?null:pointToAttack, false, false);
        } else {

            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI decided to make unit "+unit+" wait this turn out.");
        }

    }



    @Override
    protected void endTurn(){
        if(turnState == TurnState.AI_TURN){
            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [endTurn] AI ended turn.");
            turnState = TurnState.ENDING_TURN;
            super.endTurn();
        } else {
            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [endTurn] Player ended turn.");
            super.endTurn();
        }
    }

    @Override
    protected void beginTurn(){
        if(thisPlayer.equals(game.getPlayers().get(game.whoseTurn()))){
            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [beginTurn] Player begins turn.");
            map.handleStartOfTurnEffects(game, particleHandler);
            setLabelToPlayer(thisPlayer);
            super.beginTurn();
        } else {
            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [beginTurn] AI begins turn.");
            super.beginTurn();
            map.handleStartOfTurnEffects(game, particleHandler);
            setLabelToPlayer(thisPlayer);

            currentAiPlayerFowVision = map.getVisionOfPlayer(game.getPlayers().get(game.whoseTurn()).getId());
            aiCommandGivenThisTurn = false;
            for(Unit u: map.getUnits()){
                if(u.getUnitState() == UnitState.NEUTRAL && u.getOwnerId() == game.getPlayers().get(game.whoseTurn()).getId()){
                    unitsToHandle.add(u);
                }
            }

            turnState = TurnState.AI_TURN;
        }
    }
}
