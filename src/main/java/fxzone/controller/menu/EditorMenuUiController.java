package fxzone.controller.menu;

import fxzone.config.Config;
import fxzone.controller.ingame.InGameEditorUiController;
import fxzone.controller.ingame.InGameLocalUiController;
import fxzone.controller.lobby.LobbyJoinedUiController;
import fxzone.controller.menu.JoinMenuUiController.JoinMenuUiControllerFxml;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.utils.FxUtils;
import fxzone.game.logic.Map;
import fxzone.game.logic.Map.Biome;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.save.Save;
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
        TextField mapBiome;

        @FXML
        TextField mapName;

        @FXML
        public void initialize(){
            resize(anchorPane, gameController.getStage());
            mapWidth.setText("20");
            mapHeight.setText("20");
            mapBiome.setText("SAND");
        }

        @FXML
        public void back(){
            gameController.setActiveUiController(new MainMenuUiController(gameController));
        }

        @FXML
        public void create(){
            int w, h;
            Biome biome;
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
            try{
                biome = Biome.valueOf(mapBiome.getText());
            } catch (Exception e){
                System.err.println("[EDITOR-MENU-UI-CONTROLLER] ERROR Could not parse map biome");
                return;
            }
            Map map = new Map(w, h, null);
            map.setBiome(biome);
            MapSerializable mapSerializable = new MapSerializable(map, 1);
            ArrayList<Player> editorPlayerList = new ArrayList<>();
            editorPlayerList.add(new Player("Alpha", FxUtils.toColor("ff0000"), 1));
            editorPlayerList.add(new Player("Bravo", FxUtils.toColor("0000ff"), 2));
            editorPlayerList.add(new Player("Charlie", FxUtils.toColor("00ff00"), 3));
            editorPlayerList.add(new Player("Delta", FxUtils.toColor("ffff00"), 4));
            gameController.setActiveUiController(new InGameEditorUiController(gameController, new GameSerializable(mapSerializable, editorPlayerList)));
        }

        @FXML
        public void load(){
            MapSerializable loadedMap = Save.loadMap(mapName.getText());
            if(loadedMap == null){
                System.err.println("[EDITOR-MENU-UI-CONTROLLER] [load] ERROR Could not load map");
                return;
            }

            ArrayList<Player> editorPlayerList = new ArrayList<>();
            editorPlayerList.add(new Player("Alpha", FxUtils.toColor("ff0000"), 1));
            editorPlayerList.add(new Player("Bravo", FxUtils.toColor("0000ff"), 2));
            editorPlayerList.add(new Player("Charlie", FxUtils.toColor("00ff00"), 3));
            editorPlayerList.add(new Player("Delta", FxUtils.toColor("ffff00"), 4));
            GameSerializable gameSerializable = new GameSerializable(loadedMap, editorPlayerList);
            gameController.setActiveUiController(new InGameEditorUiController(gameController, gameSerializable));
        }
    }
}
