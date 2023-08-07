package fxzone.main;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import fxzone.game.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class FXZoneGameApplication extends Application {



    private GameController gameController;



    public static void init(String[] args){
        Config.loadDefaultConfig();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        gameController = new GameController(stage, this);
        stage.setTitle("FXZone");
        try {
            stage.getIcons().add(AssetHandler.getImage("/images/icon_tank_red.png", 128, 128));
        } catch (Throwable e) {
            e.printStackTrace();
            System.err.println("Couldn't load icon!");
        }

        stage.show();
    }

    @Override
    public void stop(){
        gameController.close();
        System.exit(0);
    }
}
