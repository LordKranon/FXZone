package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.Direction;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Map;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectUiMoveCommandArrowTile extends GameObjectUiTile{

    private Image imageArrow0;

    private final Direction directionOfPredecessor;
    private Direction directionOfSuccessor;

    public GameObjectUiMoveCommandArrowTile(int x, int y, Map map, Group group, Direction directionOfPredecessor) {
        super(x, y, map, group);
        this.directionOfPredecessor = directionOfPredecessor;
        this.directionOfSuccessor = Direction.NONE;
        calculateImage();
        this.setViewOrder(ViewOrder.UI_MOVE_COMMAND_ARROW);
    }

    public void setDirectionOfSuccessor(Direction direction){
        this.directionOfSuccessor = direction;
        calculateImage();
    }

    private void calculateImage(){
        this.setScale(1, 1);
        switch (directionOfSuccessor){
            case NONE:
                switch (directionOfPredecessor){
                    case UP: this.setScale(1, -1);
                    case NONE:
                    case DOWN: imageArrow0 = AssetHandler.getImage("/images/misc/move_arrow_2.png"); break;
                    case RIGHT: this.setScale(-1, 1);
                    case LEFT: imageArrow0 = AssetHandler.getImage("/images/misc/move_arrow.png"); break;
                }; break;
            case UP:
                switch (directionOfPredecessor){
                    case NONE:
                    case UP:
                        this.setScale(1, -1);
                        imageArrow0 = AssetHandler.getImage("/images/misc/move_deadend_2.png"); break;
                    case RIGHT: this.setScale(-1, 1);
                    case LEFT: imageArrow0 = AssetHandler.getImage("/images/misc/move_edge.png"); break;
                    case DOWN: imageArrow0 = AssetHandler.getImage("/images/misc/move_straight_2.png"); break;
                }; break;
            case DOWN:
                switch (directionOfPredecessor){
                    case NONE:
                    case DOWN:
                        imageArrow0 = AssetHandler.getImage("/images/misc/move_deadend_2.png"); break;
                    case RIGHT:
                        this.setScale(-1, -1);
                        imageArrow0 = AssetHandler.getImage("/images/misc/move_edge.png"); break;
                    case LEFT:
                        this.setScale(1, -1);
                        imageArrow0 = AssetHandler.getImage("/images/misc/move_edge.png"); break;
                    case UP: imageArrow0 = AssetHandler.getImage("/images/misc/move_straight_2.png"); break;
                }; break;
            case LEFT:
                switch (directionOfPredecessor){
                    case NONE:
                    case LEFT:
                        imageArrow0 = AssetHandler.getImage("/images/misc/move_deadend.png"); break;
                    case DOWN: this.setScale(1, -1);
                    case UP: imageArrow0 = AssetHandler.getImage("/images/misc/move_edge.png"); break;
                    case RIGHT: imageArrow0 = AssetHandler.getImage("/images/misc/move_straight.png"); break;
                }; break;
            case RIGHT:
                switch (directionOfPredecessor){
                    case NONE:
                    case RIGHT:
                        this.setScale(-1, 1);
                        imageArrow0 = AssetHandler.getImage("/images/misc/move_deadend.png"); break;
                    case UP:
                        this.setScale(-1, 1);
                        imageArrow0 = AssetHandler.getImage("/images/misc/move_edge.png"); break;
                    case DOWN:
                        this.setScale(-1, -1);
                        imageArrow0 = AssetHandler.getImage("/images/misc/move_edge.png"); break;
                    case LEFT: imageArrow0 = AssetHandler.getImage("/images/misc/move_straight.png"); break;
                }; break;
        }
        this.setImage(imageArrow0);

    }
}
