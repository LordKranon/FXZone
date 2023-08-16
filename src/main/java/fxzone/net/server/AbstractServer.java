package fxzone.net.server;

import fxzone.config.Config;
import fxzone.net.packet.Packet;
import fxzone.net.packet.TestPacket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractServer extends Thread{

    private ServerSocket serverSocket;

    private boolean running;

    private List<ServerProtocol> clients;

    AbstractServer(){
        this.running = false;
        this.clients = new ArrayList<>();
    }

    public void run(){
        running = true;
        System.out.println("[SERVER] Server started");
        try{
            serverSocket = new ServerSocket(Config.getInt("SERVER_PORT"));
            while(running){
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Client socket accepted");
                ServerProtocol serverProtocol = createServerProtocol(clientSocket);
                clients.add(serverProtocol);
                try{
                    serverProtocol.start();
                } catch (Throwable e){
                    e.printStackTrace();
                    clients.remove(serverProtocol);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    protected abstract ServerProtocol createServerProtocol(Socket socket);

    public void sendTestMessageToAll(String message){
        System.out.println("[SERVER] Sending test message to all");
        sendPacketToAll(new TestPacket(message));
    }

    public void sendPacketToAll(Packet packet){
        System.out.println("[SERVER] Sending packet to all");
        for (ServerProtocol serverProtocol : clients){
            serverProtocol.sendPacket(packet);
        }
    }
}
