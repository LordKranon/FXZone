package fxzone.game.render.particle;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class ParticleHit extends Particle{

    public ParticleHit(double x, double y, double tileRenderSize, Group group, int hpChange) {
        super(hpChangeImage(hpChange), x, y, tileRenderSize, group);

        this.maxLifeTime = 1;

        double speedMultiplier = tileRenderSize * Config.getDouble("PARTICLE_SPEED") * 0.25;
        this.speedX = 0;
        this.speedY = - speedMultiplier;
    }

    private static Image hpChangeImage(int hpChange){
        if(hpChange < -9 || hpChange > 9){
            System.err.println("[PARTICLE-HIT] ERROR! HP changed by over 9 at once");
            hpChange = -9;
        }
        return AssetHandler.getImage("/images/misc/hpchange/zone_hp_"+(hpChange <= 0 ? "-" : "+")+""+Math.abs(hpChange)+".png");
    }
}
