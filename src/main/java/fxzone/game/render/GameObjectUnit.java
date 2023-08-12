package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnitVehicle;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectUnit extends GameObjectInTileSpace{

    private final Image stance1;
    private final Image stance2;

    public GameObjectUnit(String unitName, int x, int y, double tileRenderSize,
        Group group) {
        super(null, x, y, tileRenderSize, group);
        this.stance1 = AssetHandler.getImageUnitVehicle(new KeyUnitVehicle(unitName, 0));
        this.stance2 = AssetHandler.getImageUnitVehicle(new KeyUnitVehicle(unitName, 1));
        this.setImage(stance1);
    }
}
