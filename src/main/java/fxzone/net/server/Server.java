package fxzone.net.server;

import fxzone.controller.ingame.InGameHostUiController;
import fxzone.controller.lobby.LobbyHostUiController;
import fxzone.controller.ServerHostController;
import fxzone.game.logic.Map;
import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.net.packet.GameStartPacket;
import fxzone.net.packet.Packet;
import java.awt.Point;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Queue;

public class Server extends AbstractServer{

    private final HashMap<ServerProtocol, Player> players;

    private ServerHostController serverHostController;


    public Server(){
        super();
        this.players = new HashMap<>();
    }

    @Override
    protected ServerProtocol createServerProtocol(Socket socket) {
        if (verbose) System.out.println("[SERVER] Creating ServerProtocol");
        return new ServerProtocol(socket, this);
    }

    @Override
    protected void connectionProtocolHasClosed(ServerProtocol serverProtocol) {
        super.connectionProtocolHasClosed(serverProtocol);
        if (verbose) System.out.println("[SERVER] Removing disconnected player");
        players.remove(serverProtocol);
        serverHostController.lobbyPlayerListChanged();
    }

    /**
     * Clients are treated as fully connected once they've sent a ClientConnectPacket.
     * Then that client will be added as a player.
     */
    public void clientConnected(ServerProtocol serverProtocol, Player player){
        if(serverHostController.playerJoinedLobby(player)){
            players.put(serverProtocol, player);
        }
        else {
            if (verbose) System.out.println("[SERVER] Player tried to join but is rejected. Closing connection with that player.");
            serverProtocol.stopConnectionRaw();
        }
    }

    public void setLobbyHostUiController(LobbyHostUiController lobbyHostUiController){
        this.serverHostController = lobbyHostUiController;
    }

    public void setInGameHostUiController(InGameHostUiController inGameHostUiController){
        this.serverHostController = inGameHostUiController;
    }

    public Collection<Player> getPlayers(){
        return players.values();
    }

    public void sendPacketToAllVerifiedPlayers(Packet packet){
        sendPacketTo(new ArrayList<ServerProtocol>(players.keySet()), packet);
    }

    public boolean startGameForAll(InGameHostUiController inGameHostUiController, GameSerializable gameSerializable){
        sendPacketToAllVerifiedPlayers(new GameStartPacket(gameSerializable));
        this.serverHostController = inGameHostUiController;
        return true;
    }

    public void unitMoveCommandByClient(Point unitPosition, ArrayDeque<Point> path){
        serverHostController.unitMoveCommandByClient(unitPosition, path);
    }
}
