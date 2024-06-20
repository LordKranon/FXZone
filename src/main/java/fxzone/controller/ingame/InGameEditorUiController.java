package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Tile;
import fxzone.game.logic.TileType;
import fxzone.game.logic.TurnState;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.game.logic.serializable.TileSerializable;
import fxzone.save.Save;
import javafx.scene.control.Button;

public class InGameEditorUiController extends InGameUiController{

    /*
    DEBUG
     */
    private static final boolean verbose = true;

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
    void createFXSceneUI(){
        super.createFXSceneUI();

        Button saveMapButton = new Button("Save Map");
        saveMapButton.setPrefWidth(400);
        saveMapButton.setTranslateX(100);
        saveMapButton.setTranslateY(600);
        saveMapButton.setViewOrder(ViewOrder.UI_BUTTON);
        saveMapButton.setVisible(true);
        saveMapButton.setOnMouseClicked(mouseEvent -> {
            saveMap();
        });
        escapeMenu.getChildren().add(saveMapButton);
    }
    private void saveMap(){
        if(verbose) System.out.println("[EDITOR] [saveMap] saving...");
        Save.saveMap(new MapSerializable(map));
        if(verbose) System.out.println("[EDITOR] [saveMap] saved");
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
