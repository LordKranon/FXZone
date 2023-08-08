package fxzone.game.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.game.controller.MainMenuUiController.MainMenuUiControllerFxml;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;

public class SettingsUiController extends AbstractUiController {

    public SettingsUiController(AbstractGameController gameController){
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SettingsView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                return new SettingsUiControllerFxml(gameController);
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }

    class SettingsUiControllerFxml {

        private final AbstractGameController gameController;

        public SettingsUiControllerFxml(AbstractGameController gameController){
            this.gameController = gameController;
        }

        @FXML
        private AnchorPane anchorPane;

        @FXML
        private void initialize(){
            resize(anchorPane, gameController.getStage());
        }

        @FXML
        private void back(){
            gameController.setActiveUiController(new MainMenuUiController(gameController));
        }
    }
}
