package fxzone.game.logic;

import fxzone.controller.ingame.InGameUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.Direction;
import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.GeometryUtils;
import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.GameObjectUiUnitHealth;
import fxzone.game.render.GameObjectUnit;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayDeque;
import javafx.scene.Group;
import javafx.scene.media.MediaPlayer;

public class Unit extends TileSpaceObject{

    /**
     * Tile position in map.
     * Alters from actual position when unit is moving.
     */
    private int visualTileX, visualTileY;

    /**
     * Identifies what kind of unit this is.
     * E. g. "Battle Tank" or "RPG Infantry"
     */
    private final UnitType unitType;

    /**
     * Graphical representation. Superclass already has a pointer, this one specifies it further.
     */
    GameObjectUnit gameObjectUnit;

    /**
     * Graphical indicator of this units health.
     */
    GameObjectUiUnitHealth gameObjectUiUnitHealth;

    private int stance = 0;

    private UnitState unitState = UnitState.NEUTRAL;

    /**
     * Whether this unit can be selected for commands or if its blacked out.
     * Closely related to UnitStates NEUTRAL & BLACKED_OUT
     */
    private boolean actionableThisTurn = true;

    private ArrayDeque<Point> movePath;

    /**
     * As the unit moves between tiles, this is the direction of the next neighboring tile along its path.
     */
    private Direction directionNextPointOnMovePath;

    private boolean hasAttackCommandAfterMoving;
    private Point pointToAttackAfterMoving;
    private Unit lastAttackedUnit;

    private int ownerId;

    /*
    GAME BALANCE
     */
    private int statMaxHealth;
    private int statRemainingHealth;
    private int statDamage;

    /*
    SOUND
     */
    MediaPlayer mediaPlayerMovement;

    /*
    DEBUG
     */
    static final boolean verbose = true;

    /**
     * Constructor
     *
     * @param x              game logical tile position in the map
     * @param y              game logical tile position in the map
     * @param tileRenderSize graphical size
     * @param group          graphical object group
     */
    public Unit(UnitType unitType, int x, int y, double tileRenderSize, Group group) {
        super(x, y, tileRenderSize, group);
        this.unitType = unitType;

        this.gameObjectUnit = new GameObjectUnit(unitType, x, y, tileRenderSize, group, Color.WHITE);
        this.gameObjectInTileSpace = this.gameObjectUnit;

        this.gameObjectUiUnitHealth = new GameObjectUiUnitHealth(x, y, tileRenderSize, group);
        initializeStats();
        initializeMediaPlayer();
    }
    public Unit(UnitSerializable unitSerializable, double tileRenderSize, Group group, Game game){
        super(unitSerializable);
        this.unitType = unitSerializable.unitType;
        this.ownerId = unitSerializable.ownerId;


        java.awt.Color playerColor = null;
        try {
            playerColor = FxUtils.toAwtColor(game.getPlayer(ownerId).getColor());
        }catch (NullPointerException e){
            System.err.println("[UNIT "+unitType+"] Initialized without owner color");
        }

        this.gameObjectUnit = new GameObjectUnit(
            unitType, x, y, tileRenderSize, group, playerColor
        );
        this.gameObjectInTileSpace = this.gameObjectUnit;

        this.gameObjectUiUnitHealth = new GameObjectUiUnitHealth(x, y, tileRenderSize, group);
        initializeStats();
        initializeMediaPlayer();
    }
    private void initializeStats(){
        this.statMaxHealth = UnitCodex.getUnitProfile(unitType).HEALTH;
        this.statRemainingHealth = this.statMaxHealth;
        this.statDamage = UnitCodex.getUnitProfile(unitType).DAMAGE;
    }
    private void initializeMediaPlayer(){
        //TODO Improve very rudimentary sound system
        this.mediaPlayerMovement = new MediaPlayer(AssetHandler.getSound("/sounds/mixkit-truck-driving-steady-1621.mp3"));
        this.mediaPlayerMovement.setRate(1 / (2 * InGameUiController.TOTAL_UNIT_MOVEMENT_INTERVAL));
    }

    public UnitType getUnitType(){
        return unitType;
    }

    /**
     * Switch between the two different images of a unit.
     */
    public void switchStance(){
        if(stance == 0){
            stance = 1;
        } else {
            stance = 0;
        }
        gameObjectUnit.setStance(stance);
    }

