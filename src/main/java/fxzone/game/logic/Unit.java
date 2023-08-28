package fxzone.game.logic;

import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.GameObjectUnit;
import javafx.scene.Group;

public class Unit extends TileSpaceObject{

    private final String unitName;

    /**
     * Graphical representation. Superclass already has a pointer, this one specifies it further.
     */
    GameObjectUnit gameObjectUnit;

    private int stance = 0;

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

    public void switchStance(){
        if(stance == 0){
            stance = 1;
        } else {
            stance = 0;
        }
        gameObjectUnit.setStance(stance);
    }
}
