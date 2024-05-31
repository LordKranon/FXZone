package fxzone.engine.controller.button;

import fxzone.game.logic.Codex.UnitType;
import javafx.scene.control.Button;

public class ButtonBuildingBuyUnit extends Button {

    /**
     * The unit bought with this button
     */
    private final UnitType unitType;

    public ButtonBuildingBuyUnit(UnitType unitType){
        super();
        this.unitType = unitType;
    }

    public UnitType getUnitType(){
        return unitType;
    }
}