    /**
     * During a game turn, receive a move command and start moving across the map.
     * @param path the path of tiles this unit will take
     */
    public UnitState moveCommand(ArrayDeque<Point> path, Map map, Point pointToAttack){
        /* TODO Remove second condition, it is temporary for testing */
        if(unitState == UnitState.NEUTRAL && path.size() <= UnitCodex.getUnitProfile(this.unitType).SPEED){
            this.movePath = path;
            Point oldPosition = new Point(x, y);
            Point finalPosition = new Point(x, y);
            directionNextPointOnMovePath = Direction.NONE;
            if(! path.isEmpty()){
                finalPosition = new Point(path.peekLast());
                directionNextPointOnMovePath = GeometryUtils.getPointToPointDirection(oldPosition, path.peek());
            }
            setPositionInMap(finalPosition.x, finalPosition.y, map);
            setPositionInMapVisual(oldPosition.x, oldPosition.y, map);

            //If an attack is included in the command, the unit will perform it after moving to the destination.
            if(pointToAttack != null){
                this.hasAttackCommandAfterMoving = true;
                this.pointToAttackAfterMoving = pointToAttack;
            }
            actionableThisTurn = false;


            if(verbose) System.out.println("[UNIT "+unitType+"] received a move command");

            //If path is empty, movement is ended immediately and the unit goes into attack immediately
            if(path.isEmpty()){
                onMovementEnd(map);
            } else {
                mediaPlayerMovement.play();
                unitState = UnitState.MOVING;
            }

            return this.unitState;
        }
        else {
            System.err.println("[UNIT "+unitType+"] received a move command it can't perform");
            return null;
        }
    }

    /**
     * Move to the next tile in queued path.
     * @return UnitState.MOVING if this unit is still moving afterwards
     */
    public UnitState performFullTileMove(Map map){
        if(unitState == UnitState.MOVING){
            Point nextPoint = movePath.poll();
            if(nextPoint == null){
                onMovementEnd(map);
                return this.unitState;
            } else {
                setPositionInMapVisual(nextPoint.x, nextPoint.y, map);
                boolean continueMove = (movePath.peek() != null);
                if(!continueMove){
                    onMovementEnd(map);
                } else{
                    directionNextPointOnMovePath = GeometryUtils.getPointToPointDirection(nextPoint, movePath.peek());
                }

                return this.unitState;
            }
        }
        return this.unitState;
    }
    public UnitFightResult performFinishAttack(Map map){
        if(unitState == UnitState.ATTACKING){
            return onAttackEnd(map);
        } else {
            System.err.println("[UNIT "+unitType+"] [performFinishAttack] ERROR: Unit is not attacking");
            return null;
        }
    }
    public boolean changeStatHealth(int changeToHealth){
        this.statRemainingHealth += changeToHealth;
        if(verbose) System.out.println("[UNIT "+unitType+"] now has "+statRemainingHealth+" HP remaining");
        gameObjectUiUnitHealth.updateUnitHealth((double) statRemainingHealth / (double) statMaxHealth);
        return statRemainingHealth > 0;
    }

    /**
     * While moving, increase in-between-tile-offset to graphically represent the unit moving from one tile to a neighboring tile.
     * @param fractionOfTile at what position between tiles should this unit be placed
     */
    public void performInBetweenTileMove(double fractionOfTile, Map map){
        if(unitState == UnitState.MOVING){
            double tileCenterOffsetX, tileCenterOffsetY;
            switch(directionNextPointOnMovePath){
                case RIGHT: tileCenterOffsetX = fractionOfTile; tileCenterOffsetY = 0; break;
                case DOWN: tileCenterOffsetX = 0; tileCenterOffsetY = fractionOfTile; break;
                case LEFT: tileCenterOffsetX = -fractionOfTile; tileCenterOffsetY = 0; break;
                case UP: tileCenterOffsetX = 0; tileCenterOffsetY = -fractionOfTile; break;
                default: tileCenterOffsetX = 0; tileCenterOffsetY = 0; break;
            }
            gameObjectUnit.setTileCenterOffset(tileCenterOffsetX, tileCenterOffsetY, visualTileX, visualTileY, map);
        } else {
            System.err.println("[UNIT "+unitType+"] [performInBetweenTileMove] Unit is not in MOVING state");
        }
    }
    public void performAttack(double fractionOfAttack, Map map){
        //TODO
        // Similar to moving the unit itself in between tiles, here a bullet of sorts will be moved/adjusted.
    }

