package fxzone.controller.ingame;

import fxzone.config.Config;
import fxzone.controller.menu.MainMenuUiController;
import fxzone.controller.menu.PlayMenuUiController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.InputHandler;
import fxzone.game.logic.Map;
import fxzone.game.logic.Tile;
import fxzone.game.logic.TurnState;
import fxzone.game.logic.Unit;
import fxzone.game.logic.UnitState;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.game.render.GameObjectInTileSpace;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class InGameUiController extends AbstractUiController {

    /*
    ENGINE ELEMENTS
     */
    private Group root2D;

    private final AbstractGameController gameController;

    /*
    UI ELEMENTS
     */
    private Button quitButton;

    /**
     * Used in secondsPrinter.
     */
    private double cumulativeDelta = 0;

    /**
     * Game logical tile of the map that the mouse pointer is hovering over.
     */
    private int tileHoveredX = 0, tileHoveredY = 0;

    /**
     * Indicates that the mouse pointer is in bounds of the map.
     */
    private boolean mousePointerInBounds;

    /**
     * Small indicator that marks the tile that the mouse pointer is hovering over.
     */
    private GameObjectInTileSpace tileSelector;

    /**
     * Used to switch graphical stance of selected unit
     */
    private double cumulativeDeltaUnitStance = 0;

    /*
    GAME LOGIC
     */
    protected Map map;

    protected Unit selectedUnit;

    protected TurnState turnState = TurnState.NEUTRAL;

    private final HashMap<Unit, Double> unitsMoving = new HashMap<Unit, Double>();


    public InGameUiController(AbstractGameController gameController, MapSerializable initialMap) {
        super(gameController);
        this.gameController = gameController;
        initializeMap(initialMap);

        //BEGIN TEST
        /*
        Queue<Point> path = new ArrayDeque<>();
        path.add(new Point(0, 0));
        path.add(new Point(1, 0));
        path.add(new Point(2, 0));
        commandUnitToMove(map.getUnits().get(0), path);
         */
        //END TEST
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        this.root2D = root2D;
        createTileSelector();
        createFXSceneUI();
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {
        //System.out.println("[InGameUiController] update()");
        //secondsPrinter(delta);
        refreshUi();
        handleClicks();
        moveMap(delta);
        zoomMap();
        findHoveredTile();
        moveSelector();
        updateSelectedUnit(delta);
        moveMovingUnits(delta);
    }

    private void createTileSelector(){
        tileSelector = new GameObjectInTileSpace(AssetHandler.getImage("/images/misc/selector.png"), 0, 0, 128, root2D);
        tileSelector.setViewOrder(-1);
    }

    private void createFXSceneUI(){
        Font font = new Font(20);
        quitButton = new Button("Quit");
        quitButton.setViewOrder(-10);
        quitButton.setVisible(true);
        quitButton.setFont(font);
        quitButton.setOnMouseClicked(mouseEvent -> {
            quitGame();
        });
        root2D.getChildren().add(quitButton);
    }

    private void initializeMap(MapSerializable initialMap){
        map = new Map(initialMap, root2D);
    }

    protected void quitGame(){
        gameController.setActiveUiController(new MainMenuUiController(gameController));
    }

    /**
     * Prints a line every second.
     */
    private void secondsPrinter(double delta){
        this.cumulativeDelta += delta;
        if(this.cumulativeDelta > 1){
            System.out.println("[secondPrinter] !!!");
            this.cumulativeDelta -= 1;

            //map.setGraphicalOffset(map.getOffsetX()+32, map.getOffsetY()+16);
            //System.out.println(gameController.getInputHandler().getCumulativeScrollDelta());

        }
    }

    /**
     * Redraw UI elements to adjust for window size changes
     */
    private void refreshUi(){
        quitButton.setTranslateX(subScene2D.getWidth() - quitButton.getWidth() - 24);
        quitButton.setTranslateY(subScene2D.getHeight() - quitButton.getHeight() - 46);
    }

    private void handleClicks(){
        if(gameController.getInputHandler().wasMousePrimaryButtonPressed()){

            //This call to input handler clarifies that the click has been processed.
            Point2D pointClicked = gameController.getInputHandler().getLastMousePrimaryButtonPressedPosition();

            if(mousePointerInBounds){
                tileClicked(tileHoveredX, tileHoveredY);
            }
        }
    }

    private void tileClicked(int x, int y){
        if(turnState == TurnState.NEUTRAL){
            Unit unitOnTileClicked = map.getTiles()[x][y].getUnitOnTile();
            if(unitOnTileClicked != null){
                selectUnit(unitOnTileClicked);
            }
        } else if(turnState == TurnState.UNIT_SELECTED){
            if(selectedUnit.getX() == x && selectedUnit.getY() == y){
                turnState = TurnState.NEUTRAL;
            }

            //TEMPORARY UNIT MOVE COMMANDS
            else if(map.isInBounds(x, y)){
                Queue<Point> path = new ArrayDeque<>();
                path.add(new Point(x, y));
                onPlayerUnitMoveCommand(path);
            }
        }
    }

    /**
     * Switch the graphical stance of the currently selected unit back and forth
     * @param delta time (in seconds) since last update
     */
    private void updateSelectedUnit(double delta){
        if(turnState == TurnState.UNIT_SELECTED && selectedUnit != null){
            this.cumulativeDeltaUnitStance += delta;
            if(this.cumulativeDeltaUnitStance > .25){
                this.cumulativeDeltaUnitStance -= .25;
                selectedUnit.switchStance();
            }
        }
    }

    /**
     * Move the map on screen as the camera moves via arrow keys
     *
     * @param delta time (in seconds) since last update
     */
    private void moveMap(double delta){

        double totalExtraOffsetX = 0, totalExtraOffsetY = 0;
        double mapMovementUnit = delta * map.getTileRenderSize() * Config.getDouble("MAP_SCROLL_SPEED");

        if(gameController.getInputHandler().isKeyPressed(KeyCode.RIGHT) || gameController.getInputHandler().isKeyPressed(KeyCode.D)){
            totalExtraOffsetX -= mapMovementUnit;
        }
        if(gameController.getInputHandler().isKeyPressed(KeyCode.LEFT) || gameController.getInputHandler().isKeyPressed(KeyCode.A)){
            totalExtraOffsetX += mapMovementUnit;
        }
        if(gameController.getInputHandler().isKeyPressed(KeyCode.UP) || gameController.getInputHandler().isKeyPressed(KeyCode.W)){
            totalExtraOffsetY += mapMovementUnit;
        }
        if(gameController.getInputHandler().isKeyPressed(KeyCode.DOWN) || gameController.getInputHandler().isKeyPressed(KeyCode.S)){
            totalExtraOffsetY -= mapMovementUnit;
        }

        map.setGraphicalOffset(map.getOffsetX() + totalExtraOffsetX, map.getOffsetY() + totalExtraOffsetY);
    }

    /**
     * Move every moving unit for which the required cumulative delta has been reached.
     * Update every moving units delta, whether it went a full tile or not.
     * Moving units with no more tiles in queued path are removed from moving unit list.
     *
     * @param delta time (in seconds) since last update
     */
    private void moveMovingUnits(double delta){
        for(Unit unit : unitsMoving.keySet()){
            double cumulativeDelta = unitsMoving.get(unit);
            cumulativeDelta += delta;
            if(cumulativeDelta > 1){
                cumulativeDelta -= 1;
                if(!unit.performFullTileMove(map)){
                    unitsMoving.remove(unit);
                    return;
                }
            }
            unitsMoving.put(unit, cumulativeDelta);
        }
    }

    /**
     * Draw the tile selector over the tile that the mouse is hovering over.
     */
    private void moveSelector(){
        tileSelector.setPositionInMap(tileHoveredX, tileHoveredY, map);
    }

    /**
     * Determine the game logical tile of the map that the mouse is hovering over.
     */
    private void findHoveredTile(){
        try {
            Point2D pointHovered = gameController.getInputHandler().getLastMousePosition();
            Tile hoveredTile = map.getTileAt(pointHovered.getX(), pointHovered.getY());
            tileHoveredX = hoveredTile.getX();
            tileHoveredY = hoveredTile.getY();
            setMousePointerInBounds(true);
        }
        catch (ArrayIndexOutOfBoundsException e){
            setMousePointerInBounds(false);
        }
    }

    private void setMousePointerInBounds(boolean mousePointerInBounds){
        this.mousePointerInBounds = mousePointerInBounds;
        tileSelector.setVisible(mousePointerInBounds);
    }

    /**
     * Change the graphical size of all map contents as the camera zooms in/out via mouse wheel
     */
    private void zoomMap(){
        double scrollDelta = gameController.getInputHandler().getCumulativeScrollDelta();
        if(scrollDelta != 0){

            double newTileRenderSize = map.getTileRenderSize() + scrollDelta;

            if(newTileRenderSize >= Config.getDouble("MIN_TILE_SIZE_ON_ZOOM") &&
            newTileRenderSize <= Config.getDouble("MAX_TILE_SIZE_ON_ZOOM")){

                map.setTileRenderSize(newTileRenderSize);

                /*
                Also adjust size of selector
                */
                tileSelector.changeTileRenderSize(tileHoveredX, tileHoveredY, map);
            }
        }
    }

    /**
     * Game logical selection of a unit. On success, change turn state to UNIT_SELECTED and store pointer to selected unit.
     *
     * @param unit unit being selected
     */
    protected void selectUnit(Unit unit){
        if(turnState == TurnState.NEUTRAL && unit.getUnitState() == UnitState.NEUTRAL){
            selectedUnit = unit;
            turnState = TurnState.UNIT_SELECTED;
        }
    }

    private void commandUnitToMove(Unit unit, Queue<Point> path){
        if(unit.moveCommand(path)){
            unitsMoving.put(unit, 0.);
        }
    }

    /**
     * A player gives a unit a move command during their turn.
     */
    private void onPlayerUnitMoveCommand(Queue<Point> path){
        commandUnitToMove(selectedUnit, path);
        turnState = TurnState.NEUTRAL;
    }
}
