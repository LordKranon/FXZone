package fxzone.game.render;

import fxzone.engine.render.AbstractGameObject;
import fxzone.game.logic.Tile;
import javafx.scene.Group;

public class GameObjectTile extends AbstractGameObject {

    /**
     * Graphical position without offset.
     * Equal to the game logical tile position times the graphical tile size.
     */
    private double xScaled, yScaled;

    public GameObjectTile(String path, int x, int y, double tileRenderSize, Group group){
        super(path, x * tileRenderSize, y * tileRenderSize, tileRenderSize, tileRenderSize, group);
        this.xScaled = x * tileRenderSize;
        this.yScaled = y * tileRenderSize;
    }

    public void setOffset(double offsetX, double offsetY){
        setX(offsetX + this.xScaled);
        setY(offsetY + this.yScaled);
    }
}
