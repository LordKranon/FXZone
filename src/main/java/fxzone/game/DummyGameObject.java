package fxzone.game;

import fxzone.engine.render.AbstractGameObject;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class DummyGameObject extends AbstractGameObject {

    public DummyGameObject(Image image, double x, double y, double w, double h, Group group){
        super(image, x, y, w, h, group);
    }
}
