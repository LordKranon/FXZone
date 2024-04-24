package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyMoveCommandArrow;
import fxzone.engine.utils.Direction;
import fxzone.game.logic.Map;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectUiMoveCommandArrowTile extends GameObjectInTileSpace{

    private Image imageArrow0;

    private final Direction directionOfPredecessor;
    private Direction directionOfSuccessor;

    /**
     * The in-game tile this arrowTile is on.
     */
    private final int tileX, tileY;

    public GameObjectUiMoveCommandArrowTile(int x, int y, Map map, Group group, Direction directionOfPredecessor) {
        super(null, x, y, map.getTileRenderSize(), group);
        this.tileX = x;
        this.tileY = y;
        this.directionOfPredecessor = directionOfPredecessor;
        this.directionOfSuccessor = Direction.NONE;
        this.setOffset(map);
        calculateImage();
    }
    public void changeTileRenderSize(Map map){
        setPositionInMap(tileX, tileY, map);
        setFit(map.getTileRenderSize());
    }

    public void setDirectionOfSuccessor(Direction direction){
        this.directionOfSuccessor = direction;
    }

    private void calculateImage(){
        switch (directionOfPredecessor){
            case DOWN: this.setScale(1, -1);
            case NONE:
            case UP: imageArrow0 = AssetHandler.getImage("/images/misc/move_arrow_2.png"); break;
            case LEFT: this.setScale(-1, 1);
            case RIGHT: imageArrow0 = AssetHandler.getImage("/images/misc/move_arrow.png"); break;
        }
        this.setImage(imageArrow0);

    }
}
