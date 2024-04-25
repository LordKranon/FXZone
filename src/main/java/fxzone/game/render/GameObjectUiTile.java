package fxzone.game.render;

import fxzone.game.logic.Map;
import javafx.scene.Group;

public abstract class GameObjectUiTile extends GameObjectInTileSpace{

    /**
     * The in-game tile this UI element is on.
     */
    private final int tileX, tileY;

    public GameObjectUiTile(int x, int y, Map map, Group group) {
        super(null, x, y, map.getTileRenderSize(), group);
        this.tileX = x;
        this.tileY = y;
        this.setOffset(map);
    }

    public void changeTileRenderSize(Map map){
        setPositionInMap(tileX, tileY, map);
        setFit(map.getTileRenderSize());
    }
}
