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
import fxzone.game.logic.Map.Biome;
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
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InGameEditorUiController extends InGameUiController{

    /*
    DEBUG
     */
    private static final boolean verbose = true;

    private int editorTileTypePlacedCurrentIndex;
    private TileType editorTileTypePlaced;
    private final TileType[] editorTileTypesPlaceable = {
        TileType.PLAINS,
        TileType.BEACH,
        TileType.WATER,
        TileType.FOREST,
        TileType.MOUNTAIN,
    };

    private int editorBuildingTypePlacedCurrentIndex;
    private BuildingType editorBuildingTypePlaced;
    private final BuildingType[] editorBuildingTypesPlaceable = {
        BuildingType.CITY,
        BuildingType.FACTORY,
        BuildingType.PORT,
        BuildingType.AIRPORT,
    };

    private int editorUnitTypePlacedCurrentIndex;
    private UnitType editorUnitTypePlaced;
    private final UnitType[] editorUnitTypesPlaceable = {
        UnitType.INFANTRY,
        UnitType.INFANTRY_RPG,
        UnitType.INFANTRY_AA,
        UnitType.CAR_HUMVEE,
        UnitType.CAR_PICKUP,
        UnitType.TANK_HUNTER,
        UnitType.TANK_IFV,
        UnitType.ARTILLERY,
        UnitType.TANK_AA,
        UnitType.TANK_BATTLE,
        UnitType.ARTILLERY_ROCKET,
        UnitType.SHIP_LANDER,
        UnitType.SHIP_GUNBOAT,
        UnitType.SHIP_DESTROYER,
        UnitType.SHIP_BATTLESHIP,
        UnitType.SHIP_CARRIER,
        UnitType.HELICOPTER_CHINOOK,
        UnitType.HELICOPTER_APACHE,
        UnitType.PLANE_PROPELLER,
        UnitType.PLANE_JET,
        UnitType.PLANE_BOMBER,
    };

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

        beginTurn();
    }

    @Override
    protected void initializeGameSpecifics(){
        Player editorPlayer = new Player("Editor", Color.CYAN, 404, null);

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

        ImageView tileIcon = initializeEditorMenuIcon(50 * UI_SIZE_IN_GAME_MENUS / 100, 2 * UI_SIZE_IN_GAME_MENUS);
        tileIcon.setImage(AssetHandler.getImageTile(new KeyTile(editorTileTypePlaced, false, Biome.SAND)));

        this.tileTypeButton = initializeEditorMenuButton(150 * UI_SIZE_IN_GAME_MENUS / 100, 2 * UI_SIZE_IN_GAME_MENUS);
        tileTypeButton.setText(""+editorTileTypePlaced);

        selectPlacingMode(EditorPlacingMode.TILE);

        tileTypeButton.setOnMouseClicked(mouseEvent -> {
            if(tileTypeButton == selectedPlacingButton) {

                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                    editorTileTypePlacedCurrentIndex--;
                    if(editorTileTypePlacedCurrentIndex < 0){
                        editorTileTypePlacedCurrentIndex = editorTileTypesPlaceable.length - 1;
                    }
                } else {
                    editorTileTypePlacedCurrentIndex++;
                    if(editorTileTypePlacedCurrentIndex >= editorTileTypesPlaceable.length){
                        editorTileTypePlacedCurrentIndex = 0;
                    }
                }
                editorTileTypePlaced = editorTileTypesPlaceable[editorTileTypePlacedCurrentIndex];

                tileTypeButton.setText("" + editorTileTypePlaced);
                tileIcon.setImage(AssetHandler.getImageTile(new KeyTile(editorTileTypePlaced, false, Biome.SAND)));
            } else {
                selectPlacingMode(EditorPlacingMode.TILE);
            }
        });



        ImageView ownerIdIcon = initializeEditorMenuIcon(50 * UI_SIZE_IN_GAME_MENUS / 100, 4*UI_SIZE_IN_GAME_MENUS);
        ownerIdIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(UnitType.INFANTRY, 1, null)));

        Button ownerIdButton = initializeEditorMenuButton(150 * UI_SIZE_IN_GAME_MENUS / 100, 4*UI_SIZE_IN_GAME_MENUS);
        ownerIdButton.setText("NONE");
        ownerIdButton.setOnMouseClicked(mouseEvent -> {

            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                editorOwnerIdPlaced--;
                if(editorOwnerIdPlaced < 0){
                    editorOwnerIdPlaced = 4;
                }
            } else {
                editorOwnerIdPlaced++;
                if(editorOwnerIdPlaced > 4){
                    editorOwnerIdPlaced = 0;
                }
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



        ImageView buildingIcon = initializeEditorMenuIcon(50 * UI_SIZE_IN_GAME_MENUS / 100, 5*UI_SIZE_IN_GAME_MENUS);
        buildingIcon.setImage(AssetHandler.getImageBuilding(new KeyBuilding(editorBuildingTypePlaced, null)));

        this.buildingTypeButton = initializeEditorMenuButton(150 * UI_SIZE_IN_GAME_MENUS / 100, 5*UI_SIZE_IN_GAME_MENUS);
        buildingTypeButton.setText(""+editorBuildingTypePlaced);
        buildingTypeButton.setOnMouseClicked(mouseEvent -> {
            if(buildingTypeButton == selectedPlacingButton) {

                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                    editorBuildingTypePlacedCurrentIndex--;
                    if(editorBuildingTypePlacedCurrentIndex < 0){
                        editorBuildingTypePlacedCurrentIndex = editorBuildingTypesPlaceable.length - 1;
                    }
                } else {
                    editorBuildingTypePlacedCurrentIndex++;
                    if(editorBuildingTypePlacedCurrentIndex >= editorBuildingTypesPlaceable.length){
                        editorBuildingTypePlacedCurrentIndex = 0;
                    }
                }
                editorBuildingTypePlaced = editorBuildingTypesPlaceable[editorBuildingTypePlacedCurrentIndex];

                buildingTypeButton.setText("" + editorBuildingTypePlaced);
                buildingIcon.setImage(AssetHandler.getImageBuilding(new KeyBuilding(editorBuildingTypePlaced, null)));
            }
            else {
                selectPlacingMode(EditorPlacingMode.BUILDING);
            }
        });




        ImageView unitIcon = initializeEditorMenuIcon(40 * UI_SIZE_IN_GAME_MENUS / 100, 6*UI_SIZE_IN_GAME_MENUS);
        unitIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(editorUnitTypePlaced, 0, null)));

        this.unitTypeButton = initializeEditorMenuButton(150 * UI_SIZE_IN_GAME_MENUS / 100, 6*UI_SIZE_IN_GAME_MENUS);
        unitTypeButton.setText(""+editorUnitTypePlaced);
        unitTypeButton.setOnMouseClicked(mouseEvent -> {
            if(unitTypeButton == selectedPlacingButton) {

                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                    editorUnitTypePlacedCurrentIndex--;
                    if(editorUnitTypePlacedCurrentIndex < 0){
                        editorUnitTypePlacedCurrentIndex = editorUnitTypesPlaceable.length - 1;
                    }
                } else {
                    editorUnitTypePlacedCurrentIndex++;
                    if(editorUnitTypePlacedCurrentIndex >= editorUnitTypesPlaceable.length){
                        editorUnitTypePlacedCurrentIndex = 0;
                    }
                }
                editorUnitTypePlaced = editorUnitTypesPlaceable[editorUnitTypePlacedCurrentIndex];

                unitTypeButton.setText("" + editorUnitTypePlaced);
                unitIcon.setImage(AssetHandler.getImageUnit(new KeyUnit(editorUnitTypePlaced, 0, null)));
            } else {
                selectPlacingMode(EditorPlacingMode.UNIT);
            }
        });


        Button saveMapButton = initializeEditorMenuButton(UI_SIZE_IN_GAME_MENUS, 8*UI_SIZE_IN_GAME_MENUS);
        saveMapButton.setText("Save Map");
        saveMapButton.setOnMouseClicked(mouseEvent -> {
            saveMap();
        });

        nameMapTextField = new TextField();
        nameMapTextField.setTranslateX(UI_SIZE_IN_GAME_MENUS);
        nameMapTextField.setTranslateY(7*UI_SIZE_IN_GAME_MENUS);
        nameMapTextField.setPrefWidth(4*UI_SIZE_IN_GAME_MENUS);
        nameMapTextField.setViewOrder(ViewOrder.UI_BUTTON);
        nameMapTextField.setVisible(true);
        nameMapTextField.setFont(new Font(30 * UI_SIZE_IN_GAME_MENUS / 100.));
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
                selectedPlacingButton.setStyle("-fx-font-size:"+UI_SIZE_IN_GAME_MENUS*40/100);
            }
            button.setStyle("-fx-font-size:"+UI_SIZE_IN_GAME_MENUS*40/100+";-fx-border-width: "+5*UI_SIZE_IN_GAME_MENUS/100+"; -fx-border-color: eeeeee;");
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
        /*
        game.addPlayer(new Player("Alpha", FxUtils.toColor("ff0000"), 1));
        game.addPlayer(new Player("Bravo", FxUtils.toColor("0000ff"), 2));
        game.addPlayer(new Player("Charlie", FxUtils.toColor("00ff00"), 3));
        game.addPlayer(new Player("Delta", FxUtils.toColor("ffff00"), 4));
        */
    }

    private ImageView initializeEditorMenuIcon(int x, int y){
        ImageView icon = new ImageView();
        icon.setFitWidth(75 * UI_SIZE_IN_GAME_MENUS / 100.);
        icon.setFitHeight(75 * UI_SIZE_IN_GAME_MENUS / 100.);
        icon.setTranslateX(x);
        icon.setTranslateY(y);
        icon.setVisible(true);
        icon.setViewOrder(ViewOrder.UI_BUTTON);
        escapeMenu.getChildren().add(icon);
        return icon;
    }
    private Button initializeEditorMenuButton(int x, int y){
        Button button = new Button();
        button.setStyle("-fx-font-size:"+UI_SIZE_IN_GAME_MENUS*40/100);
        button.setPrefWidth(4*UI_SIZE_IN_GAME_MENUS);
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
        Save.saveMap(new MapSerializable(map, runningUnitId), mapName);
        if(verbose) System.out.println("[EDITOR] [saveMap] saved");
    }

    @Override
    void tileClicked(int x, int y, boolean rightClick){
        if(turnState == TurnState.EDITOR && !escapeMenu.isVisible()){
            if (verbose) System.out.println("[EDITOR] [tileClicked] during editor");
            switch (placingMode){
                case TILE:
                    Tile tile = new Tile(x, y, rightClick ? ((editorTileTypePlaced == TileType.WATER || editorTileTypePlaced == TileType.FOREST || editorTileTypePlaced == TileType.MOUNTAIN) ? TileType.PLAINS : TileType.WATER) : editorTileTypePlaced);
                    TileSerializable tileSerializable = new TileSerializable(tile);
                    map.switchTile(tileSerializable);
                    break;
                case BUILDING:
                    if(rightClick && map.isInBounds(x, y) && map.getTiles()[x][y].hasBuildingOnTile()){
                        map.removeBuilding(map.getTiles()[x][y].getBuildingOnTile());
                        break;
                    } else if(!rightClick){
                        Building building = new Building(editorBuildingTypePlaced, x, y);
                        building.setOwnerId(editorOwnerIdPlaced);
                        BuildingSerializable buildingSerializable = new BuildingSerializable(building);
                        map.switchBuilding(buildingSerializable, game);
                        break;
                    }
                    break;
                case UNIT:
                    if(rightClick && map.isInBounds(x, y) && map.getTiles()[x][y].hasUnitOnTile()){
                        map.removeUnit(map.getTiles()[x][y].getUnitOnTile());
                        break;
                    } else if(!rightClick){
                        Unit unit = new Unit(editorUnitTypePlaced, x, y, runningUnitId++);
                        unit.setOwnerId(editorOwnerIdPlaced);
                        UnitSerializable unitSerializable = new UnitSerializable(unit);
                        map.switchUnit(unitSerializable, game);
                        break;
                    }
                    break;
                default:
                    printErr();
                    break;
            }
        }
    }

    /**
     * Also handle dragging to place tiles quickly
     */
    @Override
    boolean handleHoveredTile(){
        boolean hoveredTileChanged = super.handleHoveredTile();
        if(mousePointerInBounds && hoveredTileChanged && gameController.getInputHandler().isMouseButtonPressed(MouseButton.PRIMARY) && !gameController.getInputHandler().wasMousePrimaryButtonPressed() && placingMode == EditorPlacingMode.TILE){
            tileClicked(tileHoveredX, tileHoveredY, false);
        }
        return hoveredTileChanged;
    }

    private void printErr(){
        System.err.println("[EDITOR] Editor menu error");
    }

    public void setNameMapTextFieldText(String mapName){
        nameMapTextField.setText(mapName);
    }
}
