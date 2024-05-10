package fxzone.game.logic;

import static fxzone.game.logic.Unit.UnitType.ARTILLERY;
import static fxzone.game.logic.Unit.UnitType.INFANTRY;
import static fxzone.game.logic.Unit.UnitType.TANK_BATTLE;
import static fxzone.game.logic.Unit.UnitType.TANK_HUNTER;

import fxzone.game.logic.Unit.UnitType;
import java.util.HashMap;

public class UnitCodex {

    public static final HashMap<UnitType, UnitProfile> UNIT_PROFILE_VALUES = new HashMap<UnitType, UnitProfile>() {{
        put(INFANTRY, new UnitProfile(
            0, 8, 100, 10, UnitSuperType.LAND_INFANTRY
        ));
        put(TANK_BATTLE, new UnitProfile(
            0, 3, 100, 50, UnitSuperType.LAND_VEHICLE
        ));
        put(TANK_HUNTER, new UnitProfile(
            1, 4, 70, 30, UnitSuperType.LAND_VEHICLE
        ));
        put(ARTILLERY, new UnitProfile(
            2, 5, 40, 10, UnitSuperType.LAND_VEHICLE
        ));
    }};

    public static final HashMap<UnitType, String> UNIT_RESOURCE_NAMES = new HashMap<UnitType, String>(){{
        put(INFANTRY, "infantry");
        put(TANK_BATTLE, "tank");
        put(TANK_HUNTER, "hunter_tank");
        put(ARTILLERY, "artillery");
    }};

    public static class UnitProfile{
        public int ID, SPEED, HEALTH, DAMAGE;
        public UnitSuperType SUPERTYPE;
        UnitProfile(int ID, int SPEED, int HEALTH, int DAMAGE, UnitSuperType SUPERTYPE){
            this.ID = ID;
            this.SPEED = SPEED;
            this.HEALTH = HEALTH;
            this.DAMAGE = DAMAGE;
            this.SUPERTYPE = SUPERTYPE;
        }
    }
    public static UnitProfile getUnitProfile(UnitType unitType){
        return UNIT_PROFILE_VALUES.get(unitType);
    }

    public enum UnitSuperType{
        LAND_INFANTRY,
        LAND_VEHICLE,
        AIRCRAFT,
        SHIP
    }
}
