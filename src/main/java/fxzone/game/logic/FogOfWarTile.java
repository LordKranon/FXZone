package fxzone.game.logic;

import fxzone.engine.handler.AssetHandler;
import fxzone.game.render.GameObjectInTileSpace;
import javafx.scene.Group;

public class FogOfWarTile extends TileSpaceObject{

    public FogOfWarTile(int x, int y, double tileRenderSize, Group group) {
        super(x, y);
        this.gameObjectInTileSpace = new GameObjectInTileSpace(AssetHandler.getImage("/images/misc/fow1.png"), x, y, tileRenderSize, group);
    }
}
