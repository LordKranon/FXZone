package fxzone.controller.lobby;

import fxzone.config.Config;
import fxzone.controller.ingame.InGameLocalUiController;
import fxzone.controller.ingame.InGameVsAiUiController;
import fxzone.controller.menu.PlayMenuUiController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Building;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Game;
import fxzone.game.logic.Game.GameMode;
import fxzone.game.logic.Map;
import fxzone.game.logic.Player;
import fxzone.game.logic.Unit;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.save.Save;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class LobbyLocalUiController extends LobbyUiController{

    private final ArrayList<Player> localPlayerList;

    private int runningPlayerIdNumber = 1;

    private TextField textFieldPlayerName, textFieldPlayerColor;

    public LobbyLocalUiController(AbstractGameController gameController) {
        super(gameController);
        this.localPlayerList = new ArrayList<>();
        initializePlayerListOfLocalLobby();
    }

    private void initializePlayerListOfLocalLobby(){

        /*
        Add 2 players for debug
        TODO Remove these 2 lines
         */
        //localPlayerList.add(new Player("Alpha", Color.web("#ff0000"), runningPlayerIdNumber++));
        //localPlayerList.add(new Player("Bravo", Color.web("#0000ff"), runningPlayerIdNumber++));

        updatePlayerList(localPlayerList);
    }

    @Override
    protected void initializeOuter(AnchorPane anchorPane){
        super.initializeOuter(anchorPane);
        textFieldMapName.setText(Config.getString("LAST_USED_MAP_LOCAL"));

        textFieldPlayerName = (TextField) vBoxButtons.getChildren().get(3);
        textFieldPlayerName.setVisible(true);
        textFieldPlayerName.setText(Config.getString("LAST_USED_PLAYER_NAME_LOCAL_1"));

        textFieldPlayerColor = (TextField) vBoxButtons.getChildren().get(2);
        textFieldPlayerColor.setVisible(true);
        textFieldPlayerColor.setText(Config.getString("LAST_USED_PLAYER_COLOR_LOCAL_1"));

        vBoxButtons.getChildren().get(4).setVisible(true);
    }

    @Override
    protected void startOuter(AbstractGameController gameController, String mapName) {

        if(localPlayerList.isEmpty()){
            System.err.println("[LOBBY-LOCAL-UI-CONTROLLER] [start] ERROR Player list empty");
            return;
        }

        MapSerializable loadedMap = Save.loadMap(mapName);
        if(loadedMap == null){
            System.err.println("[LOBBY-LOCAL-UI-CONTROLLER] [start] ERROR Could not load map on game start");
            return;
        }

        Config.set("LAST_USED_MAP_LOCAL", mapName);

        GameSerializable gameSerializable = new GameSerializable(loadedMap, localPlayerList, GameMode.NORMAL);

        gameController.setActiveUiController(new InGameLocalUiController(gameController, gameSerializable));
    }

    @Override
    protected void addPlayerOuter(AbstractGameController gameController, String playerName, String playerColor){
        javafx.scene.paint.Color color;
        try {
            color = Color.web(playerColor);
        } catch (Exception e){
            System.err.println("[LOBBY-LOCAL-UI-CONTROLLER] [addPlayer] ERROR Invalid player color");
            return;
        }
        if(playerName == null || playerName.equals("")){
            System.err.println("[LOBBY-LOCAL-UI-CONTROLLER] [addPlayer] ERROR Invalid player name");
            return;
        }

        if(runningPlayerIdNumber <= 4){
            Config.set("LAST_USED_PLAYER_NAME_LOCAL_"+runningPlayerIdNumber, playerName);
            Config.set("LAST_USED_PLAYER_COLOR_LOCAL_"+runningPlayerIdNumber, playerColor);
        }

        localPlayerList.add(new Player(playerName, color, runningPlayerIdNumber++));

        try{
            textFieldPlayerName.setText(Config.getString("LAST_USED_PLAYER_NAME_LOCAL_"+runningPlayerIdNumber));
            textFieldPlayerColor.setText(Config.getString("LAST_USED_PLAYER_COLOR_LOCAL_"+runningPlayerIdNumber));
        } catch (IllegalArgumentException e){
            System.err.println("[LOBBY-LOCAL-UI-CONTROLLER] [addPlayer] ERROR Can't fetch next LAST_USED_PLAYER_NAME/COLOR");
            textFieldPlayerName.setText("");
            textFieldPlayerColor.setText("");
        }

        updatePlayerList(localPlayerList);
    }

    @Override
    protected void quitOuter(AbstractGameController gameController) {
        gameController.setActiveUiController(new PlayMenuUiController(gameController));
    }

    @Override
    protected void sendTestMessageOuter() {

    }
}
