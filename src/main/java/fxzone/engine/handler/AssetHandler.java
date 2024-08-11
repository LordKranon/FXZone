package fxzone.engine.handler;

import fxzone.engine.utils.GeometryUtils;
import fxzone.game.logic.Codex.TileSuperType;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.Codex;
import fxzone.game.logic.Tile.TileType;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javax.imageio.ImageIO;

public class AssetHandler {

    /*
    DEBUG
     */
    private static final boolean verbose = true;

    /*
    LEGACY
     */
    private static final int BUFFERED_IMAGE_TYPE = 2;

    /**
     * Links image paths to loaded raw images.
     */
    private static final HashMap<String, Image> images = new HashMap<>();

    private static final HashMap<KeyUnit, Image> imagesUnits = new HashMap<>();
    private static final HashMap<KeyBuilding, Image> imagesBuildings = new HashMap<>();
    private static final HashMap<KeyTile, Image> imagesTiles = new HashMap<>();

    private static final HashMap<String, BufferedImage> bufferedImages = new HashMap<>();

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

    /**
     * Load a BufferedImage that always needs to exist in colored state.
     * Will save this BufferedImage with color applied and that image will only ever be accessible colored.
     */
    private static BufferedImage getBufferedImageColorApplied(String path, java.awt.Color color){
        if(!bufferedImages.containsKey(path)){
            BufferedImage bufferedImage = loadBufferedImage(path);
            applyColor(bufferedImage, color);
            bufferedImages.put(path, bufferedImage);
        }
        return bufferedImages.get(path);
    }
    private static BufferedImage getBufferedImage(String path){
        if(!bufferedImages.containsKey(path)){
            BufferedImage bufferedImage = loadBufferedImage(path);
            bufferedImages.put(path, bufferedImage);
        }
        return bufferedImages.get(path);
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

    public static Image getImageTile(KeyTile keyTile){
        if(!imagesTiles.containsKey(keyTile)){

            String pathToWaterImg = "/images/terrain/tiles/tile_water_"+(keyTile.keyTileStance?"1":"0")+".png";

            if(keyTile.keyTileType == TileType.WATER){
                // No further handling needed for water
                Image img = new Image(AssetHandler.class.getResourceAsStream(pathToWaterImg), 256, 256, true, false);
                imagesTiles.put(keyTile, img);
            } else {

                String pathToTileSetBeach = "/images/terrain.tilesets/tileset_beach.png";
                BufferedImage tilesetBeach = getBufferedImageColorApplied(pathToTileSetBeach, Color.white);

                if(keyTile.keyTileType == TileType.PLAINS){
                    String pathToBaseImg = "/images/terrain/tiles/tile_plains.png";
                    String pathToEdgesImg = "/images/terrain.tilesets/tileset_plains_edges.png";

                    BufferedImage base = getBufferedImage(pathToBaseImg);
                    BufferedImage edgesSet = getBufferedImage(pathToEdgesImg);
                    BufferedImage baseWater = getBufferedImage(pathToWaterImg);

                    BufferedImage baseAllCornersCut = new BufferedImage(24, 24, BUFFERED_IMAGE_TYPE);
                    Graphics gBaseAllCornersCut = baseAllCornersCut.createGraphics();

                    // Draw base underlying water
                    gBaseAllCornersCut.drawImage(baseWater, 0, 0, null);

                    // Fill darkened corners for neighboring beaches
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_BEACH){
                        gBaseAllCornersCut.drawImage(tilesetBeach.getSubimage(72, 24, 24, 24), 0, 0, 24, 24, 0, 24, 24, 0, null);
                    }
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_BEACH){
                        gBaseAllCornersCut.drawImage(tilesetBeach.getSubimage(72, 0, 24, 24), 0, 0, 24, 24, 24, 0, 0, 24, null);
                    }
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_BEACH){
                        gBaseAllCornersCut.drawImage(tilesetBeach.getSubimage(72, 24, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                    }
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_BEACH){
                        gBaseAllCornersCut.drawImage(tilesetBeach.getSubimage(72, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                    }

                    // Draw main terrain cross without corners
                    gBaseAllCornersCut.drawImage(base.getSubimage(0, 4, 24, 16), 0, 4, 24, 20, 0, 0, 24, 16, null);
                    gBaseAllCornersCut.drawImage(base.getSubimage(4, 0, 16, 24), 4, 0, 20, 24, 0, 0, 16, 24, null);
                    gBaseAllCornersCut.dispose();

                    BufferedImage terrained = new BufferedImage(24, 24, BUFFERED_IMAGE_TYPE);
                    Graphics gTerrain = terrained.createGraphics();

                    // Put the base image with 4 empty corners
                    gTerrain.drawImage(baseAllCornersCut, 0, 0, 24, 24, 0, 0, 24, 24, null);

                    int directPlainsNeighbors = 0;

                    // Fill corners where necessary
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND){
                        gTerrain.drawImage(base.getSubimage(0, 0, 24, 4), 0, 0, 24, 4, 0, 0, 24, 4, null);
                        directPlainsNeighbors++;
                    }
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND){
                        gTerrain.drawImage(base.getSubimage(0, 0, 4, 24), 0, 0, 4, 24, 0, 0, 4, 24, null);
                        directPlainsNeighbors++;
                    }
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                        gTerrain.drawImage(base.getSubimage(0, 20, 24, 4), 0, 20, 24, 24, 0, 0, 24, 4, null);
                        directPlainsNeighbors++;
                    }
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND){
                        gTerrain.drawImage(base.getSubimage(20, 0, 4, 24), 20, 0, 24, 24, 0, 0, 4, 24, null);
                        directPlainsNeighbors++;
                    }


                    // Draw Edges
                    switch (directPlainsNeighbors){

                        // No direct plains neighbors: This is a 1 tile island
                        case 0:
                            gTerrain.drawImage(edgesSet.getSubimage(48, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            break;

                        // 1 direct plains neighbor: This is a peninsula, determine direction of peninsula
                        case 1:
                            if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(24, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(72, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(24, 0, 24, 24), 0, 0, 24, 24, 0, 24, 24, 0, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(72, 0, 24, 24), 0, 0, 24, 24, 24, 0, 0, 24, null);
                            } else {
                                System.err.println("[ASSET-HANDLER] FATAL ERROR on terrain generation");
                            }
                            break;

                        // 2 neighbors: Either a corner or a double side
                        case 2:
                            if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(0, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(0, 0, 24, 24), 0, 0, 24, 24, 24, 0, 0, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(0, 24, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                                gTerrain.drawImage(edgesSet.getSubimage(0, 24, 24, 24), 0, 0, 24, 24, 24, 0, 0, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(48, 24, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                                gTerrain.drawImage(edgesSet.getSubimage(48, 24, 24, 24), 0, 0, 24, 24, 0, 24, 24, 0, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(0, 0, 24, 24), 0, 0, 24, 24, 0, 24, 24, 0, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(0, 0, 24, 24), 0, 0, 24, 24, 24, 24, 0, 0, null);
                            } else {
                                System.err.println("[ASSET-HANDLER] FATAL ERROR on terrain generation");
                            }
                            break;

                        // 3 neighbors: One side to the sea
                        case 3:
                            if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] != TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(48, 24, 24, 24), 0, 0, 24, 24, 0, 24, 24, 0, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] != TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(0, 24, 24, 24), 0, 0, 24, 24, 24, 0, 0, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] != TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(48, 24, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] != TileSuperType.TS_LAND){
                                gTerrain.drawImage(edgesSet.getSubimage(0, 24, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else {
                                System.err.println("[ASSET-HANDLER] FATAL ERROR on terrain generation");
                            }
                            break;

                        // 4 neighbors: landlocked
                        case 4: break;

                        default:
                            System.err.println("[ASSET-HANDLER] FATAL ERROR on terrain generation");
                            break;
                    }

                    gTerrain.dispose();

                    BufferedImage upscaled = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
                    Graphics gUpscaled = upscaled.createGraphics();
                    java.awt.Image awtImgCorrectCorners = terrained.getScaledInstance(256, 256, java.awt.Image.SCALE_DEFAULT);
                    gUpscaled.drawImage(awtImgCorrectCorners, 0, 0, null);
                    gUpscaled.dispose();

                    Image imageFinished = SwingFXUtils.toFXImage(upscaled, null);

                    if(verbose) System.out.println("[ASSET-HANDLER] [getImageTile] Loaded terrain variation: "+keyTile.keyTileType+" alternate: "+keyTile.keyTileStance+" "+keyTile);
                    imagesTiles.put(keyTile, imageFinished);
                }
                else if (keyTile.keyTileType == TileType.BEACH){

                    BufferedImage baseWater = getBufferedImage(pathToWaterImg);


                    int directPlainsNeighbors = 0;

                    // Determine amount of neighbors to deduct which beach tile to draw
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND){
                        directPlainsNeighbors++;
                    }
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND){
                        directPlainsNeighbors++;
                    }
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                        directPlainsNeighbors++;
                    }
                    if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND){
                        directPlainsNeighbors++;
                    }

                    BufferedImage base = new BufferedImage(24, 24, BUFFERED_IMAGE_TYPE);
                    Graphics gBase = base.createGraphics();
                    gBase.drawImage(baseWater, 0, 0, null);

                    // Draw the beach tile
                    switch (directPlainsNeighbors){
                        case 0:
                        case 4:
                            gBase.drawImage(tilesetBeach.getSubimage(0, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            break;
                        case 1:
                            if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(24, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(48, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(24, 0, 24, 24), 0, 0, 24, 24, 0, 24, 24, 0, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(48, 0, 24, 24), 0, 0, 24, 24, 24, 0, 0, 24, null);
                            } else {
                                System.err.println("[ASSET-HANDLER] FATAL ERROR on terrain generation");
                            }
                            break;
                        case 2:
                            if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(0, 24, 24, 24), 0, 0, 24, 24, 24, 0, 0, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(0, 24, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                                // No good fit
                                gBase.drawImage(tilesetBeach.getSubimage(0, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND){
                                // No good fit
                                gBase.drawImage(tilesetBeach.getSubimage(0, 0, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(0, 24, 24, 24), 0, 0, 24, 24, 24, 24, 0, 0, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] == TileSuperType.TS_LAND &&
                                keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] == TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(0, 24, 24, 24), 0, 0, 24, 24, 0, 24, 24, 0, null);
                            } else {
                                System.err.println("[ASSET-HANDLER] FATAL ERROR on terrain generation");
                            }
                            break;
                        case 3:
                            if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.NORTH] != TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(24, 24, 24, 24), 0, 0, 24, 24, 0, 24, 24, 0, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.WEST] != TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(48, 24, 24, 24), 0, 0, 24, 24, 24, 0, 0, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.SOUTH] != TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(24, 24, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else if(keyTile.keyTileTypesOfNeighbors[GeometryUtils.EAST] != TileSuperType.TS_LAND){
                                gBase.drawImage(tilesetBeach.getSubimage(48, 24, 24, 24), 0, 0, 24, 24, 0, 0, 24, 24, null);
                            } else {
                                System.err.println("[ASSET-HANDLER] FATAL ERROR on terrain generation");
                            }
                            break;
                        default:
                            System.err.println("[ASSET-HANDLER] FATAL ERROR on terrain generation");
                            break;
                    }

                    gBase.dispose();

                    BufferedImage upscaled = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
                    Graphics gUpscaled = upscaled.createGraphics();
                    java.awt.Image awtImgCorrectCorners = base.getScaledInstance(256, 256, java.awt.Image.SCALE_DEFAULT);
                    gUpscaled.drawImage(awtImgCorrectCorners, 0, 0, null);
                    gUpscaled.dispose();

                    Image imageFinished = SwingFXUtils.toFXImage(upscaled, null);
                    imagesTiles.put(keyTile, imageFinished);
                }
            }
        }
        return imagesTiles.get(keyTile);
    }
    public static Image getImageUnit(KeyUnit keyUnit){
        if(!imagesUnits.containsKey(keyUnit)){
            Image img = loadImageOwnableTileSpaceObject(
                "units/"+Codex.UNIT_RESOURCE_NAMES.get(keyUnit.keyUnitType),
                ((keyUnit.keyStance % 2) == 0) ? 0 : 24,
                (keyUnit.keyStance < 2) ? 0 : 24,
                keyUnit.keyColor
            );
            imagesUnits.put(keyUnit, img);
        }
        return imagesUnits.get(keyUnit);
    }
    public static Image getImageBuilding(KeyBuilding keyBuilding){
        if(!imagesBuildings.containsKey(keyBuilding)){
            Image img = loadImageOwnableTileSpaceObject(
                "buildings/"+(Codex.BUILDING_RESOURCE_NAMES.get(keyBuilding.keyBuildingType)),
                0,
                0,
                keyBuilding.keyColor
            );
            imagesBuildings.put(keyBuilding, img);
        }
        return imagesBuildings.get(keyBuilding);
    }
    private static Image loadImageOwnableTileSpaceObject(String resourceName, int subImageX, int subImageY, java.awt.Color color){
        BufferedImage bImgColoredPartRaw = loadBufferedImage("/images/"+resourceName+"_cp.png");
        BufferedImage bImgColoredPartCropped = bImgColoredPartRaw.getSubimage(
            subImageX,
            subImageY,
            24, 24
        );

        BufferedImage bImgUncoloredPartRaw = getBufferedImage("/images/"+resourceName+"_up.png");
        BufferedImage bImgUncoloredPartCropped = bImgUncoloredPartRaw.getSubimage(
            subImageX,
            subImageY,
            24, 24
        );

        BufferedImage bImgColoredPartRecolored = applyColor(bImgColoredPartCropped, color);

        java.awt.Image awtImgColoredPartResized = bImgColoredPartRecolored.getScaledInstance(256, 256, java.awt.Image.SCALE_DEFAULT);
        java.awt.Image awtImgUncoloredPartResized = bImgUncoloredPartCropped.getScaledInstance(256, 256, java.awt.Image.SCALE_DEFAULT);
        BufferedImage bImgCombined = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bImgCombined.createGraphics();
        graphics2D.drawImage(awtImgColoredPartResized, 0, 0, null);
        graphics2D.drawImage(awtImgUncoloredPartResized, 0, 0, null);
        graphics2D.dispose();

        Image imgFinished = SwingFXUtils.toFXImage(bImgCombined, null);
        return imgFinished;
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
        switch (Codex.getUnitProfile(unitType).SUPERTYPE){
            case LAND_INFANTRY: return getSound("/sounds/mixkit-footsteps-through-the-wastelands-540.mp3");
            default: return getSound("/sounds/mixkit-truck-driving-steady-1621.mp3");
        }
    }
}
