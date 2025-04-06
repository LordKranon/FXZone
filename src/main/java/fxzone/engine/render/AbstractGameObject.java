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

    public AbstractGameObject(Image image, double x, double y, double w, double h, Group group){
        this.image = image;
        this.x = x;
        this.y = y;
        this.fitWidth = w;
        this.fitHeight = h;
        initialize();
        if(group != null) group.getChildren().add(imageView);
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
    public void setVisible(boolean visible){
        imageView.setVisible(visible);
    }

    public void setFit(double fit){
        this.fitWidth = fit;
        this.fitHeight = fit;
        this.imageView.setFitWidth(fitWidth);
        this.imageView.setFitHeight(fitHeight);
    }
    public double getFitWidth(){
        return fitWidth;
    }
    public double getFitHeight(){
        return fitHeight;
    }

    public void setImage(Image image){
        this.image = image;
        this.imageView.setImage(image);
    }

    public void setScale(double scaleX, double scaleY){
        this.imageView.setScaleX(scaleX);
        this.imageView.setScaleY(scaleY);
    }

    /**
     * Remove this objects imageView from the root group.
     * Called when this object will no longer be rendered again,
     * E.g. a UI Unit move command arrow after the move command is issued/cancelled.
     *
     * @param group the root group that the image was visible in
     */
    public void removeSelfFromRoot(Group group){
        group.getChildren().remove(imageView);
    }

    protected ImageView getImageView(){
        return this.imageView;
    }
}
