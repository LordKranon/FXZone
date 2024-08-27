package fxzone.controller.menu;

import fxzone.config.Config;
import fxzone.controller.ingame.InGameEditorUiController;
import fxzone.controller.lobby.LobbyJoinedUiController;
import fxzone.controller.menu.JoinMenuUiController.JoinMenuUiControllerFxml;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.game.logic.Map;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class EditorMenuUiController extends AbstractUiController {

    private EditorMenuUiControllerFxml editorMenuUiControllerFxml;

    public EditorMenuUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditorMenuView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                EditorMenuUiControllerFxml editorMenuUiControllerFxml = new EditorMenuUiControllerFxml(gameController);
                this.editorMenuUiControllerFxml = editorMenuUiControllerFxml;
                return editorMenuUiControllerFxml;
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }

    class EditorMenuUiControllerFxml{
        private final AbstractGameController gameController;

        public EditorMenuUiControllerFxml(AbstractGameController gameController) {
            this.gameController = gameController;
        }

        @FXML
        AnchorPane anchorPane;

        @FXML
        TextField mapWidth;

        @FXML
        TextField mapHeight;

        @FXML
        public void initialize(){
            resize(anchorPane, gameController.getStage());
            mapWidth.setText("20");
            mapHeight.setText("20");
        }

        @FXML
        public void back(){
            gameController.setActiveUiController(new MainMenuUiController(gameController));
        }

        @FXML
        public void create(){
            int w, h;
            try{
                w = Integer.parseInt(mapWidth.getText());
                h = Integer.parseInt(mapHeight.getText());
            } catch (Exception e){
                System.err.println("[EDITOR-MENU-UI-CONTROLLER] ERROR Could not parse map width / height");
                return;
            }
            if(!(w>0 && h>0)){
                System.err.println("[EDITOR-MENU-UI-CONTROLLER] ERROR Bad map dimensions");
                return;
            }
            Map map = new Map(w, h, null);
            MapSerializable mapSerializable = new MapSerializable(map);
            ArrayList<Player> editorPlayerList = new ArrayList<>();
            gameController.setActiveUiController(new InGameEditorUiController(gameController, new GameSerializable(mapSerializable, editorPlayerList)));
        }
    }
}
