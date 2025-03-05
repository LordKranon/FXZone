package fxzone.game.logic;

import fxzone.config.Config;
import fxzone.engine.controller.button.ButtonBuildingBuyUnit;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Codex.UnitType;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

public class ConstructionMenu {

    private Pane constructionMenuPane;

    private ArrayList<ButtonBuildingBuyUnit> constructionMenuButtons;

    private List<UnitType> buildableUnitTypes;

    ConstructionMenu(List<UnitType> buildableUnitTypes, java.awt.Color color){
        this.buildableUnitTypes = buildableUnitTypes;
        constructionMenuButtons = new ArrayList<>();
        constructionMenuPane = new Pane();
        int UI_SIZE = Config.getInt("UI_SIZE_IN_GAME");
        int FONT_SIZE = 36 * UI_SIZE / 128;
        constructionMenuPane.setPrefWidth(4* UI_SIZE);
        constructionMenuPane.setPrefHeight(this.buildableUnitTypes.size()* UI_SIZE);
        constructionMenuPane.setVisible(true);
        constructionMenuPane.setStyle("-fx-background-color: #282828;");
        constructionMenuPane.setViewOrder(ViewOrder.GAME_BUILDING_UI_BACKGROUND);

        int i = 0;
        for(UnitType unitType : (this.buildableUnitTypes)){
            ImageView unitIcon = new ImageView();
            unitIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(unitType, 0, color)));
            unitIcon.setFitWidth(UI_SIZE);
            unitIcon.setFitHeight(UI_SIZE);
            unitIcon.setTranslateX(0);
            unitIcon.setTranslateY(UI_SIZE*i);
            unitIcon.setVisible(true);
            unitIcon.setViewOrder(ViewOrder.GAME_BUILDING_UI_BUTTON);
            constructionMenuPane.getChildren().add(unitIcon);


            Label unitCostLabel = new Label();
            unitCostLabel.setText(""+Codex.getUnitProfile(unitType).COST);
            unitCostLabel.setPrefWidth(UI_SIZE);
            unitCostLabel.setPrefHeight(UI_SIZE);
            unitCostLabel.setTranslateX(UI_SIZE);
            unitCostLabel.setTranslateY(UI_SIZE*i);
            unitCostLabel.setVisible(true);
            unitCostLabel.setViewOrder(ViewOrder.GAME_BUILDING_UI_BUTTON);
            unitCostLabel.setStyle("-fx-text-fill: white; -fx-font-size:"+FONT_SIZE*2+"; ");
            unitCostLabel.setTextAlignment(TextAlignment.RIGHT);
            constructionMenuPane.getChildren().add(unitCostLabel);


            ButtonBuildingBuyUnit unitPurchaseButton = new ButtonBuildingBuyUnit(unitType);
            unitPurchaseButton.setPrefWidth(2* UI_SIZE);
            unitPurchaseButton.setPrefHeight(UI_SIZE);
            unitPurchaseButton.setTranslateX(UI_SIZE*2);
            unitPurchaseButton.setTranslateY(UI_SIZE*i);
            unitPurchaseButton.setVisible(true);
            unitPurchaseButton.setViewOrder(ViewOrder.GAME_BUILDING_UI_BUTTON);
            unitPurchaseButton.setText(Codex.getUnitProfile(unitType).NAME);
            unitPurchaseButton.setStyle(unitPurchaseButton.getStyle()+ " ;-fx-font-size:"+FONT_SIZE+";");
            constructionMenuPane.getChildren().add(unitPurchaseButton);

            constructionMenuButtons.add(unitPurchaseButton);

            i++;
        }
    }

    public Pane getConstructionMenuPane(){
        return constructionMenuPane;
    }
    public ArrayList<ButtonBuildingBuyUnit> getConstructionMenuButtons(){
        return constructionMenuButtons;
    }
}
