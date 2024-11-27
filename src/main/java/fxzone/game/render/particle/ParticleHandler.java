package fxzone.game.render.particle;

import fxzone.engine.utils.ViewOrder;
import java.awt.Point;
import java.util.ArrayList;
import javafx.scene.Group;

public class ParticleHandler {

    Group subGroupParticles;

    ArrayList<Particle> liveParticles = new ArrayList<>();

    public ParticleHandler(Group root){
        subGroupParticles = new Group();
        subGroupParticles.setViewOrder(ViewOrder.PARTICLE);
        root.getChildren().add(subGroupParticles);
    }

    public void newParticle(double x, double y, double tileRenderSize){
        for(int i = 0; i < 10; i++){

            Particle p = new Particle(x, y, tileRenderSize, subGroupParticles);
            liveParticles.add(p);
        }
    }
    public void updateParticles(double delta){
        for (int i = 0; i < liveParticles.size(); i++){
            Particle p = liveParticles.get(i);
            if(p.update(delta)){
                liveParticles.remove(p);
                p.removeSelfFromRoot(subGroupParticles);
            }
        }
    }
}
