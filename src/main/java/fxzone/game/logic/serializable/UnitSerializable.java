package fxzone.game.logic.serializable;

import fxzone.game.logic.Unit;
import fxzone.game.logic.Codex.UnitType;

public class UnitSerializable extends TileSpaceObjectSerializable{

    public UnitType unitType;

    public int ownerId;

    public int unitId;

    public UnitSerializable(Unit unit) {
        super(unit);
        this.unitType = unit.getUnitType();
        this.ownerId = unit.getOwnerId();
        this.unitId = unit.getUnitId();
    }
}
