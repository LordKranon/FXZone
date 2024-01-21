package fxzone.game.logic.serializable;

import fxzone.game.logic.Game;
import fxzone.game.logic.Player;
import java.io.Serializable;
import java.util.ArrayList;

public class GameSerializable implements Serializable {

    private static final long serialVersionUID = 1L;

    public ArrayList<PlayerSerializable> players;

    public MapSerializable map;

    public GameSerializable(Game game){
        players = new ArrayList<>();
        for (Player player : game.getPlayers()){
            PlayerSerializable playerSerializable = new PlayerSerializable(player);
            players.add(playerSerializable);
        }
        map = new MapSerializable(game.getMap());
    }

}
