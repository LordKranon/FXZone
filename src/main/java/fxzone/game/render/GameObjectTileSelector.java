package fxzone.game.render;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.ViewOrder;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectTileSelector extends GameObjectInTileSpace{

    private final Image imageTileSelector0, imageTileSelector1;

    private double cumulativeDeltaOnImageTicks;

    private boolean stance = false;

    private final double totalImageTickDelay = Config.getDouble("UI_TIlE_SELECTOR_TICK_INTERVAL");

    public GameObjectTileSelector(int x, int y, double tileRenderSize, Group group) {
        super(null, x, y, tileRenderSize, group);
        this.imageTileSelector0 = AssetHandler.getImage("/images/misc/selector.png");
        this.imageTileSelector1 = AssetHandler.getImage("/images/misc/selector2.png");
        this.setImage(imageTileSelector0);
        this.setViewOrder(ViewOrder.UI_SELECTOR);
    }

    public void updateTickingImage(double delta){
        cumulativeDeltaOnImageTicks += delta;
        if(cumulativeDeltaOnImageTicks > totalImageTickDelay){
            cumulativeDeltaOnImageTicks -= totalImageTickDelay;
            switchStance();
        }
    }

    private void switchStance(){
        stance = !stance;
        this.setImage(stance?imageTileSelector1:imageTileSelector0);
    }
}
