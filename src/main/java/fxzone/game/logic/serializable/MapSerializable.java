package fxzone.game.logic.serializable;

import fxzone.game.logic.Building;
import fxzone.game.logic.Map;
import fxzone.game.logic.Map.Biome;
import fxzone.game.logic.Unit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapSerializable implements Serializable {

    public Biome biome;

    public TileSerializable[][] tiles;

    public List<UnitSerializable> units;

    public List<BuildingSerializable> buildings;

    public int runningUnitId;

    private static final long serialVersionUID = 1L;

    public MapSerializable(Map map, int runningUnitId){
        this.biome = map.getBiome();
        int width = map.getWidth();
        int height = map.getHeight();
        this.tiles = new TileSerializable[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                this.tiles[i][j] = new TileSerializable(map.getTiles()[i][j]);
            }
        }
        this.units = new ArrayList<>();
        for(Unit unit : map.getUnits()){
            this.units.add(new UnitSerializable(unit));
        }
        this.buildings = new ArrayList<>();
        for(Building building : map.getBuildings()){
            this.buildings.add(new BuildingSerializable(building));
        }
        this.runningUnitId = runningUnitId;
    }
}
