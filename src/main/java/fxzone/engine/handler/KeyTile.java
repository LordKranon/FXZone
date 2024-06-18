package fxzone.engine.handler;

import fxzone.game.logic.TileType;

public class KeyTile {

    public TileType keyTileType;

    public TileType[] keyTileTypesOfNeighbors;

    public KeyTile(TileType keyTileType){
        this.keyTileType = keyTileType;

        this.keyTileTypesOfNeighbors = new TileType[4];
        this.keyTileTypesOfNeighbors[0] = TileType.PLAINS;
        this.keyTileTypesOfNeighbors[1] = TileType.PLAINS;
        this.keyTileTypesOfNeighbors[2] = TileType.PLAINS;
        this.keyTileTypesOfNeighbors[3] = TileType.PLAINS;
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
}
