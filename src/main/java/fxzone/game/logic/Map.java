package fxzone.game.logic;

import fxzone.game.logic.serializable.MapSerializable;
import fxzone.game.logic.serializable.UnitSerializable;
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
    public Map(MapSerializable mapSerializable, Group group, Game game){
        int width = mapSerializable.tiles.length;
        int height = mapSerializable.tiles[0].length;
        this.tiles = new Tile[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                this.tiles[i][j] = new Tile(mapSerializable.tiles[i][j], tileRenderSize, group);
            }
        }
        this.units = new ArrayList<Unit>();
        for(UnitSerializable unitSerializable : mapSerializable.units){
            addUnit(new Unit(unitSerializable, tileRenderSize, group, game));
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
        for(Unit unit : units){
            propagateGraphicalOffsetToUnit(unit);
        }
    }
    private void propagateGraphicalOffsetToUnit(Unit unit){
        unit.setGraphicalOffset(offsetX, offsetY);
    }

    public int getWidth(){
        return tiles.length;
    }
    public int getHeight(){
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
        int tileX = (int)Math.floor((graphicalX - offsetX) / tileRenderSize);
        int tileY = (int)Math.floor((graphicalY - offsetY) / tileRenderSize);
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
        try{
            tiles[unit.x][unit.y].setUnitOnTile(unit);
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("[MAP] Unit is not in bounds of map.");
        }
        propagateGraphicalOffsetToUnit(unit);
        propagateTileRenderSizeToUnit(unit);
    }

    public Tile[][] getTiles(){
        return tiles;
    }
    public List<Unit> getUnits(){
        return units;
    }

    /**
     * Whether a point with tile coordinates x and y is in bounds of the map.
     */
    public boolean isInBounds(int x, int y){
        return (x >= 0) && (y >= 0) && (x < getWidth()) && (y < getHeight());
    }

    /**
     * Used for pathfinding.
     * Whether a given unit can move through a specific tile.
     */
    public boolean checkTileForMoveThroughByUnit(int x, int y, Unit unit){
        //TODO
        //See Tile.isMovableThroughBy(Unit unit)
        if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight()){
            return false;
        }
        else {
            return this.tiles[x][y].isMovableThroughBy(unit);
        }
    }
}
