package fxzone.game.logic;


import fxzone.engine.utils.FxUtils;
import fxzone.game.logic.serializable.PlayerSerializable;
import javafx.scene.paint.Color;

public class Player {

    private final Color color;

    private final Color textColor;

    private final String name;

    private final int id;

    /*
    GAMEPLAY
     */
    private int statResourceCash;

    public Player(String name, Color color, int id){
        this.name = name;
        this.color = color;
        this.textColor = FxUtils.easeColor(color);
        this.id = id;
    }
    public Player(PlayerSerializable playerSerializable){
        this.name = playerSerializable.name;
        this.color = Color.web(playerSerializable.color);
        this.textColor = FxUtils.easeColor(this.color);
        this.id = playerSerializable.id;
        initializeStats();
    }
    private void initializeStats(){
        this.statResourceCash = Codex.PLAYER_STARTING_CASH;
    }

    public Color getColor(){
        return color;
    }

    public Color getTextColor(){
        return textColor;
    }

    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Player))
            return false;
        Player ref = (Player) object;
        return this.id == ref.id;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString(){
        return "PLAYER"+id+":"+name+":"+super.toString();
    }

    public int getStatResourceCash(){
        return statResourceCash;
    }
    public void setStatResourceCash(int cash){
        this.statResourceCash = cash;
    }
}
