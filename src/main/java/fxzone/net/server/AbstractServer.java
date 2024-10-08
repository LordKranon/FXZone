package fxzone.net.server;

import fxzone.config.Config;
import fxzone.net.packet.Packet;
import fxzone.net.packet.TestPacket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractServer extends Thread{

    private ServerSocket serverSocket;

    private boolean running;

    private final List<ServerProtocol> clients;

    protected final boolean verbose = false;

    AbstractServer(){
        this.running = false;
        this.clients = new ArrayList<>();
    }

    public void run(){
        running = true;
        if (verbose) System.out.println("[SERVER] Server started");
        try {
            serverSocket = new ServerSocket(Config.getInt("SERVER_PORT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(running){
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
            }
            catch (SocketException e){
                if (verbose) System.out.println("[SERVER] Socket exception. Sure hope the Socket is closed intentionally. Server is being closed.");
                running = false;
                return;
            }
            catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (verbose) System.out.println("[SERVER] Client socket accepted");
            ServerProtocol serverProtocol = createServerProtocol(clientSocket);
            clients.add(serverProtocol);
            try{
                serverProtocol.start();
            } catch (Throwable e){
                e.printStackTrace();
                clients.remove(serverProtocol);
            }
        }
    }

    protected abstract ServerProtocol createServerProtocol(Socket socket);

    protected void connectionProtocolHasClosed(ServerProtocol serverProtocol){
        clients.remove(serverProtocol);
    }

    public void sendTestMessageToAll(String message){
        if (verbose) System.out.println("[SERVER] Sending test message to all");
        sendPacketToAll(new TestPacket(message));
    }

    public void sendPacketToAll(Packet packet){
        if (verbose) System.out.println("[SERVER] Sending packet to all");
        sendPacketTo(clients, packet);
    }

    public void sendPacketTo(List<ServerProtocol> serverProtocols, Packet packet){
        for (ServerProtocol serverProtocol : serverProtocols){
            serverProtocol.sendPacket(packet);
        }
    }

    public void sendPacketTo(ServerProtocol serverProtocol, Packet packet){
        serverProtocol.sendPacket(packet);
    }

    public void stopServerRaw(){
        if (verbose) System.out.println("[SERVER] Stopping server RAW");
        this.running = false;
        for(ServerProtocol serverProtocol : clients){
            serverProtocol.stopConnectionRaw();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            if (verbose) System.out.println("[SERVER] Exception on intentional server-socket close");
            e.printStackTrace();
        }
    }
}
