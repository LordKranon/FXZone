package fxzone.game.render;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Codex.UnitSuperType;
import fxzone.game.logic.Map;
import fxzone.game.logic.Unit;
import fxzone.game.logic.Unit.UnitStance;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.Codex;
import java.util.ArrayList;
import java.util.List;
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

    private boolean facingLeft;

    private boolean reverseImage = false;

    private static boolean aircraftStance = false;
    private static double cumulativeDeltaOnAircraftImageTicks;
    private static final double totalAircraftImageTickDelay = Config.getDouble("GAME_SPEED_AIRCRAFT_PULSATION_INTERVAL");

    public GameObjectUnit(UnitType unitType, int x, int y, double tileRenderSize, Group group, java.awt.Color playerColor) {
        super(null, x, y, tileRenderSize, group);
        switch (Codex.getUnitProfile(unitType).SUPERTYPE){
            case LAND_VEHICLE:
                this.imageStanceNormal = AssetHandler.getImageUnit(new KeyUnit(unitType, 0, playerColor));
                this.imageStanceMove1 = imageStanceNormal;
                this.imageStanceMove2 = AssetHandler.getImageUnit(new KeyUnit(unitType, 1, playerColor));
                this.imageStanceAttack = imageStanceNormal;
                break;
            case LAND_INFANTRY:
                this.imageStanceNormal = AssetHandler.getImageUnit(new KeyUnit(unitType, 0, playerColor));
                this.imageStanceMove1 = AssetHandler.getImageUnit(new KeyUnit(unitType, 2, playerColor));
                this.imageStanceMove2 = AssetHandler.getImageUnit(new KeyUnit(unitType, 3, playerColor));
                this.imageStanceAttack = AssetHandler.getImageUnit(new KeyUnit(unitType, 1, playerColor));
                break;
            case AIRCRAFT_HELICOPTER:
            case AIRCRAFT_PLANE:
                this.reverseImage = true;
                this.imageStanceNormal = AssetHandler.getImageUnit(new KeyUnit(unitType, 0, playerColor));
                this.imageStanceMove1 = this.imageStanceNormal;
                this.imageStanceMove2 =  AssetHandler.getImageUnit(new KeyUnit(unitType, 1, playerColor));
                this.imageStanceAttack = this.imageStanceNormal;
                break;
            case SHIP_SMALL:
            case SHIP_LARGE:
            default:
                this.imageStanceNormal = AssetHandler.getImageUnit(new KeyUnit(unitType, 0, playerColor));
                this.imageStanceMove1 = this.imageStanceNormal;
                this.imageStanceMove2 = this.imageStanceNormal;
                this.imageStanceAttack = this.imageStanceNormal;
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

    public void setFacingLeft(boolean facingLeft){
        if(this.reverseImage){
            facingLeft = !facingLeft;
        }
        if(this.facingLeft == facingLeft){
            return;
        }
        this.facingLeft = facingLeft;
        if(facingLeft){
            getImageView().setScaleX(-1);
        } else {
            getImageView().setScaleX(1);
        }
    }

    public static void updatePulsatingAircraft(double delta, List<Unit> units){
        cumulativeDeltaOnAircraftImageTicks += delta;
        if(cumulativeDeltaOnAircraftImageTicks > totalAircraftImageTickDelay){
            cumulativeDeltaOnAircraftImageTicks -= totalAircraftImageTickDelay;
            aircraftStance = !aircraftStance;
            for(Unit unit : units){
                if(Codex.getUnitProfile(unit).SUPERTYPE == UnitSuperType.AIRCRAFT_HELICOPTER){
                    unit.setStance(aircraftStance?UnitStance.MOVE_1: UnitStance.MOVE_2);
                }
            }
        }
    }
}
