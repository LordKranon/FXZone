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
import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.GameObjectBuilding;
import java.awt.Color;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Building extends TileSpaceObject{

    private final BuildingType buildingType;

    GameObjectBuilding gameObjectBuilding;

    private int ownerId;

    private Pane constructionMenu;

    private ArrayList<ButtonBuildingBuyUnit> constructionMenuButtons;

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
        }

        this.gameObjectBuilding = new GameObjectBuilding(
            buildingType, x, y, tileRenderSize, group, playerColor
        );
        this.gameObjectInTileSpace = this.gameObjectBuilding;
        initializeConstructionMenuUI(playerColor);
    }

    private void initializeConstructionMenuUI(Color color){
        constructionMenuButtons = new ArrayList<>();
        constructionMenu = new Pane();
        int UI_SIZE = Config.getInt("UI_SIZE_IN_GAME");
        int FONT_SIZE = 36 * UI_SIZE / 128;
        constructionMenu.setPrefWidth(3* UI_SIZE);
        constructionMenu.setPrefHeight(8* UI_SIZE);
        constructionMenu.setVisible(true);
        constructionMenu.setStyle("-fx-background-color: #282828;");
        constructionMenu.setViewOrder(ViewOrder.GAME_BUILDING_UI_BACKGROUND);

        int i = 0;
        for(UnitType unitType : Codex.BUILDABLE_UNIT_TYPES){
            ImageView unitIcon = new ImageView();
            unitIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(unitType, 0, color)));
            unitIcon.setFitWidth(UI_SIZE);
            unitIcon.setFitHeight(UI_SIZE);
            unitIcon.setTranslateX(0);
            unitIcon.setTranslateY(UI_SIZE*i);
            unitIcon.setVisible(true);
            unitIcon.setViewOrder(ViewOrder.GAME_BUILDING_UI_BUTTON);
            constructionMenu.getChildren().add(unitIcon);

            ButtonBuildingBuyUnit unitPurchaseButton = new ButtonBuildingBuyUnit(unitType);
            unitPurchaseButton.setPrefWidth(2* UI_SIZE);
            unitPurchaseButton.setPrefHeight(UI_SIZE);
            unitPurchaseButton.setTranslateX(UI_SIZE);
            unitPurchaseButton.setTranslateY(UI_SIZE*i);
            unitPurchaseButton.setVisible(true);
            unitPurchaseButton.setViewOrder(ViewOrder.GAME_BUILDING_UI_BUTTON);
            unitPurchaseButton.setText(Codex.getUnitProfile(unitType).NAME);
            unitPurchaseButton.setStyle(unitPurchaseButton.getStyle()+ " ;-fx-font-size:"+FONT_SIZE+";");
            constructionMenu.getChildren().add(unitPurchaseButton);

            constructionMenuButtons.add(unitPurchaseButton);

            i++;
        }


    }
    public Pane getConstructionMenu(){
        return constructionMenu;
    }
    public ArrayList<ButtonBuildingBuyUnit> getConstructionMenuButtons(){
        return constructionMenuButtons;
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
        initializeConstructionMenuUI(playerColorNew);
    }

    @Override
    public String toString(){
        return "[BUILDING "+buildingType+"]";
    }
}
