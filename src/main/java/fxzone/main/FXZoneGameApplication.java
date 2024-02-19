package fxzone.main;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import fxzone.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class FXZoneGameApplication extends Application {



    private GameController gameController;



    public static void init(String[] args){
        Config.loadConfig();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        gameController = new GameController(stage, this);
        stage.setTitle("FXZone");
        try {
            stage.getIcons().add(AssetHandler.getImage("/images/icon_tank_red.png"));
        } catch (Throwable e) {
            e.printStackTrace();
            System.err.println("[FX-ZONE-GAME-APPLICATION] Couldn't load icon");
        }

        stage.show();
    }

    @Override
    public void stop(){
        gameController.close();
        Config.saveConfig();
        System.exit(0);
    }
}
