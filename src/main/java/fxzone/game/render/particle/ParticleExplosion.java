package fxzone.game.render.particle;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.GeometryUtils;
import javafx.scene.Group;

public class ParticleExplosion extends Particle{

    public ParticleExplosion(double x, double y, double tileRenderSize, Group group) {
        super(AssetHandler.getImage("/images/misc/zone_particle_test_2.png"), x, y, tileRenderSize, group);

        this.maxLifeTime = Math.random() * 0.5;

        double[] speed = GeometryUtils.getVectorFromAngle((Math.random() * 2. - 1) * Math.PI);

        double speedMultiplier = tileRenderSize * Config.getDouble("PARTICLE_SPEED") * (Math.random() + 0.5);
        this.speedX = speed[0] * speedMultiplier;
        this.speedY = speed[1] * speedMultiplier;
    }
}
