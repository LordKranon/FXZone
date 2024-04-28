package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.ViewOrder;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectUiUnitHealth extends GameObjectInTileSpace{

    public GameObjectUiUnitHealth(int x, int y, double tileRenderSize, Group group) {
        super(null, x, y, tileRenderSize, group);
        this.setImage(AssetHandler.getImage("/images/misc/zone_hp_ui_0.png"));
        this.setViewOrder(ViewOrder.UI_UNIT_HEALTH);
    }
}
