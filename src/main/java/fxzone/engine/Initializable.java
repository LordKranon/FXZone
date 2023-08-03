package fxzone.engine;

import fxzone.engine.controller.AbstractGameController;
import javafx.scene.Group;

public interface Initializable {

    void init(AbstractGameController gameController, Group root2D);
}
