package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;

public abstract class LobbyUiController extends AbstractUiController {

    protected AnchorPane anchorPane;

    public LobbyUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LobbyView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                return new LobbyUiController.LobbyUiControllerFxml(gameController);
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }

    class LobbyUiControllerFxml{
        private final AbstractGameController gameController;

        public LobbyUiControllerFxml(AbstractGameController gameController) {
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
        public void start(){
            startOuter(gameController);
        }

        @FXML
        private void quit(){
            gameController.setActiveUiController(new PlayMenuUiController(gameController));
        }

        @FXML
        private void sendTestMessage(){
            sendTestMessageOuter();
        }
    }

    protected void initializeOuter(AnchorPane anchorPane){
        this.anchorPane = anchorPane;
    }

    protected abstract void startOuter(AbstractGameController gameController);

    protected abstract void sendTestMessageOuter();
}
