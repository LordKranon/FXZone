package fxzone.game.logic;


import fxzone.engine.handler.KeyUnitVehicle;
import fxzone.game.logic.serializable.PlayerSerializable;
import javafx.scene.paint.Color;

public class Player {

    private final Color color;

    private final String name;

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

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Player))
            return false;
        Player ref = (Player) object;
        return this.name.equals(ref.name);
    }
}
