package fxzone.game.logic;

import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.PlayerSerializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;

public class Game {

    private int whoseTurn;

    private int amountPlayers = 0;

    private final ArrayList<Player> players;

    private final Map map;

    /*
     * DEBUG
     */
    static final boolean verbose = true;

    public Game(List<Player> players, Map map){
        this.players = new ArrayList<>();
        for(Player player : players){
            addPlayer(player);
        }
        this.map = map;
    }

    public Game(GameSerializable gameSerializable, Group group){
        this.players = new ArrayList<>();
        for(PlayerSerializable playerSerializable : gameSerializable.players){
            addPlayer(new Player(playerSerializable));
        }
        this.map = new Map(gameSerializable.map, group);
    }

    public void addPlayer(Player player){
        players.add(player);
        amountPlayers += 1;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public void setWhoseTurn(int whoseTurn){
        this.whoseTurn = whoseTurn;
    }

    public int whoseTurn(){
        return whoseTurn;
    }

    public void goNextTurn(){
        if (verbose) System.out.println("[GAME] [goNextTurn] Player: "+players.get(whoseTurn)+" 's turn ended");
        whoseTurn += 1;
        if (whoseTurn >= amountPlayers){
            whoseTurn = 0;
        }
        if (verbose) System.out.println("[GAME] [goNextTurn] Player: "+players.get(whoseTurn)+" 's turn begins");
    }

    public boolean itsMyTurn(Player player){
        if (verbose) System.out.println("[GAME] [ItsMyTurn] Player: "+player+"; WhoseTurn: "+players.get(whoseTurn));

        return true;
        //TODO Undebugmode
        //return players.get(whoseTurn).equals(player);
    }

    public Map getMap(){
        return map;
    }
}
