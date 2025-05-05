package fxzone.game.logic;

import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.game.logic.serializable.PlayerSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.Group;

public class Game {

    private int turnCount = 1;
    private int whoseTurn;

    private int amountPlayers = 0;

    private boolean eliminationsPending = false;
    private ArrayList<Player> pendingEliminatedPlayers;

    private final ArrayList<Player> players;

    private final Map map;

    public enum GameMode{
        NORMAL,
        CAMPAIGN,
        EDITOR,
    }
    private final GameMode gameMode;
    public static class CustomGameRules implements Serializable {
        private static final long serialVersionUID = 1L;
        private int cityCashGeneration;
        private List<UnitType> buildableUnitTypesFactory;
        private List<UnitType> buildableUnitTypesPort;
        private List<UnitType> buildableUnitTypesAirport;
        public CustomGameRules(int cityCashGeneration, List<UnitType> buildableUnitTypesFactory, List<UnitType> buildableUnitTypesPort, List<UnitType> buildableUnitTypesAirport){
            this.cityCashGeneration = cityCashGeneration;
            this.buildableUnitTypesFactory = buildableUnitTypesFactory;
            this.buildableUnitTypesPort = buildableUnitTypesPort;
            this.buildableUnitTypesAirport = buildableUnitTypesAirport;

        }
        public int getCityCashGeneration(){
            return cityCashGeneration;
        }
        public List<UnitType> getBuildableUnitTypesFactory(){
            return buildableUnitTypesFactory;
        }
        public List<UnitType> getBuildableUnitTypesPort(){
            return buildableUnitTypesPort;
        }
        public List<UnitType> getBuildableUnitTypesAirport(){
            return buildableUnitTypesAirport;
        }
    }
    private CustomGameRules customGameRules;

    /*
     * DEBUG
     */
    static final boolean verbose = true;

    public Game(List<Player> players, Map map, GameMode gameMode){
        this.gameMode = gameMode;
        initCustomGameRules();
        this.players = new ArrayList<>();
        for(Player player : players){
            addPlayer(player);
        }
        this.map = map;
    }

    public Game(GameSerializable gameSerializable, Group group){
        this.gameMode = gameSerializable.gameMode;
        if(gameSerializable.customGameRules!=null){
            this.customGameRules = gameSerializable.customGameRules;
        } else {
            initCustomGameRules();
        }
        this.players = new ArrayList<>();
        this.pendingEliminatedPlayers = new ArrayList<>();
        for(PlayerSerializable playerSerializable : gameSerializable.players){
            addPlayer(new Player(playerSerializable));
        }
        this.map = new Map(gameSerializable.map, group, this);
    }

    private void initCustomGameRules(){
        this.customGameRules = new CustomGameRules(Codex.CITY_CASH_GENERATION, Codex.BUILDABLE_UNIT_TYPES_FACTORY, Codex.BUILDABLE_UNIT_TYPES_PORT, Codex.BUILDABLE_UNIT_TYPES_AIRPORT);
    }
    /*
    public void setCustomGameRules(CustomGameRules customGameRules){
        this.customGameRules = customGameRules;
    }
     */
    public CustomGameRules getCustomGameRules(){
        return customGameRules;
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

    public int getTurnCount(){
        return turnCount;
    }

    public void goNextTurn(){


        if (verbose) System.out.println("[GAME] [goNextTurn] Player: "+((whoseTurn>=0 && whoseTurn<amountPlayers)?players.get(whoseTurn):"INDETERMINABLE")+" 's turn ended");
        whoseTurn += 1;
        if (whoseTurn >= amountPlayers ||  whoseTurn < 0){
            whoseTurn = 0;
            turnCount++;
        }
        for(Unit unit : map.getUnits()){
            unit.setActionableThisTurn(true);
        }
        if (verbose) System.out.println("[GAME] [goNextTurn] Player: "+players.get(whoseTurn)+" 's turn begins");
    }

    public boolean itsMyTurn(Player player){
        Player playerWithTurn;
        try {
            playerWithTurn = players.get(whoseTurn);
        } catch (IndexOutOfBoundsException e){
            System.err.println("[GAME] [itsMyTurn?] ERROR Player doesn't exist");
            return false;
        }
        if (verbose) System.out.println("[GAME] [itsMyTurn?]\n[GAME] [PlayerIsAsking] "+player+"\n[GAME] [PlayerWithTurn] "+playerWithTurn);

        return playerWithTurn.equals(player);
    }
    public boolean itsMyTurn(int playerID){
        return playerID == players.get(whoseTurn).getId();
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
        if(!players.contains(player)){
            System.err.println("[GAME] [eliminatePlayer] Could not eliminate "+player);
            return;
        }
        int eliminatedPlayerPosition = players.indexOf(player);
        players.remove(player);
        if(eliminatedPlayerPosition <= whoseTurn){
            whoseTurn -= 1;
        }
        amountPlayers -= 1;
        pendingEliminatedPlayers.add(player);
        eliminationsPending = true;
    }

    /**
     * Return true when a player was eliminated and not processed by the app yet.
     */
    public boolean eliminationCheckup(){
        if(eliminationsPending){
            eliminationsPending = false;
            return true;
        }
        else {
            return false;
        }
    }
    public ArrayList<Player> getPendingEliminatedPlayers(){
        if(pendingEliminatedPlayers.isEmpty()){
            System.err.println("[GAME] [getPendingEliminatedPlayers] No pending eliminated players");
            return null;
        }
        ArrayList<Player> list = new ArrayList<>(pendingEliminatedPlayers);
        pendingEliminatedPlayers.clear();
        return list;
    }
    public GameMode getGameMode(){
        return gameMode;
    }
}
