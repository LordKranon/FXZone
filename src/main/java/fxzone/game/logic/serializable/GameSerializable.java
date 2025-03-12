package fxzone.game.logic.serializable;

import fxzone.game.logic.Game;
import fxzone.game.logic.Game.GameMode;
import fxzone.game.logic.Player;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameSerializable implements Serializable {

    private static final long serialVersionUID = 1L;

    public ArrayList<PlayerSerializable> players;

    public MapSerializable map;

    public GameMode gameMode;


    public GameSerializable(MapSerializable mapSerializable, ArrayList<Player> players, GameMode gameMode){
        serializePlayerList(players);
        this.map = mapSerializable;
        this.gameMode = gameMode;
    }

    private void serializePlayerList(ArrayList<Player> playerList){
        this.players = new ArrayList<>();
        for (Player player : playerList){
            PlayerSerializable playerSerializable = new PlayerSerializable(player);
            this.players.add(playerSerializable);
        }
    }
}
