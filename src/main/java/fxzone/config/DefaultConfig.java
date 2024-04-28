package fxzone.config;

public class DefaultConfig {

    public static final String[][] DEFAULT_CONFIG = new String[][]{
        {"CONFIG_VERSION", "0"},

        {"WINDOW_WIDTH", "1280"},
        {"WINDOW_HEIGHT", "720"},
        {"ANTIALIASING", "false"},

        {"PRINT_FPS", "true"},
        {"MAX_FPS", "60"},

        {"MAP_SCROLL_SPEED", "3"},
        {"MIN_TILE_SIZE_ON_ZOOM", "40"},
        {"MAX_TILE_SIZE_ON_ZOOM", "256"},

        {"SERVER_PORT", "21337"},

        {"LAST_USED_IP_ON_JOIN", "localhost"},
        {"LAST_USED_NAME_ON_JOIN", "Player"},
        {"LAST_USED_COLOR_ON_JOIN", "#ff0000"},

        {"GAME_SPEED_UNIT_MOVEMENT_INTERVAL", ".5"}
    };
}
