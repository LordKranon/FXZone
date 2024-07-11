package fxzone.engine.handler;

import fxzone.engine.utils.GeometryUtils;
import fxzone.game.logic.Tile.TileType;

public class KeyTile {

    public TileType keyTileType;

    public TileType[] keyTileTypesOfNeighbors;

    public KeyTile(TileType keyTileType){
        this.keyTileType = keyTileType;

        this.keyTileTypesOfNeighbors = new TileType[GeometryUtils.TOTAL_AMOUNT_NEIGHBOR_DIRECTIONS];
        for(int i = 0; i < keyTileTypesOfNeighbors.length; i++){
            this.keyTileTypesOfNeighbors[i] = TileType.PLAINS;
        }
    }
    public KeyTile(TileType keyTileType, TileType[] keyTileTypesOfNeighbors){
        this.keyTileType = keyTileType;
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


        /*
        s.append("\n");
        for(int i = 0; i < 3; i++){
            s.append((keyTileTypesOfNeighbors[i] == TileType.PLAINS) ? "P" : "-");
        }
        s.append("\n");
        s.append((keyTileTypesOfNeighbors[3] == TileType.PLAINS) ? "P" : "-");
        s.append((keyTileType == TileType.PLAINS) ? "P" : "-");
        s.append((keyTileTypesOfNeighbors[4] == TileType.PLAINS) ? "P" : "-");
        s.append("\n");
        for(int i = 0; i < 3; i++){
            s.append((keyTileTypesOfNeighbors[i+5] == TileType.PLAINS) ? "P" : "-");
        }
        */

        s.append("\n "+ ((keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileType.PLAINS) ? "P" : "-") +" ");
        s.append("\n");
        s.append((keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileType.PLAINS) ? "P" : "-");
        s.append((keyTileType == TileType.PLAINS) ? "P" : "-");
        s.append((keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileType.PLAINS) ? "P" : "-");
        s.append("\n "+ ((keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileType.PLAINS) ? "P" : "-") +" ");

        return s.toString();
    }
}
