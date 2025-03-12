package fxzone.game.logic;

import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.GeometryUtils;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Codex.BuildingType;
import fxzone.game.logic.Codex.TileSuperType;
import fxzone.game.logic.Codex.UnitSuperType;
import fxzone.game.logic.Tile.TileType;
import fxzone.game.logic.serializable.BuildingSerializable;
import fxzone.game.logic.serializable.MapSerializable;
import fxzone.game.logic.serializable.TileSerializable;
import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.particle.ParticleHandler;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.Group;

public class Map {

    private Group subGroupMapTiles;
    private Group subGroupMapUnits;
    private Group subGroupMapBuildings;
    private Group subGroupFogOfWar;

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
     * Fog of war
     */
    private FogOfWarTile[][] fogOfWar;

    /**
     * Graphical size of one tile
     */
    private double tileRenderSize = 128;

    /**
     * Graphical offset of all map content
     */
    private double offsetX, offsetY;

    public enum Biome{
        SAND,
        GRASS,
        ASH,
        RED,
    }
    private Biome biome;

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
        this.subGroupFogOfWar = new Group();
        this.subGroupFogOfWar.setViewOrder(ViewOrder.FOG_OF_WAR);
        group.getChildren().add(this.subGroupMapTiles);
        group.getChildren().add(this.subGroupMapUnits);
        group.getChildren().add(this.subGroupMapBuildings);
        group.getChildren().add(this.subGroupFogOfWar);
        int width = mapSerializable.tiles.length;
        int height = mapSerializable.tiles[0].length;
        this.tiles = new Tile[width][height];
        this.fogOfWar = new FogOfWarTile[width][height];

        this.biome = mapSerializable.biome;

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                // Initialize tile
                this.tiles[i][j] = new Tile(mapSerializable.tiles[i][j], tileRenderSize, subGroupMapTiles, biome);
                // Set neighbor tileType info
                setNeighborTileTypeInfoForTile(i, j, mapSerializable.tiles);

