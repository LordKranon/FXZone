package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;

public class PlayMenuUiController extends AbstractUiController {

    public PlayMenuUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PlayMenuView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                return new PlayMenuUiControllerFxml(gameController);
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }

    class PlayMenuUiControllerFxml{
        private final AbstractGameController gameController;

        public PlayMenuUiControllerFxml(AbstractGameController gameController) {
            this.gameController = gameController;
        }

        @FXML
        AnchorPane anchorPane;

        @FXML
        public void initialize(){
            resize(anchorPane, gameController.getStage());
        }

        @FXML
        public void host(){
            gameController.setActiveUiController(new LobbyHostUiController(gameController));
        }

        @FXML
        public void join(){
            gameController.setActiveUiController(new LobbyJoinedUiController(gameController));
        }

        @FXML
        private void back(){
            gameController.setActiveUiController(new MainMenuUiController(gameController));
        }
    }
}
