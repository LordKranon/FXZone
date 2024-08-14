package fxzone.controller.lobby;

import fxzone.controller.ingame.InGameLocalUiController;
import fxzone.controller.menu.PlayMenuUiController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Building;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Game;
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
import javafx.scene.paint.Color;

public class LobbyLocalUiController extends LobbyUiController{

    private final ArrayList<Player> localPlayerList;

    public LobbyLocalUiController(AbstractGameController gameController) {
        super(gameController);
        this.localPlayerList = new ArrayList<>();
        initializePlayerListOfLocalLobby();
    }

    private void initializePlayerListOfLocalLobby(){
        localPlayerList.add(new Player("Alpha", Color.web("#ff0000"), 1));
        localPlayerList.add(new Player("Bravo", Color.web("#0000ff"), 2));
        updatePlayerList(localPlayerList);
    }

    @Override
    protected void startOuter(AbstractGameController gameController, String mapName) {

        MapSerializable loadedMap = Save.loadMap(mapName);
        if(loadedMap == null){
            System.err.println("[LOBBY-LOCAL-UI-CONTROLLER] [start] ERROR Could not load map on game start");
            return;
        }

        GameSerializable gameSerializable = new GameSerializable(loadedMap, localPlayerList);

        gameController.setActiveUiController(new InGameLocalUiController(gameController, gameSerializable));
    }

    @Override
    protected void quitOuter(AbstractGameController gameController) {
        gameController.setActiveUiController(new PlayMenuUiController(gameController));
    }

    @Override
    protected void sendTestMessageOuter() {

    }
}
