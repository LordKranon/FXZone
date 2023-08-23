package fxzone.net.client;

import fxzone.controller.ClientJoinedController;
import fxzone.controller.ingame.InGameJoinedUiController;
import fxzone.controller.lobby.LobbyJoinedUiController;
import fxzone.game.logic.Player;
import fxzone.net.packet.ClientConnectPacket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Client extends Thread{

    private ClientProtocol clientProtocol;

    private String ip;

    private int port;

    private ClientJoinedController clientJoinedController;

    /**
     * Indicates the client is currently in the process of creating a connection protocol and connecting to the server.
     */
    private boolean running;

    /**
     * Set to true once a ClientProtocol is connected to the server with no errors.
     */
    private boolean successfullyConnected;

    /**
     * Holds data for the server to create a player.
     */
    private Player playerCreationData;

    public void connectToServer(String ip, int port, Player playerCreationData){
        this.ip = ip;
        this.port = port;
        this.running = true;
        this.playerCreationData = playerCreationData;
        start();
    }

    public void setLobbyJoinedUiController(LobbyJoinedUiController lobbyJoinedUiController){
        this.clientJoinedController = lobbyJoinedUiController;
    }

    public void setInGameJoinedUiController(InGameJoinedUiController inGameJoinedUiController){
        this.clientJoinedController = inGameJoinedUiController;
    }

    /**
     * Execute the connection to the server in a separate Thread as not to halt the FX window/application
     */
    public void run(){
        System.out.println("[CLIENT] connectToServer()");

        try {
            //This command in particular can take time, that's why it's on an extra thread.
            this.clientProtocol = new ClientProtocol(this, ip, port);
        } catch (SocketTimeoutException e){
            this.running = false;
            System.out.println("[CLIENT] Connection timed out. ClientProtocol is not established and this client is closed.");
            return;
        }
        System.out.println("[CLIENT] ClientProtocol created");
        this.clientProtocol.start();
        System.out.println("[CLIENT] ClientProtocol started");
        this.successfullyConnected = true;
        this.running = false;
    }

    /**
     * Called by client protocol when connection to server is closed/lost.
     */
    public void connectionProtocolHasClosed(){
        clientJoinedController.connectionClosed();
    }

    public void sendClientConnectPacket(){
        clientProtocol.sendPacket(new ClientConnectPacket(playerCreationData));
    }

    public void lobbyPlayerListHasUpdated(ArrayList<Player> players){
        clientJoinedController.setLatestPlayerList(players);
        clientJoinedController.lobbyPlayerListChanged();
    }

    public boolean isRunning(){
        return running;
    }

    public boolean isSuccessfullyConnected(){
        return successfullyConnected;
    }

    public void closeConnectionRaw(){
        System.out.println("[CLIENT] Stopping client connection protocol RAW");
        clientProtocol.stopConnectionRaw();
    }

    /**
     * Server has sent notice that the host has started the game from the lobby. Set UI controller to in-game.
     */
    public void gameStart(){
        clientJoinedController.gameStart();
    }
}
