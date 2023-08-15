package fxzone.net.client;

import fxzone.config.Config;
import fxzone.net.packet.ClientConnectPacket;

public class Client extends Thread{

    private ClientProtocol clientProtocol;

    private String ip;

    private int port;

    public void connectToServer(String ip, int port){
        this.ip = ip;
        this.port = port;
        start();

    }

    /**
     * Execute the connection to the server in a separate Thread as not to halt the FX window/application
     */
    public void run(){
        System.out.println("[CLIENT] connectToServer()");
        this.clientProtocol = new ClientProtocol(ip, port);
        System.out.println("[CLIENT] ClientProtocol created");
        this.clientProtocol.start();
        System.out.println("[CLIENT] ClientProtocol started");
    }

    public void sendClientConnectPacket(){
        clientProtocol.sendPacket(new ClientConnectPacket());
    }
}
