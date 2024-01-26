package fxzone.net.packet;

import fxzone.game.logic.Player;

public class EndTurnPacket extends GameActionPacket{

    public EndTurnPacket() {
        super(PacketType.END_TURN);
    }
}
