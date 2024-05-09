package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Map;
import fxzone.game.logic.Unit.UnitStance;
import fxzone.game.logic.Unit.UnitType;
import fxzone.game.logic.UnitCodex;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;

/**
 * Graphical representation class of a unit
 */
public class GameObjectUnit extends GameObjectInTileSpace{

    private final Image imageStanceNormal;
    private final Image imageStanceMove1;
    private final Image imageStanceMove2;
    private final Image imageStanceAttack;

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
        switch (UnitCodex.getUnitProfile(unitType).SUPERTYPE){
            case LAND_VEHICLE:
                this.imageStanceNormal = AssetHandler.getImageUnitVehicle(new KeyUnit(unitType, 0, playerColor));
                this.imageStanceMove1 = imageStanceNormal;
                this.imageStanceMove2 = AssetHandler.getImageUnitVehicle(new KeyUnit(unitType, 1, playerColor));
                this.imageStanceAttack = imageStanceNormal;
                break;
            case LAND_INFANTRY:
            default:
                this.imageStanceNormal = AssetHandler.getImageUnitVehicle(new KeyUnit(unitType, 0, playerColor));
                this.imageStanceMove1 = AssetHandler.getImageUnitVehicle(new KeyUnit(unitType, 2, playerColor));
                this.imageStanceMove2 = AssetHandler.getImageUnitVehicle(new KeyUnit(unitType, 3, playerColor));
                this.imageStanceAttack = AssetHandler.getImageUnitVehicle(new KeyUnit(unitType, 1, playerColor));
                break;
        }
        this.setImage(imageStanceNormal);
        this.setViewOrder(ViewOrder.GAME_UNIT);
    }

    public void setStance(UnitStance unitStance){
        switch (unitStance){
            case NORMAL: this.setImage(imageStanceNormal); break;
            case MOVE_1: this.setImage(imageStanceMove1); break;
            case MOVE_2: this.setImage(imageStanceMove2); break;
            case ATTACK: this.setImage(imageStanceAttack); break;
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
