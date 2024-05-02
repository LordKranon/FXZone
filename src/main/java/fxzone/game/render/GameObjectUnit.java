package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnitVehicle;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Map;
import fxzone.game.logic.UnitType;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;

/**
 * Graphical representation class of a unit
 */
public class GameObjectUnit extends GameObjectInTileSpace{

    private final Image imageStance0;
    private final Image imageStance1;

    /**
     * A unit goes in between tiles while moving, these values range from 0 - 1
     * with 0 meaning the unit is exactly on the center of the tile it's on
     * and 1 meaning the unit has reached the center of the very next tile.
     */
    private double tileCenterOffsetX, tileCenterOffsetY;

    private boolean blackedOut;
    private boolean attacking;

    public GameObjectUnit(UnitType unitType, int x, int y, double tileRenderSize, Group group, java.awt.Color playerColor) {
        super(null, x, y, tileRenderSize, group);
        this.imageStance0 = AssetHandler.getImageUnitVehicle(new KeyUnitVehicle(unitType, 0, playerColor));
        this.imageStance1 = AssetHandler.getImageUnitVehicle(new KeyUnitVehicle(unitType, 1, playerColor));
        this.setImage(imageStance0);
        this.setViewOrder(ViewOrder.GAME_UNIT);
    }

    public void setStance(int stance){
        if(stance == 0){
            this.setImage(imageStance0);
        } else {
            this.setImage(imageStance1);
        }
    }

    @Override
    public void setPositionInMap(double x, double y, Map map){
        super.setPositionInMap(x + tileCenterOffsetX, y + tileCenterOffsetY, map);
    }

    public void setTileCenterOffset(double tileCenterOffsetX, double tileCenterOffsetY, double x, double y, Map map){
        this.tileCenterOffsetX = tileCenterOffsetX;
        this.tileCenterOffsetY = tileCenterOffsetY;
        this.setPositionInMap(x, y, map);
    }

    public void setBlackedOut(boolean blackedOut){
        if(this.blackedOut == blackedOut){
            return;
        }
        this.blackedOut = blackedOut;
        if(blackedOut){
            ColorAdjust colorAdjustBlackout = new ColorAdjust();
            colorAdjustBlackout.setBrightness(-.5);
            getImageView().setEffect(colorAdjustBlackout);
        } else {
            getImageView().setEffect(null);
        }
    }

    public void setAttackingStance(boolean attacking){
        if(this.attacking == attacking){
            return;
        }
        this.attacking = attacking;
        if(attacking){
            ColorAdjust colorAdjustBlackout = new ColorAdjust();
            colorAdjustBlackout.setBrightness(-1);
            getImageView().setEffect(colorAdjustBlackout);
        } else {
            getImageView().setEffect(null);
        }
    }
}
