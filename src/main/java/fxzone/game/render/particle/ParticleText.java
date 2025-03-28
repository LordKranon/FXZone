package fxzone.game.render.particle;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class ParticleText extends Particle{

    public ParticleText(double x, double y, double tileRenderSize, Group group, double lifetime) {
        super(AssetHandler.getImage("/images/misc/capture_bar/captured_text.png"), x-tileRenderSize/2., y-tileRenderSize/2., 2*tileRenderSize, group);

        this.maxLifeTime = lifetime;

        double speedMultiplier = tileRenderSize * Config.getDouble("PARTICLE_SPEED") *  0.125;
        this.speedX = 0;
        this.speedY = - speedMultiplier;
    }
}
