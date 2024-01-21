package fxzone.net.packet;

import fxzone.game.logic.Game;
import fxzone.game.logic.Map;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.MapSerializable;

public class GameStartPacket extends Packet{

    private GameSerializable gameSerializable;

    public GameStartPacket(Game game) {
        super(PacketType.GAME_START);
        this.gameSerializable = new GameSerializable(game);
    }
    public GameStartPacket(GameSerializable gameSerializable){
        super(PacketType.GAME_START);
        this.gameSerializable = gameSerializable;
    }

    public GameSerializable getGameSerializable(){
        return gameSerializable;
    }
}
