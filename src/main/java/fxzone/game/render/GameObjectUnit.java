package fxzone.game.render;

import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectUnit extends GameObjectInTileSpace{

    public GameObjectUnit(Image image, int x, int y, double tileRenderSize,
        Group group) {
        super(image, x, y, tileRenderSize, group);
    }
}
