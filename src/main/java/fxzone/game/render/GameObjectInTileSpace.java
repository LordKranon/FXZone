package fxzone.game.render;

import fxzone.engine.render.AbstractGameObject;
import fxzone.game.logic.Map;
import fxzone.game.logic.Tile;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectInTileSpace extends AbstractGameObject {

    /**
     * Graphical position without offset.
     * Equal to the game logical tile position times the graphical tile size.
     */
    private double xScaled, yScaled;


    public GameObjectInTileSpace(Image image, int x, int y, double tileRenderSize, Group group){
        super(image, x * tileRenderSize, y * tileRenderSize, tileRenderSize, tileRenderSize, group);
        this.xScaled = x * tileRenderSize;
        this.yScaled = y * tileRenderSize;
    }

    public void setOffset(double offsetX, double offsetY){
        setX(offsetX + this.xScaled);
        setY(offsetY + this.yScaled);
    }

    /**
     * Change the game logical position of this object in a given map.
     * @param x tile space x
     * @param y tile space y
     * @param map map this object is in the tile space of
     */
    public void setPositionInMap(int x, int y, Map map){
        this.xScaled = x * map.getTileRenderSize();
        this.yScaled = y * map.getTileRenderSize();
        setOffset(map.getOffsetX(), map.getOffsetY());
    }

    public void changeTileRenderSize(int x, int y, Map map){
        setPositionInMap(x, y, map);
        setFit(map.getTileRenderSize());
    }
}
