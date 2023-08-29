package fxzone.game.logic;

import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.GameObjectUnit;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Queue;
import javafx.scene.Group;

public class Unit extends TileSpaceObject{

    private final String unitName;

    /**
     * Graphical representation. Superclass already has a pointer, this one specifies it further.
     */
    GameObjectUnit gameObjectUnit;

    private int stance = 0;

    private UnitState unitState = UnitState.NEUTRAL;

    private Queue<Point> movePath;

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
    public boolean moveCommand(Queue<Point> path){
        if(unitState == UnitState.NEUTRAL){
            this.movePath = path;
            unitState = UnitState.MOVING;
            return true;
        }
        else {
            System.err.println("[UNIT "+unitName+"] received a move command it can't perform");
            return false;
        }
    }

    public void performFullTileMove(Map map){
        if(unitState == UnitState.MOVING){
            Point nextPoint = movePath.poll();
            if(nextPoint == null){
                unitState = UnitState.NEUTRAL;
                return;
            } else {
                setPositionInMap(nextPoint.x, nextPoint.y, map);
            }
        }
    }

    @Override
    public void setPositionInMap(int x, int y, Map map){
        map.getTiles()[this.x][this.y].setUnitOnTile(null);
        super.setPositionInMap(x, y, map);
        map.getTiles()[x][y].setUnitOnTile(this);
    }

}
