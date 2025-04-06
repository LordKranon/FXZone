package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.render.AbstractGameObject;
import fxzone.engine.utils.ViewOrder;
import javafx.scene.Group;

public class GameObjectCharacter extends AbstractGameObject {

    public GameObjectCharacter(double x, double y, double size, Group group){
        super(AssetHandler.getImage("/images/characters/character_infantry_cl.png", 512, 512), x, y, size, size, group);
        setViewOrder(ViewOrder.CHARACTER);
    }
}
