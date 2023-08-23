package fxzone.engine.controller;

import fxzone.config.Config;
import fxzone.engine.handler.InputHandler;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

public abstract class AbstractGameController extends AnimationTimer {


    private AbstractUiController activeUiController;

    private final Stage stage;

    private final Group gameRoot;

    private final Application application;



    private long lastNanoTime;

    private int framesCounter;

    private long fpsTimer;

    private final boolean printFps = Config.getBool("PRINT_FPS");



    private final InputHandler inputHandler;



    public AbstractGameController(Stage stage, Application application){
        gameRoot = new Group();
        Scene scene = new Scene(gameRoot,
            Config.getInt("WINDOW_WIDTH"), Config.getInt("WINDOW_HEIGHT"),
            false,
            Config.getBool("ANTIALIASING") ? SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED);
        stage.setScene(scene);
        this.stage = stage;
        this.application = application;
        this.lastNanoTime = System.nanoTime();
        this.fpsTimer = System.currentTimeMillis();
        this.framesCounter = 0;
        this.inputHandler = new InputHandler(scene);
    }

    @Override
    public void handle(long currentNanoTime) {
        // calculate time delta
        double deltaTime = (currentNanoTime - lastNanoTime) / 1e9f;
        lastNanoTime = currentNanoTime;
        framesCounter++;

        // game logic
        //inputHandler.startFrame();
        if (activeUiController != null) {
            activeUiController.update(this, deltaTime);
        }
        //inputHandler.endFrame();

        // FPS cap
        long updateTime = System.nanoTime() - currentNanoTime;
        long wait = ((1000000000 / Config.getInt("MAX_FPS")) - updateTime) / 1000000;

        if (wait > 0) {
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (System.currentTimeMillis() - fpsTimer > 5000) {
            fpsTimer += 5000;
            if (printFps) {
                System.out.println("FPS: " + framesCounter / 5);
            }
            framesCounter = 0;
        }
    }

    public void setActiveUiController(AbstractUiController activeUiController){
        System.out.println("[GAME-CONTROLLER] set new active UI controller");
        this.activeUiController = activeUiController;
        gameRoot.getChildren().clear();
        activeUiController.attachToRoot(gameRoot);
    }


    public Stage getStage(){
        return this.stage;
    }

    public Application getApplication(){
        return this.application;
    }

    public void close(){
        super.stop();
        //TODO
        //activeUiController.onExit();
    }

    public InputHandler getInputHandler(){
        return inputHandler;
    }
}
