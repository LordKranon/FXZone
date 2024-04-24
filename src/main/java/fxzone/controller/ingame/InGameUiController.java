package fxzone.controller.ingame;

import fxzone.config.Config;
import fxzone.controller.menu.MainMenuUiController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.Direction;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Game;
import fxzone.game.logic.Map;
import fxzone.game.logic.Player;
import fxzone.game.logic.Tile;
import fxzone.game.logic.TurnState;
import fxzone.game.logic.Unit;
import fxzone.game.logic.UnitState;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.render.GameObjectInTileSpace;
import fxzone.game.render.GameObjectUiMoveCommandArrowTile;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
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

    private Button endTurnButton;

    private Label labelPlayerName;


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
     * Used to switch graphical stance of selected unit.
     */
    private double cumulativeDeltaUnitStance = 0;

    /**
     * For Unit Move Commands.
     * Saves the last tile that was hovered over while issuing a path.
     */
    private Point lastTileForUnitPathQueue;

    /**
     * For Unit Move Commands.
     * Stores the graphical objects representing the current move command queue while issuing a path.
     */
    private ArrayList<GameObjectUiMoveCommandArrowTile> moveCommandArrowTiles;

    /*
    SOUND
     */
    MediaPlayer mediaPlayer;

    /*
    GAME LOGIC
     */
    protected Map map;

    protected Game game;

    protected Player thisPlayer;

    /**
     * Only used at initialization to determine players.
     */
    protected int thisPlayerId;

    protected Unit selectedUnit;

    protected TurnState turnState = TurnState.NEUTRAL;

    private final HashMap<Unit, Double> unitsMoving = new HashMap<Unit, Double>();

    private ArrayDeque<Point> selectedUnitQueuedPath;

    /*
    DEBUG
    * */
    static final boolean verbose = true;


    public InGameUiController(AbstractGameController gameController, GameSerializable initialGame, int thisPlayerId) {
        super(gameController);
        this.gameController = gameController;
        this.thisPlayerId = thisPlayerId;
        initializeGame(initialGame);
        initializeGameSpecifics();

        //BEGIN TEST
        mediaPlayer = new MediaPlayer(AssetHandler.getSound("/sounds/test_sound_uiInteraction.mp3"));
        mediaPlayer.play();
        //END TEST
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        this.root2D = root2D;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/InGameView.fxml"));
            loader.setControllerFactory(c -> {
                return new InGameUiControllerFxml(gameController);
            });
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        moveMoveCommandArrowTiles();
        handleSelectedUnitPathQueue();
        updateSelectedUnit(delta);
        moveMovingUnits(delta);
    }

    class InGameUiControllerFxml{
        private final AbstractGameController gameController;

        public InGameUiControllerFxml(AbstractGameController gameController) {
            this.gameController = gameController;
        }

        @FXML
        HBox hBox;

        @FXML
        public void initialize(){
            resize(hBox, gameController.getStage());
            initializeOuter(hBox);
        }
    }

    private void initializeOuter(Pane hBox){

        root2D.getChildren().add(hBox);
        VBox vBox = (VBox) hBox.getChildren().get(0);
        HBox hBoxInner = (HBox) vBox.getChildren().get(1);
        VBox vBoxInner1 = (VBox) hBoxInner.getChildren().get(0);
        labelPlayerName = (Label) vBoxInner1.getChildren().get(0);

    }

    protected void initializeGameSpecifics(){
        initializeGameSpecificUi();
    }

    private void initializeGameSpecificUi(){
        labelPlayerName.setText(thisPlayer.getName());
        labelPlayerName.setTextFill(Color.web(thisPlayer.getColor().toString()));
    }

    private void createTileSelector(){
        tileSelector = new GameObjectInTileSpace(AssetHandler.getImage("/images/misc/selector.png"), 0, 0, 128, root2D);
        tileSelector.setViewOrder(ViewOrder.UI_SELECTOR);
    }

    private void createFXSceneUI(){
        Font font = new Font(36);
        quitButton = new Button("Quit");
        quitButton.setPrefWidth(400);
        quitButton.setViewOrder(ViewOrder.UI_BUTTON);
        quitButton.setVisible(true);
        quitButton.setFont(font);
        quitButton.setOnMouseClicked(mouseEvent -> {
            quitGame();
        });
        root2D.getChildren().add(quitButton);

        endTurnButton = new Button("End Turn");
        endTurnButton.setPrefWidth(400);
        endTurnButton.setViewOrder(ViewOrder.UI_BUTTON);
        endTurnButton.setVisible(true);
        endTurnButton.setFont(font);
        endTurnButton.setOnMouseClicked(mouseEvent -> {
            endTurnButtonClicked();
        });
        root2D.getChildren().add(endTurnButton);
    }

    private void initializeGame(GameSerializable initialGame){
        game = new Game(initialGame, root2D);
        map = game.getMap();
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
        }
    }

    /**
     * Redraw UI elements to adjust for window size changes
     */
    private void refreshUi(){
        quitButton.setTranslateX(subScene2D.getWidth() - quitButton.getWidth() - 24);
        quitButton.setTranslateY(subScene2D.getHeight() - quitButton.getHeight() - 46);
        endTurnButton.setTranslateX(subScene2D.getWidth() - endTurnButton.getWidth() - 24);
        endTurnButton.setTranslateY(subScene2D.getHeight() - endTurnButton.getHeight() - 46 - quitButton.getHeight());
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

    private void handleSelectedUnitPathQueue(){
        if(turnState == TurnState.UNIT_SELECTED){
            Point hoveredPoint = new Point(tileHoveredX, tileHoveredY);
            if(!hoveredPoint.equals(lastTileForUnitPathQueue)){
                addPointToSelectedUnitPathQueue(hoveredPoint);
            }
        }
    }

    private void addPointToSelectedUnitPathQueue(Point point){
        /*Logic*/
        selectedUnitQueuedPath.add(point);

        // Determine direction of previous point from perspective of new point
        // Simultaneously do this the other way around, to correctly set the successor direction of preceding arrow tile
        Direction directionPredecessor;
        Direction directionOfThisAsSuccessor;
        if(lastTileForUnitPathQueue.x < point.x){
            directionPredecessor = Direction.LEFT;
            directionOfThisAsSuccessor = Direction.RIGHT;
        } else if (lastTileForUnitPathQueue.x > point.x){
            directionPredecessor = Direction.RIGHT;
            directionOfThisAsSuccessor = Direction.LEFT;
        } else if (lastTileForUnitPathQueue.y < point.y){
            directionPredecessor = Direction.UP;
            directionOfThisAsSuccessor = Direction.DOWN;
        } else if (lastTileForUnitPathQueue.y > point.y){
            directionPredecessor = Direction.DOWN;
            directionOfThisAsSuccessor = Direction.UP;
        } else {
            System.err.println("[IN-GAME-UI-CONTROLLER] Error in move command path issuing process");
            directionPredecessor = Direction.NONE;
            directionOfThisAsSuccessor = Direction.NONE;
        }

        // Set successor direction of preceding arrow tile
        if(!moveCommandArrowTiles.isEmpty()){
            GameObjectUiMoveCommandArrowTile predecessorArrowTile = moveCommandArrowTiles.get(moveCommandArrowTiles.size()-1);
            predecessorArrowTile.setDirectionOfSuccessor(directionOfThisAsSuccessor);
        }

        lastTileForUnitPathQueue = point;

        /*Graphics*/
        GameObjectUiMoveCommandArrowTile arrowTile = new GameObjectUiMoveCommandArrowTile(
            point.x, point.y, map, root2D, directionPredecessor
        );
        moveCommandArrowTiles.add(arrowTile);
    }

    private void tileClicked(int x, int y){
        if(game.itsMyTurn(thisPlayer) && turnState == TurnState.NEUTRAL){
            if (verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [tileClicked] during your turn with turn-state neutral");
            Unit unitOnTileClicked = map.getTiles()[x][y].getUnitOnTile();
            if(unitOnTileClicked != null){
                selectUnit(unitOnTileClicked);
            }
        } else if(turnState == TurnState.UNIT_SELECTED){
            if(selectedUnit.getX() == x && selectedUnit.getY() == y){
                turnStateToNeutral();
            }

            //TEMPORARY UNIT MOVE COMMANDS
            else if(map.isInBounds(x, y) && map.getTiles()[x][y].getUnitOnTile() == null){

                onPlayerUnitMoveCommand(selectedUnitQueuedPath);
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
     * Draw the tile selector over the tile that the mouse is hovering over and adjust for map offset.
     */
    private void moveSelector(){
        tileSelector.setPositionInMap(tileHoveredX, tileHoveredY, map);
    }

    /**
     * Redraw the UI move command arrow to adjust for map offset.
     */
    private void moveMoveCommandArrowTiles(){
        if(turnState == TurnState.UNIT_SELECTED){
            for(GameObjectUiMoveCommandArrowTile arrowTile : moveCommandArrowTiles){
                arrowTile.setOffset(map);
            }
        }
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

                adjustUiElementsInTileSpaceOnZoom();
            }
        }
    }

    private void adjustUiElementsInTileSpaceOnZoom(){
        tileSelector.changeTileRenderSize(tileHoveredX, tileHoveredY, map);
        if(turnState == TurnState.UNIT_SELECTED){
            for (GameObjectUiMoveCommandArrowTile arrowTile : moveCommandArrowTiles){
                arrowTile.changeTileRenderSize(map);
            }
        }
    }

    /**
     * Game logical selection of a unit. On success, change turn state to UNIT_SELECTED and store pointer to selected unit.
     *
     * @param unit unit being selected
     */
    protected void selectUnit(Unit unit){
        if (verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [selectUnit] trying");
        if(
            turnState == TurnState.NEUTRAL &&
            unit.getUnitState() == UnitState.NEUTRAL &&
            thisPlayer != null &&
            (thisPlayer.getId() == unit.getOwnerId())
        ){
            selectedUnit = unit;

            // Initialize unit path queue
            selectedUnitQueuedPath = new ArrayDeque<>();
            lastTileForUnitPathQueue = new Point(selectedUnit.getX(), selectedUnit.getY());

            // Initialize move command arrow
            moveCommandArrowTiles = new ArrayList<>();

            // Add the first part of the arrow, which is on the tile that the selected unit is standing on
            GameObjectUiMoveCommandArrowTile arrowTile = new GameObjectUiMoveCommandArrowTile(
                selectedUnit.getX(), selectedUnit.getY(), map, root2D, Direction.NONE
            );
            moveCommandArrowTiles.add(arrowTile);

            turnState = TurnState.UNIT_SELECTED;
            if(verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [selectUnit] unit selected");
        }
    }

    protected void commandUnitToMove(Unit unit, ArrayDeque<Point> path){
        if(unit.moveCommand(path, map)){
            unitsMoving.put(unit, 0.);
        }
    }

    /**
     * The player gives a unit a move command during their turn.
     */
    protected void onPlayerUnitMoveCommand(ArrayDeque<Point> path){
        commandUnitToMove(selectedUnit, path);
        turnStateToNeutral();
    }

    /**
     * The player has clicked the "end turn" button.
     * Now, depending on the turn state, the "end turn demand" may or may not be given.
     */
    private void endTurnButtonClicked(){
        if (verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [endTurnButtonClicked] trying");
        if(game.itsMyTurn(thisPlayer) && turnState == TurnState.NEUTRAL){
            turnState = TurnState.ENDING_TURN;
            onPlayerEndTurn();
        }
    }

    /**
     * The player demands to end their turn.
     */
    protected void onPlayerEndTurn(){
        endTurn();
    }

    /**
     * End the turn.
     */
    protected void endTurn(){
        /*TODO
        If turn is ended via network packet reception, the turnStateToNeutral() cleanup function may not clean up
        graphical elements as it is not on the FX thread. This should, in theory, never occur since you can't end your turn
        while still having a unit selected and with move command arrows being displayed.
        */
        turnStateToNeutral();
        game.goNextTurn();
    }

    /**
     * Set the turnState to NEUTRAL.
     * Handle and close all the stuff from any preceding turn states.
     */
    protected void turnStateToNeutral(){
        if(moveCommandArrowTiles != null){
            for(GameObjectUiMoveCommandArrowTile arrowTile : moveCommandArrowTiles){
                arrowTile.removeSelfFromRoot(root2D);
            }
        }
        turnState = TurnState.NEUTRAL;
    }

}
