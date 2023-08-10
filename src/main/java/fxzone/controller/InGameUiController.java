package fxzone.controller;

import fxzone.config.Config;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.game.DummyGameObject;
import fxzone.game.logic.Map;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class InGameUiController extends AbstractUiController {

    private Group root2D;

    private final AbstractGameController gameController;

    /**
     * Used in secondsPrinter.
     */
    private double cumulativeDelta = 0;

    private Map map;

    public InGameUiController(AbstractGameController gameController) {
        super(gameController);
        this.gameController = gameController;
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {

        this.root2D = root2D;

        DummyGameObject tank = new DummyGameObject("/images/icon_tank_blue.png", 0, 0, 128, 128, root2D);
        //DummyGameObject tile = new DummyGameObject("/images/terrain/tiles/tile_plains.png", 0, 0, 128, 128, root2D);
        map = new Map(5, 3, root2D);

        tank.setViewOrder(-1);
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {
        //System.out.println("[InGameUiController] update()");
        //secondsPrinter(delta);
        moveMap(delta);
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

        if(gameController.getInputHandler().isKeyPressed(KeyCode.RIGHT)){
            totalExtraOffsetX -= mapMovementUnit;
        }
        if(gameController.getInputHandler().isKeyPressed(KeyCode.LEFT)){
            totalExtraOffsetX += mapMovementUnit;
        }
        if(gameController.getInputHandler().isKeyPressed(KeyCode.UP)){
            totalExtraOffsetY += mapMovementUnit;
        }
        if(gameController.getInputHandler().isKeyPressed(KeyCode.DOWN)){
            totalExtraOffsetY -= mapMovementUnit;
        }

        map.setGraphicalOffset(map.getOffsetX() + totalExtraOffsetX, map.getOffsetY() + totalExtraOffsetY);
    }
}
