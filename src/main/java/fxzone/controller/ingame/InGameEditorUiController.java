package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.game.logic.Tile;
import fxzone.game.logic.TileType;
import fxzone.game.logic.TurnState;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.TileSerializable;

public class InGameEditorUiController extends InGameUiController{

    public InGameEditorUiController(AbstractGameController gameController, GameSerializable initialGame) {
        super(gameController, initialGame, 1);
    }

    @Override
    protected void initializeGameSpecifics(){
        this.thisPlayer = game.getPlayers().get(0);
        turnState = TurnState.EDITOR;
        super.initializeGameSpecifics();
    }

    @Override
    protected void onPlayerEndTurn(){
        System.err.println("[EDITOR] [onPlayerEndTurn] No such action is possible in Editor");
    }

    @Override
    void tileClicked(int x, int y){
        if(game.itsMyTurn(thisPlayer) && turnState == TurnState.EDITOR){
            if (verbose) System.out.println("[EDITOR] [tileClicked] during editor");
            Tile tile = new Tile(x, y, TileType.PLAINS);
            TileSerializable tileSerializable = new TileSerializable(tile);
            map.switchTile(tileSerializable);
        }
    }
}
