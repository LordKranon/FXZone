package fxzone.save;

import fxzone.game.logic.Map;
import fxzone.game.logic.serializable.MapSerializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Save {

    private static final String SAVE_FILE_PATH = "./fxzone/map.zonemap";

    public static MapSerializable loadMap(){
        try {
            File saveFile = new File(SAVE_FILE_PATH);

            if (!saveFile.exists()) {
                System.err.println("[SAVE] [loadMap] Save file does not exist");
                return null;
            }

            ObjectInputStream inStream =
                new ObjectInputStream(new FileInputStream(saveFile));
            MapSerializable map = (MapSerializable) inStream.readObject();
            inStream.close();

            return map;

        } catch (IOException | IllegalArgumentException | SecurityException | ClassNotFoundException e) {
            System.err.println("[SAVE] [loadMap] Error on loading map");
            return null;
        }
    }

    public static void saveMap(MapSerializable map){
        try {
            File saveFile = new File(SAVE_FILE_PATH);
            ObjectOutputStream outStream =
                new ObjectOutputStream(new FileOutputStream(saveFile));
            outStream.writeObject(map);
            outStream.close();
        } catch (IOException e) {
            System.err.println("[SAVE] [saveMap] Error on saving map");
        }
    }
}
