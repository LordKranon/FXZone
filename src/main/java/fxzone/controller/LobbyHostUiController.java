package fxzone.controller;

import fxzone.engine.controller.AbstractGameController;
import fxzone.engine.handler.AssetHandler;
import fxzone.game.logic.Player;
import fxzone.net.server.Server;
import java.util.Collection;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LobbyHostUiController extends LobbyUiController {

    private Server server;

    /**
     * Indicates that the lobby status has updated and needs graphical adjustments.
     */
    private boolean playerListUpdateFlag;

    public LobbyHostUiController(AbstractGameController gameController, Server server) {
        super(gameController);
        this.server = server;
        this.server.setLobbyHostUiController(this);
    }

    @Override
    public void update(AbstractGameController gameController, double delta){
        if(playerListUpdateFlag){
            updatePlayerList(server.getPlayers());
            playerListUpdateFlag = false;
        }
    }

    @Override
    protected void initializeOuter(AnchorPane anchorPane) {
        super.initializeOuter(anchorPane);
        GridPane gridPane = (GridPane) anchorPane.getChildren().get(0);
        Label label = (Label) gridPane.getChildren().get(1);
        label.setText("HOST LOBBY");
    }

    @Override
    protected void startOuter(AbstractGameController gameController) {

    }

    @Override
    protected void sendTestMessageOuter() {
        server.sendTestMessageToAll("[MESSAGE] (Server): Kappa 123");
    }

    private void updatePlayerList(Collection<Player> players){
        vBoxPlayerList.getChildren().clear();
        vBoxIcons.getChildren().clear();
        for (Player player: players){
            addNewPlayerCard();
        }
    }

    private void addNewPlayerCard(){
        Font font = new Font(36);
        Button button = new Button();
        button.setFont(font);
        button.setText("New Player");
        button.setGraphicTextGap(20);
        button.setAlignment(Pos.CENTER);
        button.setMnemonicParsing(false);
        button.setPrefWidth(400);

        vBoxPlayerList.getChildren().add(button);

        ImageView imageView = new ImageView();
        imageView.setImage(AssetHandler.getImage("/images/icon_tank_red.png"));
        imageView.setFitHeight(110);
        imageView.setFitWidth(110);

        vBoxIcons.getChildren().add(imageView);
    }

    public void playerJoinedLobby(Player player){
        playerListUpdateFlag = true;
    }
}