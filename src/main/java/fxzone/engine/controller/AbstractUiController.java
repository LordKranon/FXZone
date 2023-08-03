package fxzone.engine.controller;

import fxzone.config.Config;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import fxzone.engine.Initializable;

public abstract class AbstractUiController implements Initializable{

    private final SubScene subScene2D;


    public AbstractUiController(AbstractGameController gameController){

        Group root2D = new Group();
        root2D.setDepthTest(DepthTest.ENABLE);

        subScene2D = new SubScene(root2D,
            Config.getInt("WINDOW_WIDTH"), Config.getInt("WINDOW_HEIGHT"),
            false,
            Config.getBool("ANTIALIASING") ? SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED);

        this.init(gameController, root2D);
    }


    public void attachToRoot(Group gameRoot) {
        gameRoot.getChildren().addAll(subScene2D);
    }
}
