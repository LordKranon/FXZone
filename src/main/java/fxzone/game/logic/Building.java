package fxzone.game.logic;

import fxzone.engine.utils.FxUtils;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.serializable.BuildingSerializable;
import fxzone.game.render.GameObjectBuilding;
import java.awt.Color;
import javafx.scene.Group;

public class Building extends TileSpaceObject{

    private final BuildingType buildingType;

    GameObjectBuilding gameObjectBuilding;

    private int ownerId;

    public Building(BuildingType buildingType, int x, int y, double tileRenderSize, Group group) {
        super(x, y, tileRenderSize, group);
        this.buildingType = buildingType;
        this.gameObjectBuilding = new GameObjectBuilding(buildingType, x, y, tileRenderSize, group, Color.WHITE);
        this.gameObjectInTileSpace = this.gameObjectBuilding;
    }
    public Building(BuildingSerializable buildingSerializable, double tileRenderSize, Group group, Game game){
        super(buildingSerializable);
        this.buildingType = buildingSerializable.buildingType;
        this.ownerId = buildingSerializable.ownerId;

        java.awt.Color playerColor = null;
        try {
            playerColor = FxUtils.toAwtColor(game.getPlayer(ownerId).getColor());
        }catch (NullPointerException e){
            System.err.println(this+" Initialized without owner color");
        }

        this.gameObjectBuilding = new GameObjectBuilding(
            buildingType, x, y, tileRenderSize, group, playerColor
        );
        this.gameObjectInTileSpace = this.gameObjectBuilding;
    }

    public BuildingType getBuildingType(){
        return buildingType;
    }

    public int getOwnerId(){
        return ownerId;
    }
    public void setOwnerId(int ownerId){
        this.ownerId = ownerId;
    }

    @Override
    public String toString(){
        return "[BUILDING "+buildingType+"]";
    }
}
