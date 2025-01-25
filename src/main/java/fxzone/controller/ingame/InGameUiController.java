package fxzone.controller.ingame;

import fxzone.config.Config;
import fxzone.controller.menu.MainMenuUiController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.controller.button.ButtonBuildingBuyUnit;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyUnit;
import fxzone.engine.utils.Direction;
import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.GeometryUtils;
import fxzone.engine.utils.ViewOrder;
import fxzone.engine.utils.ZoneMediaPlayer;
import fxzone.game.logic.Building;
import fxzone.game.logic.Codex;
import fxzone.game.logic.Codex.UnitAttackType;
import fxzone.game.logic.Codex.UnitSuperType;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.Game;
import fxzone.game.logic.Map;
import fxzone.game.logic.Player;
import fxzone.game.logic.Tile;
import fxzone.game.logic.Unit;
import fxzone.game.logic.Unit.AttackResult;
import fxzone.game.logic.Unit.UnitState;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.GameObjectTile;
import fxzone.game.render.GameObjectTileSelector;
import fxzone.game.render.GameObjectUiMoveCommandArrowTile;
import fxzone.game.render.GameObjectUiMoveCommandGridTile;
import fxzone.game.render.GameObjectUnit;
import fxzone.game.render.particle.ParticleHandler;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class InGameUiController extends AbstractUiController {

    /*
    ENGINE ELEMENTS
     */
    private Group root2D;

    private final AbstractGameController gameController;

    protected boolean offThreadGraphicsNeedHandling;
    protected boolean startOfTurnEffectFlag;

    /*
    UI ELEMENTS
     */
    private Button escapeMenuButton;
    protected Button endTurnButton;

    private final Font fontBottomUiBar = new Font(50);
    private final Font fontBottomUiBarSmall = new Font(25);
    private TextFlow[][] textFlowsBottomUiBar;

    Pane escapeMenu;

    /**
     * Announce GAME OVER at game end.
     */
    TextFlow globalMessageTextFlow;
    Text globalMessageText;
    Text globalMessageName;


    /**
     * Used in secondsPrinter.
     */
    private double cumulativeDelta = 0;

    /**
     * Game logical tile of the map that the mouse pointer is hovering over.
     */
    private int tileHoveredX = 0, tileHoveredY = 0;
    private Point tileHovered;

    /**
     * Indicates that the mouse pointer is in bounds of the map.
     */
    private boolean mousePointerInBounds;

    /**
     * Small indicator that marks the tile that the mouse pointer is hovering over.
     */
    private GameObjectTileSelector tileSelector;

    /**
     * Used to switch graphical stance of selected unit.
     */
    private double cumulativeDeltaUnitStance = 0;

    /**
     * For Unit Move Commands.
     * Saves the last tile that was hovered over while issuing a path.
     */
    private Point lastTileHoveredForUnitPathQueue;

    /**
     * For Unit Move Commands.
     * Saves the last tile that was actually added to the move command queue while issuing a path.
     */
    private Point lastTileAddedToPathQueue;

    /**
     * For Unit Move Commands.
     * Stores the graphical objects representing the current move command queue while issuing a path.
     */
    private ArrayList<GameObjectUiMoveCommandArrowTile> moveCommandArrowTiles;

    private boolean[][] moveCommandPathFinderMarks;

    private boolean[][] moveCommandGridMovableSquares;
    private boolean[][] moveCommandGridAttackableSquares;
    private ArrayList<GameObjectUiMoveCommandGridTile> moveCommandGridTiles;

    private ArrayList<GameObjectUiMoveCommandGridTile> rangeCheckGridTiles;

    /*
    SOUND
     */
    MediaPlayer mediaPlayer;
    private static final boolean GAME_SOUND_MUSIC_ENABLED = Config.getBool("GAME_SOUND_MUSIC_ENABLED");

    /*
    GAME LOGIC
     */
    protected Map map;

    protected Game game;

    protected Player thisPlayer;
    protected boolean[][] thisPlayerFowVision;

    /**
     * Only used at initialization to determine players.
     */
    protected int thisPlayerIdTemp;

    protected Unit selectedUnit;
    protected Building selectedBuilding;
    private Pane selectedBuildingUI;


    /**
     * Affects how inputs are processed and how UI elements behave / what is shown on screen during IN-GAME
     */
    public enum TurnState {

        GAME_STARTING,
        NO_TURN,
        NEUTRAL,
        UNIT_SELECTED,
        UNIT_SELECTED_FOR_ATTACK_AFTER_MOVE,
        BUILDING_SELECTED,
        GAME_OVER,

        /**
         * Only for network syncing purposes
         */
        ENDING_TURN,
        BEGINNING_TURN,

        /**
         * Only in editor mode
         */
        EDITOR,
    }
    protected TurnState turnState = TurnState.GAME_STARTING;

    private final HashMap<Unit, Double> unitsMoving = new HashMap<Unit, Double>();
    private final HashMap<Unit, Double> unitsAttacking = new HashMap<Unit, Double>();

    private ArrayDeque<Point> selectedUnitQueuedPath;

    /**
     * Used in case of unit creation info coming in over network,
     * which needs to be transferred to the FX app thread for graphics.
     */
    protected final HashMap<UnitSerializable, Integer> unitsToBeCreated = new HashMap<>();

    /*
    GAME DECOR SETTINGS
     */
    public static final double TOTAL_UNIT_MOVEMENT_INTERVAL = Config.getDouble("GAME_SPEED_UNIT_MOVEMENT_INTERVAL");
    public static final double TOTAL_UNIT_ATTACK_INTERVAL = Config.getDouble("GAME_SPEED_UNIT_ATTACK_INTERVAL");
    private static final double MAP_SCROLL_SPEED = Config.getDouble("MAP_SCROLL_SPEED");


    private ParticleHandler particleHandler;

    /*
    DEBUG
    * */
    static final boolean verbose = true;


    public InGameUiController(AbstractGameController gameController, GameSerializable initialGame, int thisPlayerIdTemp) {
        super(gameController);
        this.gameController = gameController;
        this.thisPlayerIdTemp = thisPlayerIdTemp;
        initializeGame(initialGame);
        initializeGameSpecifics();

        //Begin the first turn
        beginTurn();

        //In-Game music
        mediaPlayer = new MediaPlayer(AssetHandler.getSound("/sounds/zone_jr_v1.2.mp3"));
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
        if(GAME_SOUND_MUSIC_ENABLED) mediaPlayer.play();
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

        this.particleHandler = new ParticleHandler(root2D);
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

        handleOffThreadGraphics();
        handleClicks();
        moveMap(delta);
        refreshUi();
        handlePulsatingElements(delta);
        zoomMap();
        handleHoveredTile();
        moveSelector();
        moveMoveCommandArrowAndGridTiles();
        handleSelectedUnitPathQueue();
        updateSelectedUnit(delta);
        handleAttackingUnits(delta);
        moveMovingUnits(delta);

        handleParticleEffects(delta);
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
        HBox hBoxInner = (HBox) vBox.getChildren().get(2);


        textFlowsBottomUiBar = new TextFlow[4][3];

        for(int i = 0; i < 4; i++){
            VBox vBoxInner = (VBox) hBoxInner.lookup("#vBox0"+(i+1));

            for(int j = 0; j < 3; j++){
                textFlowsBottomUiBar[i][j] = (TextFlow) vBoxInner.lookup("#tf0"+(i+1)+"0"+(j+1));
            }
        }
        for(int i = 0; i < 4; i++){
            textFlowsBottomUiBar[i][1].setStyle("-fx-background-color: #202020;");
            textFlowsBottomUiBar[i][1].setTextAlignment(TextAlignment.CENTER);
        }
    }

    protected void initializeGameSpecifics(){
        initializeGameSpecificUi();
    }
    private void initializeGameSpecificUi(){
        setLabelToPlayer(thisPlayer);
    }
    protected void setLabelToPlayer(Player player){

        textFlowsBottomUiBar[0][0].getChildren().clear();
        Text textName = new Text(player.getName());
        textName.setFont(fontBottomUiBar);
        textName.setStyle("-fx-fill: "+ FxUtils.toRGBCode(player.getTextColor()));
        textFlowsBottomUiBar[0][0].getChildren().add(textName);

        textFlowsBottomUiBar[0][1].getChildren().clear();
        Text textCash = new Text("$"+player.getStatResourceCash());
        textCash.setFont(fontBottomUiBar);
        textCash.setStyle("-fx-fill: white");
        textFlowsBottomUiBar[0][1].getChildren().add(textCash);

        textFlowsBottomUiBar[0][2].getChildren().clear();
        Text textNothing = new Text(" ");
        textNothing.setFont(fontBottomUiBar);
        textFlowsBottomUiBar[0][2].getChildren().add(textNothing);


    }

    private void createTileSelector(){
        tileSelector = new GameObjectTileSelector(0, 0, 128, root2D);
    }

    void createFXSceneUI(){
        try{
            String css = this.getClass().getResource("/views/style.css").toExternalForm();
            root2D.getStylesheets().add(css);
        }catch (Exception e){
            System.err.println("[IN-GAME-UI-CONTROLLER] Could not load css");
        }

        escapeMenuButton = new Button("Menu");
        escapeMenuButton.setPrefWidth(400);
        escapeMenuButton.setViewOrder(ViewOrder.UI_BUTTON);
        escapeMenuButton.setVisible(true);
        escapeMenuButton.setOnMouseClicked(mouseEvent -> {
            toggleEscapeMenu();
        });
        root2D.getChildren().add(escapeMenuButton);

        endTurnButton = new Button("End Turn");
        endTurnButton.setPrefWidth(400);
        endTurnButton.setViewOrder(ViewOrder.UI_BUTTON);
        endTurnButton.setVisible(true);
        endTurnButton.setOnMouseClicked(mouseEvent -> {
            endTurnButtonClicked();
        });
        root2D.getChildren().add(endTurnButton);



        escapeMenu = new Pane();
        escapeMenu.setPrefWidth(600);
        escapeMenu.setPrefHeight(1000);
        escapeMenu.setVisible(false);
        escapeMenu.setStyle("-fx-background-color: #282828;");
        escapeMenu.setViewOrder(ViewOrder.UI_BUTTON);
        root2D.getChildren().add(escapeMenu);

        Button quitConfirmButton = new Button("Quit");
        quitConfirmButton.setPrefWidth(400);
        quitConfirmButton.setTranslateX(100);
        quitConfirmButton.setTranslateY(900);
        quitConfirmButton.setViewOrder(ViewOrder.UI_BUTTON);
        quitConfirmButton.setVisible(true);
        quitConfirmButton.setOnMouseClicked(mouseEvent -> {
            quitGame();
        });
        escapeMenu.getChildren().add(quitConfirmButton);


        globalMessageTextFlow = new TextFlow();
        globalMessageTextFlow.setVisible(false);
        globalMessageTextFlow.setViewOrder(ViewOrder.UI_IN_GAME_ANNOUNCEMENT);
        globalMessageTextFlow.setTranslateX((subScene2D.getWidth() - globalMessageTextFlow.getWidth()) / 2);
        globalMessageTextFlow.setTranslateY((subScene2D.getHeight() - globalMessageTextFlow.getHeight()) / 2);
        globalMessageTextFlow.setTextAlignment(TextAlignment.CENTER);

        globalMessageText = new Text("DEFEAT");
        globalMessageText.setFont(new Font(100));
        globalMessageText.setVisible(true);
        globalMessageText.setStyle("-fx-fill: white");
        globalMessageText.setTextAlignment(TextAlignment.CENTER);

        globalMessageTextFlow.getChildren().add(globalMessageText);

        globalMessageName = new Text("\nName");
        globalMessageName.setFont(new Font(100));
        globalMessageName.setVisible(true);
        globalMessageName.setStyle("-fx-fill: white");
        globalMessageName.setTextAlignment(TextAlignment.CENTER);

        globalMessageTextFlow.getChildren().add(globalMessageName);

        root2D.getChildren().add(globalMessageTextFlow);
    }

    void initializeGame(GameSerializable initialGame){
        game = new Game(initialGame, root2D);
        map = game.getMap();
    }

    protected void toggleEscapeMenu(){
        if(escapeMenu.isVisible()){
            escapeMenu.setVisible(false);
        } else{
            escapeMenu.setVisible(true);
        }
    }

    protected void quitGame(){
        mediaPlayer.stop();
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
        escapeMenuButton.setTranslateX(subScene2D.getWidth() - escapeMenuButton.getWidth() - 24);
        escapeMenuButton.setTranslateY(subScene2D.getHeight() - escapeMenuButton.getHeight() - 46);
        endTurnButton.setTranslateX(subScene2D.getWidth() - endTurnButton.getWidth() - 24);
        endTurnButton.setTranslateY(subScene2D.getHeight() - endTurnButton.getHeight() - 66 - escapeMenuButton.getHeight());

        escapeMenu.setTranslateX((subScene2D.getWidth() - escapeMenu.getWidth())/2);
        escapeMenu.setTranslateY((subScene2D.getHeight() - escapeMenu.getHeight())/2);

        if(turnState == TurnState.GAME_OVER || turnState == TurnState.NO_TURN){
            globalMessageTextFlow.setTranslateX((subScene2D.getWidth() - globalMessageTextFlow.getWidth()) / 2);
            globalMessageTextFlow.setTranslateY((subScene2D.getHeight() - globalMessageTextFlow.getHeight()) / 2);
        } else if(turnState == TurnState.BUILDING_SELECTED){
            adjustSelectedBuildingUI();
        }
    }
    private void adjustSelectedBuildingUI(){
        double buildingUIX = (double)(selectedBuilding.getX()+1)*map.getTileRenderSize() + map.getOffsetX();
        double buildingUIY = (double)(selectedBuilding.getY())*map.getTileRenderSize() + map.getOffsetY();
        if(buildingUIY < 0){
            buildingUIY = 0;
        } else if(buildingUIY + selectedBuildingUI.getHeight() > subScene2D.getHeight() - 26 - 220){
            buildingUIY = subScene2D.getHeight() - selectedBuildingUI.getHeight() - 26 - 220;
        }

        selectedBuildingUI.setTranslateX(buildingUIX);
        selectedBuildingUI.setTranslateY(buildingUIY);
    }

    private void handleOffThreadGraphics(){
        if(offThreadGraphicsNeedHandling){
            offThreadGraphicsNeedHandling = false;
            if(!unitsToBeCreated.isEmpty()) {
                for(UnitSerializable unitSerializable : unitsToBeCreated.keySet()){
                    createUnit(unitSerializable, unitsToBeCreated.get(unitSerializable));
                }
                unitsToBeCreated.clear();
            }

            if(startOfTurnEffectFlag){
                startOfTurnEffectFlag = false;

                map.handleStartOfTurnEffects(game);
                setLabelToPlayer(thisPlayer);
            }
        }
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
            if(!hoveredPoint.equals(lastTileHoveredForUnitPathQueue)){
                lastTileHoveredForUnitPathQueue = hoveredPoint;

                if(
                    GeometryUtils.isPointNeighborOf(lastTileAddedToPathQueue, hoveredPoint) &&
                        selectedUnitQueuedPath.size() < Codex.getUnitProfile(selectedUnit.getUnitType()).SPEED &&
                        moveCommandGridMovableSquares[hoveredPoint.x][hoveredPoint.y] &&
                        !moveCommandGridAttackableSquares[hoveredPoint.x][hoveredPoint.y]
                ){
                    // Player manually adds another tile to pathing arrow
                    addPointToSelectedUnitPathQueue(hoveredPoint);
                } else if(
                    moveCommandGridMovableSquares[hoveredPoint.x][hoveredPoint.y] &&
                    !moveCommandGridAttackableSquares[hoveredPoint.x][hoveredPoint.y]
                ){
                    // Player hovers any green tile and arrow is either too long already or arrowhead is not neighboring
                    // Redo the pathing arrow with automatic pathfinding
                    autoFindNewSelectedUnitPathQueue(hoveredPoint);
                } else if (moveCommandGridAttackableSquares[hoveredPoint.x][hoveredPoint.y]){
                    // Attackable square hovered
                    // Do nothing if path already allows the attack, else find a path that allows the attack
                    // Or, in case of RANGED units, remove path
                    if(Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.RANGED){
                        clearSelectedUnitPathQueue();
                        addPathQueueArrowBase();
                    } else if(Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.MELEE || Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.RANGERMELEE){
                        if(
                            (GeometryUtils.getPointToPointDistance(lastTileAddedToPathQueue, hoveredPoint) <= Codex.getUnitProfile(selectedUnit).MAXRANGE &&
                            map.checkTileForMoveToByUnitPerceived(lastTileAddedToPathQueue.x, lastTileAddedToPathQueue.y, selectedUnit, thisPlayerFowVision, false)) ||
                            (selectedUnitQueuedPath.isEmpty() &&
                            GeometryUtils.getPointToPointDistance(new Point(selectedUnit.getX(), selectedUnit.getY()), hoveredPoint) <= Codex.getUnitProfile(selectedUnit).MAXRANGE)
                        ){
                            // Move & Attack command is valid as is, do nothing
                            return;
                        }
                        else {
                            // Move & Attack command is not valid as is, find a move path which allows attack of hovered point

                            // First, check if a move is necessary at all
                            // Then, if it is:
                            // From all movable squares, find ones that are in range of proposed attack, and check for moveTo
                            // From all candidates that fulfill requirements, pick closest one to unit and auto-find path to there

                            Point selectedUnitPosition = new Point(selectedUnit.getX(), selectedUnit.getY());
                            if(GeometryUtils.getPointToPointDistance(selectedUnitPosition, hoveredPoint) <= Codex.getUnitProfile(selectedUnit).MAXRANGE){
                                clearSelectedUnitPathQueue();
                                addPathQueueArrowBase();
                                return;
                            }

                            ArrayList<Point> movableTilesInRange = new ArrayList<>();
                            for(int i = 0; i < moveCommandGridMovableSquares.length; i++){
                                for(int j = 0; j < moveCommandGridMovableSquares[i].length; j++){
                                    if(
                                        moveCommandGridMovableSquares[i][j] &&
                                        GeometryUtils.getPointToPointDistance(new Point(i, j), hoveredPoint) <= Codex.getUnitProfile(selectedUnit).MAXRANGE &&
                                            map.checkTileForMoveToByUnitPerceived(i, j, selectedUnit, thisPlayerFowVision, false)
                                    ){
                                        movableTilesInRange.add(new Point(i, j));
                                    }
                                }
                            }
                            if(movableTilesInRange.isEmpty()){
                                System.err.println("[IN-GAME-UI-CONTROLLER] FATAL ERROR on pathfinding");
                            } else {
                                Point closest = movableTilesInRange.get(0);

                                for(Point p : movableTilesInRange){
                                    if(GeometryUtils.getPointToPointDistance(p, selectedUnitPosition) < GeometryUtils.getPointToPointDistance(closest, selectedUnitPosition)){
                                        closest = p;
                                    }
                                }
                                autoFindNewSelectedUnitPathQueue(closest);
                            }
                        }
                    }
                }
            }
        }
    }
    private void addPointToSelectedUnitPathQueue(Point point){

        selectedUnitQueuedPath.add(point);

        // Determine direction of this new point from perspective of previous point
        Direction directionOfThisAsSuccessor = GeometryUtils.getPointToPointDirection(lastTileAddedToPathQueue, point);

        // Set successor direction of preceding arrow tile
        if(!moveCommandArrowTiles.isEmpty()){
            GameObjectUiMoveCommandArrowTile predecessorArrowTile = moveCommandArrowTiles.get(moveCommandArrowTiles.size()-1);
            predecessorArrowTile.setDirectionOfSuccessor(directionOfThisAsSuccessor);
        }

        lastTileAddedToPathQueue = point;

        // This new arrow tile gets the earlier calculated direction as its predecessor direction
        GameObjectUiMoveCommandArrowTile arrowTile = new GameObjectUiMoveCommandArrowTile(
            point.x, point.y, map, root2D, directionOfThisAsSuccessor
        );
        moveCommandArrowTiles.add(arrowTile);
    }
    private void autoFindNewSelectedUnitPathQueue(Point point){
        if(verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [PATH-FINDER] Finding path");

        // Clear old path (geometric part and graphical part)
        clearSelectedUnitPathQueue();
        // Reset pathfinder markings
        // These are used to remember which tiles were already visited
        moveCommandPathFinderMarks = new boolean[map.getWidth()][map.getHeight()];


        PathFinder pathFinder = new PathFinder(selectedUnit.getX(), selectedUnit.getY(), Codex.getUnitProfile(selectedUnit).SPEED, point);
        int pathLength = 0;
        boolean pathFound = false;
        for(int i = 0; i <= Codex.getUnitProfile(selectedUnit).SPEED; i++){
            if(pathFinder.find(i)){
                if(verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [PATH-FINDER] Found path with length "+i);
                pathFound = true;
                pathLength = i;
                break;
            }
        }
        if(!pathFound){
            System.err.println("[IN-GAME-UI-CONTROLLER] [PATH-FINDER] Could not find path");
            return;
        }
        addPathQueueArrowBase();
        for(int i = 1; i <= pathLength; i++){
            addPointToSelectedUnitPathQueue(pathFinder.traceFoundPath(i));
        }

    }
    private class PathFinder {
        // TODO Can probably optimize and remove some checks and variables here
        private PathFinder up, left, down, right;
        private int x, y;
        private Point destination;
        private boolean found;
        private int stepsLeft;
        private PathFinder(int x, int y, int stepsLeft, Point destination){
            this.x = x;
            this.y = y;
            this.stepsLeft = stepsLeft;
            this.destination = destination;
            moveCommandPathFinderMarks[x][y] = true;
        }
        private boolean find(int currentDepth){
            if(this.x == destination.x && this.y == destination.y){
                this.found = true;
                return true;
            } else if(stepsLeft <= 0){
                return false;
            } else if(currentDepth > 0) {
                if(checkForPathFinding(x, y-1)){
                    this.up = new PathFinder(x, y-1, stepsLeft - 1, destination);
                }
                if(checkForPathFinding(x-1, y)){
                    this.left = new PathFinder(x-1, y, stepsLeft - 1, destination);
                }
                if(checkForPathFinding(x, y+1)){
                    this.down = new PathFinder(x, y+1, stepsLeft - 1, destination);
                }
                if(checkForPathFinding(x+1, y)){
                    this.right = new PathFinder(x+1, y, stepsLeft - 1, destination);
                }

                boolean upFound = false, leftFound = false, downFound = false, rightFound = false;
                if(up != null){
                    upFound = up.find(currentDepth-1);
                }
                if(left != null){
                    leftFound = left.find(currentDepth-1);
                }
                if(down != null){
                    downFound = down.find(currentDepth-1);
                }
                if(right != null){
                    rightFound = right.find(currentDepth-1);
                }
                this.found = upFound || leftFound || downFound || rightFound;
                return this.found;
            } else {
                return false;
            }
        }
        private boolean checkForPathFinding(int x, int y){
            return map.isInBounds(x, y) && moveCommandGridMovableSquares[x][y] && !moveCommandPathFinderMarks[x][y];
        }
        private Point traceFoundPath(int currentDepth){
            if(currentDepth <= 0){
                return new Point(x, y);
            } else {
                if(up != null && up.found){
                    return up.traceFoundPath(currentDepth-1);
                }
                if(left != null && left.found){
                    return left.traceFoundPath(currentDepth-1);
                }
                if(down != null && down.found){
                    return down.traceFoundPath(currentDepth-1);
                }
                if(right != null && right.found){
                    return right.traceFoundPath(currentDepth-1);
                }
                System.err.println("[PATH-FINDER] Error when tracing found path");
                return null;
            }
        }
    }
    private void addPathQueueArrowBase(){
        GameObjectUiMoveCommandArrowTile arrowTile = new GameObjectUiMoveCommandArrowTile(
            selectedUnit.getX(), selectedUnit.getY(), map, root2D, Direction.NONE
        );
        moveCommandArrowTiles.add(arrowTile);

        lastTileAddedToPathQueue = new Point(selectedUnit.getX(), selectedUnit.getY());
    }
    private void clearSelectedUnitPathQueue(){

        selectedUnitQueuedPath = new ArrayDeque<>();
        lastTileAddedToPathQueue = new Point(selectedUnit.getX(), selectedUnit.getY());

        if(moveCommandArrowTiles != null){
            for(GameObjectUiMoveCommandArrowTile arrowTile : moveCommandArrowTiles){
                arrowTile.removeSelfFromRoot(root2D);
            }
        }
    }

    void tileClicked(int x, int y){
        if(game.itsMyTurn(thisPlayer) && turnState == TurnState.NEUTRAL){
            if (verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [tileClicked] during your turn with turn-state neutral");
            Tile tileClicked = map.getTiles()[x][y];
            Unit unitOnTileClicked = tileClicked.getUnitOnTile();
            Building buildingOnTileClicked = tileClicked.getBuildingOnTile();
            if(unitOnTileClicked != null){
                trySelectUnit(unitOnTileClicked);
            } else if(buildingOnTileClicked != null){
                selectBuilding(buildingOnTileClicked);
            }
        }
        else if(turnState == TurnState.UNIT_SELECTED){
            if(selectedUnit.getX() == x && selectedUnit.getY() == y){
                // Deselect unit
                deselectUnit();
            }
            else if(
                lastTileAddedToPathQueue.x == x && lastTileAddedToPathQueue.y == y &&
                map.checkTileForMoveToByUnitPerceived(x, y, selectedUnit, thisPlayerFowVision, true)
            ){
                // Move and don't attack
                onPlayerUnitMoveCommand(selectedUnitQueuedPath, null);
            }

            else { // Attacks

                /*
                MELEE / RANGERMELEE ATTACK
                 */
                if (
                    (Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.MELEE || Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.RANGERMELEE) &&
                    moveCommandGridAttackableSquares[x][y] &&
                        GeometryUtils.getPointToPointDistance(new Point(x, y), lastTileAddedToPathQueue) <= Codex.getUnitProfile(selectedUnit).MAXRANGE &&
                        (
                            map.checkTileForMoveToByUnitPerceived(lastTileAddedToPathQueue.x, lastTileAddedToPathQueue.y,
                                selectedUnit, thisPlayerFowVision, false) ||
                                selectedUnitQueuedPath.isEmpty()
                        )
                ) {
                    // Move (or don't, if path is empty) and then melee attack
                    onPlayerUnitMoveCommand(selectedUnitQueuedPath, new Point(x, y));
                }

                /*
                RANGED ATTACK
                 */
                else if (
                    Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.RANGED &&
                        moveCommandGridAttackableSquares[x][y] &&
                        selectedUnitQueuedPath.isEmpty()
                ){
                    // Path is empty, do a ranged attack
                    onPlayerUnitMoveCommand(selectedUnitQueuedPath, new Point(x, y));
                }
            }

        }
        else if(turnState == TurnState.UNIT_SELECTED_FOR_ATTACK_AFTER_MOVE){
            if(selectedUnit.getX() == x && selectedUnit.getY() == y){
                // Deselect unit
                deselectUnit();
            }
            else if(
                moveCommandGridAttackableSquares[x][y] &&
                    selectedUnit.getUnitState() == UnitState.MOVED_AND_WAITING_FOR_ATTACK
            ){ // Attack
                onPlayerUnitMoveCommand(new ArrayDeque<Point>(), new Point(x, y));
            }
        }
        else if(turnState == TurnState.BUILDING_SELECTED){
            if(selectedBuilding.getX() == x && selectedBuilding.getY() == y){
                // Deselect building
                deselectBuilding();
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
            if(this.cumulativeDeltaUnitStance > TOTAL_UNIT_MOVEMENT_INTERVAL){
                this.cumulativeDeltaUnitStance -= TOTAL_UNIT_MOVEMENT_INTERVAL;

                if(Codex.getUnitProfile(selectedUnit).SUPERTYPE != UnitSuperType.AIRCRAFT_HELICOPTER){
                    selectedUnit.switchStanceOnMove();
                }
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
        double mapMovementUnit = delta * Math.log(map.getTileRenderSize()) * MAP_SCROLL_SPEED;

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
            if(cumulativeDelta > TOTAL_UNIT_MOVEMENT_INTERVAL){
                cumulativeDelta -= TOTAL_UNIT_MOVEMENT_INTERVAL;

                /*TODO Remove this, it is temporary for testing*/
                if(Codex.getUnitProfile(unit).SUPERTYPE != UnitSuperType.AIRCRAFT_HELICOPTER){
                    unit.switchStanceOnMove();
                }
                /* ^ remove */

                UnitState nextState = unit.performFullTileMove(map);
                if(nextState != UnitState.MOVING){
                    unitsMoving.remove(unit);

                    // Add more vision
                    if(unit.getOwnerId() == thisPlayer.getId()){
                        map.setFogOfWarToVision(map.addVisionOnUnitMove(thisPlayerFowVision, unit.getX(), unit.getY(), Codex.getUnitProfile(unit).VISION));
                    }
                    // On vision update, update hovered tile
                    setHoveredTileInfoLabel(tileHovered);

                    if(nextState == UnitState.ATTACKING){
                        onAttackAddFightingUnits(unit);
                    }
                    return;
                }
            } else {
                unit.performInBetweenTileMove(cumulativeDelta / TOTAL_UNIT_MOVEMENT_INTERVAL, map);
            }
            unitsMoving.put(unit, cumulativeDelta);
        }
    }
    private void handleAttackingUnits(double delta){
        for(Unit unit : unitsAttacking.keySet()){
            double cumulativeDelta = unitsAttacking.get(unit);
            cumulativeDelta += delta;
            if(cumulativeDelta > TOTAL_UNIT_ATTACK_INTERVAL){
                boolean wasCounterAttack = unit.getUnitState() == UnitState.COUNTERATTACKING;
                AttackResult attackResult = unit.performFinishAttack(map);
                boolean attackedUnitSurvived = attackResult.attackedUnitSurvived;
                unitsAttacking.remove(unit);

                Unit attackedUnit = unit.getLastAttackedUnit();

                //Explosion particles
                double[] graphicalPositionExplosion = map.getGraphicalPosition(attackedUnit.getX(), attackedUnit.getY());
                if(!attackedUnitSurvived){
                    particleHandler.newParticleExplosion(graphicalPositionExplosion[0], graphicalPositionExplosion[1], map.getTileRenderSize(), 20);
                } else if(!wasCounterAttack){
                    particleHandler.newParticleExplosion(graphicalPositionExplosion[0], graphicalPositionExplosion[1], map.getTileRenderSize(), 8);
                }
                //Explosion sound
                if(!wasCounterAttack){
                    ZoneMediaPlayer mediaPlayerExplosion = new ZoneMediaPlayer(AssetHandler.getSoundExplosion(unit.getUnitType()));
                    mediaPlayerExplosion.play();
                    if(!attackedUnitSurvived){
                        ZoneMediaPlayer mediaPlayerKill = new ZoneMediaPlayer(AssetHandler.getSound("/sounds/battlefeild-1-kill-sound-effect-made-with-Voicemod.mp3"));
                        mediaPlayerKill.play();
                    }
                }
                //Hit (HP change) particle
                particleHandler.newParticleHit(graphicalPositionExplosion[0], graphicalPositionExplosion[1], map.getTileRenderSize(), attackResult.hpChange);

                //Removed attacked unit if it died
                if(!attackedUnitSurvived){
                    map.removeUnit(attackedUnit);
                }
                return;
            } else {
                unit.performAttack(cumulativeDelta / TOTAL_UNIT_ATTACK_INTERVAL, map);
            }
            unitsAttacking.put(unit, cumulativeDelta);
        }
    }

    /**
     * Draw the tile selector over the tile that the mouse is hovering over and adjust for map offset.
     */
    private void moveSelector(){
        tileSelector.setPositionInMap(tileHoveredX, tileHoveredY, map);
    }

    /**
     * Handle all elements that constantly change with passing time
     * E.g. the tile selector changing its image back and forth
     *
     * @param delta time (in seconds) since last update
     */
    private void handlePulsatingElements(double delta){
        tileSelector.updateTickingImage(delta);
        GameObjectTile.updatePulsatingTiles(delta, map.getTiles());
        GameObjectUnit.updatePulsatingAircraft(delta, map.getUnits());
    }

    /**
     * Redraw the UI move command arrow and the UI move command green grid to adjust for map offset.
     */
    private void moveMoveCommandArrowAndGridTiles(){
        if(turnState == TurnState.UNIT_SELECTED){
            for(GameObjectUiMoveCommandArrowTile arrowTile : moveCommandArrowTiles){
                arrowTile.setOffset(map);
            }
            for(GameObjectUiMoveCommandGridTile gridTile : moveCommandGridTiles){
                gridTile.setOffset(map);
            }
        }
    }

    /**
     * Determine the game logical tile of the map that the mouse is hovering over.
     */
    private void handleHoveredTile(){
        Point2D pointHovered = gameController.getInputHandler().getLastMousePosition();
        Point pointHoveredInTileSpace = map.getPointAt(pointHovered.getX(), pointHovered.getY());

        /*
        SUBJECT TO CHANGE
        This check happens every frame and needs to be really fast.
         */
        boolean hoveredTileChanged = !pointHoveredInTileSpace.equals(tileHovered);
        if(map.isInBounds(pointHoveredInTileSpace.x, pointHoveredInTileSpace.y)){
            tileHoveredX = pointHoveredInTileSpace.x;
            tileHoveredY = pointHoveredInTileSpace.y;
            tileHovered = pointHoveredInTileSpace;
            setMousePointerInBounds(true);

            if(hoveredTileChanged){
                setHoveredTileInfoLabel(tileHovered);
            }

        } else {
            setMousePointerInBounds(false);
        }
    }

    private void setMousePointerInBounds(boolean mousePointerInBounds){
        this.mousePointerInBounds = mousePointerInBounds;
        tileSelector.setVisible(mousePointerInBounds);
    }
    private void setHoveredTileInfoLabel(Point hoveredPoint){
        Tile tile = map.getTiles()[hoveredPoint.x][hoveredPoint.y];
        Unit unit = tile.getUnitOnTile();
        Building building = tile.getBuildingOnTile();

        textFlowsBottomUiBar[1][0].getChildren().clear();
        textFlowsBottomUiBar[1][1].getChildren().clear();
        textFlowsBottomUiBar[2][0].getChildren().clear();
        textFlowsBottomUiBar[2][1].getChildren().clear();
        textFlowsBottomUiBar[3][0].getChildren().clear();
        textFlowsBottomUiBar[3][1].getChildren().clear();

        if(unit != null && thisPlayerFowVision[hoveredPoint.x][hoveredPoint.y]){

            Text textUnitName = new Text(Codex.getUnitProfile(unit.getUnitType()).NAME);
            textUnitName.setFont(fontBottomUiBar);
            textFlowsBottomUiBar[1][0].getChildren().add(textUnitName);
            Text textUnitHealth = new Text(Codex.getUnitHealthDigit(unit) + " HP");
            textUnitHealth.setFont(fontBottomUiBar);
            textFlowsBottomUiBar[1][1].getChildren().add(textUnitHealth);
            textUnitHealth.setStyle("-fx-fill: white");

            boolean buildingCapTextVisible = false;

            if(game.playerExists(unit.getOwnerId())){
                textUnitName.setStyle("-fx-fill: "+FxUtils.toRGBCode(game.getPlayer(unit.getOwnerId()).getTextColor()));
            }
            else {
                textUnitName.setStyle("-fx-fill: white");
            }
            if(building != null && building.getOwnerId() != unit.getOwnerId() && Codex.canCapture(unit)){

                buildingCapTextVisible = true;

                String unitColor = ""+(!game.playerExists(unit.getOwnerId())?"white":FxUtils.toRGBCode(game.getPlayer(unit.getOwnerId()).getTextColor()));
                String buildingColor = ""+(!game.playerExists(building.getOwnerId())?"white":FxUtils.toRGBCode(game.getPlayer(building.getOwnerId()).getTextColor()));

                Text textCapturing = new Text("Capturing ");
                Text textCapturedBuildingName = new Text(Codex.BUILDING_NAMES.get(building.getBuildingType()));
                textCapturing.setFont(fontBottomUiBar);
                textCapturedBuildingName.setFont(fontBottomUiBar);
                textCapturing.setStyle("-fx-fill: white");
                textCapturedBuildingName.setStyle("-fx-fill: "+buildingColor);
                textFlowsBottomUiBar[2][0].getChildren().add(textCapturing);
                textFlowsBottomUiBar[2][0].getChildren().add(textCapturedBuildingName);


                Text[] textCapturedBuildingProgress = new Text[6];
                String[] stringsCapturedBuildingProgress = {
                    ""+building.getStatCaptureProgress(),
                    "/",
                    ""+Codex.BUILDING_CAPTURE_TOTAL,
                    " (+",
                    ""+Codex.getUnitHealthDigit(unit),
                    ")"
                };
                String[] colorsCapturedBuildingProgress = {
                    unitColor,
                    "white",
                    buildingColor,
                    "white",
                    unitColor,
                    "white"
                };
                for(int i = 0; i < 6; i++){
                    textCapturedBuildingProgress[i] = new Text(stringsCapturedBuildingProgress[i]);
                    textCapturedBuildingProgress[i].setFont(fontBottomUiBar);
                    textCapturedBuildingProgress[i].setStyle("-fx-fill: "+colorsCapturedBuildingProgress[i]);
                    textFlowsBottomUiBar[2][1].getChildren().add(textCapturedBuildingProgress[i]);
                }

            }
            if(!unit.getTransportLoadedUnits().isEmpty()) {
                Text textTransportedUnits = new Text("\n");
                for(Unit transported : unit.getTransportLoadedUnits()){
                    textTransportedUnits.setText(textTransportedUnits.getText()+Codex.UNIT_PROFILE_VALUES.get(transported.getUnitType()).NAME+"   ");
                    ImageView imgTransportedUnit = new ImageView(AssetHandler.getImageUnit(new KeyUnit(transported.getUnitType(), 0, FxUtils.toAwtColor(game.getPlayer(transported.getOwnerId()).getColor()))));
                    imgTransportedUnit.setFitWidth(96);
                    imgTransportedUnit.setFitHeight(96);
                    textFlowsBottomUiBar[buildingCapTextVisible?3:2][1].getChildren().add(imgTransportedUnit);
                }
                textTransportedUnits.setFont(fontBottomUiBarSmall);
                textTransportedUnits.setStyle("-fx-fill: #a0a0a0");
                textFlowsBottomUiBar[buildingCapTextVisible?3:2][0].getChildren().add(textTransportedUnits);
            }
            else {
                Text textUnitDescription = new Text("\n"+Codex.UNIT_DESCRIPTIONS.get(unit.getUnitType()));
                textUnitDescription.setFont(fontBottomUiBarSmall);
                textUnitDescription.setStyle("-fx-fill: #a0a0a0");
                textFlowsBottomUiBar[buildingCapTextVisible?3:2][0].getChildren().add(textUnitDescription);
            }
        }
        else if(building != null){

            Text textBuildingName = new Text(Codex.BUILDING_NAMES.get(building.getBuildingType()));
            textBuildingName.setFont(fontBottomUiBar);
            textFlowsBottomUiBar[1][0].getChildren().add(textBuildingName);


            if(game.playerExists(building.getOwnerId())){
                textBuildingName.setStyle("-fx-fill: "+FxUtils.toRGBCode(game.getPlayer(building.getOwnerId()).getTextColor()));
            } else {
                textBuildingName.setStyle("-fx-fill: white");
            }

        }
        else{
            Text textTileName = new Text(Codex.TILE_NAMES.get(tile.getTileType()));
            textTileName.setFont(fontBottomUiBar);
            textFlowsBottomUiBar[1][0].getChildren().add(textTileName);
            textTileName.setStyle("-fx-fill: white");

        }

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

                /*
                Magic from Legacy Code
                 */
                Point2D lastMousePosition = gameController.getInputHandler().getLastMousePosition();
                int mouseX = (int) lastMousePosition.getX();
                int mouseY = (int) lastMousePosition.getY();
                double drawOffsetX = map.getOffsetX();
                double drawOffsetY = map.getOffsetY();
                drawOffsetX = (int)(mouseX - (mouseX - drawOffsetX) * newTileRenderSize / (newTileRenderSize - scrollDelta));
                drawOffsetY = (int)(mouseY - (mouseY - drawOffsetY) * newTileRenderSize / (newTileRenderSize - scrollDelta));

                map.setGraphicalOffset(drawOffsetX, drawOffsetY);
            }
        }
    }

    private void adjustUiElementsInTileSpaceOnZoom(){
        tileSelector.changeTileRenderSize(tileHoveredX, tileHoveredY, map);
        if(turnState == TurnState.UNIT_SELECTED){
            for (GameObjectUiMoveCommandArrowTile arrowTile : moveCommandArrowTiles){
                arrowTile.changeTileRenderSize(map);
            }
            for (GameObjectUiMoveCommandGridTile gridTile : moveCommandGridTiles){
                gridTile.changeTileRenderSize(map);
            }
        }
    }

    /**
     * Game logical selection of a unit. On success, change turn state to UNIT_SELECTED and store pointer to selected unit.
     *
     * @param unit unit being selected
     */
    protected void trySelectUnit(Unit unit){
        if (verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [trySelectUnit]");
        if(
            turnState == TurnState.NEUTRAL &&
            unit.getUnitState() == UnitState.NEUTRAL &&
            thisPlayer != null &&
            (thisPlayer.getId() == unit.getOwnerId())
        ){
            selectUnit(unit);
        }
        else if(
            turnState == TurnState.NEUTRAL &&
                unit.getUnitState() == UnitState.BLACKED_OUT &&
                thisPlayer != null &&
                (thisPlayer.getId() == unit.getOwnerId()) &&
                Codex.getTransportCapacity(unit) > 0 &&
                !unit.getTransportLoadedUnits().isEmpty()
        ){
            Unit transportedByClickedUnit = unit.getTransportLoadedUnits().get(0);
            if(transportedByClickedUnit.getUnitState() == UnitState.IN_TRANSPORT && map.checkTileForMoveThroughByUnitFinal(unit.getX(), unit.getY(), transportedByClickedUnit)){
                selectUnit(transportedByClickedUnit);
            }
        }
    }
    private void selectUnit(Unit unit){
        selectedUnit = unit;

        // Initialize unit path queue
        clearSelectedUnitPathQueue();

        // Initialize move command arrow
        moveCommandArrowTiles = new ArrayList<>();

        // Add the first part of the arrow, which is on the tile that the selected unit is standing on
        addPathQueueArrowBase();

        // Calculate the move command grid
        onSelectUnitCalculateMoveCommandGrid();

        // Initialize move command grid graphics
        moveCommandGridTiles = new ArrayList<>();

        // For all squares the selected unit can move to, add a green tile to the move command grid
        // For squares with attackable enemies, add a red tile
        for(int i_x = 0; i_x < moveCommandGridMovableSquares.length; i_x++){
            for(int i_y = 0; i_y < moveCommandGridMovableSquares[i_x].length; i_y++){
                if(moveCommandGridMovableSquares[i_x][i_y] && !moveCommandGridAttackableSquares[i_x][i_y]){
                    moveCommandGridTiles.add(
                        new GameObjectUiMoveCommandGridTile(i_x, i_y, map, root2D, false)
                    );
                }
                if(moveCommandGridAttackableSquares[i_x][i_y]){
                    moveCommandGridTiles.add(
                        new GameObjectUiMoveCommandGridTile(i_x, i_y, map, root2D, true)
                    );
                }
            }
        }

        // To make the unit graphically move - rotating its tracks or walking in place
        selectedUnit.onSelect();

        turnState = TurnState.UNIT_SELECTED;
        if(verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [selectUnit] unit selected");
    }
    protected void selectBuilding(Building building){
        if (verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [selectBuilding] trying");
        if(
            turnState == TurnState.NEUTRAL &&
            thisPlayer != null &&
            (thisPlayer.getId() == building.getOwnerId()) &&
                building.isSelectable()
        ){
            selectedBuilding = building;

            // Show the building UI
            selectedBuildingUI = selectedBuilding.getConstructionMenu();
            adjustSelectedBuildingUI();
            root2D.getChildren().add(selectedBuildingUI);

            // Configure construction menu buttons
            for (ButtonBuildingBuyUnit button : building.getConstructionMenuButtons()){
                button.setOnMouseClicked(mouseEvent -> {
                    if(verbose) System.out.println("[IN-GAME-UI-CONTROLLER] Building button clicked, buy unit "+button.getUnitType());
                    buyUnitButtonClicked(button.getUnitType());
                });
            }


            turnState = TurnState.BUILDING_SELECTED;
            if(verbose) System.out.println("[IN-GAME-UI-CONTROLLER] [selectBuilding] building selected");
        }
    }
    protected void deselectBuilding(){
        turnStateToNeutral();
    }
    protected void deselectUnit(){
        selectedUnit.onDeselect();
        turnStateToNeutral();
    }

    private void buyUnitButtonClicked(UnitType unitType){

        // Check if sufficient cash and creation tile empty
        if(turnState != TurnState.BUILDING_SELECTED || thisPlayer.getStatResourceCash() < Codex.getUnitProfile(unitType).COST || map.getTiles()[selectedBuilding.getX()][selectedBuilding.getY()].hasUnitOnTile()){
            if(verbose) System.err.println("[IN-GAME-UI-CONTROLLER] [buyUnitButtonClicked] Cannot buy unit");
            return;
        }

        Unit createdUnit = new Unit(unitType, selectedBuilding.getX(), selectedBuilding.getY());
        createdUnit.setOwnerId(thisPlayer.getId());
        UnitSerializable createdUnitSerializable = new UnitSerializable(createdUnit);
        onPlayerCreatesUnit(createdUnitSerializable, Codex.getUnitProfile(unitType).COST);
        deselectBuilding();
    }

    private void onSelectUnitCalculateMoveCommandGrid(){
        // Initialize move command grid logic
        moveCommandGridMovableSquares = new boolean[map.getWidth()][map.getHeight()];
        moveCommandGridAttackableSquares = new boolean[map.getWidth()][map.getHeight()];

        //For units in transport, only calculate move grid and skip any attack options
        //Except planes in an aircraft carrier, which can receive a normal command
        if(
            selectedUnit.getUnitState() == UnitState.IN_TRANSPORT &&
                !(Codex.getUnitProfile(selectedUnit).SUPERTYPE == UnitSuperType.AIRCRAFT_PLANE)

        ){
            onSelectUnitCalculateMoveCommandGridRecursive(
                selectedUnit.getX(), selectedUnit.getY(), 1, selectedUnit,
                moveCommandGridMovableSquares, new boolean[map.getWidth()][map.getHeight()], false
            );
            return;
        }

        onSelectUnitCalculateMoveCommandGridRecursive(
            selectedUnit.getX(), selectedUnit.getY(), Codex.getUnitProfile(selectedUnit.getUnitType()).SPEED, selectedUnit,
            moveCommandGridMovableSquares, moveCommandGridAttackableSquares, false
        );

        // Add attacks that are possible from start square without moving
        if(Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.RANGED){
            onSelectUnitCalculateRangedAttackGrid(selectedUnit, moveCommandGridAttackableSquares, false);
        }
        else if (Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.RANGERMELEE ||
        Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.MELEE){
            onCalculateMoveCommandGridAddToAttackGridFromTileRangerMelee(selectedUnit.getX(), selectedUnit.getY(), selectedUnit, moveCommandGridAttackableSquares, false);
        }
    }

    private void onSelectUnitCalculateMoveCommandGridRecursive(int x, int y, int remainingSteps, Unit unit, boolean[][] refArrayMove, boolean[][] refArrayAttack, boolean addAllInRange){
        if(
            (Codex.getUnitProfile(unit).ATTACKTYPE == UnitAttackType.MELEE ||
                Codex.getUnitProfile(unit).ATTACKTYPE == UnitAttackType.RANGERMELEE) &&
                map.checkTileForMoveToByUnitPerceived(x, y, unit, thisPlayerFowVision, false)
        ){
            onCalculateMoveCommandGridAddToAttackGridFromTileRangerMelee(x, y, unit, refArrayAttack, addAllInRange);
        }
        if(remainingSteps > 0){
            if(map.checkTileForMoveThroughByUnitPerceived(x, y-1, unit, thisPlayerFowVision)){
                refArrayMove[x][y-1] = true;
                onSelectUnitCalculateMoveCommandGridRecursive(x, y-1, remainingSteps-1, unit, refArrayMove, refArrayAttack, addAllInRange);
            }
            if(map.checkTileForMoveThroughByUnitPerceived(x-1, y, unit, thisPlayerFowVision)){
                refArrayMove[x-1][y] = true;
                onSelectUnitCalculateMoveCommandGridRecursive(x-1, y, remainingSteps-1, unit, refArrayMove, refArrayAttack, addAllInRange);
            }
            if(map.checkTileForMoveThroughByUnitPerceived(x, y+1, unit, thisPlayerFowVision)){
                refArrayMove[x][y+1] = true;
                onSelectUnitCalculateMoveCommandGridRecursive(x, y+1, remainingSteps-1, unit, refArrayMove, refArrayAttack, addAllInRange);
            }
            if(map.checkTileForMoveThroughByUnitPerceived(x+1, y, unit, thisPlayerFowVision)){
                refArrayMove[x+1][y] = true;
                onSelectUnitCalculateMoveCommandGridRecursive(x+1, y, remainingSteps-1, unit, refArrayMove, refArrayAttack, addAllInRange);
            }
        }
    }
    private void onCalculateMoveCommandGridAddToAttackGridFromTileRangerMelee(int x, int y, Unit unit, boolean[][] refArray, boolean addAllInRange){
        for(Point p : GeometryUtils.getPointsInRange(Codex.getUnitProfile(unit).MAXRANGE)){
            if(
                map.checkTileForAttackByUnit(x+p.x, y+p.y, unit, thisPlayerFowVision) ||
                    (addAllInRange && map.isInBounds(x+p.x, y+p.y))
            ){
                refArray[x+p.x][y+p.y] = true;
            }
        }
    }
    private void onSelectUnitCalculateRangedAttackGrid(Unit unit, boolean[][] refArray, boolean addAllInRange){
        for(Point p : GeometryUtils.getPointsInRange(Codex.getUnitProfile(unit).MAXRANGE)){
            if(
                map.checkTileForAttackByUnit(unit.getX()+p.x, unit.getY()+p.y, unit, thisPlayerFowVision) ||
                (addAllInRange && map.isInBounds(unit.getX()+p.x, unit.getY()+p.y))
            ){
                refArray[unit.getX()+p.x][unit.getY()+p.y] = true;
            }
        }
        for(Point p : GeometryUtils.getPointsInRange(Codex.getUnitProfile(unit).MINRANGE-1)){
            if(map.isInBounds(unit.getX()+p.x, unit.getY()+p.y)){
                refArray[unit.getX()+p.x][unit.getY()+p.y] = false;
            }
        }
    }


    protected void commandUnitToMove(Unit unit, ArrayDeque<Point> path, Point pointToAttack, boolean waitForAttack, boolean enterTransport){
        UnitState unitStateAfterCommand = unit.moveCommand(path, game, pointToAttack, waitForAttack, enterTransport);
        if(unitStateAfterCommand == UnitState.MOVING){
            unitsMoving.put(unit, 0.);
        } else if(unitStateAfterCommand == UnitState.ATTACKING){
            onAttackAddFightingUnits(unit);
        }
        setHoveredTileInfoLabel(tileHovered);
    }
    private void onAttackAddFightingUnits(Unit unit){
        unitsAttacking.put(unit, 0.);
        Unit attackedUnit = unit.getCurrentlyAttackedUnit();
        if(attackedUnit.onAttacked(unit)){
            unitsAttacking.put(attackedUnit, 0.);
        }
    }

    /**
     * The player gives a unit a move command during their turn.
     */
    protected boolean onPlayerUnitMoveCommand(ArrayDeque<Point> path, Point pointToAttack){

        turnStateToNeutral();

        /*
        If the path leads into the fog of war, the unit might not complete the entire path and might be stopped by a
        previously invisible enemy unit.
         */
        boolean wasStopped = verifyPathOnMoveCommand(path);

        /*
        Enter transport
         */
        boolean enterTransport = checkEnterTransportOnMoveCommand(path);

        /*
        If unit is ordered to not attack, but has valid attacks from the tile it goes to, the unit will remain actionable
        and will be able to receive a non-move attack command after moving.
         */
        boolean waitForAttack = checkWaitForAttackOnMoveCommand(path, pointToAttack, wasStopped, enterTransport);

        commandUnitToMove(selectedUnit, path, wasStopped?null:pointToAttack, waitForAttack, enterTransport);

        return wasStopped;
    }
    protected boolean verifyPathOnMoveCommand(ArrayDeque<Point> path){
        if(path.isEmpty()){
            return false;
        }
        boolean wasStopped = false;
        ArrayDeque<Point> trimmedPath = new ArrayDeque<>();


        for(Point p : path){
            if(map.checkTileForMoveThroughByUnitFinal(p.x, p.y, selectedUnit)){
                trimmedPath.add(p);
            } else {
                wasStopped = true;
                break;
            }
        }
        path.clear();
        path.addAll(trimmedPath);


        while(!path.isEmpty()){
            Point pl = path.peekLast();
            if(!map.checkTileForMoveToByUnitPerceived(pl.x, pl.y, selectedUnit, map.getVisionOfGod(), !wasStopped)){
                path.remove(pl);
                wasStopped = true;
            }
            else {
                break;
            }
        }

        return wasStopped;
    }
    protected boolean checkEnterTransportOnMoveCommand(ArrayDeque<Point> path){
        boolean enterTransport = false;
        if(!path.isEmpty() && map.getTiles()[path.peekLast().x][path.peekLast().y].hasUnitOnTile()){
            enterTransport = true;
        }
        return enterTransport;
    }
    protected boolean checkWaitForAttackOnMoveCommand(ArrayDeque<Point> path, Point pointToAttack, boolean wasStopped, boolean enterTransport){
        boolean waitForAttack = false;
        boolean[][] attackableSquaresAfterMove = new boolean[map.getWidth()][map.getHeight()];
        if(
            !wasStopped && pointToAttack == null && !path.isEmpty() && !enterTransport &&
                (Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.MELEE || Codex.getUnitProfile(selectedUnit).ATTACKTYPE == UnitAttackType.RANGERMELEE)
        ){
            for(Point p : GeometryUtils.getPointsInRange(Codex.getUnitProfile(selectedUnit).MAXRANGE)){
                int x = path.peekLast().x + p.x;
                int y = path.peekLast().y + p.y;
                if(map.isInBounds(x, y) && moveCommandGridAttackableSquares[x][y]){
                    waitForAttack = true;
                    attackableSquaresAfterMove[x][y] = true;
                    moveCommandGridTiles.add(
                        new GameObjectUiMoveCommandGridTile(x, y, map, root2D, true)
                    );
                }
            }
        }
        if(waitForAttack){
            turnState = TurnState.UNIT_SELECTED_FOR_ATTACK_AFTER_MOVE;
            moveCommandGridAttackableSquares = attackableSquaresAfterMove;
        }
        return waitForAttack;
    }

    protected void onPlayerCreatesUnit(UnitSerializable unitSerializable, int statPurchasingPrice){
        createUnit(unitSerializable, statPurchasingPrice);
    }
    protected void createUnit(UnitSerializable unitSerializable, int statPurchasingPrice){
        map.createNewUnit(unitSerializable, game);
        payUnitPurchasingPrice(unitSerializable, statPurchasingPrice);

        // Add more vision
        if(unitSerializable.ownerId == thisPlayer.getId()){
            map.setFogOfWarToVision(map.addVisionOnUnitMove(thisPlayerFowVision, unitSerializable.x, unitSerializable.y, Codex.getUnitProfile(unitSerializable.unitType).VISION));
        }
        // On vision update, update hovered tile info
        setHoveredTileInfoLabel(tileHovered);

    }
    protected void payUnitPurchasingPrice(UnitSerializable unitSerializable, int statPurchasingPrice){
        if((statPurchasingPrice != 0) && (unitSerializable.ownerId != 0)){
            Player owner = game.getPlayer(unitSerializable.ownerId);
            if(owner == null){
                System.err.println("[IN-GAME-UI-CONTROLLER] [createUnit] could not find unit owner");
                return;
            }
            owner.setStatResourceCash(owner.getStatResourceCash() - statPurchasingPrice);
            setLabelToPlayer(thisPlayer);
        }
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
        } else if(game.itsMyTurn(thisPlayer) && turnState == TurnState.NO_TURN){
            turnState = TurnState.BEGINNING_TURN;
            onPlayerBeginTurn();
        }
    }

    /**
     * The player demands to end their turn.
     */
    protected void onPlayerEndTurn(){
        endTurn();
    }

    /**
     * The player demands to begin their turn.
     * Only in local games!
     * In online games, the next turn begins automatically on end of previous turn.
     */
    protected void onPlayerBeginTurn(){
        beginTurn();
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
        if(!(turnState == TurnState.ENDING_TURN || turnState == TurnState.NEUTRAL || turnState == TurnState.GAME_OVER)){
            System.err.println("[IN-GAME-UI-CONTROLLER] [endTurn] Bad turn state");
            return;
        }
        if(turnState!=TurnState.GAME_OVER){
            turnStateToNoTurn();
        }
        game.handleEndOfTurnEffects();
        if(game.eliminationCheckup()){
            ArrayList<Player> playersEliminated = game.getPendingEliminatedPlayers();
            if(playersEliminated.contains(thisPlayer)){
                turnStateToGameOver(false, 0);
                return;
            } else if(game.getPlayers().size() < 2){
                turnStateToGameOver(game.playerExists(thisPlayer.getId()), 0);
                return;
            }
        }
        game.goNextTurn();
        beginTurn();
    }
    protected void beginTurn(){
        if(!(turnState == TurnState.NO_TURN || turnState == TurnState.BEGINNING_TURN || turnState == TurnState.GAME_STARTING || turnState == TurnState.GAME_OVER)){
            System.err.println("[IN-GAME-UI-CONTROLLER] [beginTurn] Bad turn state");
            return;
        }
        if(turnState == TurnState.GAME_OVER){
            return;
        }
        map.setVisible(true);
        thisPlayerFowVision = map.getVisionOfPlayer(thisPlayer.getId());
        map.setFogOfWarToVision(thisPlayerFowVision);
        turnStateToNeutral();
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
        if(moveCommandGridTiles != null){
            for(GameObjectUiMoveCommandGridTile gridTile : moveCommandGridTiles){
                gridTile.removeSelfFromRoot(root2D);
            }
        }
        if(selectedBuildingUI != null){
            root2D.getChildren().remove(selectedBuildingUI);
            selectedBuildingUI = null;
        }
        turnState = TurnState.NEUTRAL;
    }
    protected void turnStateToNoTurn(){
        map.setVisible(false);
        turnState = TurnState.NO_TURN;
    }
    protected void turnStateToGameOver(boolean victory, int playerDisplayed){
        if(verbose) System.out.println("[IN-GAME-UI-CONTROLLER] GAME OVER");
        map.setVisible(true);
        map.setFogOfWarToVision(map.getVisionOfGod());
        globalMessageTextFlow.setVisible(true);
        if(victory){
            globalMessageText.setText("VICTORY");
        } else {
            globalMessageText.setText("DEFEAT");
        }
        if(playerDisplayed == 0){
            globalMessageName.setVisible(false);
        } else {
            Player victorDisplayed = game.getPlayer(playerDisplayed);
            globalMessageName.setText("\n"+victorDisplayed.getName());
            globalMessageName.setStyle("-fx-fill: "+FxUtils.toRGBCode(victorDisplayed.getTextColor()));
        }
        turnState = TurnState.GAME_OVER;
    }

    @Override
    public void keyPressed(KeyCode keyCode){
        switch (keyCode){
            case ESCAPE: escapeKeyPressed(); break;
            case K: showRangeKeyChanged(true); break;
            default: break;
        }
    }
    @Override
    public void keyReleased(KeyCode keyCode){
        switch (keyCode){
            case K: showRangeKeyChanged(false); break;
            default: break;
        }
    }
    private void escapeKeyPressed(){
        if(turnState == TurnState.BUILDING_SELECTED){
            deselectBuilding();
        }
        else if(turnState == TurnState.UNIT_SELECTED){
            deselectUnit();
        }
        else if(turnState == TurnState.UNIT_SELECTED_FOR_ATTACK_AFTER_MOVE){
            deselectUnit();
        }
        else if(turnState == TurnState.NEUTRAL || turnState == TurnState.EDITOR){
            toggleEscapeMenu();
        }
    }
    private void showRangeKeyChanged(boolean pressed){
        if(pressed && turnState == TurnState.NEUTRAL && map.getTiles()[tileHoveredX][tileHoveredY].hasUnitOnTile()){
            Unit rangeCheckedUnit = map.getTiles()[tileHoveredX][tileHoveredY].getUnitOnTile();

            boolean[][] rangeCheckGridMovableSquares = new boolean[map.getWidth()][map.getHeight()];
            boolean[][] rangeCheckGridAttackableSquares = new boolean[map.getWidth()][map.getHeight()];

            if(Codex.getUnitProfile(rangeCheckedUnit).ATTACKTYPE == UnitAttackType.RANGED){
                onSelectUnitCalculateRangedAttackGrid(rangeCheckedUnit, rangeCheckGridAttackableSquares, true);
            } else{
                onSelectUnitCalculateMoveCommandGridRecursive(rangeCheckedUnit.getX(), rangeCheckedUnit.getY(), Codex.getUnitProfile(rangeCheckedUnit.getUnitType()).SPEED, rangeCheckedUnit,
                    rangeCheckGridMovableSquares, rangeCheckGridAttackableSquares, true);
            }



            rangeCheckGridTiles = new ArrayList<>();
            for(int i_x = 0; i_x < rangeCheckGridAttackableSquares.length; i_x++){
                for(int i_y = 0; i_y < rangeCheckGridAttackableSquares[i_x].length; i_y++){
                    if(rangeCheckGridAttackableSquares[i_x][i_y]){
                        rangeCheckGridTiles.add(
                            new GameObjectUiMoveCommandGridTile(i_x, i_y, map, root2D, true)
                        );
                    }
                }
            }
        }

        else if(!pressed){
            if(rangeCheckGridTiles != null){
                for(GameObjectUiMoveCommandGridTile gridTile : rangeCheckGridTiles){
                    gridTile.removeSelfFromRoot(root2D);
                }
            }
        }
    }

    private void handleParticleEffects(double delta){
        particleHandler.updateParticles(delta);
    }
}