                // Initialize fog of war on this tile
                this.fogOfWar[i][j] = new FogOfWarTile(i, j, tileRenderSize, subGroupFogOfWar);
            }
        }
        this.units = new ArrayList<Unit>();
        for(UnitSerializable unitSerializable : mapSerializable.units){
            addUnit(new Unit(unitSerializable, tileRenderSize, subGroupMapUnits, game), game, false);
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
        this.tiles[x][y].updateTileTypesOfNeighbors(tileTypesOfNeighbors, biome);
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
                fogOfWar[i][j].setGraphicalOffset(offsetX, offsetY);
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
    public double[] getGraphicalPosition(int tileX, int tileY){
        double graphicalX = (tileX * tileRenderSize + offsetX);
        double graphicalY = (tileY * tileRenderSize + offsetY);
        return new double[]{graphicalX, graphicalY};
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
                fogOfWar[i][j].changeTileRenderSize(this);
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

    public void createNewUnit(UnitSerializable unitSerializable, Game game, boolean isInVision, boolean inTransport){
        Unit unit = new Unit(unitSerializable, tileRenderSize, subGroupMapUnits, game);
        unit.setActionableThisTurn(false);
        unit.setInVision(isInVision);

        addUnit(unit, game, inTransport);
    }
    /**
     * Add a unit fully and graphically to a finished map in a started or running game.
     */
    private void addUnit(Unit unit, Game game, boolean inTransport){
        // Add construction menu for units like Aircraft Carrier
        if(Codex.hasConstructionMode(unit) && unit.getOwnerId() != 0 && game.getPlayer(unit.getOwnerId())!= null){
            unit.setConstructionMenu(new ConstructionMenu(Codex.BUILDABLE_UNIT_TYPES_CARRIER, FxUtils.toAwtColor(game.getPlayer(unit.getOwnerId()).getColor())));
        }

        if(inTransport){
            addUnitInTransport(unit);
        } else {
            addUnitNotInTransport(unit);
        }
    }
    private void addUnitNotInTransport(Unit unit){
        units.add(unit);
        try{
            tiles[unit.x][unit.y].setUnitOnTile(unit);
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("[MAP] Unit is not in bounds of map");
        }
        propagateGraphicalOffsetToTileSpaceObject(unit);
        propagateTileRenderSizeToTileSpaceObject(unit);
    }
    private void addUnitInTransport(Unit unit){
        units.add(unit);
        if(!isInBounds(unit.x, unit.y)){
            System.err.println("[MAP] ERROR Unit to be added in transport is not in bounds of map");
            return;
        }
        if(!tiles[unit.x][unit.y].hasUnitOnTile()){
            System.err.println("[MAP] ERROR Can not find transporter for unit to be added in transport");
        }
        tiles[unit.x][unit.y].getUnitOnTile().loadToTransport(unit);
        unit.setTransportedBy(tiles[unit.x][unit.y].getUnitOnTile());
    }
    public void removeUnit(Unit unit){
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
        //If unit is removed from tile with building, reset that buildings capture progress
        if(tiles[unit.x][unit.y].hasBuildingOnTile()){
            tiles[unit.x][unit.y].getBuildingOnTile().setStatCaptureProgress(0);
        }

        //Remove transported units
        for(Unit transportedUnit : unit.getTransportLoadedUnits()){
            removeUnit(transportedUnit);
        }
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
    public void removeBuilding(Building building){
        boolean successfullyRemoved = buildings.remove(building);
        if(!successfullyRemoved){
            System.err.println("[MAP] [removeBuilding] Building not in this maps list of buildings");
        }
        try{
            tiles[building.x][building.y].setBuildingOnTile(null);
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("[MAP] Building is not in bounds of map");
        }
        building.onRemoval(subGroupMapBuildings);
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
     * This only takes into account the information that the units owner has,
     * an inaccessible tile that has its obstruction hidden by fog of war will still be classified as accessible.
     */
    public boolean checkTileForMoveThroughByUnitPerceived(int x, int y, Unit unit, boolean[][] vision){
        if(!isInBounds(x, y)){
            return false;
        }
        else {
            return this.tiles[x][y].isMovableThroughBy(unit, vision[x][y]);
        }
    }
    public boolean checkTileForMoveToByUnitPerceived(int x, int y, Unit unit, boolean[][] vision, boolean includeEnterTransport){
        if(!isInBounds(x, y)){
            return false;
        }
        else if(!includeEnterTransport) {
            return this.tiles[x][y].isMovableToBy(unit, vision[x][y]);
        } else {
            return this.tiles[x][y].isMovableToOrCanEnterTransportBy(unit, vision[x][y]);
        }
    }
    public boolean checkTileForMoveThroughByUnitFinal(int x, int y, Unit unit){
        if(!isInBounds(x, y)){
            return false;
        }
        else {
            return this.tiles[x][y].isMovableThroughBy(unit, true);
        }
    }

    public boolean checkTileForAttackByUnit(int x, int y, Unit unit, boolean[][] vision){
        if(!isInBounds(x, y)){
            return false;
        }
        else {
            return this.tiles[x][y].isAttackableBy(unit, vision[x][y]);
        }
    }

    /**
     * Make all map elements (units & tiles) vanish or reappear.
     */
    public void setVisible(boolean visible){
        this.subGroupMapTiles.setVisible(visible);
        this.subGroupMapUnits.setVisible(visible);
        this.subGroupMapBuildings.setVisible(visible);
        this.subGroupFogOfWar.setVisible(visible);
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
        Tile newTile = new Tile(newTileSerializable, tileRenderSize, subGroupMapTiles, biome);
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
        propagateGraphicalOffsetToTileSpaceObject(newTile);
    }
    public void switchBuilding(BuildingSerializable newBuildingSerializable, Game game){
        Building oldBuilding = tiles[newBuildingSerializable.x][newBuildingSerializable.y].getBuildingOnTile();
        if(oldBuilding != null){
            removeBuilding(oldBuilding);
        }
        Building newBuilding = new Building(newBuildingSerializable, tileRenderSize, subGroupMapBuildings, game);
        addBuilding(newBuilding);
    }
    public void switchUnit(UnitSerializable newUnitSerializable, Game game){
        Unit oldUnit = tiles[newUnitSerializable.x][newUnitSerializable.y].getUnitOnTile();
        if(oldUnit != null){
            removeUnit(oldUnit);
        }
        Unit newUnit = new Unit(newUnitSerializable, tileRenderSize, subGroupMapUnits, game);
        addUnitNotInTransport(newUnit);
    }

    /**
     * Calculate and mark all visible tiles of given player.
     *
     * @param ownerId id of player
     * @return boolean array of same size as map, with true = visible
     */
    public boolean[][] getVisionOfPlayer(int ownerId){
        boolean[][] tileVisible = new boolean[getWidth()][getHeight()];
        for(Unit unit : units){
            if(unit.getOwnerId() == ownerId){
                ArrayList<Point> pointsInVision = GeometryUtils.getPointsInRange(Codex.getUnitProfile(unit).VISION);
                for(Point p : pointsInVision){
                    int tileX = unit.getX()+p.x;
                    int tileY = unit.getY()+p.y;
                    if(isInBounds(tileX, tileY)){

                        // Obstruct vision with forests
                        if(tiles[tileX][tileY].getTileType() == TileType.FOREST){
                            if(GeometryUtils.getPointToPointDistance(new Point(unit.getX(), unit.getY()), new Point(tileX, tileY)) <= 1){
                                tileVisible[tileX][tileY] = true;
                            }
                        } else{
                            // Vision unobstructed
                            tileVisible[tileX][tileY] = true;
                        }
                    }
                }
            }
        }
        for (Building building : buildings){
            if(building.getOwnerId() == ownerId){
                tileVisible[building.getX()][building.getY()] = true;
            }
        }
        return tileVisible;
    }
    public boolean[][] getVisionOfGod(){
        boolean[][] tileVisible = new boolean[getWidth()][getHeight()];
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                tileVisible[i][j] = true;
            }
        }
        return tileVisible;
    }

    public void setFogOfWarToVision(boolean[][] tileVisible){
        if(tileVisible.length != getWidth() || tileVisible[0].length != getHeight()){
            System.err.println("[MAP] [setFogOfWarToVision] Bad vision array");
            return;
        }
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                fogOfWar[i][j].setVisible(!tileVisible[i][j]);
            }
        }
        for(Unit unit : units){
            unit.setInVision(tileVisible[unit.getX()][unit.getY()]);
        }
    }

    public boolean[][] addVisionOnUnitMove(boolean[][] tileVisibleBefore, int unitX, int unitY, int visionRange){
        ArrayList<Point> pointsInVision = GeometryUtils.getPointsInRange(visionRange);
        for(Point p : pointsInVision){
            int tileX = unitX+p.x;
            int tileY = unitY+p.y;
            if(isInBounds(tileX, tileY)){
                // Obstruct vision with forests
                if(tiles[tileX][tileY].getTileType() == TileType.FOREST){
                    if(GeometryUtils.getPointToPointDistance(new Point(unitX, unitY), new Point(tileX, tileY)) <= 1){
                        tileVisibleBefore[tileX][tileY] = true;
                    }
                } else{
                    // Vision unobstructed
                    tileVisibleBefore[tileX][tileY] = true;
                }
            }
        }
        return tileVisibleBefore;
    }

    public HashMap<Player, Boolean> handleEndOfTurnEffects(Game game, ArrayList<Player> playersRemaining){

        Player playerWithTurn = game.getPlayers().get(game.whoseTurn());

        HashMap<Player, Boolean> playersEliminated = new HashMap<>();
        HashMap<Integer, Boolean> playersWithUnitsRemaining = new HashMap<>();

        for(Player player : game.getPlayers()){
            playersEliminated.put(player, true);
            playersWithUnitsRemaining.put(player.getId(), false);
        }

        for(Unit unit : units){
            if(unit.getOwnerId() == playerWithTurn.getId()){
                unit.doCaptureAtEndOfTurn(game);
            }
            if(unit.hasOwner()){
                playersWithUnitsRemaining.put(unit.getOwnerId(), true);
            }
        }
        for(Building building: buildings){
            Unit unitOnBuilding = tiles[building.getX()][building.getY()].getUnitOnTile();
            if(unitOnBuilding == null || unitOnBuilding.getOwnerId() == building.getOwnerId()){
                building.setStatCaptureProgress(0);
            }
            if(building.hasOwner() && building.isSelectable() && (unitOnBuilding == null || unitOnBuilding.getOwnerId() == building
                .getOwnerId())){
                //Players with an unblocked production building are not eliminated
                playersEliminated.put(game.getPlayer(building.getOwnerId()), false);
            }
            else if(building.hasOwner() && playersWithUnitsRemaining.get(building.getOwnerId())){
                //Players with any buildings are not eliminated IF they have at least one unit
                playersEliminated.put(game.getPlayer(building.getOwnerId()), false);
            }
        }
        return playersEliminated;
    }
    public void handleStartOfTurnEffects(Game game, ParticleHandler particleHandler){
        Player playerWithStartedTurn = game.getPlayers().get(game.whoseTurn());

        for(Building building: buildings){

            if(building.hasOwner() && building.getOwnerId() == playerWithStartedTurn.getId()) {

                Unit unitOnBuilding = tiles[building.getX()][building.getY()].getUnitOnTile();

                if (unitOnBuilding != null && unitOnBuilding.getOwnerId() == building.getOwnerId()
                    && building.getBuildingType() == BuildingType.CITY) {
                    //Cities heal units, except aircraft
                    if(unitOnBuilding.isDamaged() && Codex.getUnitProfile(unitOnBuilding).SUPERTYPE != UnitSuperType.AIRCRAFT_HELICOPTER && Codex.getUnitProfile(unitOnBuilding).SUPERTYPE != UnitSuperType.AIRCRAFT_PLANE){
                        unitOnBuilding.changeStatRemainingHealth(Codex.BUILDING_HEALING_TOTAL);
                        healingParticles(particleHandler, unitOnBuilding, 2);
                    }
                } else if(unitOnBuilding != null && unitOnBuilding.getOwnerId() == building.getOwnerId()
                    && building.getBuildingType() == BuildingType.PORT){
                    //Ports heal water units
                    if(unitOnBuilding.isDamaged() && (
                        Codex.UNIT_PROFILE_VALUES.get(unitOnBuilding.getUnitType()).SUPERTYPE == UnitSuperType.SHIP_SMALL ||
                        Codex.UNIT_PROFILE_VALUES.get(unitOnBuilding.getUnitType()).SUPERTYPE == UnitSuperType.SHIP_LARGE
                    )
                    ){
                        unitOnBuilding.changeStatRemainingHealth(Codex.BUILDING_HEALING_TOTAL);
                        healingParticles(particleHandler, unitOnBuilding, 2);
                    }
                } else if(unitOnBuilding != null && unitOnBuilding.getOwnerId() == building.getOwnerId()
                    && building.getBuildingType() == BuildingType.AIRPORT){
                    //Airports heal aircraft
                    if(unitOnBuilding.isDamaged() && (
                        Codex.UNIT_PROFILE_VALUES.get(unitOnBuilding.getUnitType()).SUPERTYPE == UnitSuperType.AIRCRAFT_PLANE ||
                            Codex.UNIT_PROFILE_VALUES.get(unitOnBuilding.getUnitType()).SUPERTYPE == UnitSuperType.AIRCRAFT_HELICOPTER
                    )
                    ){
                        unitOnBuilding.changeStatRemainingHealth(Codex.BUILDING_HEALING_TOTAL);
                        healingParticles(particleHandler, unitOnBuilding, 2);
                    }
                }
                if (building.getBuildingType() == BuildingType.CITY) {
                    //Cities generate money
                    playerWithStartedTurn.setStatResourceCash(playerWithStartedTurn.getStatResourceCash() + Codex.CITY_CASH_GENERATION);
                }
            }
        }
    }
    private void healingParticles(ParticleHandler particleHandler, Unit unit, int hpChange){

        double[] graphicalPositionOfParticles = getGraphicalPosition(unit.getX(), unit.getY());
        particleHandler.newParticleHit(graphicalPositionOfParticles[0], graphicalPositionOfParticles[1], getTileRenderSize(), hpChange);
    }

    public Biome getBiome(){
        return biome;
    }
    public void setBiome(Biome biome){
        this.biome = biome;
    }
}
