package fxzone.engine.render;

import fxzone.engine.handler.AssetHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class AbstractGameObject {

    private Image image;
    private ImageView imageView;

    private double fitWidth = 128;
    private double fitHeight = 128;

    private double x = 0;
    private double y = 0;

    public AbstractGameObject(){
        initialize();
    }

    public AbstractGameObject(String pathToImage, double x, double y, double w, double h, Group group){
        this.image = AssetHandler.getImage(pathToImage);
        this.x = x;
        this.y = y;
        this.fitWidth = w;
        this.fitHeight = h;
        initialize();
        group.getChildren().add(imageView);
    }

    private void initialize(){
        imageView = new ImageView(image);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setX(x);
        imageView.setY(y);
    }

    public void setViewOrder(double viewOrder){
        imageView.setViewOrder(viewOrder);
    }

    public void setX(double x){
        this.x = x;
        imageView.setX(x);
    }
    public void setY(double y){
        this.y = y;
        imageView.setY(y);
    }
}
