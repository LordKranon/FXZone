package fxzone.controller.menu;

import fxzone.config.Config;
import fxzone.controller.menu.CampaignMenuUiController.CampaignMenuUiControllerFxml;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.game.logic.Codex;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    }
}
