package fxzone.net.packet;

public class TestPacket extends Packet{

    private String message;

    public TestPacket(String message) {
        super(PacketType.TEST);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
