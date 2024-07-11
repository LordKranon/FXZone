package fxzone.game.logic.serializable;


import fxzone.game.logic.Tile;
import fxzone.game.logic.Tile.TileType;

public class TileSerializable extends TileSpaceObjectSerializable{

    public TileType tileType;

    public TileSerializable(Tile tile) {
        super(tile);
        this.tileType = tile.getTileType();
    }
}
