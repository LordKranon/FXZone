package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Map;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectUiMoveCommandGridTile extends GameObjectUiTile{

    public GameObjectUiMoveCommandGridTile(int x, int y, Map map, Group group, boolean redForEnemy) {
        super(x, y, map, group);
        Image image = AssetHandler.getImage(redForEnemy ? "/images/misc/s_red.png" : "/images/misc/s_green.png");
        this.setImage(image);
        this.setViewOrder(ViewOrder.UI_MOVE_COMMAND_GRID);
    }
}