    /**
     * Render methods are overwritten to include the UI health indicator object in render operations.
     */
    @Override
    public void setPositionInMap(int x, int y, Map map){
        map.getTiles()[this.x][this.y].setUnitOnTile(null);
        super.setPositionInMap(x, y, map);
        map.getTiles()[x][y].setUnitOnTile(this);
        this.gameObjectUiUnitHealth.setPositionInMap(x, y, map);
    }
    @Override
    public void changeTileRenderSize(Map map){
        super.changeTileRenderSize(map);
        this.gameObjectUiUnitHealth.changeTileRenderSize(x, y, map);
    }
    @Override
    public void setGraphicalOffset(double offsetX, double offsetY){
        super.setGraphicalOffset(offsetX, offsetY);
        this.gameObjectUiUnitHealth.setOffset(offsetX, offsetY);
    }

    /**
     * Used while the unit is performing its move command. The unit will update it's position only visually while actually
     * being on the destination tile.
     */
    private void setPositionInMapVisual(int x, int y, Map map){
        visualTileX = x;
        visualTileY = y;
        gameObjectUnit.setTileCenterOffset(0, 0, x, y, map);
        //gameObjectInTileSpace.setPositionInMap(x, y, map);
    }
    public int getVisualTileX(){
        return visualTileX;
    }
    public int getVisualTileY(){
        return visualTileY;
    }

    public UnitState getUnitState(){
        return unitState;
    }

    public int getOwnerId(){
        return ownerId;
    }
    public void setOwnerId(int ownerId){
        this.ownerId = ownerId;
    }

    private void onMovementEnd(Map map){
        cleanUpMovingState(map);
        if(hasAttackCommandAfterMoving){
            if(verbose) System.out.println("[UNIT "+unitType+"] on movement end, going into attack");
            unitStateToAttacking();
        } else {
            if(actionableThisTurn){
                unitStateToNeutral();
            } else{
                unitStateToBlackedOut();
            }
        }
    }
    private UnitFightResult onAttackEnd(Map map){
        //TODO
        // This is a temporary and rudimentary attack script
        Tile attackedTile = map.getTiles()[pointToAttackAfterMoving.x][pointToAttackAfterMoving.y];
        Unit attackedUnit = attackedTile.getUnitOnTile();
        boolean attackedUnitSurvived = true;
        if(attackedUnit != null){
            attackedUnitSurvived = attackedUnit.changeStatHealth(- this.statDamage);
            this.lastAttackedUnit = attackedUnit;
        }

        gameObjectUnit.setAttackingStance(false);
        if(actionableThisTurn){
            unitStateToNeutral();
        } else{
            unitStateToBlackedOut();
        }

        return attackedUnitSurvived ? UnitFightResult.BOTH_LIVE : UnitFightResult.DEFENDER_DEAD;
    }
    public enum UnitFightResult{
        DEFENDER_DEAD,
        ATTACKER_DEAD,
        BOTH_LIVE,
        BOTH_DEAD
    }
    public Unit getLastAttackedUnit(){
        return lastAttackedUnit;
    }
    private void unitStateToNeutral(){
        gameObjectUnit.setBlackedOut(false);
        unitState = UnitState.NEUTRAL;
    }
    private void unitStateToBlackedOut(){
        gameObjectUnit.setBlackedOut(true);
        unitState = UnitState.BLACKED_OUT;
    }
    private void unitStateToAttacking(){
        this.hasAttackCommandAfterMoving = false;
        //TODO
        gameObjectUnit.setAttackingStance(true);
        unitState = UnitState.ATTACKING;
    }
    private void cleanUpMovingState(Map map){
        gameObjectUnit.setTileCenterOffset(0, 0, x, y, map);
        mediaPlayerMovement.stop();
    }

    public void setActionableThisTurn(boolean actionable){
        if(actionable){
            if(unitState == UnitState.BLACKED_OUT){
                unitStateToNeutral();
            }
        } else {
            if(unitState == UnitState.NEUTRAL){
                unitStateToBlackedOut();
            }
        }
        this.actionableThisTurn = actionable;
    }

    /**
     * Called when this unit is killed.
     * Remove all graphical game objects.
     */
    public void onRemoval(Group group){
        gameObjectUnit.removeSelfFromRoot(group);
        gameObjectUiUnitHealth.removeSelfFromRoot(group);
    }
}
