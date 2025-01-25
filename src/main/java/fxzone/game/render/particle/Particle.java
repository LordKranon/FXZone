package fxzone.game.render.particle;

import fxzone.engine.render.AbstractGameObject;
import javafx.scene.Group;
import javafx.scene.image.Image;

public abstract class Particle extends AbstractGameObject{

    private double lifetime = 0;
    double maxLifeTime;

    private final double initialX, initialY;
    double speedX, speedY;

    public Particle(Image image, double x, double y, double tileRenderSize, Group group){
        super(image, x, y, tileRenderSize, tileRenderSize, group);

        this.initialX = x;
        this.initialY = y;

    }

    public boolean update(double delta){
        lifetime += delta;
        setX(initialX + speedX * lifetime);
        setY(initialY + speedY * lifetime);
        return lifetime >= maxLifeTime;
    }
}
