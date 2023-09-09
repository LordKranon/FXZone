package fxzone.game.logic;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private int whoseTurn;

    private int amountPlayers = 0;

    private final ArrayList<Player> players;

    public Game(){
        this.players = new ArrayList<>();
    }

    public Game(List<Player> players){
        this.players = new ArrayList<>();
        for(Player player : players){
            addPlayer(player);
        }
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
        whoseTurn += 1;
        if (whoseTurn >= amountPlayers){
            whoseTurn = 0;
        }
    }
}
