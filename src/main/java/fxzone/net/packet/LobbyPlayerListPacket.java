package fxzone.net.packet;

import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.PlayerSerializable;
import java.util.ArrayList;
import java.util.Collection;

public class LobbyPlayerListPacket extends Packet{

    private ArrayList<PlayerSerializable> playersSerializable;

    public LobbyPlayerListPacket(Collection<Player> players){
        super(PacketType.LOBBY_PLAYER_LIST);
        this.playersSerializable = new ArrayList<>();
        for (Player player : players){
            playersSerializable.add(new PlayerSerializable(player));
        }
    }

    public Collection<PlayerSerializable> getPlayersSerializable(){
        return playersSerializable;
    }

    public ArrayList<Player> getPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        for (PlayerSerializable playerSerializable : playersSerializable){
            players.add(new Player(playerSerializable));
        }
        return players;
    }
}
