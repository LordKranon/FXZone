package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnitVehicle;
import javafx.scene.Group;
import javafx.scene.image.Image;

/**
 * Graphical representation class of a unit
 */
public class GameObjectUnit extends GameObjectInTileSpace{

    private final Image imageStance0;
    private final Image imageStance1;

    public GameObjectUnit(String unitName, int x, int y, double tileRenderSize,
        Group group) {
        super(null, x, y, tileRenderSize, group);
        this.imageStance0 = AssetHandler.getImageUnitVehicle(new KeyUnitVehicle(unitName, 0));
        this.imageStance1 = AssetHandler.getImageUnitVehicle(new KeyUnitVehicle(unitName, 1));
        this.setImage(imageStance0);
    }

    public void setStance(int stance){
        if(stance == 0){
            this.setImage(imageStance0);
        } else {
            this.setImage(imageStance1);
        }
    }
}
