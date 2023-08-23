package fxzone.controller.lobby;

import fxzone.controller.NetworkController;
import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.controller.AbstractUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.FxUtils;
import fxzone.game.logic.Player;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public abstract class LobbyUiController extends AbstractUiController implements NetworkController {

    protected AnchorPane anchorPane;

    protected GridPane gridPaneOuter;
    protected GridPane gridPaneInner;
    protected VBox vBoxPlayerList;
    protected VBox vBoxIcons;

    /**
     * Indicates that the lobby status has updated and needs graphical adjustments.
     */
    protected boolean playerListUpdateFlag;

    public LobbyUiController(AbstractGameController gameController) {
        super(gameController);
    }

    @Override
    public void init(AbstractGameController gameController, Group root2D) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LobbyView.fxml"));
            loader.setControllerFactory(c -> {  //Override the controller factory to pass constructor args
                return new LobbyUiController.LobbyUiControllerFxml(gameController);
            });
            root2D.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AbstractGameController gameController, double delta) {

    }

    class LobbyUiControllerFxml{
        private final AbstractGameController gameController;

        public LobbyUiControllerFxml(AbstractGameController gameController) {
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
        public void start(){
            startOuter(gameController);
        }

        @FXML
        private void quit(){
            quitOuter(gameController);
            //gameController.setActiveUiController(new PlayMenuUiController(gameController));
        }

        @FXML
        private void sendTestMessage(){
            sendTestMessageOuter();
        }
    }

    protected void initializeOuter(AnchorPane anchorPane){
        this.anchorPane = anchorPane;
        this.gridPaneOuter = (GridPane) anchorPane.getChildren().get(0);
        this.gridPaneInner = (GridPane) gridPaneOuter.getChildren().get(2);
        this.vBoxPlayerList = (VBox) gridPaneInner.getChildren().get(1);
        this.vBoxIcons = (VBox) gridPaneInner.getChildren().get(2);
    }

    protected abstract void startOuter(AbstractGameController gameController);

    protected abstract void quitOuter(AbstractGameController gameController);

    protected abstract void sendTestMessageOuter();

    /**
     * Updates the displayed graphical player list.
     * @param players players to show
     */
    protected void updatePlayerList(ArrayList<Player> players){
        vBoxPlayerList.getChildren().clear();
        vBoxIcons.getChildren().clear();
        for (Player player: players){
            addNewPlayerCard(player);
        }
    }

    protected void addNewPlayerCard(Player player){
        Font font = new Font(36);
        Button button = new Button();
        button.setFont(font);
        button.setText(player.getName());
        button.setGraphicTextGap(20);
        button.setAlignment(Pos.CENTER);
        button.setMnemonicParsing(false);
        button.setPrefWidth(400);
        button.setStyle("-fx-text-fill: "+ FxUtils.toRGBCode(player.getColor()) +";");

        vBoxPlayerList.getChildren().add(button);

        ImageView imageView = new ImageView();
        imageView.setImage(AssetHandler.getImage("/images/icon_tank_red.png"));
        imageView.setFitHeight(110);
        imageView.setFitWidth(110);

        vBoxIcons.getChildren().add(imageView);
    }

    public void lobbyPlayerListChanged(){
        this.playerListUpdateFlag = true;
    }
}
