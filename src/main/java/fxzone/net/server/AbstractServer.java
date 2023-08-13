package fxzone.net.server;

import fxzone.config.Config;
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
        try{
            while(running){
                serverSocket = new ServerSocket(Config.getInt("SERVER_PORT"));
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
}
