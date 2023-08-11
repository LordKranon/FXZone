package fxzone.game.logic;

import fxzone.game.render.GameObjectInTileSpace;
import javafx.scene.Group;

public class Tile {

    /**
     * Tile position in map.
     * min = 0; max = map width/height - 1
     */
    private int x, y;

    /**
     * Graphical representation of this tile.
     */
    private GameObjectInTileSpace gameObjectTile;


    /**
     * Constructor
     *
     * @param x game logical tile position in the map
     * @param y game logical tile position in the map
     * @param tileRenderSize graphical size
     * @param group graphical object group
     */
    public Tile(int x, int y, double tileRenderSize, Group group){
        this.x = x;
        this.y = y;

        this.gameObjectTile = new GameObjectInTileSpace("/images/terrain/tiles/tile_plains.png", x, y, tileRenderSize, group);
    }

    public void setGraphicalOffset(double offsetX, double offsetY){
        this.gameObjectTile.setOffset(offsetX, offsetY);
    }
    public void changeTileRenderSize(Map map){
        this.gameObjectTile.changeTileRenderSize(x, y, map);
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
