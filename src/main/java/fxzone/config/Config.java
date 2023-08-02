package fxzone.config;

import java.util.Properties;

public class Config {

    private static final String propertiesFilePath = "./fxzone/game.properties";

    private static final Properties properties = new Properties();

    private Config() {

    }

    public static void loadDefaultConfig() {
        for (int i = 0; i < DefaultConfig.DEFAULT_CONFIG.length; i++) {
            properties.setProperty(DefaultConfig.DEFAULT_CONFIG[i][0],
                DefaultConfig.DEFAULT_CONFIG[i][1]);
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

}
