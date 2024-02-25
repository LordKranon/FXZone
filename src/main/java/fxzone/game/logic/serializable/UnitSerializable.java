package fxzone.game.logic.serializable;

import fxzone.game.logic.Player;
import fxzone.game.logic.Unit;

public class UnitSerializable extends TileSpaceObjectSerializable{

    public String unitName;

    public int ownerId;

    public UnitSerializable(Unit unit) {
        super(unit);
        this.unitName = unit.getUnitName();
        this.ownerId = unit.getOwnerId();
    }
}
