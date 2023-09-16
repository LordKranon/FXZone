package fxzone.game.logic.serializable;

import fxzone.game.logic.Player;
import fxzone.game.logic.Unit;

public class UnitSerializable extends TileSpaceObjectSerializable{

    public String unitName;

    public PlayerSerializable owner;

    public UnitSerializable(Unit unit) {
        super(unit);
        this.unitName = unit.getUnitName();
        Player owner = unit.getOwner();
        if(owner != null){
            this.owner = new PlayerSerializable(owner);
        }
    }
}
