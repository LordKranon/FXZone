package fxzone.game.logic;

import fxzone.config.Config;
import fxzone.engine.controller.button.ButtonBuildingBuyUnit;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.serializable.BuildingSerializable;
import fxzone.game.render.GameObjectBuilding;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

public class Building extends TileSpaceObject{

    private final BuildingType buildingType;

    GameObjectBuilding gameObjectBuilding;

    private int ownerId;

    private ConstructionMenu constructionMenu;

    private List<UnitType> buildableUnitTypes;
    private boolean selectable;

    /*
    GAMEPLAY
     */
    private int statCaptureProgress;

    /*
    DEBUG
     */
    private static final boolean verbose = true;

    public Building(BuildingType buildingType, int x, int y) {
        super(x, y);
        this.buildingType = buildingType;
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
            // Set owner id to 0 if owner-less
            this.ownerId = 0;
        }

        this.gameObjectBuilding = new GameObjectBuilding(
            buildingType, x, y, tileRenderSize, group, playerColor
        );
        this.gameObjectInTileSpace = this.gameObjectBuilding;


        switch (this.buildingType){
            case FACTORY:
                this.buildableUnitTypes = Codex.BUILDABLE_UNIT_TYPES_FACTORY;
                this.selectable = true;
                break;
            case PORT:
                this.buildableUnitTypes = Codex.BUILDABLE_UNIT_TYPES_PORT;
                this.selectable = true;
                break;
            case AIRPORT:
                this.buildableUnitTypes = Codex.BUILDABLE_UNIT_TYPES_AIRPORT;
                this.selectable = true;
                break;
        }
        initializeConstructionMenuUI(buildableUnitTypes, playerColor);
    }

    private void initializeConstructionMenuUI(List<UnitType> buildableUnitTypes, Color color){
        if(!this.selectable){
            return;
        }
        this.constructionMenu = new ConstructionMenu(buildableUnitTypes, color);

    }
    public Pane getConstructionMenuPane(){
        return constructionMenu.getConstructionMenuPane();
    }
    public ArrayList<ButtonBuildingBuyUnit> getConstructionMenuButtons(){
        return constructionMenu.getConstructionMenuButtons();
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
    public boolean hasOwner(){
        return this.ownerId != 0;
    }

    public void setStatCaptureProgress(int captureProgress){
        this.statCaptureProgress = captureProgress;
    }
    public int getStatCaptureProgress(){
        return statCaptureProgress;
    }

    public void captureAtEndOfTurn(int extraCaptureProgress, int newOwnerId, Game game){
        this.statCaptureProgress += extraCaptureProgress;
        if(statCaptureProgress >= Codex.BUILDING_CAPTURE_TOTAL){
            this.statCaptureProgress = 0;
            this.ownerChanged(newOwnerId, game);
        }
    }
    /**
     * This building is captured meaning it gets a new owner,
     * or this buildings owner is gone and it becomes unowned.
     */
    public void ownerChanged(int newOwnerId, Game game){
        if(this.ownerId == newOwnerId){
            System.err.println(this+" [ownerChanged] New owner is old owner");
            return;
        }
        this.ownerId = newOwnerId;
        java.awt.Color playerColorNew = null;
        try {
            playerColorNew = FxUtils.toAwtColor(game.getPlayer(ownerId).getColor());
        }catch (NullPointerException e){
            System.err.println(this+" [ownerChanged] No color on owner change");
        }
        this.gameObjectBuilding.setImageToNewOwner(this.buildingType, playerColorNew);
        initializeConstructionMenuUI(buildableUnitTypes, playerColorNew);
    }

    @Override
    public String toString(){
        return "[BUILDING "+buildingType+"]";
    }

    public boolean isSelectable(){
        return this.selectable;
    }
}
