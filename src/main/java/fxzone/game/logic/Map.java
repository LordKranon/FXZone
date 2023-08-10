package fxzone.game.logic;

import javafx.scene.Group;

public class Map {

    /**
     * Game logic tiles
     */
    private Tile[][] tiles;

    /**
     * Graphical size of one tile
     */
    private double tileRenderSize = 128;

    /**
     * Graphical offset of all map content
     */
    private double offsetX, offsetY;

    /**
     * Constructor
     *
     * @param width game logical amount of tiles horizontally
     * @param height game logical amount of tiles vertically
     * @param group graphical object group
     */
    public Map(int width, int height, Group group){
        this.tiles = new Tile[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                this.tiles[i][j] = new Tile(i, j, tileRenderSize, group);
            }
        }
    }

    /**
     * Change position of where the map and all contents are drawn on the screen.
     * E.g. when the camera is moved.
     *
     * @param offsetX x
     * @param offsetY y
     */
    public void setGraphicalOffset(double offsetX, double offsetY){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        propagateGraphicalOffset();
    }

    /**
     * Propagates a newly changed graphical offset to all graphical objects the map contains.
     */
    private void propagateGraphicalOffset(){
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                tiles[i][j].setGraphicalOffset(offsetX, offsetY);
            }
        }
    }

    public double getWidth(){
        return tiles.length;
    }
    public double getHeight(){
        return tiles[0].length;
    }
    public double getOffsetX(){
        return offsetX;
    }
    public double getOffsetY(){
        return offsetY;
    }
    public double getTileRenderSize(){
        return tileRenderSize;
    }
}
