package fxzone.game.logic;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.serializable.BuildingSerializable;
import fxzone.game.render.GameObjectBuilding;
import java.awt.Color;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Building extends TileSpaceObject{

    private final BuildingType buildingType;

    GameObjectBuilding gameObjectBuilding;

    private int ownerId;

    private VBox constructionMenu;

    public Building(BuildingType buildingType, int x, int y, double tileRenderSize, Group group) {
        super(x, y, tileRenderSize, group);
        this.buildingType = buildingType;
        this.gameObjectBuilding = new GameObjectBuilding(buildingType, x, y, tileRenderSize, group, Color.WHITE);
        this.gameObjectInTileSpace = this.gameObjectBuilding;
        initializeConstructionMenuUI();
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
        initializeConstructionMenuUI();
    }

    private void initializeConstructionMenuUI(){
        constructionMenu = new VBox();
        constructionMenu.setPrefWidth(256);
        constructionMenu.setPrefHeight(512);
        constructionMenu.setVisible(true);
        constructionMenu.setStyle("-fx-background-color: #282828;");
        constructionMenu.setViewOrder(ViewOrder.GAME_BUILDING_UI_BACKGROUND);

        ImageView unitIcon = new ImageView();
        unitIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.INFANTRY, 0, Color.CYAN)));
        unitIcon.setFitWidth(128);
        unitIcon.setFitHeight(128);
        unitIcon.setTranslateX(0);
        unitIcon.setTranslateY(0);
        unitIcon.setVisible(true);
        unitIcon.setViewOrder(ViewOrder.GAME_BUILDING_UI_BUTTON);
        constructionMenu.getChildren().add(unitIcon);

        Button unitPurchaseButton = new Button();
        unitPurchaseButton.setPrefWidth(128);
        unitPurchaseButton.setPrefHeight(128);
        unitPurchaseButton.setTranslateX(128);
        unitPurchaseButton.setTranslateY(-128);
        unitPurchaseButton.setVisible(true);
        unitPurchaseButton.setViewOrder(ViewOrder.GAME_BUILDING_UI_BUTTON);
        unitPurchaseButton.setText("Buy");
        constructionMenu.getChildren().add(unitPurchaseButton);
    }
    public VBox getConstructionMenu(){
        return constructionMenu;
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
