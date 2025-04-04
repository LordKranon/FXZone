package fxzone.controller.menu;

import fxzone.config.Config;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.FxUtils;
import fxzone.game.logic.Codex.UnitType;
import java.util.HashMap;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ArmyUiController extends AbstractUiController {


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

        VBox column1 = (VBox) hBox.getChildren().get(0);

        HBox subRow1 = new HBox();
        subRow1.setAlignment(Pos.CENTER);
        HBox subRow2 = new HBox();
        subRow2.setAlignment(Pos.CENTER);
        HBox subRow3 = new HBox();
        subRow3.setAlignment(Pos.CENTER);

        subRow1.getChildren().add(imageViewInfantry);
        subRow2.getChildren().add(imageViewTank);
        subRow3.getChildren().add(imageViewJet);

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

        Button buttonJingle = new Button(armyJingles.get(Config.getString("ARMY_JINGLE")));
        buttonJingle.setVisible(true);
        buttonJingle.setStyle("-fx-font-size: "+fontSize);
        buttonJingle.setPrefWidth(4*uiSizeInGameMenus);
        subRow3.getChildren().add(buttonJingle);

        VBox column2 = (VBox) hBox.getChildren().get(1);
        Button buttonApply = new Button("Apply");
        buttonApply.setVisible(true);
        buttonApply.setStyle("-fx-font-size: "+fontSize);
        buttonApply.setPrefWidth(2*uiSizeInGameMenus);
        column2.getChildren().add(buttonApply);

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

            } catch (Exception e){
                System.err.println("[ARMY-UI-CONTROLLER] ERROR on applying army color.");
            }
        });
    }

    private static HashMap<String, String> armyJingles = new HashMap<>(){{
        put("arma", "King of the Hill");
        put("jr_1", "Realm Beyond");
        put("lamour_tojours", "Savior of Europe");
        put("socialist_world_republic", "Socialist World Republic");
    }};
}
