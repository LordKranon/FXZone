package fxzone.engine.handler;

import fxzone.game.logic.UnitType;

public class KeyUnitVehicle {
    public UnitType keyType;
    public Integer keyStance;
    public java.awt.Color keyColor;

    public KeyUnitVehicle(UnitType keyType, int keyStance, java.awt.Color keyColor) {
        this.keyType = keyType;
        this.keyStance = keyStance;
        this.keyColor = keyColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyUnitVehicle))
            return false;
        KeyUnitVehicle ref = (KeyUnitVehicle) obj;
        return this.keyType.equals(ref.keyType) &&
            this.keyStance.equals(ref.keyStance) &&
            (
                (this.keyColor == null && ref.keyColor == null) ||
                    ((this.keyColor != null && ref.keyColor != null) &&
                        (this.keyColor.equals(ref.keyColor)))
            );
    }

    @Override
    public int hashCode() {
        return keyType.hashCode() ^ keyStance.hashCode();
    }
}
