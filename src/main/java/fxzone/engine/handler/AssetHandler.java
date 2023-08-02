package fxzone.engine.handler;

import java.util.HashMap;
import javafx.scene.image.Image;

public class AssetHandler {

    /**
     * Links image paths to loaded images.
     */
    private static final HashMap<String, Image> images = new HashMap<>();

    /**
     * Returns image from 'images' HashMap. If the image is not yet in the HashMap, it is loaded and
     * added to the HashMap.
     *
     * @param path path to the required image
     * @return image at specified path
     */
    public static Image getImage(String path) {
        if (!images.containsKey(path)) {
            loadImage(path);
        }
        return images.get(path);
    }

    /**
     * Loads an Image from a specified path and saves it in the 'images' HashMap.
     *
     * @param path path to image to load
     */
    public static void loadImage(String path) {
        images.put(path, new Image(AssetHandler.class.getResourceAsStream(path)));
    }
}
