package fxzone.game.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InGameUiController extends AbstractUiController {

    Group root2D;

    public InGameUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        Image testTile = AssetHandler.getImage("/images/terrain/tiles/tile_plains.png", 128, 128);
        ImageView testTileView = new ImageView(testTile);
        testTileView.setFitWidth(128);
        testTileView.setFitHeight(128);

        root2D.getChildren().add(testTileView);
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }
}
