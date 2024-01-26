package fxzone.net.packet;

public abstract class GameActionPacket extends Packet{

    private final PacketType gameActionSpecification;

    public GameActionPacket(PacketType gameActionSpecification) {
        super(PacketType.GAME_ACTION);
        this.gameActionSpecification = gameActionSpecification;
    }

    public PacketType getGameActionSpecification(){
        return gameActionSpecification;
    }
}
