package fxzone.game.logic.serializable;

import fxzone.game.logic.Unit;
import fxzone.game.logic.Unit.UnitType;

public class UnitSerializable extends TileSpaceObjectSerializable{

    public UnitType unitType;

    public int ownerId;

    public UnitSerializable(Unit unit) {
        super(unit);
        this.unitType = unit.getUnitType();
        this.ownerId = unit.getOwnerId();
    }
}
