package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InGameUiController extends AbstractUiController {

    Group root2D;

    /**
     * Used in secondsPrinter.
     */
    private double cumulativeDelta = 0;

    private ImageView testUnitView;

    public InGameUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        Image testTile = AssetHandler.getImage("/images/terrain/tiles/tile_plains.png", 24, 24);
        ImageView testTileView = new ImageView(testTile);
        testTileView.setFitWidth(128);
        testTileView.setFitHeight(128);

        Image testUnit = AssetHandler.getImage("/images/icon_tank_blue.png", 128, 128);
        testUnitView = new ImageView(testUnit);
        testUnitView.setFitWidth(128);
        testUnitView.setFitHeight(128);


        root2D.getChildren().add(testTileView);
        root2D.getChildren().add(testUnitView);
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
            testUnitView.setX(testUnitView.getX() + 128);
        }
    }
}
