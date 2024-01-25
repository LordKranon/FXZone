package fxzone.game.logic;

import fxzone.engine.handler.AssetHandler;
import fxzone.game.logic.serializable.TileSerializable;
import fxzone.game.render.GameObjectInTileSpace;
import javafx.scene.Group;

public class Tile extends TileSpaceObject{

    private final TileType tileType;

    private Unit unitOnTile;

    /*
    * DEBUG
    * */
    static final boolean verbose = false;

    /**
     * Constructor
     *
     * @param x              game logical tile position in the map
     * @param y              game logical tile position in the map
     * @param tileRenderSize graphical size
     * @param group          graphical object group
     */
    public Tile(int x, int y, double tileRenderSize, Group group) {
        super(x, y, tileRenderSize, group);
        this.tileType = TileType.PLAINS;
        this.gameObjectInTileSpace = new GameObjectInTileSpace(AssetHandler.getImage(
            "/images/terrain/tiles/tile_plains.png"), x, y, tileRenderSize, group);
    }

    public Tile(TileSerializable tileSerializable, double tileRenderSize, Group group){
        super(tileSerializable);
        this.tileType = tileSerializable.tileType;
        this.gameObjectInTileSpace = new GameObjectInTileSpace(AssetHandler.getImage(
            "/images/terrain/tiles/tile_plains.png"), x, y, tileRenderSize, group);
    }

    public TileType getTileType(){
        return tileType;
    }

    public void setUnitOnTile(Unit unit){
        this.unitOnTile = unit;
    }

    public Unit getUnitOnTile(){
        if (verbose && unitOnTile == null) System.err.println("[TILE] No unit on tile ("+x+"; "+y+")");
        return unitOnTile;
    }
}
