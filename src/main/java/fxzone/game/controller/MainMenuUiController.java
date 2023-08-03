package fxzone.game.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;

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
    }
}
