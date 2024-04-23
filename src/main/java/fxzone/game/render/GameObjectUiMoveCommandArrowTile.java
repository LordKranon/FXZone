package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.game.logic.Map;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectUiMoveCommandArrowTile extends GameObjectInTileSpace{

    private final Image imageArrow0;

    /**
     * The in-game tile this arrowTile is on.
     */
    private final int tileX, tileY;

    public GameObjectUiMoveCommandArrowTile(int x, int y, Map map, Group group) {
        super(null, x, y, map.getTileRenderSize(), group);
        this.tileX = x;
        this.tileY = y;
        this.imageArrow0 = AssetHandler.getImage("/images/misc/move_arrow.png");
        this.setOffset(map);
        this.setImage(imageArrow0);
    }
    public void changeTileRenderSize(Map map){
        setPositionInMap(tileX, tileY, map);
        setFit(map.getTileRenderSize());
    }
}
