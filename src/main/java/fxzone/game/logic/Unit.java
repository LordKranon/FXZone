package fxzone.game.logic;

import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.GameObjectUnit;
import javafx.scene.Group;

public class Unit extends TileSpaceObject{

    private final String unitName;

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
        this.gameObjectInTileSpace = new GameObjectUnit(unitName, x, y, tileRenderSize, group);
    }

    public Unit(UnitSerializable unitSerializable, double tileRenderSize, Group group){
        super(unitSerializable);
        this.unitName = unitSerializable.unitName;
        this.gameObjectInTileSpace = new GameObjectUnit(unitName, x, y, tileRenderSize, group);
    }

    public String getUnitName(){
        return unitName;
    }
}
