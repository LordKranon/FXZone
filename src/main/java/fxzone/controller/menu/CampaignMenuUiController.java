package fxzone.controller.menu;

import fxzone.config.Config;
import fxzone.controller.ingame.InGameLocalUiController;
import fxzone.controller.ingame.InGameVsAiUiController;
import fxzone.controller.lobby.LobbyHostUiController;
import fxzone.controller.lobby.LobbyLocalUiController;
import fxzone.controller.menu.PlayMenuUiController.PlayMenuUiControllerFxml;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Codex;
import fxzone.game.logic.Game.GameMode;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.save.Save;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CampaignMenuUiController extends AbstractUiController {

    AbstractGameController gameController;

    public CampaignMenuUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        this.gameController = gameController;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CampaignMenuView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                return new CampaignMenuUiControllerFxml(gameController);
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeOuter(AnchorPane anchorPane){
        GridPane gridPaneOuter = (GridPane) anchorPane.getChildren().get(0);
        GridPane gridPaneInner = (GridPane) gridPaneOuter.getChildren().get(2);
        VBox vBox = (VBox) gridPaneInner.getChildren().get(1);
        HBox hBox = (HBox) vBox.getChildren().get(0);

        double uiSizeInGameMenus = Config.getDouble("UI_SIZE_IN_GAME_MENUS");
        double fontSize = ((uiSizeInGameMenus) / 2.5);

        putButton(vBox, 0, 4*uiSizeInGameMenus, fontSize, 0);
        for(int h = 0; h < Codex.TOTAL_CAMPAIGN_COLUMNS; h++) {
            VBox vBoxInner = (VBox) hBox.getChildren().get(h);
            for (int i = 0; i < Codex.TOTAL_CAMPAIGN_MISSIONS_PER_COLUMN; i++) {
                int missionNumber = i + h * Codex.TOTAL_CAMPAIGN_MISSIONS_PER_COLUMN + 1;
                putButton(vBoxInner, i,4*uiSizeInGameMenus, fontSize, missionNumber);
            }
        }
    }
    private void putButton(Pane container, int position, double prefWidth, double fontSize, int missionNumber){
        Button button = new Button("MISSION " + missionNumber);
        button.setPrefWidth(prefWidth);
        button.setVisible(true);

        if(missionNumber > Config.getInt("GAME_PROGRESS_HIGHEST_CAMPAIGN_MISSION_BEATEN") + 1){
            button.setStyle("-fx-text-fill: #505050; -fx-font-size:"+fontSize);
        } else if(missionNumber <= Config.getInt("GAME_PROGRESS_HIGHEST_CAMPAIGN_MISSION_BEATEN")){
            button.setStyle("-fx-text-fill: #386538; -fx-font-size:"+fontSize);
        } else if(missionNumber == Codex.TOTAL_CAMPAIGN_COLUMNS * Codex.TOTAL_CAMPAIGN_MISSIONS_PER_COLUMN){
            button.setStyle("-fx-text-fill: #ff4040; -fx-font-size:"+fontSize);
            button.setText("FINAL MISSION");
        } else {
            button.setStyle("-fx-font-size:"+fontSize);
        }

        button.setOnMouseClicked(mouseEvent -> {
            missionClicked(missionNumber);
        });
        container.getChildren().add(position, button);
    }

    class CampaignMenuUiControllerFxml{
        private final AbstractGameController gameController;

        public CampaignMenuUiControllerFxml(AbstractGameController gameController) {
            this.gameController = gameController;
        }

        @FXML
        AnchorPane anchorPane;

        @FXML
        public void initialize(){
            resize(anchorPane, gameController.getStage());
            initializeOuter(anchorPane);
        }

        @FXML
        private void back(){
            gameController.setActiveUiController(new PlayMenuUiController(gameController));
        }
    }

    private void missionClicked(int mission){
        if(mission > Config.getInt("GAME_PROGRESS_HIGHEST_CAMPAIGN_MISSION_BEATEN") + 1){
            System.err.println("[CAMPAIGN-MENU-UI-CONTROLLER] [missionClicked] ERROR Mission not unlocked yet");
            return;
        }
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(new Player("You", Color.RED, 1, "arma"));
        playerList.add(Codex.getEnemyPlayerOfCampaignMission(mission));

        MapSerializable loadedMap = Save.loadMap("campaign_"+mission);
        if(loadedMap == null){
            System.err.println("[CAMPAIGN-MENU-UI-CONTROLLER] [missionClicked] ERROR Could not load map on game start");
            return;
        }

        GameSerializable gameSerializable = new GameSerializable(loadedMap, playerList, GameMode.CAMPAIGN, Codex.getCustomGameRulesOfCampaign(mission));

        gameController.setActiveUiController(new InGameVsAiUiController(gameController, gameSerializable, mission));
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }
}
