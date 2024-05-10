package fxzone.game.logic;

import static fxzone.game.logic.Unit.UnitType.ARTILLERY;
import static fxzone.game.logic.Unit.UnitType.ARTILLERY_ROCKET;
import static fxzone.game.logic.Unit.UnitType.CAR_HUMVEE;
import static fxzone.game.logic.Unit.UnitType.INFANTRY;
import static fxzone.game.logic.Unit.UnitType.INFANTRY_RPG;
import static fxzone.game.logic.Unit.UnitType.TANK_BATTLE;
import static fxzone.game.logic.Unit.UnitType.TANK_HUNTER;
import static fxzone.game.logic.Unit.UnitType.TRUCK_TRANSPORT;

import fxzone.game.logic.Unit.UnitType;
import java.util.HashMap;

public class UnitCodex {

    public static final HashMap<UnitType, UnitProfile> UNIT_PROFILE_VALUES = new HashMap<UnitType, UnitProfile>() {{
        put(INFANTRY, new UnitProfile(
            0, 8, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_INFANTRY,
            UnitSuperType.LAND_INFANTRY
        ));
        put(INFANTRY_RPG, new UnitProfile(
            1, 8, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_INFANTRY,
            UnitSuperType.LAND_INFANTRY
        ));
        put(CAR_HUMVEE, new UnitProfile(
            2, 8, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));
        put(TRUCK_TRANSPORT, new UnitProfile(
            3, 5, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));
        put(TANK_HUNTER, new UnitProfile(
            4, 4, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.LAND_VEHICLE
        ));
        put(ARTILLERY, new UnitProfile(
            5, 5, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));
        put(TANK_BATTLE, new UnitProfile(
            6, 3, 10, 100, 10, 10, 1, 1,
            UnitAttackType.RANGERMELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.LAND_VEHICLE
        ));
        put(ARTILLERY_ROCKET, new UnitProfile(
            7, 5, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));

    }};

    public static final HashMap<UnitType, String> UNIT_RESOURCE_NAMES = new HashMap<UnitType, String>(){{
        put(INFANTRY, "infantry");
        put(INFANTRY_RPG, "rpg");
        put(CAR_HUMVEE, "car");
        put(TRUCK_TRANSPORT, "truck_transport");
        put(TANK_HUNTER, "hunter_tank");
        put(ARTILLERY, "artillery");
        put(TANK_BATTLE, "tank");
        put(ARTILLERY_ROCKET, "rocketartillery");
    }};

    public static class UnitProfile{
        public int ID, SPEED, VISION, HEALTH, DAMAGE, DEFENSE, MINRANGE, MAXRANGE;
        public UnitAttackType ATTACKTYPE;
        public UnitArmorClass ARMORCLASS;
        public UnitSuperType SUPERTYPE;
        UnitProfile(
            int ID, int SPEED, int VISION, int HEALTH, int DAMAGE, int DEFENSE, int MINRANGE, int MAXRANGE,
            UnitAttackType ATTACKTYPE,
            UnitArmorClass ARMORCLASS,
            UnitSuperType SUPERTYPE
        ){
            this.ID = ID;
            this.SPEED = SPEED;
            this.VISION = VISION;
            this.HEALTH = HEALTH;
            this.DAMAGE = DAMAGE;
            this.DEFENSE = DEFENSE;
            this.MINRANGE = MINRANGE;
            this.MAXRANGE = MAXRANGE;
            this.ATTACKTYPE = ATTACKTYPE;
            this.ARMORCLASS = ARMORCLASS;
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
    public enum UnitAttackType{
        MELEE,
        RANGED,
        RANGERMELEE,
        PACIFIST
    }
    public enum UnitArmorClass{
        ARMORCLASS_INFANTRY,
        ARMORCLASS_ARMORED,
        ARMORCLASS_HEAVY_ARMOR
    }
}
