package fxzone.game.render;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.ViewOrder;
import fxzone.game.logic.Map;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectTileSelector extends GameObjectInTileSpace{

    private final Image imageTileSelector0, imageTileSelector1;

    private static double cumulativeDeltaOnImageTicks;
    private static final double UI_TILE_SELECTOR_TICK_INTERVAL = Config.getDouble("UI_TIlE_SELECTOR_TICK_INTERVAL");

    private static boolean stance = false;

    private int tileX, tileY;

    public GameObjectTileSelector(int x, int y, double tileRenderSize, Group group, String selectorType) {
        super(null, x, y, tileRenderSize, group);
        this.imageTileSelector0 = AssetHandler.getImage("/images/misc/selector"+selectorType+".png");
        this.imageTileSelector1 = AssetHandler.getImage("/images/misc/selector"+selectorType+"2.png");
        this.tileX = x;
        this.tileY = y;
        this.setImage(imageTileSelector0);
        this.setViewOrder(ViewOrder.UI_SELECTOR);
    }

    public static boolean updateTickingImage(double delta){
        cumulativeDeltaOnImageTicks += delta;
        if(cumulativeDeltaOnImageTicks > UI_TILE_SELECTOR_TICK_INTERVAL){
            cumulativeDeltaOnImageTicks -= UI_TILE_SELECTOR_TICK_INTERVAL;
            return true;
        }
        return false;
    }

    public static void switchStance(){
        stance = !stance;
    }
    public void adaptStance(){
        this.setImage(stance?imageTileSelector1:imageTileSelector0);
    }
    public void changeTileRenderSize(Map map){
        setPositionInMap(tileX, tileY, map);
        setFit(map.getTileRenderSize());
    }
}
