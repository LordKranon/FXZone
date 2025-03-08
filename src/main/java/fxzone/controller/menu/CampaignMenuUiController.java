package fxzone.controller.menu;

import fxzone.config.Config;
import fxzone.controller.ingame.InGameLocalUiController;
import fxzone.controller.ingame.InGameVsAiUiController;
import fxzone.controller.lobby.LobbyHostUiController;
import fxzone.controller.lobby.LobbyLocalUiController;
import fxzone.controller.menu.PlayMenuUiController.PlayMenuUiControllerFxml;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.save.Save;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class CampaignMenuUiController extends AbstractUiController {


    public CampaignMenuUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CampaignMenuView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                return new CampaignMenuUiControllerFxml(gameController);
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class CampaignMenuUiControllerFxml{
        private final AbstractGameController gameController;

        public CampaignMenuUiControllerFxml(AbstractGameController gameController) {
            this.gameController = gameController;
        }

        @FXML
        AnchorPane anchorPane;

        @FXML
        public void initialize(){
            resize(anchorPane, gameController.getStage());
        }

        @FXML
        public void mission1(){
            ArrayList<Player> playerList = new ArrayList<>();
            playerList.add(new Player("Alpha", Color.RED, 1));
            playerList.add(new Player("Bravo", Color.BLUE, 2));

            MapSerializable loadedMap = Save.loadMap("campaign_test");
            if(loadedMap == null){
                System.err.println("[CAMPAIGN-MENU-UI-CONTROLLER] [mission1] ERROR Could not load map on game start");
                return;
            }

            GameSerializable gameSerializable = new GameSerializable(loadedMap, playerList);

            gameController.setActiveUiController(new InGameVsAiUiController(gameController, gameSerializable));
        }

        @FXML
        private void back(){
            gameController.setActiveUiController(new PlayMenuUiController(gameController));
        }
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }
}
