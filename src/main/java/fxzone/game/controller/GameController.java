package fxzone.game.controller;

import fxzone.engine.controller.AbstractGameController;
import javafx.stage.Stage;

public class GameController extends AbstractGameController {

    public GameController(Stage stage){
        super(stage);
        super.setActiveUiController(new MainMenuUiController(this));
    }
}
