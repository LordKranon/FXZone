package fxzone.game.render.particle;

import fxzone.config.Config;
import fxzone.engine.utils.ViewOrder;
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

    public void newParticleExplosion(double x, double y, double tileRenderSize){
        int total = (int)(Math.random() * 20) + 20;
        for(int i = 0; i < 20; i++){

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
