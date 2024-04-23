package fxzone.engine.handler;

import fxzone.game.logic.UnitType;

public class KeyUnitVehicle {
    public UnitType keyType;
    public Integer keyStance;

    public KeyUnitVehicle(UnitType keyType, int keyStance) {
        this.keyType = keyType;
        this.keyStance = keyStance;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyUnitVehicle))
            return false;
        KeyUnitVehicle ref = (KeyUnitVehicle) obj;
        return this.keyType.equals(ref.keyType) &&
            this.keyStance.equals(ref.keyStance);
    }

    @Override
    public int hashCode() {
        return keyType.hashCode() ^ keyStance.hashCode();
    }
}
