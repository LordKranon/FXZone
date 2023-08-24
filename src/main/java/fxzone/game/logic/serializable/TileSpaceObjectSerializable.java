package fxzone.game.logic.serializable;

import fxzone.game.logic.TileSpaceObject;
import java.io.Serializable;

public class TileSpaceObjectSerializable implements Serializable {

    public int x, y;

    private static final long serialVersionUID = 1L;

    public TileSpaceObjectSerializable(TileSpaceObject tileSpaceObject){
        this.x = tileSpaceObject.getX();
        this.y = tileSpaceObject.getY();
    }
}
