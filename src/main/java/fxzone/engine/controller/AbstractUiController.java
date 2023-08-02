package fxzone.engine.controller;

import fxzone.config.Config;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;

public abstract class AbstractUiController {

    private final SubScene subScene2D;


    public AbstractUiController(){

        Group root2D = new Group();
        root2D.setDepthTest(DepthTest.ENABLE);

        subScene2D = new SubScene(root2D,
            Config.getInt("WINDOW_WIDTH"), Config.getInt("WINDOW_HEIGHT"),
            false,
            Config.getBool("ANTIALIASING") ? SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED);
    }
}
