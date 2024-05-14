package fxzone.game.logic.serializable;

import fxzone.game.logic.Building;
import fxzone.game.logic.Codex.BuildingType;

public class BuildingSerializable extends TileSpaceObjectSerializable{

    public BuildingType buildingType;

    public int ownerId;

    public BuildingSerializable(Building building) {
        super(building);
        this.buildingType = building.getBuildingType();
        this.ownerId = building.getOwnerId();
    }
}
