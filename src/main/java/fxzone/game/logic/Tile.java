package fxzone.game.logic;

import fxzone.game.logic.Codex.TileSuperType;
import fxzone.game.logic.Codex.UnitSuperType;
import fxzone.game.logic.Map.Biome;
import fxzone.game.logic.Unit.UnitState;
import fxzone.game.logic.serializable.TileSerializable;
import fxzone.game.render.GameObjectTile;
import javafx.scene.Group;

public class Tile extends TileSpaceObject{

    public enum TileType {
        PLAINS,
        WATER,
        BEACH,
        FOREST,
        MOUNTAIN,
    }
    private final TileType tileType;

    private Unit unitOnTile;

    private Building buildingOnTile;

    private GameObjectTile gameObjectTile;

    /*
    * DEBUG
    * */
    static final boolean verbose = false;

    /**
     * Constructor
     *
     * @param x              game logical tile position in the map
     * @param y              game logical tile position in the map
     */
    public Tile(int x, int y, TileType tileType) {
        super(x, y);
        this.tileType = tileType;
    }
    public Tile(TileSerializable tileSerializable, double tileRenderSize, Group group, Biome biome){
        super(tileSerializable);
        this.tileType = tileSerializable.tileType;
        this.gameObjectTile = new GameObjectTile(
            this.tileType, new TileType[4],
            x, y, tileRenderSize, group, biome);
        this.gameObjectInTileSpace = this.gameObjectTile;
    }

    public TileType getTileType(){
        return tileType;
    }
    public TileSuperType getTileSuperType(){
        return Codex.getTileSuperType(tileType);
    }

    public void setUnitOnTile(Unit unit){
        if(this.unitOnTile != null && unit != null){
            System.err.println(this+" Already has a unit! Overwriting...");
        }
        this.unitOnTile = unit;
    }
    public void setBuildingOnTile(Building building){
        if(this.buildingOnTile != null && building != null){
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
    public boolean hasUnitOnTile(){
        return unitOnTile != null;
    }
    public boolean hasBuildingOnTile(){
        return buildingOnTile != null;
    }

    public boolean isMovableThroughBy(Unit unit, boolean tileVisible){
        boolean blockedByOtherUnit = false;
        if(tileVisible && this.unitOnTile != null){
            if(this.unitOnTile.getOwnerId() != unit.getOwnerId()){
                blockedByOtherUnit = !(
                    Codex.getUnitProfile(unit).SUPERTYPE == UnitSuperType.AIRCRAFT_HELICOPTER ||
                        Codex.getUnitProfile(unit).SUPERTYPE == UnitSuperType.AIRCRAFT_PLANE ||
                        Codex.getUnitProfile(this.unitOnTile).SUPERTYPE == UnitSuperType.AIRCRAFT_HELICOPTER ||
                        Codex.getUnitProfile(this.unitOnTile).SUPERTYPE == UnitSuperType.AIRCRAFT_PLANE
                );
            }
        }
        return (!blockedByOtherUnit) && (Codex.tileTypeFitsUnitSuperType(this, unit));
    }
    public boolean isMovableToBy(Unit unit, boolean tileVisible){
        return (!tileVisible ||
            (this.unitOnTile == null)
        ) && (Codex.tileTypeFitsUnitSuperType(this, unit));
    }
    public boolean isMovableToOrCanEnterTransportBy(Unit unit, boolean tileVisible){
        return (!tileVisible ||
            (this.unitOnTile == null ||
                (this.unitOnTile.getOwnerId() == unit.getOwnerId() && this.unitOnTile.canTransportLoad(unit) && (unit.getUnitState() != UnitState.IN_TRANSPORT))
            )
        ) && (Codex.tileTypeFitsUnitSuperType(this, unit));
    }


    public boolean isAttackableBy(Unit unit, boolean tileVisible){
        return tileVisible && (this.unitOnTile!=null) && this.unitOnTile.getOwnerId()!=unit.getOwnerId() && Codex.canHit(unit, this.unitOnTile);
    }

    @Override
    public String toString(){
        return "[TILE ("+x+"; "+y+")]";
    }

    @Override
    public void onRemoval(Group group){
        super.onRemoval(group);
    }

    /**
     * Change graphical image displayed of this tile depending on neighboring tile types.
     */
    public void updateTileTypesOfNeighbors(TileType[] tileTypesOfNeighbors, Biome biome){
        gameObjectTile.adjustToTileTypesOfNeighbors(this.tileType, tileTypesOfNeighbors, biome);
    }

    public GameObjectTile getGameObjectTile(){
        return gameObjectTile;
    }
}
