package fxzone.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

    private static final String propertiesFilePath = "./fxzone/game.properties";

    private static final String propertiesFileComment = "FXZone config file";

    private static final Properties properties = new Properties();

    private Config() {

    }

    public static void loadDefaultConfig() {
        for (int i = 0; i < DefaultConfig.DEFAULT_CONFIG.length; i++) {
            properties.setProperty(DefaultConfig.DEFAULT_CONFIG[i][0],
                DefaultConfig.DEFAULT_CONFIG[i][1]);
        }
        System.out.println("[CONFIG] Loaded default config");
    }
    public static void saveConfig(){
        try {
            OutputStream out = new FileOutputStream(propertiesFilePath);
            properties.store(out, propertiesFileComment);
            out.close();
        } catch (IOException e){
            System.err.println("[CONFIG] Exception when saving config");
            e.printStackTrace();
        }
    }
    public static void loadConfig(){
        try {
            File propertiesFile = new File(propertiesFilePath);

            // If the config doesn't exist load the default config
            if (!propertiesFile.exists()) {
                try {
                    Files.createDirectories(Paths.get("./fxzone"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                loadDefaultConfig();
                return;
            }

            BufferedInputStream inStream =
                new BufferedInputStream(new FileInputStream(propertiesFile));
            properties.load(inStream);
            inStream.close();

            // If the config is outdated reset the config to the default config.
            if (!properties.containsKey("CONFIG_VERSION")
                &&
                !properties.getProperty("CONFIG_VERSION").equals(DefaultConfig.DEFAULT_CONFIG[0][1])) {
                loadDefaultConfig();
            }
        } catch (IOException | IllegalArgumentException | SecurityException e) {
            System.err.println("[CONFIG] Could not load a saved config, loading default config");
            loadDefaultConfig();
        }
    }

    public static String getString(String property) {
        //If the property doesn't exist, check if the config is in the default config and return it.
        if (!properties.containsKey(property)) {
            for (int i = 0; i < DefaultConfig.DEFAULT_CONFIG.length; i++) {
                if (DefaultConfig.DEFAULT_CONFIG[i][0].equals(property)) {
                    properties.setProperty(property, DefaultConfig.DEFAULT_CONFIG[i][1]);
                    return DefaultConfig.DEFAULT_CONFIG[i][1];
                }
            }
            throw new IllegalArgumentException(
                "Error in Config: Property \"" + property + "\" doesn't exist!");
        }
        return properties.getProperty(property);
    }

    public static int getInt(String property) {
        return Integer.parseInt(getString(property));
    }
    public static boolean getBool(String property) {
        return Boolean.parseBoolean(getString(property));
    }
    public static double getDouble(String property){
        return Double.parseDouble(getString(property));
    }

}
