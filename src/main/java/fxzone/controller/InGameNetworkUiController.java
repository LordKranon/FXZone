package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;

public abstract class InGameNetworkUiController extends InGameUiController implements NetworkController{

    public InGameNetworkUiController(AbstractGameController gameController) {
        super(gameController);
    }
}
