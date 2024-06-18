package fxzone.game.logic.serializable;

import fxzone.game.logic.Game;
import fxzone.game.logic.Player;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameSerializable implements Serializable {

    private static final long serialVersionUID = 1L;

    public ArrayList<PlayerSerializable> players;

    public MapSerializable map;

    public GameSerializable(Game game){
        serializePlayerList(game.getPlayers());
        this.map = new MapSerializable(game.getMap());
    }
    public GameSerializable(MapSerializable mapSerializable, ArrayList<Player> players){
        serializePlayerList(players);
        this.map = mapSerializable;
    }

    private void serializePlayerList(ArrayList<Player> playerList){
        this.players = new ArrayList<>();
        for (Player player : playerList){
            PlayerSerializable playerSerializable = new PlayerSerializable(player);
            this.players.add(playerSerializable);
        }
    }
}
