package fxzone.controller.ingame;

import fxzone.config.Config;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.game.logic.Map;
import fxzone.game.logic.Tile;
import fxzone.game.logic.Unit;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.game.render.GameObjectInTileSpace;
import fxzone.game.render.GameObjectUnit;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;

public class InGameUiController extends AbstractUiController {

    private Group root2D;

    private final AbstractGameController gameController;

    /**
     * Used in secondsPrinter.
     */
    private double cumulativeDelta = 0;

    private Map map;

    /**
     * Game logical tile of the map that the mouse pointer is hovering over.
     */
    private int tileHoveredX = 0, tileHoveredY = 0;

    /**
     * Small indicator that marks the tile that the mouse pointer is hovering over.
     */
    private GameObjectInTileSpace tileSelector;

    public InGameUiController(AbstractGameController gameController, MapSerializable initialMap) {
        super(gameController);
        this.gameController = gameController;
        initializeMap(initialMap);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {

        this.root2D = root2D;

        //DummyGameObject tank = new DummyGameObject(AssetHandler.getImageUnitVehicle(new KeyUnitVehicle("car", 0)), 0, 0, 128, 128, root2D);
        //DummyGameObject tile = new DummyGameObject("/images/terrain/tiles/tile_plains.png", 0, 0, 128, 128, root2D);

        //GameObjectUnit tank = new GameObjectUnit("tank", 0, 0, 128, root2D);

        //tileSelector = new GameObjectInTileSpace(AssetHandler.getImage("/images/misc/selector.png"), 0, 0, 128, root2D);
        //tileSelector.setViewOrder(-1);
        createTileSelector();

        /*
        map = new Map(5, 3, root2D);

        Unit tank = new Unit("tank", 1, 1, map.getTileRenderSize(), root2D);
        map.addUnit(tank);

        map.addUnit(new Unit("hunter_tank", 2, 1, 0, root2D));
        map.addUnit(new Unit("artillery", 3, 1, 0, root2D));
        map.addUnit(new Unit("tank", 5, 3, 0, root2D));
        */

        //tank.setViewOrder(-1);
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {
        //System.out.println("[InGameUiController] update()");
        //secondsPrinter(delta);
        moveMap(delta);
        zoomMap();
        findHoveredTile();
        moveSelector();
    }

    protected void createTileSelector(){
        tileSelector = new GameObjectInTileSpace(AssetHandler.getImage("/images/misc/selector.png"), 0, 0, 128, root2D);
        tileSelector.setViewOrder(-1);
    }

    protected void initializeMap(MapSerializable initialMap){
        map = new Map(initialMap, root2D);
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
            Tile hoveredTile = map
                .getTileAt(gameController.getInputHandler().getLastMousePosition().getX(),
                    gameController.getInputHandler().getLastMousePosition().getY());
            tileHoveredX = hoveredTile.getX();
            tileHoveredY = hoveredTile.getY();
        }
        catch (ArrayIndexOutOfBoundsException ignored){

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

                /*
                Also adjust size of selector
                */
                tileSelector.changeTileRenderSize(tileHoveredX, tileHoveredY, map);
            }
        }
    }
}
