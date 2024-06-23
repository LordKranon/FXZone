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
        setImageToNewOwner(buildingType, playerColor);
        this.setViewOrder(ViewOrder.GAME_BUILDING);
    }

    public void setImageToNewOwner(BuildingType buildingType, java.awt.Color color){
        Image image = AssetHandler.getImageBuilding(new KeyBuilding(buildingType, color));
        this.setImage(image);
    }
}
