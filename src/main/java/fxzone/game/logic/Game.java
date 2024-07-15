package fxzone.game.logic;

import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.PlayerSerializable;
import java.util.ArrayList;
import java.util.HashMap;
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
        this.map = new Map(gameSerializable.map, group, this);
    }

    public void addPlayer(Player player){
        players.add(player);
        amountPlayers += 1;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public Player getPlayer(int playerId){
        for(Player player : players){
            if(player.getId() == playerId){
                return player;
            }
        }
        System.err.println("[GAME] [getPlayer] Player with ID: "+playerId+" not found");
        return null;
    }
    public boolean playerExists(int playerId){
        for(Player player : players){
            if(player.getId() == playerId){
                return true;
            }
        }
        return false;
    }

    public void setWhoseTurn(int whoseTurn){
        this.whoseTurn = whoseTurn;
    }

    public int whoseTurn(){
        return whoseTurn;
    }

    public void goNextTurn(){


        if (verbose) System.out.println("[GAME] [goNextTurn] Player: "+((whoseTurn>=0 && whoseTurn<amountPlayers)?players.get(whoseTurn):"INDETERMINABLE")+" 's turn ended");
        whoseTurn += 1;
        if (whoseTurn >= amountPlayers ||  whoseTurn < 0){
            whoseTurn = 0;
        }
        for(Unit unit : map.getUnits()){
            unit.setActionableThisTurn(true);
        }
        if (verbose) System.out.println("[GAME] [goNextTurn] Player: "+players.get(whoseTurn)+" 's turn begins");
    }

    public boolean itsMyTurn(Player player){
        if (verbose) System.out.println("[GAME] [itsMyTurn?]\n[GAME] [PlayerIsAsking] "+player+"\n[GAME] [PlayerWithTurn] "+players.get(whoseTurn));

        return players.get(whoseTurn).equals(player);
    }

    public Map getMap(){
        return map;
    }

    public void handleEndOfTurnEffects(){
        HashMap<Player, Boolean> playersEliminated = map.handleEndOfTurnEffects(this, players);
        for(Player player : playersEliminated.keySet()){
            if(playersEliminated.get(player)){
                if(verbose) System.out.println("[GAME] "+player+ " ELIMINATED");
                eliminatePlayer(player);
            }
        }
    }

    public void eliminatePlayer(Player player){
        if(!players.remove(player)){
            System.err.println("[GAME] [eliminatePlayer] Could not eliminate "+player);
            return;
        }
        amountPlayers -= 1;
        whoseTurn -= 1;
    }
}
