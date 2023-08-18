package fxzone.net.client;

import fxzone.net.AbstractConnectionProtocol;
import fxzone.net.packet.LobbyPlayerListPacket;
import fxzone.net.packet.Packet;
import fxzone.net.packet.TestPacket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientProtocol extends AbstractConnectionProtocol {

    private Client client;

    public ClientProtocol(Client client, String ip, int port) throws SocketTimeoutException {
        super();
        this.client = client;
        this.socket = new Socket();
        this.running = false;
        try{
            System.out.println("[CLIENT-PROTOCOL] try");
            socket.connect(new InetSocketAddress(ip, port), 2000);
            System.out.println("[CLIENT-PROTOCOL] Socket connected");
            this.out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("[CLIENT-PROTOCOL] OutputStream connected");
            this.in = new ObjectInputStream(socket.getInputStream());
            System.out.println("[CLIENT-PROTOCOL] InputStream connected");
        }
        catch (SocketTimeoutException e){
            throw e;
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void receivePacket(Packet packet) {
        switch (packet.getPacketType()){
            case TEST: testPacketReceived((TestPacket) packet); break;
            case CHAT_MESSAGE: break;
            case LOBBY_PLAYER_LIST: lobbyPlayerListPacketReceived((LobbyPlayerListPacket) packet); break;
            default: unknownPacketReceived(packet); break;
        }
    }

    @Override
    protected void onSocketClosed() {

    }

    private void testPacketReceived(TestPacket testPacket){
        System.out.println("[CLIENT-PROTOCOL] Received test message packet:");
        System.out.println(testPacket.getMessage());
    }

    private void unknownPacketReceived(Packet packet){
        System.out.println("[CLIENT-PROTOCOL] Received unknown packet");
    }

    private void lobbyPlayerListPacketReceived(LobbyPlayerListPacket lobbyPlayerListPacket){
        System.out.println("[CLIENT-PROTOCOL] Received lobby player list packet");
        client.lobbyPlayerListHasUpdated(lobbyPlayerListPacket.getPlayers());
    }
}
