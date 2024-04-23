package fxzone.game.logic;

import static fxzone.game.logic.UnitType.ARTILLERY;
import static fxzone.game.logic.UnitType.BATTLE_TANK;
import static fxzone.game.logic.UnitType.HUNTER_TANK;

import java.util.HashMap;

public class UnitCodex {

    public static final HashMap<UnitType, int[]> UNIT_PROFILE_VALUES = new HashMap<UnitType, int[]>() {{
        put(BATTLE_TANK, new int[]{0, 3});
        put(HUNTER_TANK, new int[]{1, 4});
        put(ARTILLERY, new int[]{2, 5});
    }};

    public static final HashMap<UnitType, String> UNIT_RESOURCE_NAMES = new HashMap<UnitType, String>(){{
        put(BATTLE_TANK, "tank");
        put(HUNTER_TANK, "hunter_tank");
        put(ARTILLERY, "artillery");
    }};

}
