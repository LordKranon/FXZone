package fxzone.net.server;

import fxzone.net.AbstractConnectionProtocol;
import fxzone.net.packet.ClientConnectPacket;
import fxzone.net.packet.EndTurnPacket;
import fxzone.net.packet.GameActionPacket;
import fxzone.net.packet.Packet;
import fxzone.net.packet.TestPacket;
import fxzone.net.packet.UnitMoveCommandPacket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerProtocol extends AbstractConnectionProtocol {

    private Server server;

    /*
    DEBUG
     */
    private static final boolean verbose = true;

    public ServerProtocol(Socket socket, Server server) {
        super();
        this.server = server;
        this.socket = socket;
        this.running = false;
        try{
            if(verbose) System.out.println("[SERVER-PROTOCOL] try");
            this.in = new ObjectInputStream(socket.getInputStream());
            if(verbose) System.out.println("[SERVER-PROTOCOL] InputStream connected");
            this.out = new ObjectOutputStream(socket.getOutputStream());
            if(verbose) System.out.println("[SERVER-PROTOCOL] OutputStream connected");
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
            case GAME_ACTION: gameActionPacketReceived((GameActionPacket) packet); break;
            default: unknownPacketReceived(packet); break;
        }
    }

    @Override
    protected void onSocketClosed() {
        server.connectionProtocolHasClosed(this);
    }

    private void clientConnectPacketReceived(ClientConnectPacket clientConnectPacket){
        if(verbose) System.out.println("[SERVER-PROTOCOL] Received client connect packet");
        server.clientConnected(this, clientConnectPacket.getPlayer());
    }

    private void testPacketReceived(TestPacket testPacket){
        if(verbose) System.out.println("[SERVER-PROTOCOL] Received test message packet:");
        if(verbose) System.out.println(testPacket.getMessage());
    }

    private void gameActionPacketReceived(GameActionPacket gameActionPacket){
        if(verbose) System.out.println("[SERVER-PROTOCOL] Received game action packet");
        server.gameActionByClient(gameActionPacket);
    }

    private void unknownPacketReceived(Packet packet){
        if(verbose) System.err.println("[SERVER-PROTOCOL] Received unknown packet");
    }
}
