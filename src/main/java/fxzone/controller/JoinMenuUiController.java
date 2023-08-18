package fxzone.controller;

import fxzone.config.Config;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.net.client.Client;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class JoinMenuUiController extends AbstractUiController {

    private JoinMenuUiControllerFxml joinMenuUiControllerFxml;

    private Client client;

    private boolean tryingToJoin;

    public JoinMenuUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/JoinMenuView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                JoinMenuUiControllerFxml joinMenuUiControllerFxml = new JoinMenuUiControllerFxml(gameController);
                this.joinMenuUiControllerFxml = joinMenuUiControllerFxml;
                return joinMenuUiControllerFxml;
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {
        joinMenuUiControllerFxml.update();
    }

    class JoinMenuUiControllerFxml{
        private final AbstractGameController gameController;

        public JoinMenuUiControllerFxml(AbstractGameController gameController) {
            this.gameController = gameController;
        }

        @FXML
        AnchorPane anchorPane;

        @FXML
        TextField ipAddress;

        @FXML
        TextField username;

        @FXML
        TextField color;

        @FXML
        public void initialize(){
            resize(anchorPane, gameController.getStage());
        }
        @FXML
        public void join(){
            if(!tryingToJoin){
                handleJoinGame(ipAddress.getText(), username.getText(), color.getText());
            }
            //gameController.setActiveUiController(new LobbyJoinedUiController(gameController, client));
        }

        @FXML
        public void back(){
            if(!tryingToJoin) {
                gameController.setActiveUiController(new PlayMenuUiController(gameController));
            }
        }

        private void update(){
            if(tryingToJoin){
                if(!client.isRunning()){
                    if(client.isSuccessfullyConnected()){
                        gameController.setActiveUiController(new LobbyJoinedUiController(gameController, client));
                    } else {
                        tryingToJoin = false;
                    }
                }
            }
        }
    }

    private void handleJoinGame(String ip, String name, String colorRGBCode){
        System.out.println("[JOIN-MENU-UI-CONTROLLER] IP:");
        System.out.println(ip);
        client = new Client();
        client.connectToServer(ip, Config.getInt("SERVER_PORT"));
        tryingToJoin = true;
    }
}
