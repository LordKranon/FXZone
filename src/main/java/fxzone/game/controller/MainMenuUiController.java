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
             * TODO
             * WARNING! Bypassing asset handler!
             */

            /*
            Adding icon image
             */


            Image image = new Image("/images/icon_tank_red.png", 128, 128, true, false);

            ImageView img = new ImageView(image);
            img.setFitHeight(128);
            img.setFitWidth(128);

            GridPane gp = (GridPane) anchorPane.getChildren().get(0);
            gp.getChildren().add(img);


            /*
            End adding icon image
             */
        }
    }
}
