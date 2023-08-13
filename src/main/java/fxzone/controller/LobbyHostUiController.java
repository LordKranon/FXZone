package fxzone.controller;

import fxzone.controller.PlayMenuUiController.PlayMenuUiControllerFxml;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.net.server.Server;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;

public class LobbyHostUiController extends AbstractUiController {

    public LobbyHostUiController(AbstractGameController gameController, Server server) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LobbyHostView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                return new LobbyHostUiControllerFxml(gameController);
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }

    class LobbyHostUiControllerFxml{
        private final AbstractGameController gameController;

        public LobbyHostUiControllerFxml(AbstractGameController gameController) {
            this.gameController = gameController;
        }

        @FXML
        AnchorPane anchorPane;

        @FXML
        public void initialize(){
            resize(anchorPane, gameController.getStage());
        }

        @FXML
        public void start(){
            gameController.setActiveUiController(new InGameUiController(gameController));
        }

        @FXML
        private void quit(){
            gameController.setActiveUiController(new PlayMenuUiController(gameController));
        }
    }
}
