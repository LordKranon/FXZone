package fxzone.net.server;

import fxzone.controller.LobbyHostUiController;
import fxzone.game.logic.Player;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javafx.scene.paint.Color;

public class Server extends AbstractServer{

    private HashMap<ServerProtocol, Player> players;

    private LobbyHostUiController lobbyHostUiController;

    public Server(){
        super();
        this.players = new HashMap<>();
    }

    @Override
    protected ServerProtocol createServerProtocol(Socket socket) {
        System.out.println("[SERVER] Creating ServerProtocol");
        return new ServerProtocol(socket, this);
    }

    /**
     * Clients are treated as fully connected once they've sent a ClientConnectPacket.
     * Then that client will be added as a player.
     */
    public void clientConnected(ServerProtocol serverProtocol){
        Player player = new Player(Color.web("#ff0000"));
        players.put(serverProtocol, player);
        lobbyHostUiController.playerJoinedLobby(player);
    }

    public void setLobbyHostUiController(LobbyHostUiController lobbyHostUiController){
        this.lobbyHostUiController = lobbyHostUiController;
    }

    public Collection<Player> getPlayers(){
        return players.values();
    }
}
