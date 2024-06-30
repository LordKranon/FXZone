package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyBuilding;
import fxzone.engine.handler.KeyTile;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.Player;
import fxzone.game.logic.Tile;
import fxzone.game.logic.TileType;
import fxzone.game.logic.TurnState;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.game.logic.serializable.TileSerializable;
import fxzone.save.Save;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class InGameEditorUiController extends InGameUiController{

    /*
    DEBUG
     */
    private static final boolean verbose = true;

    private TileType editorTileTypePlaced;

    private BuildingType editorBuildingTypePlaced;

    private UnitType editorUnitTypePlaced;

    private int editorOwnerIdPlaced;

    public InGameEditorUiController(AbstractGameController gameController, GameSerializable initialGame) {
        super(gameController, initialGame, 1);
    }

    @Override
    protected void initializeGameSpecifics(){
        Player editorPlayer = new Player("Editor", Color.CYAN, 404);

        this.thisPlayer = editorPlayer;

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
        this.editorBuildingTypePlaced = BuildingType.CITY;
        this.editorUnitTypePlaced = UnitType.INFANTRY;
        this.editorOwnerIdPlaced = 0;

        ImageView tileIcon = initializeEditorMenuIcon(50, 200);
        tileIcon.setImage(AssetHandler.getImageTile(new KeyTile(editorTileTypePlaced)));

        Button tileTypeButton = initializeEditorMenuButton(150, 200);
        tileTypeButton.setText(""+editorTileTypePlaced);
        tileTypeButton.setOnMouseClicked(mouseEvent -> {
            if(editorTileTypePlaced == TileType.PLAINS){
                editorTileTypePlaced = TileType.WATER;
            } else {
                editorTileTypePlaced = TileType.PLAINS;
            }
            tileTypeButton.setText(""+editorTileTypePlaced);
            tileIcon.setImage(AssetHandler.getImageTile(new KeyTile(editorTileTypePlaced)));
        });



        ImageView ownerIdIcon = initializeEditorMenuIcon(50, 400);
        ownerIdIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.INFANTRY, 1, null)));

        Button ownerIdButton = initializeEditorMenuButton(150, 400);
        ownerIdButton.setText("NONE");
        ownerIdButton.setOnMouseClicked(mouseEvent -> {
            switch (editorOwnerIdPlaced){
                case 0:
                case 1:
                case 2:
                case 3:
                    editorOwnerIdPlaced++; break;
                case 4:
                    editorOwnerIdPlaced = 0; break;
                default:
                    System.err.println("[EDITOR] Editor menu error");
                    editorOwnerIdPlaced = 0; break;
            }
            if(editorOwnerIdPlaced != 0){
                ownerIdButton.setText(game.getPlayer(editorOwnerIdPlaced).getName());
                ownerIdIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.INFANTRY, 1, FxUtils.toAwtColor(game.getPlayer(editorOwnerIdPlaced).getColor()))));
            }else {
                ownerIdButton.setText("NONE");
                ownerIdIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.INFANTRY, 1, null)));
            }

        });



        ImageView buildingIcon = initializeEditorMenuIcon(50, 500);
        buildingIcon.setImage(AssetHandler.getImageBuilding(new KeyBuilding(editorBuildingTypePlaced, null)));

        Button buildingTypeButton = initializeEditorMenuButton(150, 500);
        buildingTypeButton.setText(""+editorBuildingTypePlaced);
        buildingTypeButton.setOnMouseClicked(mouseEvent -> {
            switch (editorBuildingTypePlaced){
                case CITY: editorBuildingTypePlaced = BuildingType.FACTORY; break;
                case FACTORY: editorBuildingTypePlaced = BuildingType.AIRPORT; break;
                case AIRPORT: editorBuildingTypePlaced = BuildingType.PORT; break;
                case PORT: editorBuildingTypePlaced = BuildingType.CITY; break;
                default:
                    System.err.println("[EDITOR] Editor menu error");
                    editorBuildingTypePlaced = BuildingType.CITY; break;
            }
            buildingTypeButton.setText(""+editorBuildingTypePlaced);
            buildingIcon.setImage(AssetHandler.getImageBuilding(new KeyBuilding(editorBuildingTypePlaced, null)));
        });




        ImageView unitIcon = initializeEditorMenuIcon(40, 600);
        unitIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(editorUnitTypePlaced, 0, null)));

        Button unitTypeButton = initializeEditorMenuButton(150, 600);
        unitTypeButton.setText(""+editorUnitTypePlaced);
        unitTypeButton.setOnMouseClicked(mouseEvent -> {
            switch (editorUnitTypePlaced){
                case INFANTRY: editorUnitTypePlaced = UnitType.INFANTRY_RPG; break;
                case INFANTRY_RPG: editorUnitTypePlaced = UnitType.CAR_HUMVEE; break;
                case CAR_HUMVEE: editorUnitTypePlaced = UnitType.TRUCK_TRANSPORT; break;
                case TRUCK_TRANSPORT: editorUnitTypePlaced = UnitType.TANK_HUNTER; break;
                case TANK_HUNTER: editorUnitTypePlaced = UnitType.ARTILLERY; break;
                case ARTILLERY: editorUnitTypePlaced = UnitType.TANK_BATTLE; break;
                case TANK_BATTLE: editorUnitTypePlaced = UnitType.ARTILLERY_ROCKET; break;
                case ARTILLERY_ROCKET: editorUnitTypePlaced = UnitType.INFANTRY; break;
                default:
                    System.err.println("[EDITOR] Editor menu error");
                    editorUnitTypePlaced = UnitType.INFANTRY; break;
            }
            unitTypeButton.setText(""+editorUnitTypePlaced);
            unitIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(editorUnitTypePlaced, 0, null)));
        });


        Button saveMapButton = initializeEditorMenuButton(100, 800);
        saveMapButton.setText("Save Map");
        saveMapButton.setOnMouseClicked(mouseEvent -> {
            saveMap();
        });
    }

    @Override
    void initializeGame(GameSerializable initialGame){
        super.initializeGame(initialGame);
        game.addPlayer(new Player("Alpha", FxUtils.toColor("ff0000"), 1));
        game.addPlayer(new Player("Bravo", FxUtils.toColor("0000ff"), 2));
        game.addPlayer(new Player("Charlie", FxUtils.toColor("00ff00"), 3));
        game.addPlayer(new Player("Delta", FxUtils.toColor("ffff00"), 4));
    }

    private ImageView initializeEditorMenuIcon(int x, int y){
        ImageView icon = new ImageView();
        icon.setFitWidth(75);
        icon.setFitHeight(75);
        icon.setTranslateX(x);
        icon.setTranslateY(y);
        icon.setVisible(true);
        icon.setViewOrder(ViewOrder.UI_BUTTON);
        escapeMenu.getChildren().add(icon);
        return icon;
    }
    private Button initializeEditorMenuButton(int x, int y){
        Button button = new Button();
        button.setPrefWidth(400);
        button.setTranslateX(x);
        button.setTranslateY(y);
        button.setViewOrder(ViewOrder.UI_BUTTON);
        button.setVisible(true);
        escapeMenu.getChildren().add(button);
        return button;
    }

    private void saveMap(){
        if(verbose) System.out.println("[EDITOR] [saveMap] saving...");
        Save.saveMap(new MapSerializable(map));
        if(verbose) System.out.println("[EDITOR] [saveMap] saved");
    }

    @Override
    void tileClicked(int x, int y){
        if(turnState == TurnState.EDITOR){
            if (verbose) System.out.println("[EDITOR] [tileClicked] during editor");
            Tile tile = new Tile(x, y, editorTileTypePlaced);
            TileSerializable tileSerializable = new TileSerializable(tile);
            map.switchTile(tileSerializable);
        }
    }
}
