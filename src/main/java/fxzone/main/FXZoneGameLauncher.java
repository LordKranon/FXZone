package fxzone.main;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamGameServerAPI;

public class FXZoneGameLauncher {

    public static void main(String[] args){
        try {
            SteamAPI.loadLibraries();
            SteamGameServerAPI.loadLibraries();

            if (!SteamAPI.init()) {
                SteamAPI.printDebugInfo(System.err);
                throw new RuntimeException("Could not initialize Steam SDK!");
            }

            FXZoneGameApplication.init(args);
        } catch (SteamException e) {
            e.printStackTrace();
            SteamAPI.printDebugInfo(System.err);
        }
    }
}
