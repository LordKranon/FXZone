package fxzone.game.logic;

import fxzone.engine.utils.Direction;
import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.GeometryUtils;
import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.GameObjectUiUnitHealth;
import fxzone.game.render.GameObjectUnit;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import javafx.scene.Group;

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

    private ArrayDeque<Point> movePath;

    /**
     * As the unit moves between tiles, this is the direction of the next neighboring tile along its path.
     */
    private Direction directionNextPointOnMovePath;

    private int ownerId;

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
    public boolean moveCommand(ArrayDeque<Point> path, Map map){
        /* TODO Remove second condition, it is temporary for testing */
        if(unitState == UnitState.NEUTRAL && path.size() <= UnitCodex.getUnitProfile(this.unitType).SPEED){
            this.movePath = path;
            Point oldPosition = new Point(x, y);
            setPositionInMap(path.peekLast().x, path.peekLast().y, map);
            setPositionInMapVisual(oldPosition.x, oldPosition.y, map);
            directionNextPointOnMovePath = GeometryUtils.getPointToPointDirection(oldPosition, path.peek());
            unitState = UnitState.MOVING;
            if(verbose) System.out.println("[UNIT "+unitType+"] received a move command");
            return true;
        }
        else {
            System.err.println("[UNIT "+unitType+"] received a move command it can't perform");
            return false;
        }
    }

    /**
     * Move to the next tile in queued path.
     * @return true if this unit is still moving afterwards
     */
    public boolean performFullTileMove(Map map){
        if(unitState == UnitState.MOVING){
            Point nextPoint = movePath.poll();
            if(nextPoint == null){
                unitStateToNeutral(map);
                return false;
            } else {
                setPositionInMapVisual(nextPoint.x, nextPoint.y, map);
                boolean continueMove = (movePath.peek() != null);
                if(!continueMove){
                    unitStateToNeutral(map);
                } else{
                    directionNextPointOnMovePath = GeometryUtils.getPointToPointDirection(nextPoint, movePath.peek());
                }

                return continueMove;
            }
        }
        return false;
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

    private void unitStateToNeutral(Map map){
        gameObjectUnit.setTileCenterOffset(0, 0, x, y, map);
        unitState = UnitState.NEUTRAL;
    }

}
