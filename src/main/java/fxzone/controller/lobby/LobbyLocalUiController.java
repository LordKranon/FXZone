package fxzone.controller.lobby;

import fxzone.controller.ingame.InGameLocalUiController;
import fxzone.controller.menu.PlayMenuUiController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Game;
import fxzone.game.logic.Map;
import fxzone.game.logic.Player;
import fxzone.game.logic.Unit;
import fxzone.game.logic.Unit.UnitType;
import fxzone.game.logic.serializable.GameSerializable;
import java.util.ArrayList;
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
        Map map = new Map(15, 25, null);

        Unit u0 = new Unit(UnitType.INFANTRY, 1, 1, 0, null);
        map.addUnit(u0);
        Unit u1 = new Unit(UnitType.INFANTRY_RPG, 1, 2, 0, null);
        map.addUnit(u1);
        Unit u2 = new Unit(UnitType.CAR_HUMVEE, 1, 3, 0, null);
        map.addUnit(u2);
        Unit u3 = new Unit(UnitType.TRUCK_TRANSPORT, 1, 4, 0, null);
        map.addUnit(u3);
        Unit u4 = new Unit(UnitType.TANK_HUNTER, 1, 5, 0, null);
        map.addUnit(u4);
        Unit u5 = new Unit(UnitType.ARTILLERY, 1, 6, 0, null);
        map.addUnit(u5);
        Unit u6 = new Unit(UnitType.TANK_BATTLE, 1, 7, 0, null);
        map.addUnit(u6);
        Unit u7 = new Unit(UnitType.ARTILLERY_ROCKET, 1, 8, 0, null);
        map.addUnit(u7);

        Unit infantry1 = new Unit(UnitType.INFANTRY, 3, 3, 0, null);
        map.addUnit(infantry1);
        /*
        END Creating map.
        */

        u0.setOwnerId(1);
        u1.setOwnerId(1);
        u2.setOwnerId(1);
        u3.setOwnerId(1);
        u4.setOwnerId(1);
        u5.setOwnerId(1);
        u6.setOwnerId(1);
        u7.setOwnerId(1);
        infantry1.setOwnerId(2);

        /*
         * START Creating game.
         * */
        Game game = new Game(localPlayerList, map);
        GameSerializable gameSerializable = new GameSerializable(game);
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
