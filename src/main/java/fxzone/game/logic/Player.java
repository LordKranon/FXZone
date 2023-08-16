package fxzone.game.logic;


import fxzone.game.logic.serializable.PlayerSerializable;
import javafx.scene.paint.Color;

public class Player {

    private Color color;

    private String name;

    public Player(String name, Color color){
        this.name = name;
        this.color = color;
    }

    public Player(PlayerSerializable playerSerializable){
        this.name = playerSerializable.name;
        this.color = Color.web(playerSerializable.color);
    }

    public Color getColor(){
        return color;
    }

    public String getName(){
        return name;
    }
}
