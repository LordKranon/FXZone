package fxzone.controller.menu;

import fxzone.config.Config;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyBuilding;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.ZoneMediaPlayer;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Codex.UnitType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ArmyUiController extends AbstractUiController {

    private int jingleSelectedCurrentIndex;
    private boolean jingleSelectorActive = true;

    public ArmyUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ArmyView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                return new ArmyUiControllerFxml(gameController);
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }

    class ArmyUiControllerFxml{
        private final AbstractGameController gameController;

        public ArmyUiControllerFxml(AbstractGameController gameController) {
            this.gameController = gameController;
        }

        @FXML
        AnchorPane anchorPane;

        @FXML
        public void initialize(){
            resize(anchorPane, gameController.getStage());
            initializeOuter(anchorPane);
        }

        @FXML
        private void back(){
            gameController.setActiveUiController(new CampaignMenuUiController(gameController));
        }
    }

    private void initializeOuter(AnchorPane anchorPane){
        GridPane gridPaneOuter = (GridPane) anchorPane.getChildren().get(0);
        GridPane gridPaneInner = (GridPane) gridPaneOuter.getChildren().get(2);
        VBox vBox = (VBox) gridPaneInner.getChildren().get(1);
        HBox hBox = (HBox) vBox.getChildren().get(0);

        double uiSizeInGameMenus = Config.getDouble("UI_SIZE_IN_GAME_MENUS");
        double fontSize = ((uiSizeInGameMenus) / 2.5);

        Color color;
        try{
            color = Color.web(Config.getString("ARMY_COLOR"));
        } catch (Exception e){
            color = Color.RED;
            System.err.println("[ARMY-UI-CONTROLLER] [initializeOuter] ERROR on getting player army color.");
        }

        double imageSize = uiSizeInGameMenus / 100. * 256.;
        ImageView imageViewInfantry = new ImageView(AssetHandler.getImageUnit(new KeyUnit(UnitType.INFANTRY, 0, FxUtils.toAwtColor(color))));
        imageViewInfantry.setFitWidth(imageSize);
        imageViewInfantry.setFitHeight(imageSize);
        imageViewInfantry.setVisible(true);
        ImageView imageViewTank = new ImageView(AssetHandler.getImageUnit(new KeyUnit(UnitType.TANK_BATTLE, 0, FxUtils.toAwtColor(color))));
        imageViewTank.setFitWidth(imageSize);
        imageViewTank.setFitHeight(imageSize);
        imageViewTank.setVisible(true);
        ImageView imageViewJet = new ImageView(AssetHandler.getImageUnit(new KeyUnit(UnitType.PLANE_JET, 0, FxUtils.toAwtColor(color))));
        imageViewJet.setScaleX(-1);
        imageViewJet.setFitWidth(imageSize);
        imageViewJet.setFitHeight(imageSize);
        imageViewJet.setVisible(true);
        ImageView imageViewCity = new ImageView(AssetHandler.getImageBuilding(new KeyBuilding(BuildingType.CITY, FxUtils.toAwtColor(color))));
        imageViewCity.setFitWidth(imageSize);
        imageViewCity.setFitHeight(imageSize);
        imageViewCity.setVisible(true);
        ImageView imageViewFactory = new ImageView(AssetHandler.getImageBuilding(new KeyBuilding(BuildingType.FACTORY, FxUtils.toAwtColor(color))));
        imageViewFactory.setFitWidth(imageSize);
        imageViewFactory.setFitHeight(imageSize);
        imageViewFactory.setVisible(true);

        VBox column0 = (VBox) hBox.getChildren().get(0);
        VBox column1 = (VBox) hBox.getChildren().get(1);

        HBox subRow01 = new HBox();
        subRow01.setAlignment(Pos.CENTER);
        HBox subRow02 = new HBox();
        subRow02.setAlignment(Pos.CENTER);

        HBox subRow1 = new HBox();
        subRow1.setAlignment(Pos.CENTER);
        HBox subRow2 = new HBox();
        subRow2.setAlignment(Pos.CENTER);
        HBox subRow3 = new HBox();
        subRow3.setAlignment(Pos.CENTER);

        subRow01.getChildren().add(imageViewCity);
        subRow02.getChildren().add(imageViewFactory);
        subRow1.getChildren().add(imageViewInfantry);
        subRow2.getChildren().add(imageViewTank);
        subRow3.getChildren().add(imageViewJet);

        column0.getChildren().add(subRow01);
        column0.getChildren().add(subRow02);

        column1.getChildren().add(subRow1);
        column1.getChildren().add(subRow2);
        column1.getChildren().add(subRow3);

        TextField textFieldName = new TextField();
        textFieldName.setVisible(true);
        textFieldName.setFont(new Font(fontSize));
        textFieldName.setPromptText("Army Name");
        textFieldName.setText(Config.getString("ARMY_NAME"));
        textFieldName.setPrefWidth(4*uiSizeInGameMenus);
        subRow1.getChildren().add(textFieldName);

        TextField textFieldColor = new TextField();
        textFieldColor.setVisible(true);
        textFieldColor.setFont(new Font(fontSize));
        textFieldColor.setPromptText("Army Color");
        textFieldColor.setText(Config.getString("ARMY_COLOR"));
        textFieldColor.setPrefWidth(4*uiSizeInGameMenus);
        subRow2.getChildren().add(textFieldColor);

        String currentJingle = Config.getString("ARMY_JINGLE");
        Button buttonJingle = new Button(armyJinglesNames.get(currentJingle));
        buttonJingle.setVisible(true);
        buttonJingle.setStyle("-fx-font-size: "+(armyJinglesNames.get(currentJingle).length() <= 16 ? fontSize : fontSize/2.));
        buttonJingle.setPrefWidth(4*uiSizeInGameMenus);
        buttonJingle.setPrefHeight(0.85*uiSizeInGameMenus);
        subRow3.getChildren().add(buttonJingle);
        for(int i = 0; i < armyJinglesList.size(); i++){
            if(armyJinglesList.get(i).equals(currentJingle)){
                jingleSelectedCurrentIndex = i;
                break;
            }
        }
        buttonJingle.setOnMouseClicked(mouseEvent -> {
            if(jingleSelectorActive) {

                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                    jingleSelectedCurrentIndex--;
                    if(jingleSelectedCurrentIndex < 0){
                        jingleSelectedCurrentIndex = armyJinglesList.size() - 1;
                    }
                } else {
                    jingleSelectedCurrentIndex++;
                    if(jingleSelectedCurrentIndex >= armyJinglesList.size()){
                        jingleSelectedCurrentIndex = 0;
                    }
                }
                buttonJingle.setStyle("-fx-font-size: "+(armyJinglesNames.get(armyJinglesList.get(jingleSelectedCurrentIndex)).length() <= 16 ? fontSize : fontSize/2.));
                buttonJingle.setText(armyJinglesNames.get(armyJinglesList.get(jingleSelectedCurrentIndex)));

                ZoneMediaPlayer jinglePreview = new ZoneMediaPlayer("/sounds/effects_musical/jingle/zone_jingle_"+armyJinglesList.get(jingleSelectedCurrentIndex)+".mp3");
                jinglePreview.play();
            } else {
                jingleSelectorActive = true;
                //buttonJingle.setStyle("-fx-font-size: "+fontSize+"; -fx-border-width: "+5*uiSizeInGameMenus/100+"; -fx-border-color: eeeeee;");
            }
        });

        VBox column2 = (VBox) hBox.getChildren().get(2);
        Button buttonApply = new Button("Apply");
        buttonApply.setVisible(true);
        buttonApply.setStyle("-fx-font-size: "+fontSize);
        buttonApply.setPrefWidth(2*uiSizeInGameMenus);
        column2.getChildren().add(buttonApply);
        column2.setPrefWidth(5*uiSizeInGameMenus);

        buttonApply.setOnMouseClicked(mouseEvent -> {
            String nameEntered = textFieldName.getText();
            if(!nameEntered.equals("")){
                Config.set("ARMY_NAME", nameEntered);
                Config.saveConfig();
            } else {
                System.err.println("[ARMY-UI-CONTROLLER] ERROR Army name cannot be empty.");
            }
            String colorEntered = textFieldColor.getText();
            try{
                Color newColor = Color.web(colorEntered);
                Config.set("ARMY_COLOR", colorEntered);
                Config.saveConfig();

                imageViewInfantry.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.INFANTRY, 0, FxUtils.toAwtColor(newColor))));
                imageViewTank.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.TANK_BATTLE, 0, FxUtils.toAwtColor(newColor))));
                imageViewJet.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.PLANE_JET, 0, FxUtils.toAwtColor(newColor))));

                imageViewCity.setImage(AssetHandler.getImageBuilding(new KeyBuilding(BuildingType.CITY, FxUtils.toAwtColor(newColor))));
                imageViewFactory.setImage(AssetHandler.getImageBuilding(new KeyBuilding(BuildingType.FACTORY, FxUtils.toAwtColor(newColor))));

            } catch (Exception e){
                System.err.println("[ARMY-UI-CONTROLLER] ERROR on applying army color.");
            }
            String jingleSelected = armyJinglesList.get(jingleSelectedCurrentIndex);
            Config.set("ARMY_JINGLE", jingleSelected);
            Config.saveConfig();
        });
    }

    private static HashMap<String, String> armyJinglesNames = new HashMap<>(){{
        put("arma", "King of the Hill");
        put("jr_1", "Realm Beyond");
        put("lamour_tojours", "Savior of Europe");
        put("socialist_world_republic", "Socialist World Republic");
    }};
    private static List<String> armyJinglesList = Arrays.asList(
        "arma",
        "jr_1",
        "lamour_tojours",
        "socialist_world_republic"
    );
}
