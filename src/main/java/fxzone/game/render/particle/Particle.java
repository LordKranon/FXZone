package fxzone.game.render.particle;

import fxzone.config.Config;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.render.AbstractGameObject;
import fxzone.engine.utils.GeometryUtils;
import javafx.scene.Group;

public class Particle extends AbstractGameObject{


    private double lifetime = 0;
    private final double maxLifeTime = Math.random() * 0.5;

    private final double initialX, initialY;
    private final double speedX, speedY;

    public Particle(double x, double y, double tileRenderSize, Group group){
        super(AssetHandler.getImage("/images/misc/zone_particle_test_2.png"), x, y, tileRenderSize, tileRenderSize, group);

        this.initialX = x;
        this.initialY = y;

        double[] speed = GeometryUtils.getVectorFromAngle((Math.random() * 2. - 1) * Math.PI);

        double speedMultiplier = tileRenderSize * Config.getDouble("PARTICLE_SPEED") * (Math.random() + 0.5);
        this.speedX = speed[0] * speedMultiplier;
        this.speedY = speed[1] * speedMultiplier;
    }

    public boolean update(double delta){
        lifetime += delta;
        setX(initialX + speedX * lifetime);
        setY(initialY + speedY * lifetime);
        return lifetime >= maxLifeTime;
    }
}
