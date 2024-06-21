package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyTile;
import fxzone.game.logic.TileType;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectTile extends GameObjectInTileSpace{

    private final Image imageBase;

    public GameObjectTile(TileType tileType, TileType[] tileTypesOfNeighbors, int x, int y, double tileRenderSize, Group group) {
        super(null, x, y, tileRenderSize, group);
        this.imageBase = AssetHandler.getImageTile(new KeyTile(tileType));
        this.setImage(imageBase);
    }

    public void adjustToTileTypesOfNeighbors(TileType thisTileType, TileType[] tileTypesOfNeighbors){
        Image imageAdjusted = AssetHandler.getImageTile(new KeyTile(thisTileType, tileTypesOfNeighbors));
        this.setImage(imageAdjusted);
    }
}
