package fxzone.game.logic;


import fxzone.engine.handler.KeyUnitVehicle;
import fxzone.game.logic.serializable.PlayerSerializable;
import javafx.scene.paint.Color;

public class Player {

    private final Color color;

    private final String name;

    private final int id;

    public Player(String name, Color color, int id){
        this.name = name;
        this.color = color;
        this.id = id;
    }

    public Player(PlayerSerializable playerSerializable){
        this.name = playerSerializable.name;
        this.color = Color.web(playerSerializable.color);
        this.id = playerSerializable.id;
    }

    public Color getColor(){
        return color;
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
}
