package fxzone.game.render.particle;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class ParticleHit extends Particle{

    public ParticleHit(double x, double y, double tileRenderSize, Group group, int hpChange) {
        super(hpChangeImage(hpChange), x, y, tileRenderSize, group);

        this.maxLifeTime = (hpChange > 0) ? 1.5 :.75;

        double speedMultiplier = tileRenderSize * Config.getDouble("PARTICLE_SPEED") * ((hpChange > 0) ? 0.125 : 0.25);
        this.speedX = 0;
        this.speedY = - speedMultiplier;
    }

    private static Image hpChangeImage(int hpChange){
        if(hpChange < -10 || hpChange > 10){
            System.err.println("[PARTICLE-HIT] ERROR! HP changed by over 10 at once");
            hpChange = -10;
        }
        return AssetHandler.getImage("/images/misc/hpchange/zone_hp_"+(hpChange <= 0 ? "-" : "+")+""+Math.abs(hpChange)+".png");
    }
}
