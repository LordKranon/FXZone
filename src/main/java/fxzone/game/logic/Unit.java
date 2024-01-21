package fxzone.game.logic;

import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.GameObjectUnit;
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

    private final String unitName;

    /**
     * Graphical representation. Superclass already has a pointer, this one specifies it further.
     */
    GameObjectUnit gameObjectUnit;

    private int stance = 0;

    private UnitState unitState = UnitState.NEUTRAL;

    private ArrayDeque<Point> movePath;

    private Player owner;

    /**
     * Constructor
     *
     * @param x              game logical tile position in the map
     * @param y              game logical tile position in the map
     * @param tileRenderSize graphical size
     * @param group          graphical object group
     */
    public Unit(String unitName, int x, int y, double tileRenderSize, Group group) {
        super(x, y, tileRenderSize, group);
        this.unitName = unitName;
        this.gameObjectUnit = new GameObjectUnit(unitName, x, y, tileRenderSize, group);
        this.gameObjectInTileSpace = this.gameObjectUnit;
    }

    public Unit(UnitSerializable unitSerializable, double tileRenderSize, Group group){
        super(unitSerializable);
        this.unitName = unitSerializable.unitName;
        if(unitSerializable.owner != null){
            this.owner = new Player(unitSerializable.owner);
        }
        this.gameObjectUnit = new GameObjectUnit(unitName, x, y, tileRenderSize, group);
        this.gameObjectInTileSpace = this.gameObjectUnit;
    }

    public String getUnitName(){
        return unitName;
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
        if(unitState == UnitState.NEUTRAL){
            this.movePath = path;
            setPositionInMap(path.peekLast().x, path.peekLast().y, map);
            setPositionInMapVisual(path.peek().x, path.peek().y, map);
            unitState = UnitState.MOVING;
            return true;
        }
        else {
            System.err.println("[UNIT "+unitName+"] received a move command it can't perform");
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
                unitState = UnitState.NEUTRAL;
                return false;
            } else {
                setPositionInMapVisual(nextPoint.x, nextPoint.y, map);
                boolean continueMove = (movePath.peek() != null);
                if(!continueMove){
                    unitState = UnitState.NEUTRAL;
                }
                return continueMove;
            }
        }
        return false;
    }

    @Override
    public void setPositionInMap(int x, int y, Map map){
        map.getTiles()[this.x][this.y].setUnitOnTile(null);
        super.setPositionInMap(x, y, map);
        map.getTiles()[x][y].setUnitOnTile(this);
    }

    /**
     * Used while the unit is performing its move command. The unit will update it's position only visually while actually
     * being on the destination tile.
     */
    private void setPositionInMapVisual(int x, int y, Map map){
        visualTileX = x;
        visualTileY = y;
        gameObjectInTileSpace.setPositionInMap(x, y, map);
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

    public Player getOwner(){
        return owner;
    }
    public void setOwner(Player player){
        this.owner = player;
    }

}
