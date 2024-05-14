package fxzone.engine.handler;

import fxzone.game.logic.Codex.UnitType;

public class KeyUnit {
    public UnitType keyUnitType;
    public Integer keyStance;
    public java.awt.Color keyColor;

    public KeyUnit(UnitType keyUnitType, int keyStance, java.awt.Color keyColor) {
        this.keyUnitType = keyUnitType;
        this.keyStance = keyStance;
        this.keyColor = keyColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyUnit))
            return false;
        KeyUnit ref = (KeyUnit) obj;
        return this.keyUnitType.equals(ref.keyUnitType) &&
            this.keyStance.equals(ref.keyStance) &&
            (
                (this.keyColor == null && ref.keyColor == null) ||
                    ((this.keyColor != null && ref.keyColor != null) &&
                        (this.keyColor.equals(ref.keyColor)))
            );
    }

    @Override
    public int hashCode() {
        return keyUnitType.hashCode() ^ keyStance.hashCode();
    }
}
