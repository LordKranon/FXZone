package fxzone.engine;

import fxzone.engine.controller.AbstractGameController;

public interface Updatable {

    void update(AbstractGameController gameController, double delta);
}
