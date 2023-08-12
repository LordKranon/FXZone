package fxzone.net.server;

import fxzone.net.packet.Packet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AbstractServerProtocol extends Thread{

    private final Socket socket;

    private boolean running;

    private ObjectInputStream in;

    private ObjectOutputStream out;

    public AbstractServerProtocol(Socket socket){
        this.socket = socket;
        this.running = false;
        try{
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e){
            e.printStackTrace();
        }
        try{
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        running = true;
        Packet packet;
        try{
            while (running){
                packet = (Packet) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
