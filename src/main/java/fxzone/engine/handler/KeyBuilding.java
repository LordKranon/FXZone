package fxzone.engine.handler;

import fxzone.game.logic.Codex.BuildingType;

public class KeyBuilding {

    public BuildingType keyBuildingType;
    public java.awt.Color keyColor;

    public KeyBuilding(BuildingType keyBuildingType, java.awt.Color keyColor) {
        this.keyBuildingType = keyBuildingType;
        this.keyColor = keyColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyBuilding))
            return false;
        KeyBuilding ref = (KeyBuilding) obj;
        return this.keyBuildingType.equals(ref.keyBuildingType) &&
            (
                (this.keyColor == null && ref.keyColor == null) ||
                    ((this.keyColor != null && ref.keyColor != null) &&
                        (this.keyColor.equals(ref.keyColor)))
            );
    }

    @Override
    public int hashCode() {
        return keyBuildingType.hashCode();
    }
}
