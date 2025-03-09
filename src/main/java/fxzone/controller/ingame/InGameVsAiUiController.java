package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.utils.GeometryUtils;
import fxzone.game.logic.Building;
import fxzone.game.logic.Codex;
import fxzone.game.logic.Codex.UnitType;
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

    private ArrayList<Building> buildingsToHandle = new ArrayList<>();

    private double waitTime;

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
            handleAiTurn(delta);
        }

    }
    private void handleAiTurn(double delta){
        if(waitTime > 0){
            waitTime -= delta;
            return;
        }
        if(unitsMoving.isEmpty() && unitsAttacking.isEmpty()){

            // Find actionable unit
            if(!unitsToHandle.isEmpty()){
                Unit u = unitsToHandle.get(0);
                handleAiCommandToUnit(u);
                return;
            }
            if(!buildingsToHandle.isEmpty()){
                Building b = buildingsToHandle.get(0);
                handleAiUsageOfBuilding(b);
                return;
            }

            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI decided to end turn.");
            endTurn();
        }
    }

    private void handleAiCommandToUnit(Unit unit){
        selectedUnit = unit;
        clearSelectedUnitPathQueue();
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

            Point pointToAttack = attackablePoints.get((int)(Math.random()*attackablePoints.size()));
            handleSelectedUnitPathQueueNewPointToAttack(pointToAttack);

            boolean wasStoppedOnFow = verifyPathOnMoveCommand(selectedUnitQueuedPath);
            commandUnitToMove(selectedUnit, selectedUnitQueuedPath, wasStoppedOnFow?null:pointToAttack, false, false);

            return;
        }

        //If a move towards enemy structures is possible, do it
        ArrayList<Point> movablePoints = new ArrayList<>();
        ArrayList<Point> enemyBuildingsToCaptureImmediately = new ArrayList<>();
        for(int i = 0; i < moveCommandGridMovableSquares.length; i++){
            for (int j = 0; j < moveCommandGridMovableSquares[i].length; j++){
                if(moveCommandGridMovableSquares[i][j] && map.checkTileForMoveToByUnitPerceived(i, j, unit, currentAiPlayerFowVision, false)){
                    movablePoints.add(new Point(i, j));

                    // Find enemy-controlled buildings that this unit can move to (and capture) this turn
                    if(map.getTiles()[i][j].hasBuildingOnTile() && map.getTiles()[i][j].getBuildingOnTile().hasOwner() && map.getTiles()[i][j].getBuildingOnTile().getOwnerId() != unit.getOwnerId()){
                        enemyBuildingsToCaptureImmediately.add(new Point(i, j));
                    }
                }
            }
        }
        if(!movablePoints.isEmpty()){

            ArrayList<Point> enemyBuildingLocations = new ArrayList<>();
            for(Building b : map.getBuildings()){
                if(b.hasOwner() && b.getOwnerId() != unit.getOwnerId()){
                    enemyBuildingLocations.add(new Point(b.getX(), b.getY()));
                }
            }

            //If enemy buildings exist and this unit is not currently standing on an enemy building or unclaimed building
            if(!enemyBuildingLocations.isEmpty() && !(map.getTiles()[unit.getX()][unit.getY()].hasBuildingOnTile() && map.getTiles()[unit.getX()][unit.getY()].getBuildingOnTile().getOwnerId() != unit.getOwnerId())){

                if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI decided to give a move command.");

                Point bestPointToMoveTo;

                //Find enemy buildings this unit can move onto (and capture), then pick one at random and move onto it
                if(!enemyBuildingsToCaptureImmediately.isEmpty()){
                    bestPointToMoveTo = enemyBuildingsToCaptureImmediately.get((int)(Math.random()*enemyBuildingsToCaptureImmediately.size()));

                } else {
                    //Find closest enemy building
                    Point closestEnemyBuilding = enemyBuildingLocations.get(0);
                    Point unitPosition = new Point(unit.getX(), unit.getY());
                    for(Point p: enemyBuildingLocations){
                        if(GeometryUtils.getPointToPointDistance(p, unitPosition) < GeometryUtils.getPointToPointDistance(closestEnemyBuilding, unitPosition)){
                            closestEnemyBuilding = p;
                        }
                    }

                    //Find point closest to closest enemy building and move there
                    //From equally close points, pick one that is diagonally/visually the closest
                    bestPointToMoveTo = movablePoints.get(0);
                    for(Point p : movablePoints){
                        if(
                            GeometryUtils.getPointToPointDistance(p, closestEnemyBuilding) <= GeometryUtils.getPointToPointDistance(bestPointToMoveTo, closestEnemyBuilding) &&
                                Math.max(Math.abs(p.x-closestEnemyBuilding.x), Math.abs(p.y-closestEnemyBuilding.y)) < Math.max(Math.abs(bestPointToMoveTo.x-closestEnemyBuilding.x), Math.abs(bestPointToMoveTo.y-closestEnemyBuilding.y))
                        ){
                            bestPointToMoveTo = p;
                        }
                    }
                }


                autoFindNewSelectedUnitPathQueue(bestPointToMoveTo);

                boolean wasStoppedOnFow = verifyPathOnMoveCommand(selectedUnitQueuedPath);
                boolean enterTransport = checkEnterTransportOnMoveCommand(selectedUnitQueuedPath);
                commandUnitToMove(selectedUnit, selectedUnitQueuedPath, null, enterTransport, false);

                return;

            }

        }

        if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI decided to make unit "+unit+" wait this turn out.");
    }
    private void handleAiUsageOfBuilding(Building building){
        buildingsToHandle.remove(building);

        if(map.getTiles()[building.getX()][building.getY()].hasUnitOnTile()){
            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI decided to make building "+building+" wait this turn out as it is blocked.");
            return;
        }
        // Select random unit the building can build and build it if AI player has enough cash
        List<UnitType> buildableUnitTypes = Codex.getBuildableUnitTypes(building.getBuildingType());
        if(buildableUnitTypes == null || buildableUnitTypes.isEmpty()){
            System.err.println("[IN-GAME-VS-AI-CONTROLLER] [handleAiTurn] ERROR on retrieving buildable unit types of building.");
            return;
        }
        UnitType unitTypeToBuild = buildableUnitTypes.get((int)(Math.random()*buildableUnitTypes.size()));
        if(game.getPlayers().get(game.whoseTurn()).getStatResourceCash() >= Codex.UNIT_PROFILE_VALUES.get(unitTypeToBuild).COST){

            if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI uses building "+building+".");

            waitTime = 1;
            onBuyUnitCreateUnit(unitTypeToBuild, building.getX(), building.getY(), game.getPlayers().get(game.whoseTurn()).getId(), false);
            return;
        }

        if(verbose) System.out.println("[IN-GAME-VS-AI-UI-CONTROLLER] [handleAiTurn] AI decided to make building "+building+" wait this turn out as it can't afford units.");
    }

    @Override
    void addVisionForNotThisPlayer(Unit unit){
        if(turnState == TurnState.AI_TURN && unit.getOwnerId() == game.getPlayers().get(game.whoseTurn()).getId()){
            map.addVisionOnUnitMove(currentAiPlayerFowVision, unit.getX(), unit.getY(), Codex.getUnitProfile(unit).VISION);
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
            for(Building b: map.getBuildings()){
                if(b.hasOwner() && b.isSelectable() && b.getOwnerId() == game.getPlayers().get(game.whoseTurn()).getId()){
                    buildingsToHandle.add(b);
                }
            }

            turnState = TurnState.AI_TURN;
        }
    }
}
