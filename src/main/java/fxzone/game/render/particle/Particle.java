package fxzone.game.render.particle;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.render.AbstractGameObject;
import fxzone.engine.utils.GeometryUtils;
import fxzone.engine.utils.GeometryUtils.DoublePoint;
import fxzone.engine.utils.ViewOrder;
import java.util.ArrayList;
import javafx.scene.Group;

public class Particle extends AbstractGameObject{


    private double lifetime = 0;

    private double initialX, initialY;
    private double speedX, speedY;

    public Particle(double x, double y, double tileRenderSize, Group group){
        super(AssetHandler.getImage("/images/misc/zone_particle_test_2.png"), x, y, tileRenderSize, tileRenderSize, group);
        this.initialX = x;
        this.initialY = y;
        DoublePoint speed = GeometryUtils.getVectorFromAngle((Math.random() * 2. - 1) * Math.PI);
        this.speedX = speed.x * tileRenderSize * 2;
        this.speedY = speed.y * tileRenderSize * 2;
    }

    public boolean update(double delta){
        lifetime += delta;
        setX(initialX + speedX * lifetime);
        setY(initialY + speedY * lifetime);
        return lifetime >= 1;
    }
}
