package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyBuilding;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Codex.BuildingType;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectBuilding extends GameObjectInTileSpace{

    public GameObjectBuilding(BuildingType buildingType, int x, int y, double tileRenderSize, Group group, java.awt.Color playerColor) {
        super(null, x, y, tileRenderSize, group);
        Image image = AssetHandler.getImageBuilding(new KeyBuilding(buildingType, playerColor));
        this.setImage(image);
        this.setViewOrder(ViewOrder.GAME_BUILDING);
    }
}
