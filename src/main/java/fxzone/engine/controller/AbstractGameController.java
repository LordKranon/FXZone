package fxzone.engine.controller;

import fxzone.config.Config;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

public abstract class AbstractGameController extends AnimationTimer {


    private AbstractUiController activeUiController;

    private final Stage stage;

    private final Group gameRoot;

    public AbstractGameController(Stage stage){
        gameRoot = new Group();
        Scene scene = new Scene(gameRoot,
            Config.getInt("WINDOW_WIDTH"), Config.getInt("WINDOW_HEIGHT"),
            false,
            Config.getBool("ANTIALIASING") ? SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED);
        stage.setScene(scene);
        this.stage = stage;
    }

    @Override
    public void handle(long currentNanoTime) {

    }

    public void setActiveUiController(AbstractUiController activeUiController){
        System.out.println("[GameController] set new active UI controller");
        this.activeUiController = activeUiController;
        gameRoot.getChildren().clear();
        activeUiController.attachToRoot(gameRoot);
    }


    public Stage getStage(){
        return this.stage;
    }
}
