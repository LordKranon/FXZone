package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.ViewOrder;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectUiUnitHealth extends GameObjectInTileSpace{

    public GameObjectUiUnitHealth(int x, int y, double tileRenderSize, Group group) {
        super(null, x, y, tileRenderSize, group);
        this.setImage(AssetHandler.getImage("/images/misc/zone_hp_ui_0.png"));
        this.setViewOrder(ViewOrder.UI_UNIT_HEALTH);
        this.setVisible(false);
    }

    public void updateUnitHealth(double fractionOfMaxHealthRemaining){
        if(fractionOfMaxHealthRemaining >= 1){
            this.setVisible(false);
        } else if(fractionOfMaxHealthRemaining <= 0){
            this.setImage(AssetHandler.getImage("/images/misc/zone_hp_ui_0.png"));
            this.setVisible(true);
        } else {
            int hpDigit = (int)(fractionOfMaxHealthRemaining * 10.0);
            if (hpDigit == 0){
                hpDigit = 1;
            }
            this.setImage(AssetHandler.getImage("/images/misc/zone_hp_ui_"+hpDigit+".png"));
            this.setVisible(true);
        }
    }
}
