package fxzone.game.logic;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;

public class Map {

    /**
     * Game logic tiles
     */
    private Tile[][] tiles;

    /**
     * Game logic units
     */
    private List<Unit> units;

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
        this.units = new ArrayList<Unit>();
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
        for(Unit unit : units){
            propagateGraphicalOffsetToUnit(unit);
        }
    }
    private void propagateGraphicalOffsetToUnit(Unit unit){
        unit.setGraphicalOffset(offsetX, offsetY);
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

    /**
     * Find the game logical tile at given unprocessed graphical coordinates.
     *
     * @param graphicalX screen coordinate x
     * @param graphicalY screen coordinate y
     * @return tile at given screen coordinates
     */
    public Tile getTileAt(double graphicalX, double graphicalY) throws ArrayIndexOutOfBoundsException{
        int tileX = (int)((graphicalX - offsetX) / tileRenderSize);
        int tileY = (int)((graphicalY - offsetY) / tileRenderSize);
        return tiles[tileX][tileY];
    }

    /**
     * Change graphical size of all map contents.
     * @param tileRenderSize new size (in pixels) of one tile
     */
    public void setTileRenderSize(double tileRenderSize){
        this.tileRenderSize = tileRenderSize;
        propagateTileRenderSize();
    }
    private void propagateTileRenderSize(){
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                tiles[i][j].changeTileRenderSize(this);
            }
        }
        for (Unit unit : units){
            propagateTileRenderSizeToUnit(unit);
        }
    }
    private void propagateTileRenderSizeToUnit(Unit unit){
        unit.changeTileRenderSize(this);
    }

    public void addUnit(Unit unit){
        units.add(unit);
        propagateGraphicalOffsetToUnit(unit);
        propagateTileRenderSizeToUnit(unit);
    }
}
