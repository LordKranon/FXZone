package fxzone.engine.controller;

import fxzone.config.Config;
import fxzone.engine.Updatable;
import javafx.fxml.FXML;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import fxzone.engine.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class AbstractUiController implements Initializable, Updatable {

    protected final SubScene subScene2D;


    public AbstractUiController(AbstractGameController gameController){

        Group root2D = new Group();
        root2D.setDepthTest(DepthTest.ENABLE);

        subScene2D = new SubScene(root2D,
            Config.getInt("WINDOW_WIDTH"),
            Config.getInt("WINDOW_HEIGHT"),
            false,
            SceneAntialiasing.DISABLED);

        Stage stage = gameController.getStage();
        subScene2D.heightProperty().bind(stage.heightProperty());
        subScene2D.widthProperty().bind(stage.widthProperty());
        subScene2D.setFill(Color.web("#202020"));

        this.init(gameController, root2D);
    }


    /**
     * Used when this becomes the new active UI controller.
     *
     * @param gameRoot root group of the game controller
     */
    public void attachToRoot(Group gameRoot) {
        gameRoot.getChildren().addAll(subScene2D);
    }

    /**
     * Potentially hacky resize method. Call in initialize() of UI controller implementations.
     */
    @FXML
    public void resize(AnchorPane anchorPane, Stage stage) {
        int initialWidthCorrection = stage.isFullScreen() ? 0 : 16;
        int initialHeightCorrection = stage.isFullScreen() ? 0 : 28;
        anchorPane.setPrefWidth(stage.getWidth() - initialWidthCorrection);
        anchorPane.setPrefHeight(stage.getHeight() - initialHeightCorrection);
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            int widthCorrection = stage.isFullScreen() ? 0 : 16;
            anchorPane.setPrefWidth(stage.getWidth() - widthCorrection);
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            int heightCorrection = stage.isFullScreen() ? 0 : 28;
            anchorPane.setPrefHeight(stage.getHeight() - heightCorrection);
        });
    }
}
