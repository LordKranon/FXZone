package fxzone.net.server;

import fxzone.net.AbstractConnectionProtocol;
import fxzone.net.packet.Packet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerProtocol extends AbstractConnectionProtocol {

    public ServerProtocol(Socket socket) {
        super();
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

    }
}
