package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.game.DummyGameObject;
import fxzone.game.logic.Map;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InGameUiController extends AbstractUiController {

    Group root2D;

    /**
     * Used in secondsPrinter.
     */
    private double cumulativeDelta = 0;

    private Map map;

    public InGameUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {



        DummyGameObject tank = new DummyGameObject("/images/icon_tank_blue.png", 0, 0, 128, 128, root2D);
        //DummyGameObject tile = new DummyGameObject("/images/terrain/tiles/tile_plains.png", 0, 0, 128, 128, root2D);
        map = new Map(3, 3, root2D);

        tank.setViewOrder(-1);
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {
        //System.out.println("[InGameUiController] update()");
        secondsPrinter(delta);
    }

    /**
     * Prints a line every second.
     */
    private void secondsPrinter(double delta){
        this.cumulativeDelta += delta;
        if(this.cumulativeDelta > 1){
            System.out.println("[secondPrinter] !!!");
            this.cumulativeDelta -= 1;
            //testUnitView.setX(testUnitView.getX() + 128);
            map.setGraphicalOffset(map.getOffsetX()+32, map.getOffsetY()+16);

        }
    }
}
