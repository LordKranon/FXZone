package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.net.server.Server;

public class InGameHostUiController extends InGameUiController{

    private final Server server;

    public InGameHostUiController(AbstractGameController gameController, Server server) {
        super(gameController);
        this.server = server;
        //this.server.setInGameHostUiController(this);
    }
}
