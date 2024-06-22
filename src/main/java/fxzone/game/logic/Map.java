package fxzone.game.logic;

import fxzone.engine.utils.GeometryUtils;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.serializable.BuildingSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.game.logic.serializable.TileSerializable;
import fxzone.game.logic.serializable.UnitSerializable;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;

public class Map {

    private Group subGroupMapTiles;
    private Group subGroupMapUnits;
    private Group subGroupMapBuildings;

    /**
     * Game logic tiles
     */
    private Tile[][] tiles;

    /**
     * Game logic units
     */
    private List<Unit> units;

    private List<Building> buildings;

    /**
     * Graphical size of one tile
     */
    private double tileRenderSize = 128;

    /**
     * Graphical offset of all map content
     */
    private double offsetX, offsetY;

    /**
     * Constructor.
     * Not ready for gameplay.
     *
     * @param width game logical amount of tiles horizontally
     * @param height game logical amount of tiles vertically
     * @param group graphical object group
     */
    public Map(int width, int height, Group group){
        this.tiles = new Tile[width][height];

        // Temporary tile filler
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                this.tiles[i][j] = new Tile(i, j, i > 5 ? TileType.WATER:TileType.WATER);
            }
        }


        this.units = new ArrayList<Unit>();
        this.buildings = new ArrayList<Building>();
    }
    /**
     * Temporary Constructor.
     * Construct from loaded map and discard units and buildings, keep only tiles.
     * Not ready for gameplay.
     * TODO Remove
     */
    public Map(MapSerializable mapSerializable){
        int width = mapSerializable.tiles.length;
        int height = mapSerializable.tiles[0].length;
        this.tiles = new Tile[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                this.tiles[i][j] = new Tile(
                    mapSerializable.tiles[i][j].x, mapSerializable.tiles[i][j].y, mapSerializable.tiles[i][j].tileType
                );
            }
        }
        this.units = new ArrayList<Unit>();
        this.buildings = new ArrayList<Building>();
    }
    /**
     * Construct a map and initialize all graphics for gameplay readiness.
     */
    public Map(MapSerializable mapSerializable, Group group, Game game){
        this.subGroupMapTiles = new Group();
        this.subGroupMapTiles.setViewOrder(ViewOrder.MAP_TILE);
        this.subGroupMapUnits = new Group();
        this.subGroupMapUnits.setViewOrder(ViewOrder.GAME_UNIT);
        this.subGroupMapBuildings = new Group();
        this.subGroupMapBuildings.setViewOrder(ViewOrder.GAME_BUILDING);
        group.getChildren().add(this.subGroupMapTiles);
        group.getChildren().add(this.subGroupMapUnits);
        group.getChildren().add(this.subGroupMapBuildings);
        int width = mapSerializable.tiles.length;
        int height = mapSerializable.tiles[0].length;
        this.tiles = new Tile[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                this.tiles[i][j] = new Tile(mapSerializable.tiles[i][j], tileRenderSize, subGroupMapTiles);

                // Set neighbor tileType info
                /*
                 */
                setNeighborTileTypeInfoForTile(i, j, mapSerializable.tiles);

            }
        }
        this.units = new ArrayList<Unit>();
        for(UnitSerializable unitSerializable : mapSerializable.units){
            addUnit(new Unit(unitSerializable, tileRenderSize, subGroupMapUnits, game));
        }
        this.buildings = new ArrayList<Building>();
        for(BuildingSerializable buildingSerializable : mapSerializable.buildings){
            addBuilding(new Building(buildingSerializable, tileRenderSize, subGroupMapBuildings, game));
        }
    }

    private void setNeighborTileTypeInfoForTile(int x, int y, TileSerializable[][] tilesSerializable) throws ArrayIndexOutOfBoundsException{
        TileType[] tileTypesOfNeighbors = new TileType[GeometryUtils.TOTAL_AMOUNT_NEIGHBOR_DIRECTIONS];
        for(int k = 0; k < tileTypesOfNeighbors.length; k++) {
            try {
                Point neighborsPosition = GeometryUtils.getNeighborsPosition(x, y, k);
                if(tilesSerializable != null){
                    tileTypesOfNeighbors[k] = tilesSerializable[neighborsPosition.x][neighborsPosition.y].tileType;
                } else {
                    tileTypesOfNeighbors[k] = this.tiles[neighborsPosition.x][neighborsPosition.y].getTileType();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                tileTypesOfNeighbors[k] = TileType.PLAINS;
            }
        }
        this.tiles[x][y].updateTileTypesOfNeighbors(tileTypesOfNeighbors);
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
            propagateGraphicalOffsetToTileSpaceObject(unit);
        }
        for(Building building : buildings){
            propagateGraphicalOffsetToTileSpaceObject(building);
        }
    }
    private void propagateGraphicalOffsetToTileSpaceObject(TileSpaceObject tileSpaceObject){
        tileSpaceObject.setGraphicalOffset(offsetX, offsetY);
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
        Point point = getPointAt(graphicalX, graphicalY);
        return tiles[point.x][point.y];
    }
    /**
     * Convert graphical true position to tilespace position.
     */
    public Point getPointAt(double graphicalX, double graphicalY) {
        int tileX = (int)Math.floor((graphicalX - offsetX) / tileRenderSize);
        int tileY = (int)Math.floor((graphicalY - offsetY) / tileRenderSize);
        return new Point(tileX, tileY);
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
            propagateTileRenderSizeToTileSpaceObject(unit);
        }
        for (Building building : buildings){
            propagateTileRenderSizeToTileSpaceObject(building);
        }
    }
    private void propagateTileRenderSizeToTileSpaceObject(TileSpaceObject tileSpaceObject){
        tileSpaceObject.changeTileRenderSize(this);
    }

    public void createNewUnit(UnitSerializable unitSerializable, Game game){
        Unit unit = new Unit(unitSerializable, tileRenderSize, subGroupMapUnits, game);
        addUnit(unit);
    }
    /**
     * Add a unit fully and graphically to a finished map in a started or running game.
     */
    private void addUnit(Unit unit){
        units.add(unit);
        try{
            tiles[unit.x][unit.y].setUnitOnTile(unit);
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("[MAP] Unit is not in bounds of map");
        }
        propagateGraphicalOffsetToTileSpaceObject(unit);
        propagateTileRenderSizeToTileSpaceObject(unit);
    }
    public void removeUnit(Unit unit, Group group){
        boolean successfullyRemoved = units.remove(unit);
        if(!successfullyRemoved){
            System.err.println("[MAP] [removeUnit] Unit not in this maps list of units");
        }
        try{
            tiles[unit.x][unit.y].setUnitOnTile(null);
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("[MAP] Unit is not in bounds of map");
        }
        unit.onRemoval(subGroupMapUnits);
    }
    private void addBuilding(Building building){
        buildings.add(building);
        try{
            tiles[building.x][building.y].setBuildingOnTile(building);
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("[MAP] Building is not in bounds of map");
        }
        propagateGraphicalOffsetToTileSpaceObject(building);
        propagateTileRenderSizeToTileSpaceObject(building);
    }

    public Tile[][] getTiles(){
        return tiles;
    }
    public List<Unit> getUnits(){
        return units;
    }
    public List<Building> getBuildings(){
        return buildings;
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
        if(!isInBounds(x, y)){
            return false;
        }
        else {
            return this.tiles[x][y].isMovableThroughBy(unit);
        }
    }

    public boolean checkTileForAttackByUnit(int x, int y, Unit unit){
        if(!isInBounds(x, y)){
            return false;
        }
        else {
            return this.tiles[x][y].isAttackableBy(unit);
        }
    }

    /**
     * Make all map elements (units & tiles) vanish or reappear.
     */
    public void setVisible(boolean visible){
        this.subGroupMapTiles.setVisible(visible);
        this.subGroupMapUnits.setVisible(visible);
        this.subGroupMapBuildings.setVisible(visible);
    }

    /**
     * Used for map editor.
     * Remove a tile from the map and substitute a new tile for it.
     *
     * @param newTileSerializable the fresh tile that will replace the old tile at that position
     */
    public void switchTile(TileSerializable newTileSerializable){
        Tile oldTile = tiles[newTileSerializable.x][newTileSerializable.y];
        oldTile.onRemoval(subGroupMapTiles);
        Tile newTile = new Tile(newTileSerializable, tileRenderSize, subGroupMapTiles);
        tiles[newTileSerializable.x][newTileSerializable.y] = newTile;
        newTile.setBuildingOnTile(oldTile.getBuildingOnTile());
        newTile.setUnitOnTile(oldTile.getUnitOnTile());

        for(int i = 0; i < GeometryUtils.TOTAL_AMOUNT_NEIGHBOR_DIRECTIONS; i++){
            Point neighbor = GeometryUtils.getNeighborsPosition(newTileSerializable.x, newTileSerializable.y, i);
            try {
                setNeighborTileTypeInfoForTile(neighbor.x, neighbor.y, null);
            } catch (ArrayIndexOutOfBoundsException ignored){

            }
        }
        setNeighborTileTypeInfoForTile(newTileSerializable.x, newTileSerializable.y, null);
    }
}
