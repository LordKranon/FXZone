package fxzone.net;

import fxzone.net.packet.Packet;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public abstract class AbstractConnectionProtocol extends Thread{
    protected Socket socket;

    protected boolean running;

    protected ObjectInputStream in;

    protected ObjectOutputStream out;

    private final boolean verbose = false;

    public AbstractConnectionProtocol(){

    }

    @Override
    public void run(){
        running = true;
        Packet packet;
        if(verbose) System.out.println("[CONNECTION-PROTOCOL] started");
        try{
            while (running){
                if(verbose) System.out.println("[CONNECTION-PROTOCOL] try receiving packet");
                packet = (Packet) in.readObject();
                if(verbose) System.out.println("[CONNECTION-PROTOCOL] packet read from InputStream");
                receivePacket(packet);
                if(verbose) System.out.println("[CONNECTION-PROTOCOL] handled packet");
            }
        }
        catch (EOFException e){
            if(verbose) System.out.println("[CONNECTION-PROTOCOL] EOF exception. This might have happened unintentionally. This connection is being closed.");
            running = false;
            onSocketClosed();
        }
        catch (SocketException e){
            if(verbose) System.out.println("[CONNECTION-PROTOCOL] Socket exception. Sure hope the Socket is closed intentionally. This connection is being closed.");
            running = false;
            onSocketClosed();
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    protected abstract void receivePacket(Packet packet);

    /**
     * Called when an exception that requires a socket close occurs while this connection is running.
     */
    protected abstract void onSocketClosed();

    public synchronized void sendPacket(Packet packet){
        try{
            out.writeObject(packet);
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopConnectionRaw(){
        if(verbose) System.out.println("[CONNECTION-PROTOCOL] stopping connection protocol RAW");
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            if(verbose) System.out.println("[CONNECTION-PROTOCOL] Exception on intentional socket close");
            e.printStackTrace();
        }
    }
}
