package fxzone.game.logic;

import static fxzone.game.logic.Unit.UnitType.ARTILLERY;
import static fxzone.game.logic.Unit.UnitType.TANK_BATTLE;
import static fxzone.game.logic.Unit.UnitType.TANK_HUNTER;

import fxzone.game.logic.Unit.UnitType;
import java.util.HashMap;

public class UnitCodex {

    public static final HashMap<UnitType, UnitProfile> UNIT_PROFILE_VALUES = new HashMap<UnitType, UnitProfile>() {{
        put(TANK_BATTLE, new UnitProfile(
            0, 3, 100, 50
        ));
        put(TANK_HUNTER, new UnitProfile(
            1, 4, 70, 30
        ));
        put(ARTILLERY, new UnitProfile(
            2, 5, 40, 10
        ));
    }};

    public static final HashMap<UnitType, String> UNIT_RESOURCE_NAMES = new HashMap<UnitType, String>(){{
        put(TANK_BATTLE, "tank");
        put(TANK_HUNTER, "hunter_tank");
        put(ARTILLERY, "artillery");
    }};

    public static class UnitProfile{
        public int ID, SPEED, HEALTH, DAMAGE;
        UnitProfile(int ID, int SPEED, int HEALTH, int DAMAGE){
            this.ID = ID;
            this.SPEED = SPEED;
            this.HEALTH = HEALTH;
            this.DAMAGE = DAMAGE;
        }
    }

    public static UnitProfile getUnitProfile(UnitType unitType){
        return UNIT_PROFILE_VALUES.get(unitType);
    }
}
