package fxzone.engine.render;

import fxzone.engine.handler.AssetHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class AbstractGameObject {
    private Image image;
    private ImageView imageView;

    public AbstractGameObject(String pathToImage){
        this.image = AssetHandler.getImage(pathToImage);
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(128);
        this.imageView.setFitHeight(128);
    }
}
