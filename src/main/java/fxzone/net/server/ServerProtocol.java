package fxzone.net.server;

import fxzone.net.AbstractConnectionProtocol;
import fxzone.net.packet.ClientConnectPacket;
import fxzone.net.packet.Packet;
import fxzone.net.packet.TestPacket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerProtocol extends AbstractConnectionProtocol {

    private Server server;

    public ServerProtocol(Socket socket, Server server) {
        super();
        this.server = server;
        this.socket = socket;
        this.running = false;
        try{
            System.out.println("[SERVER-PROTOCOL] try");
            this.in = new ObjectInputStream(socket.getInputStream());
            System.out.println("[SERVER-PROTOCOL] InputStream connected");
            this.out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("[SERVER-PROTOCOL] OutputStream connected");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void receivePacket(Packet packet) {
        switch (packet.getPacketType()){
            case TEST: testPacketReceived((TestPacket) packet); break;
            case CHAT_MESSAGE: break;
            case CLIENT_CONNECT: clientConnectPacketReceived((ClientConnectPacket) packet); break;
            default: unknownPacketReceived(packet); break;
        }
    }

    private void clientConnectPacketReceived(ClientConnectPacket clientConnectPacket){
        System.out.println("[SERVER-PROTOCOL] Received client connect packet");
        server.clientConnected(this);
    }

    private void testPacketReceived(TestPacket testPacket){
        System.out.println("[SERVER-PROTOCOL] Received test message packet:");
        System.out.println(testPacket.getMessage());
    }

    private void unknownPacketReceived(Packet packet){
        System.out.println("[SERVER-PROTOCOL] Received unknown packet");
    }
}
