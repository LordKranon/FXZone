package fxzone.engine.handler;

import fxzone.game.logic.UnitCodex;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;

public class AssetHandler {

    /**
     * Links image paths to loaded raw images.
     */
    private static final HashMap<String, Image> images = new HashMap<>();

    private static final HashMap<KeyUnitVehicle, Image> imagesUnitsVehicles = new HashMap<>();

    private static final HashMap<KeyMoveCommandArrow, Image> imagesMoveCommandArrows = new HashMap<>();

    private static final HashMap<String, Media> sounds = new HashMap<>();

    /**
     * Returns image from 'images' HashMap. If the image is not yet in the HashMap, it is loaded and
     * added to the HashMap.
     *
     * @param path path to the required image
     * @return image at specified path
     */
    public static Image getImage(String path) {
        if (!imageLoaded(path)) {
            loadImage(path, 256, 256);
        }
        return images.get(path);
    }

    /**
     * Returns image from 'images' HashMap.
     * If the image is not yet in the HashMap, it is loaded with specified dimensions
     * and added to the HashMap.
     *
     * @param path path to the required image
     * @param width image dimensions
     * @param height image dimensions
     * @return image at specified path
     */
    public static Image getImage(String path, int width, int height){
        if(!imageLoaded(path)){
            loadImage(path, width, height);
        }
        return images.get(path);
    }

    private static boolean imageLoaded(String path){
        return images.containsKey(path);
    }

    /**
     * Loads an Image from a specified path and saves it in the 'images' HashMap.
     *
     * @param path path to image to load
     */
    public static void loadImage(String path) {
        images.put(path, new Image(AssetHandler.class.getResourceAsStream(path)));
    }

    /**
     * Loads an Image from a specified path and saves it in the 'images' HashMap.
     * Adjusts the size of the image to specified params.
     *
     * @param path path to image to load
     * @param width image dimensions
     * @param height image dimensions
     */
    public static void loadImage(String path, int width, int height){
        images.put(path, new Image(AssetHandler.class.getResourceAsStream(path), width, height, true, false));
    }

    public static Image getImageUnitVehicle(KeyUnitVehicle keyUnitVehicle){
        if(!imagesUnitsVehicles.containsKey(keyUnitVehicle)){
            Image imageUnitVehicleRaw = getImage("/images/units/"+(UnitCodex.UNIT_RESOURCE_NAMES.get(keyUnitVehicle.keyType))+"_cl.png", 512, 256);
            PixelReader reader = imageUnitVehicleRaw.getPixelReader();
            WritableImage imageUnitVehicleCropped = new WritableImage(
                reader, keyUnitVehicle.keyStance == 0 ? 0 : 256, 0, 256, 256
            );
            imagesUnitsVehicles.put(keyUnitVehicle, imageUnitVehicleCropped);
        }
        return imagesUnitsVehicles.get(keyUnitVehicle);
    }

    public static Image getImageMoveCommandArrow(KeyMoveCommandArrow keyMoveCommandArrow){
        if(!imagesMoveCommandArrows.containsKey(keyMoveCommandArrow)){
            Image imageMoveCommandArrowRaw = getImage("/images/misc/"+keyMoveCommandArrow.keyPath+".png", 256, 256);

            /*
            PixelReader reader = imageMoveCommandArrowRaw.getPixelReader();
            WritableImage imageMoveCommandArrowEdited = new WritableImage(
                reader,
                keyMoveCommandArrow.mirrorOnX ? 256 : 0,
                keyMoveCommandArrow.mirrorOnY ? 256 : 0,
                keyMoveCommandArrow.mirrorOnX ? 0 : 256,
                keyMoveCommandArrow.mirrorOnY ? 0 : 256
            );
             */


            imagesMoveCommandArrows.put(keyMoveCommandArrow, imageMoveCommandArrowRaw);
        }
        return imagesMoveCommandArrows.get(keyMoveCommandArrow);
    }

    public static Media getSound(String path){
        if(!sounds.containsKey(path)){
            loadSound(path);
        }
        return sounds.get(path);
    }

    public static void loadSound(String path){
        sounds.put(path, new Media(AssetHandler.class.getResource(path).toExternalForm()));
    }

}
