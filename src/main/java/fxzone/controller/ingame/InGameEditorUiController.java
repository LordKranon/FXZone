package fxzone.controller.ingame;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyBuilding;
import fxzone.engine.handler.KeyTile;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Building;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.Player;
import fxzone.game.logic.Tile;
import fxzone.game.logic.Tile.TileType;
import fxzone.game.logic.Unit;
import fxzone.game.logic.serializable.BuildingSerializable;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.game.logic.serializable.TileSerializable;
import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.save.Save;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InGameEditorUiController extends InGameUiController{

    /*
    DEBUG
     */
    private static final boolean verbose = true;

    private TileType editorTileTypePlaced;

    private BuildingType editorBuildingTypePlaced;

    private UnitType editorUnitTypePlaced;

    private int editorOwnerIdPlaced;

    private Button selectedPlacingButton;

    private enum EditorPlacingMode {
        TILE, BUILDING, UNIT
    }
    private EditorPlacingMode placingMode;

    private Button tileTypeButton, buildingTypeButton, unitTypeButton;

    private TextField nameMapTextField;

    public InGameEditorUiController(AbstractGameController gameController, GameSerializable initialGame) {
        super(gameController, initialGame, 1);
    }

    @Override
    protected void initializeGameSpecifics(){
        Player editorPlayer = new Player("Editor", Color.CYAN, 404);

        this.thisPlayer = editorPlayer;

        this.thisPlayerFowVision = map.getVisionOfGod();

        super.initializeGameSpecifics();
    }

    @Override
    protected void beginTurn(){
        if(verbose) System.out.println("[EDITOR] [beginTurn] Begin editor mode");
        turnState = TurnState.EDITOR;
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
        tileIcon.setImage(AssetHandler.getImageTile(new KeyTile(editorTileTypePlaced, false)));

        this.tileTypeButton = initializeEditorMenuButton(150, 200);
        tileTypeButton.setText(""+editorTileTypePlaced);

        selectPlacingMode(EditorPlacingMode.TILE);

        tileTypeButton.setOnMouseClicked(mouseEvent -> {
            if(tileTypeButton == selectedPlacingButton) {

                switch (editorTileTypePlaced){
                    case PLAINS:
                        editorTileTypePlaced = TileType.BEACH;
                        break;
                    case BEACH:
                        editorTileTypePlaced = TileType.WATER;
                        break;
                    case WATER:
                        editorTileTypePlaced = TileType.FOREST;
                        break;
                    case FOREST:
                    default:
                        editorTileTypePlaced = TileType.PLAINS;
                        break;
                }
                tileTypeButton.setText("" + editorTileTypePlaced);
                tileIcon.setImage(AssetHandler.getImageTile(new KeyTile(editorTileTypePlaced, false)));
            } else {
                selectPlacingMode(EditorPlacingMode.TILE);
            }
        });



        ImageView ownerIdIcon = initializeEditorMenuIcon(50, 400);
        ownerIdIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.INFANTRY, 1, null)));

        Button ownerIdButton = initializeEditorMenuButton(150, 400);
        ownerIdButton.setText("NONE");
        ownerIdButton.setOnMouseClicked(mouseEvent -> {
            switch (editorOwnerIdPlaced) {
                case 0:
                case 1:
                case 2:
                case 3:
                    editorOwnerIdPlaced++;
                    break;
                case 4:
                    editorOwnerIdPlaced = 0;
                    break;
                default:
                    System.err.println("[EDITOR] Editor menu error");
                    editorOwnerIdPlaced = 0;
                    break;
            }
            if (editorOwnerIdPlaced != 0) {
                ownerIdButton.setText(game.getPlayer(editorOwnerIdPlaced).getName());
                ownerIdIcon.setImage(AssetHandler.getImageUnit(
                    new KeyUnit(UnitType.INFANTRY, 1, FxUtils.toAwtColor(game.getPlayer(editorOwnerIdPlaced).getColor()))));
            } else {
                ownerIdButton.setText("NONE");
                ownerIdIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.INFANTRY, 1, null)));
            }

        });



        ImageView buildingIcon = initializeEditorMenuIcon(50, 500);
        buildingIcon.setImage(AssetHandler.getImageBuilding(new KeyBuilding(editorBuildingTypePlaced, null)));

        this.buildingTypeButton = initializeEditorMenuButton(150, 500);
        buildingTypeButton.setText(""+editorBuildingTypePlaced);
        buildingTypeButton.setOnMouseClicked(mouseEvent -> {
            if(buildingTypeButton == selectedPlacingButton) {

                switch (editorBuildingTypePlaced) {
                    case CITY:
                        editorBuildingTypePlaced = BuildingType.FACTORY;
                        break;
                    case FACTORY:
                        editorBuildingTypePlaced = BuildingType.AIRPORT;
                        break;
                    case AIRPORT:
                        editorBuildingTypePlaced = BuildingType.PORT;
                        break;
                    case PORT:
                        editorBuildingTypePlaced = BuildingType.CITY;
                        break;
                    default:
                        System.err.println("[EDITOR] Editor menu error");
                        editorBuildingTypePlaced = BuildingType.CITY;
                        break;
                }
                buildingTypeButton.setText("" + editorBuildingTypePlaced);
                buildingIcon.setImage(AssetHandler.getImageBuilding(new KeyBuilding(editorBuildingTypePlaced, null)));
            }
            else {
                selectPlacingMode(EditorPlacingMode.BUILDING);
            }
        });




        ImageView unitIcon = initializeEditorMenuIcon(40, 600);
        unitIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(editorUnitTypePlaced, 0, null)));

        this.unitTypeButton = initializeEditorMenuButton(150, 600);
        unitTypeButton.setText(""+editorUnitTypePlaced);
        unitTypeButton.setOnMouseClicked(mouseEvent -> {
            if(unitTypeButton == selectedPlacingButton) {
                switch (editorUnitTypePlaced) {
                    case INFANTRY:
                        editorUnitTypePlaced = UnitType.INFANTRY_RPG;
                        break;
                    case INFANTRY_RPG:
                        editorUnitTypePlaced = UnitType.CAR_HUMVEE;
                        break;
                    case CAR_HUMVEE:
                        editorUnitTypePlaced = UnitType.TRUCK_TRANSPORT;
                        break;
                    case TRUCK_TRANSPORT:
                        editorUnitTypePlaced = UnitType.TANK_HUNTER;
                        break;
                    case TANK_HUNTER:
                        editorUnitTypePlaced = UnitType.ARTILLERY;
                        break;
                    case ARTILLERY:
                        editorUnitTypePlaced = UnitType.TANK_BATTLE;
                        break;
                    case TANK_BATTLE:
                        editorUnitTypePlaced = UnitType.ARTILLERY_ROCKET;
                        break;
                    case ARTILLERY_ROCKET:
                        editorUnitTypePlaced = UnitType.INFANTRY;
                        break;
                    default:
                        printErr();
                        editorUnitTypePlaced = UnitType.INFANTRY;
                        break;
                }
                unitTypeButton.setText("" + editorUnitTypePlaced);
                unitIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(editorUnitTypePlaced, 0, null)));
            } else {
                selectPlacingMode(EditorPlacingMode.UNIT);
            }
        });


        Button saveMapButton = initializeEditorMenuButton(100, 800);
        saveMapButton.setText("Save Map");
        saveMapButton.setOnMouseClicked(mouseEvent -> {
            saveMap();
        });

        nameMapTextField = new TextField();
        nameMapTextField.setTranslateX(100);
        nameMapTextField.setTranslateY(700);
        nameMapTextField.setPrefWidth(400);
        nameMapTextField.setViewOrder(ViewOrder.UI_BUTTON);
        nameMapTextField.setVisible(true);
        nameMapTextField.setFont(new Font(30));
        nameMapTextField.setPromptText("Name map");
        escapeMenu.getChildren().add(nameMapTextField);
    }

    private void selectPlacingMode(EditorPlacingMode newPlacingMode){
        Button button;
        switch (newPlacingMode){
            case TILE: button = tileTypeButton; break;
            case BUILDING: button = buildingTypeButton; break;
            case UNIT: button = unitTypeButton; break;
            default:
                printErr();
                button = tileTypeButton; break;
        }
        if(selectedPlacingButton != button){
            if(selectedPlacingButton != null){
                selectedPlacingButton.setStyle(null);
            }
            button.setStyle("-fx-border-width: 5; -fx-border-color: eeeeee;");
            selectedPlacingButton = button;
        } else {
            printErr();
        }
        if(this.placingMode != newPlacingMode){
            this.placingMode = newPlacingMode;
        } else {
            printErr();
        }
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
        String mapName = nameMapTextField.getText();
        if(mapName.equals("")){
            System.err.println("[EDITOR] [saveMap] ERROR Map has no name");
            return;
        }
        if(verbose) System.out.println("[EDITOR] [saveMap] saving...");
        Save.saveMap(new MapSerializable(map), mapName);
        if(verbose) System.out.println("[EDITOR] [saveMap] saved");
    }

    @Override
    void tileClicked(int x, int y){
        if(turnState == TurnState.EDITOR && !escapeMenu.isVisible()){
            if (verbose) System.out.println("[EDITOR] [tileClicked] during editor");
            switch (placingMode){
                case TILE:
                    Tile tile = new Tile(x, y, editorTileTypePlaced);
                    TileSerializable tileSerializable = new TileSerializable(tile);
                    map.switchTile(tileSerializable);
                    break;
                case BUILDING:
                    Building building = new Building(editorBuildingTypePlaced, x, y);
                    building.setOwnerId(editorOwnerIdPlaced);
                    BuildingSerializable buildingSerializable = new BuildingSerializable(building);
                    map.switchBuilding(buildingSerializable, game);
                    break;
                case UNIT:
                    Unit unit = new Unit(editorUnitTypePlaced, x, y);
                    unit.setOwnerId(editorOwnerIdPlaced);
                    UnitSerializable unitSerializable = new UnitSerializable(unit);
                    map.switchUnit(unitSerializable, game);
                    break;
                default:
                    printErr();
                    break;
            }
        }
    }

    private void printErr(){
        System.err.println("[EDITOR] Editor menu error");
    }
}
