package fxzone.game.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MainMenuUiController extends AbstractUiController {

    public MainMenuUiController(AbstractGameController gameController) {
        super(gameController);
    }

    public void init(AbstractGameController gameController, Group root2D){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainMenuView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                return new MainMenuUiControllerFxml(gameController);
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MainMenuUiControllerFxml{
        private final AbstractGameController gameController;

        public MainMenuUiControllerFxml(AbstractGameController gameController) {
            this.gameController = gameController;
        }

        @FXML
        AnchorPane anchorPane;

        @FXML
        public void initialize(){

            resize(anchorPane, gameController.getStage());

            /*
            Adding icon image
             */
            Image image = AssetHandler.getImage("/images/icon_tank_red.png", 128, 128);
            Image image2 = AssetHandler.getImage("/images/icon_tank_blue.png", 128, 128);

            ImageView img = new ImageView(image);
            img.setFitHeight(128);
            img.setFitWidth(128);
            ImageView img2 = new ImageView(image2);
            img2.setFitHeight(128);
            img2.setFitWidth(128);

            BorderPane bp = new BorderPane();
            bp.setRight(img2);
            bp.setLeft(img);

            GridPane gp = (GridPane) anchorPane.getChildren().get(0);
            gp.getChildren().add(bp);
            /*
            End adding icon image
             */
        }

        @FXML
        public void settings(){
            gameController.setActiveUiController(new SettingsUiController(gameController));
        }
    }
}
