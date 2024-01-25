package fxzone.net.packet;

import fxzone.game.logic.Player;

public class EndTurnPacket extends Packet{

    public EndTurnPacket() {
        super(PacketType.END_TURN);
    }
}
