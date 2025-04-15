package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyCharacter;
import fxzone.engine.render.AbstractGameObject;
import fxzone.engine.utils.ViewOrder;
import javafx.scene.Group;

public class GameObjectCharacter extends AbstractGameObject {

    private boolean screenSideLeft;

    public GameObjectCharacter(double x, double y, double size, Group group, KeyCharacter keyCharacter){
        super(AssetHandler.getImageCharacter(keyCharacter), x, y, size, size, group);
        setViewOrder(ViewOrder.CHARACTER);
    }

    public void setCharacter(KeyCharacter keyCharacter){
        setImage(AssetHandler.getImageCharacter(keyCharacter));
    }

    public boolean isScreenSideLeft(){
        return screenSideLeft;
    }
    public void setScreenSideLeft(boolean screenSideLeft){
        this.screenSideLeft = screenSideLeft;
        if(screenSideLeft){
            getImageView().setScaleX(-1);
        } else {
            getImageView().setScaleX(1);
        }
    }
}
