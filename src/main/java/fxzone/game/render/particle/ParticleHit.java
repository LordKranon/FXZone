package fxzone.game.render.particle;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.GeometryUtils;
import javafx.scene.Group;

public class ParticleHit extends Particle{

    public ParticleHit(double x, double y, double tileRenderSize, Group group) {
        super(AssetHandler.getImage("/images/misc/hpchange/zone_hp_+0.png"), x, y, tileRenderSize, group);

        this.maxLifeTime = 1;

        double speedMultiplier = tileRenderSize * Config.getDouble("PARTICLE_SPEED") * 1;
        this.speedX = 0;
        this.speedY = - speedMultiplier;
    }
}
