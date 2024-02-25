package fxzone.game.logic.serializable;

import fxzone.engine.utils.FxUtils;
import fxzone.game.logic.Player;
import java.io.Serializable;
import javafx.scene.paint.Color;

public class PlayerSerializable implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;
    public String color;
    public int id;

    public PlayerSerializable(Player player){
        this.name = player.getName();
        this.color = FxUtils.toRGBCode(player.getColor());
        this.id = player.getId();
    }
}
