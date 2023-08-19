package fxzone.net.packet;

import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.PlayerSerializable;

public class ClientConnectPacket extends Packet{

    private PlayerSerializable playerSerializable;

    public ClientConnectPacket(Player player) {
        super(PacketType.CLIENT_CONNECT);
        this.playerSerializable = new PlayerSerializable(player);
    }

    public Player getPlayer(){
        return new Player(playerSerializable);
    }
}
