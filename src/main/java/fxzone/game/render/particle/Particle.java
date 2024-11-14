package fxzone.game.render.particle;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.render.AbstractGameObject;
import fxzone.engine.utils.ViewOrder;
import java.util.ArrayList;
import javafx.scene.Group;

public class Particle extends AbstractGameObject{


    private double lifetime = 1;

    public Particle(Group group){
        super(AssetHandler.getImage("/images/misc/zone_particle_test_2.png"), 50, 50, 50, 50, group);
    }

    public boolean update(double delta){
        lifetime -= delta;
        return lifetime <= 0;
    }
}
