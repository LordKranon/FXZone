package fxzone.controller.menu;

import fxzone.config.Config;
import fxzone.controller.ingame.InGameEditorUiController;
import fxzone.controller.ingame.InGameLocalUiController;
import fxzone.controller.ingame.InGameVsAiUiController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.game.logic.Game.GameMode;
import fxzone.game.logic.Map;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.save.Save;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class MainMenuUiController extends AbstractUiController {

    public MainMenuUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
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

    @Override
    public void update(AbstractGameController gameController, double delta) {

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
            Image image = AssetHandler.getImage("/images/icon_tank_red.png");
            Image image2 = AssetHandler.getImage("/images/icon_tank_blue.png");

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
        public void play(){
            gameController.setActiveUiController(new PlayMenuUiController(gameController));
        }

        @FXML
        public void test(){

            ArrayList<Player> playerList = new ArrayList<>();
            playerList.add(new Player("Alpha", Color.RED, 1, null));
            playerList.add(new Player("Bravo", Color.BLUE, 2, null));

            MapSerializable loadedMap = Save.loadMap(Config.getString("LAST_USED_MAP_LOCAL"));
            if(loadedMap == null){
                System.err.println("[MAIN-MENU-UI-CONTROLLER] [test] ERROR Could not load map on game start");
                return;
            }

            GameSerializable gameSerializable = new GameSerializable(loadedMap, playerList, GameMode.NORMAL);

            gameController.setActiveUiController(new InGameVsAiUiController(gameController, gameSerializable, 0));
        }

        @FXML
        public void settings(){
            gameController.setActiveUiController(new SettingsUiController(gameController));
        }

        @FXML
        public void editor(){
            gameController.setActiveUiController(new EditorMenuUiController(gameController));
        }

        @FXML
        public void quit(){
            try {
                gameController.getApplication().stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
