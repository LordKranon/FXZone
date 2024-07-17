package fxzone.engine.handler;

import fxzone.engine.utils.GeometryUtils;
import fxzone.game.logic.Tile.TileType;

public class KeyTile {

    public TileType keyTileType;

    public TileType[] keyTileTypesOfNeighbors;

    public boolean keyTileStance;

    public KeyTile(TileType keyTileType, boolean keyTileStance){
        this.keyTileType = keyTileType;
        this.keyTileStance = keyTileStance;

        this.keyTileTypesOfNeighbors = new TileType[GeometryUtils.TOTAL_AMOUNT_NEIGHBOR_DIRECTIONS];
        for(int i = 0; i < keyTileTypesOfNeighbors.length; i++){
            this.keyTileTypesOfNeighbors[i] = TileType.PLAINS;
        }
    }
    public KeyTile(TileType keyTileType, TileType[] keyTileTypesOfNeighbors, boolean keyTileStance){
        this.keyTileType = keyTileType;
        this.keyTileStance = keyTileStance;
        this.keyTileTypesOfNeighbors = keyTileTypesOfNeighbors;
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


        s.append("\n "+ ((keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileType.PLAINS) ? "P" : "-") +" ");
        s.append("\n");
        s.append((keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileType.PLAINS) ? "P" : "-");
        s.append((keyTileType == TileType.PLAINS) ? "P" : "-");
        s.append((keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileType.PLAINS) ? "P" : "-");
        s.append("\n "+ ((keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileType.PLAINS) ? "P" : "-") +" ");

        return s.toString();
    }
}
