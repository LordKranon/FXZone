package fxzone.engine.handler;

import fxzone.engine.utils.GeometryUtils;
import fxzone.game.logic.Codex;
import fxzone.game.logic.Codex.TileSuperType;
import fxzone.game.logic.Map.Biome;
import fxzone.game.logic.Tile.TileType;

public class KeyTile {

    public Biome keyBiome;

    public TileType keyTileType;

    public TileType[] keyTileTypesOfNeighbors;

    public boolean keyTileStance;

    public KeyTile(TileType keyTileType, boolean keyTileStance, Biome keyBiome){
        this.keyTileType = keyTileType;
        this.keyTileStance = keyTileStance;
        this.keyBiome = keyBiome;

        this.keyTileTypesOfNeighbors = new TileType[GeometryUtils.TOTAL_AMOUNT_NEIGHBOR_DIRECTIONS];
        for(int i = 0; i < keyTileTypesOfNeighbors.length; i++){
            this.keyTileTypesOfNeighbors[i] = TileType.PLAINS;
        }
    }
    public KeyTile(TileType keyTileType, TileType[] keyTileTypesOfNeighbors, boolean keyTileStance, Biome keyBiome){
        this.keyTileType = keyTileType;
        this.keyTileStance = keyTileStance;
        this.keyTileTypesOfNeighbors = keyTileTypesOfNeighbors;
        this.keyBiome = keyBiome;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyTile))
            return false;
        KeyTile ref = (KeyTile) obj;
        if(!(ref.keyTileType == this.keyTileType)){
            return false;
        }
        if(ref.keyTileStance != this.keyTileStance){
            return false;
        }
        if(ref.keyBiome != this.keyBiome){
            return false;
        }
        for(int i = 0; i < this.keyTileTypesOfNeighbors.length; i++){
            if(ref.keyTileTypesOfNeighbors[i] != this.keyTileTypesOfNeighbors[i]){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return keyTileType.hashCode();
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder("");


        s.append("\n "+ ((Codex.getTileSuperType(keyTileTypesOfNeighbors[GeometryUtils.NORTH]) == TileSuperType.TS_LAND) ? "P" : "-") +" ");
        s.append("\n");
        s.append((Codex.getTileSuperType(keyTileTypesOfNeighbors[GeometryUtils.WEST]) == TileSuperType.TS_LAND) ? "P" : "-");
        s.append((keyTileType == TileType.PLAINS) ? "P" : "-");
        s.append((Codex.getTileSuperType(keyTileTypesOfNeighbors[GeometryUtils.EAST]) == TileSuperType.TS_LAND) ? "P" : "-");
        s.append("\n "+ ((Codex.getTileSuperType(keyTileTypesOfNeighbors[GeometryUtils.SOUTH]) == TileSuperType.TS_LAND) ? "P" : "-") +" ");

        return s.toString();
    }
}
