package fxzone.net.packet;

import java.io.Serializable;

public class Packet implements Serializable {

    private static final long serialVersionUID = 1L;

    private final PacketType packetType;

    public Packet(PacketType packetType){
        this.packetType = packetType;
    }

    public PacketType getPacketType(){
        return packetType;
    }
}
