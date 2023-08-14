package fxzone.net;

import fxzone.net.packet.Packet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class AbstractConnectionProtocol extends Thread{
    protected Socket socket;

    protected boolean running;

    protected ObjectInputStream in;

    protected ObjectOutputStream out;

    public AbstractConnectionProtocol(){

    }

    @Override
    public void run(){
        running = true;
        Packet packet;
        System.out.println("[CONNECTION-PROTOCOL] started");
        try{
            while (running){
                System.out.println("[CONNECTION-PROTOCOL] try receiving packet");
                packet = (Packet) in.readObject();
                System.out.println("[CONNECTION-PROTOCOL] packet read from InputStream");
                receivePacket(packet);
                System.out.println("[CONNECTION-PROTOCOL] handled packet");
            }
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    protected abstract void receivePacket(Packet packet);

    public synchronized void sendPacket(Packet packet){
        try{
            out.writeObject(packet);
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
