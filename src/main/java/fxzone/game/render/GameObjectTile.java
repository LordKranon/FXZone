package fxzone.game.render;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyTile;
import fxzone.game.logic.Codex.TileSuperType;
import fxzone.game.logic.Tile;
import fxzone.game.logic.Tile.TileType;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectTile extends GameObjectInTileSpace{

    private Image imageBase;

    private Image imageAlternate;

    /**
     * Graphical tile pulsation, like e.g. waves in the water.
     */
    private static boolean tileStance = false;
    private static double cumulativeDeltaOnImageTicks;
    private static final double totalImageTickDelay = Config.getDouble("GAME_SPEED_TILE_PULSATION_INTERVAL");

    public GameObjectTile(TileType tileType, TileType[] tileTypesOfNeighbors, int x, int y, double tileRenderSize, Group group) {
        super(null, x, y, tileRenderSize, group);
        this.imageBase = AssetHandler.getImageTile(new KeyTile(tileType, false));
        this.imageAlternate = AssetHandler.getImageTile(new KeyTile(tileType, true));
        this.setImage(imageBase);

    }

    public void adjustToTileTypesOfNeighbors(TileType thisTileType, TileSuperType[] tileTypesOfNeighbors){
        Image imageAdjusted = AssetHandler.getImageTile(new KeyTile(thisTileType, tileTypesOfNeighbors, false));
        Image imageAdjustedAlternate = AssetHandler.getImageTile(new KeyTile(thisTileType, tileTypesOfNeighbors, true));
        this.imageBase = imageAdjusted;
        this.imageAlternate = imageAdjustedAlternate;
        this.setImage(imageBase);
    }

    public static void updatePulsatingTiles(double delta, Tile[][] tiles){
        cumulativeDeltaOnImageTicks += delta;
        if(cumulativeDeltaOnImageTicks > totalImageTickDelay){
            cumulativeDeltaOnImageTicks -= totalImageTickDelay;
            tileStance = !tileStance;
            for(int i = 0; i < tiles.length; i++){
                for(int j = 0; j < tiles[i].length; j++){
                    tiles[i][j].getGameObjectTile().setStance(tileStance);
                }
            }
        }
    }

    public void setStance(boolean stance){
        this.setImage(stance?imageAlternate:imageBase);
    }
}
