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
        Unit tank1 = new Unit(UnitType.TANK_BATTLE, 1, 1, 0, null);
        map.addUnit(tank1);

        Unit infantry1 = new Unit(UnitType.INFANTRY, 2, 2, 0, null);
        map.addUnit(infantry1);
        /*
        END Creating map.
        */

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
