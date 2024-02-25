package fxzone.net.packet;

import fxzone.game.logic.serializable.GameSerializable;

public class GameStartPacket extends Packet{

    private GameSerializable gameSerializable;

    /**
     * Indicates to each client/player, which in-game player from the lobby list they are.
     * This way, every client can correctly set their Player "thisPlayer"
     */
    private int playerId;


    public GameStartPacket(GameSerializable gameSerializable, int playerId){
        super(PacketType.GAME_START);
        this.gameSerializable = gameSerializable;
        this.playerId = playerId;
    }

    public GameSerializable getGameSerializable(){
        return gameSerializable;
    }
    public int getPlayerId(){
        return playerId;
    }
}
