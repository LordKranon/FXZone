package fxzone.net.packet;

import fxzone.game.logic.Map;
import fxzone.game.logic.serializable.MapSerializable;

public class GameStartPacket extends Packet{

    private MapSerializable mapSerializable;

    public GameStartPacket(Map map) {
        super(PacketType.GAME_START);
        this.mapSerializable = new MapSerializable(map);
    }
    public GameStartPacket(MapSerializable mapSerializable){
        super(PacketType.GAME_START);
        this.mapSerializable = mapSerializable;
    }

    public MapSerializable getMapSerializable(){
        return mapSerializable;
    }
}
