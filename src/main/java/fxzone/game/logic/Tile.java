package fxzone.game.logic;

import fxzone.engine.handler.AssetHandler;
import fxzone.game.logic.serializable.TileSerializable;
import fxzone.game.render.GameObjectInTileSpace;
import javafx.scene.Group;

public class Tile extends TileSpaceObject{

    private final TileType tileType;

    private Unit unitOnTile;

    private Building buildingOnTile;

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
    public void setBuildingOnTile(Building building){
        if(this.buildingOnTile != null){
            System.err.println(this+" Already has a building! Overwriting...");
        }
        this.buildingOnTile = building;
    }

    public Unit getUnitOnTile(){
        if (verbose && unitOnTile == null) System.err.println(this+" No unit on tile");
        return unitOnTile;
    }
    public Building getBuildingOnTile(){
        if (verbose && buildingOnTile == null) System.err.println(this+" No building on tile");
        return buildingOnTile;
    }

    public boolean isMovableThroughBy(Unit unit){
        //TODO
        //Implement more intelligent checks for whether a unit can move through this tile or not
        return this.unitOnTile == null;
    }

    public boolean isAttackableBy(Unit unit){
        return (this.unitOnTile!=null) && this.unitOnTile.getOwnerId()!=unit.getOwnerId();
    }

    @Override
    public String toString(){
        return "[TILE ("+x+"; "+y+")]";
    }
}
