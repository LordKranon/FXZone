package fxzone.net.client;

import fxzone.config.Config;
import fxzone.controller.LobbyJoinedUiController;
import fxzone.game.logic.Player;
import fxzone.net.packet.ClientConnectPacket;
import java.util.Collection;

public class Client extends Thread{

    private ClientProtocol clientProtocol;

    private String ip;

    private int port;

    private LobbyJoinedUiController lobbyJoinedUiController;

    public void connectToServer(String ip, int port){
        this.ip = ip;
        this.port = port;
        start();

    }

    public void setLobbyJoinedUiController(LobbyJoinedUiController lobbyJoinedUiController){
        this.lobbyJoinedUiController = lobbyJoinedUiController;
    }

    /**
     * Execute the connection to the server in a separate Thread as not to halt the FX window/application
     */
    public void run(){
        System.out.println("[CLIENT] connectToServer()");
        this.clientProtocol = new ClientProtocol(this, ip, port);
        System.out.println("[CLIENT] ClientProtocol created");
        this.clientProtocol.start();
        System.out.println("[CLIENT] ClientProtocol started");
    }

    public void sendClientConnectPacket(){
        clientProtocol.sendPacket(new ClientConnectPacket());
    }

    public void lobbyPlayerListHasUpdated(Collection<Player> players){
        lobbyJoinedUiController.setLatestPlayerList(players);
        lobbyJoinedUiController.lobbyPlayerListChanged();
    }
}
