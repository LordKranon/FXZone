package fxzone.game.logic;

import fxzone.game.render.GameObjectUnit;
import javafx.scene.Group;

public class Unit extends TileSpaceObject{

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
        this.gameObjectInTileSpace = new GameObjectUnit(unitName, x, y, tileRenderSize, group);
    }
}
