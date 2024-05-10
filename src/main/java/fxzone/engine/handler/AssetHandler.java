package fxzone.engine.handler;

import fxzone.game.logic.Unit.UnitType;
import fxzone.game.logic.UnitCodex;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javax.imageio.ImageIO;

public class AssetHandler {

    /**
     * Links image paths to loaded raw images.
     */
    private static final HashMap<String, Image> images = new HashMap<>();

    private static final HashMap<KeyUnit, Image> imagesUnitsVehicles = new HashMap<>();

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

    private static BufferedImage loadBufferedImage(String path){
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(AssetHandler.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public static Image getImageUnitVehicle(KeyUnit keyUnit){
        if(!imagesUnitsVehicles.containsKey(keyUnit)){

            BufferedImage bImgColoredPartRaw = loadBufferedImage("/images/units/"+(UnitCodex.UNIT_RESOURCE_NAMES.get(keyUnit.keyType))+"_cp.png");
            BufferedImage bImgColoredPartCropped = bImgColoredPartRaw.getSubimage(
                ((keyUnit.keyStance % 2) == 0) ? 0 : 24,
                (keyUnit.keyStance < 2) ? 0 : 24,
                24, 24
            );

            BufferedImage bImgUncoloredPartRaw = loadBufferedImage("/images/units/"+(UnitCodex.UNIT_RESOURCE_NAMES.get(keyUnit.keyType))+"_up.png");
            BufferedImage bImgUncoloredPartCropped = bImgUncoloredPartRaw.getSubimage(
                ((keyUnit.keyStance % 2) == 0) ? 0 : 24,
                (keyUnit.keyStance < 2) ? 0 : 24,
                24, 24
            );

            BufferedImage bImgColoredPartRecolored = applyColor(bImgColoredPartCropped, keyUnit.keyColor);

            java.awt.Image awtImgColoredPartResized = bImgColoredPartRecolored.getScaledInstance(256, 256, java.awt.Image.SCALE_DEFAULT);
            java.awt.Image awtImgUncoloredPartResized = bImgUncoloredPartCropped.getScaledInstance(256, 256, java.awt.Image.SCALE_DEFAULT);
            BufferedImage bImgCombined = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);

            Graphics2D graphics2D = bImgCombined.createGraphics();
            graphics2D.drawImage(awtImgColoredPartResized, 0, 0, null);
            graphics2D.drawImage(awtImgUncoloredPartResized, 0, 0, null);
            graphics2D.dispose();

            Image imgFinished = SwingFXUtils.toFXImage(bImgCombined, null);

            imagesUnitsVehicles.put(keyUnit, imgFinished);
        }
        return imagesUnitsVehicles.get(keyUnit);
    }

    /**
     * Repaint a given image of a unit or building to have the color of the owning player.
     * Magic function copied from legacy code.
     * WARNING: To save performance, use this on a small image and then resize afterwards.
     */
    private static BufferedImage applyColor(BufferedImage bufferedImage, java.awt.Color color){
        if(color == null){
            return bufferedImage;
        }
        /*
        MAGIC from old Zone
         */

        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();

        int[][] a = new int[h][w];
        int[][] r = new int[h][w];
        int[][] g = new int[h][w];
        int[][] b = new int[h][w];

        int[] rgb = bufferedImage.getRGB(0, 0, w, h, null, 0, w);

        /*Player Color*/
        int rgbP = color.getRGB();
        int aP = (rgbP>>24)&0xff;
        int rP = (rgbP>>16)&0xff;
        int gP = (rgbP>>8)&0xff;
        int bP = (rgbP)&0xff;


        int count = 0;
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                a[i][j] = ((rgb[count]>>24)&0xff);
                r[i][j] = ((rgb[count]>>16)&0xff);
                g[i][j] = ((rgb[count]>>8)&0xff);
                b[i][j] = ((rgb[count])&0xff);


                /*CHANGES TO COLORS HERE*/

                int rAvg = (r[i][j] + rP)/2;
                //rAvg = rP; //temp
                r[i][j] = rAvg;
                if(r[i][j]>255) {
                    r[i][j] = 255;
                }
                int gAvg = (g[i][j] + gP)/2;
                //gAvg = gP; //temp
                g[i][j] = gAvg;
                if(g[i][j]>255) {
                    g[i][j] = 255;
                }
                int bAvg = (b[i][j] + bP)/2;
                //bAvg = bP; //temp
                b[i][j] = bAvg;
                if(b[i][j]>255) {
                    b[i][j] = 255;
                }


                count++;
            }
        }

        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {

                int rgb1 = ((a[i][j]&0xff)<<24|
                    (r[i][j]&0xff)<<16|
                    (g[i][j]&0xff)<<8|
                    (b[i][j]&0xff));

                bufferedImage.setRGB(j, i, rgb1);
            }
        }
        return bufferedImage;
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

    public static Media getSound(UnitType unitType){
        switch (UnitCodex.getUnitProfile(unitType).SUPERTYPE){
            case LAND_INFANTRY: return getSound("/sounds/mixkit-footsteps-through-the-wastelands-540.mp3");
            default: return getSound("/sounds/mixkit-truck-driving-steady-1621.mp3");
        }
    }
}
