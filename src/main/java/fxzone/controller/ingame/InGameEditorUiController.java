package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyTile;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Tile;
import fxzone.game.logic.TileType;
import fxzone.game.logic.TurnState;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.game.logic.serializable.TileSerializable;
import fxzone.save.Save;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class InGameEditorUiController extends InGameUiController{

    /*
    DEBUG
     */
    private static final boolean verbose = true;

    private TileType editorTileTypePlaced;

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

        this.editorTileTypePlaced = TileType.PLAINS;

        ImageView tileIcon = new ImageView();
        tileIcon.setImage(AssetHandler.getImageTile(new KeyTile(editorTileTypePlaced)));
        tileIcon.setFitWidth(75);
        tileIcon.setFitHeight(75);
        tileIcon.setTranslateX(50);
        tileIcon.setTranslateY(600);
        tileIcon.setVisible(true);
        tileIcon.setViewOrder(ViewOrder.UI_BUTTON);
        escapeMenu.getChildren().add(tileIcon);

        Button tileTypeButton = new Button(""+editorTileTypePlaced);
        tileTypeButton.setPrefWidth(400);
        tileTypeButton.setTranslateX(150);
        tileTypeButton.setTranslateY(600);
        tileTypeButton.setViewOrder(ViewOrder.UI_BUTTON);
        tileTypeButton.setVisible(true);
        tileTypeButton.setOnMouseClicked(mouseEvent -> {
            if(editorTileTypePlaced == TileType.PLAINS){
                editorTileTypePlaced = TileType.WATER;
            } else {
                editorTileTypePlaced = TileType.PLAINS;
            }
            tileTypeButton.setText(""+editorTileTypePlaced);
            tileIcon.setImage(AssetHandler.getImageTile(new KeyTile(editorTileTypePlaced)));
        });
        escapeMenu.getChildren().add(tileTypeButton);


        Button saveMapButton = new Button("Save Map");
        saveMapButton.setPrefWidth(400);
        saveMapButton.setTranslateX(100);
        saveMapButton.setTranslateY(700);
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
            Tile tile = new Tile(x, y, editorTileTypePlaced);
            TileSerializable tileSerializable = new TileSerializable(tile);
            map.switchTile(tileSerializable);
        }
    }
}
