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
    protected void startOuter(AbstractGameController gameController) {

        /*
        START Creating map.
        */
        Map map = new Map(Save.loadMap());

        int i = 1;
        Set<UnitType> unitTypes = EnumSet.allOf(UnitType.class);
        for(UnitType unitType : unitTypes){
            Unit u = new Unit(unitType, 1, i);
            map.getUnits().add(u);
            u.setOwnerId(1);

            Unit v = new Unit(unitType, 4, i++);
            map.getUnits().add(v);
            v.setOwnerId(2);
        }

        Building b = new Building(BuildingType.CITY, 0, 0);
        map.getBuildings().add(b);
        b.setOwnerId(1);

        MapSerializable mapSerializable = new MapSerializable(map);
        /*
        END Creating map.
        */

        /*
         * START Creating game.
         * */
        GameSerializable gameSerializable = new GameSerializable(mapSerializable, localPlayerList);
        /*
         * END Creating game.
         * */

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
