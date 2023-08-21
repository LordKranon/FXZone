package fxzone.net.server;

import fxzone.controller.LobbyHostUiController;
import fxzone.game.logic.Player;
import fxzone.net.packet.Packet;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javafx.scene.paint.Color;

public class Server extends AbstractServer{

    private final HashMap<ServerProtocol, Player> players;

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

    @Override
    protected void connectionProtocolHasClosed(ServerProtocol serverProtocol) {
        super.connectionProtocolHasClosed(serverProtocol);
        System.out.println("[SERVER] Removing disconnected player");
        players.remove(serverProtocol);
        lobbyHostUiController.lobbyPlayerListChanged();
    }

    /**
     * Clients are treated as fully connected once they've sent a ClientConnectPacket.
     * Then that client will be added as a player.
     */
    public void clientConnected(ServerProtocol serverProtocol, Player player){
        players.put(serverProtocol, player);
        lobbyHostUiController.playerJoinedLobby(player);
    }

    public void setLobbyHostUiController(LobbyHostUiController lobbyHostUiController){
        this.lobbyHostUiController = lobbyHostUiController;
    }

    public Collection<Player> getPlayers(){
        return players.values();
    }

    public void sendPacketToAllVerifiedPlayers(Packet packet){
        sendPacketTo(new ArrayList<ServerProtocol>(players.keySet()), packet);
    }
}
