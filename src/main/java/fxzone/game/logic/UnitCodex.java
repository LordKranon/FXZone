package fxzone.game.logic;

import java.util.HashMap;
public class UnitCodex {

    public static final HashMap<UnitType, UnitProfile> UNIT_PROFILE_VALUES = new HashMap<UnitType, UnitProfile>() {{
        put(UnitType.INFANTRY, new UnitProfile(
            0, 8, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_INFANTRY,
            UnitSuperType.LAND_INFANTRY
        ));
        put(UnitType.INFANTRY_RPG, new UnitProfile(
            1, 8, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_INFANTRY,
            UnitSuperType.LAND_INFANTRY
        ));
        put(UnitType.CAR_HUMVEE, new UnitProfile(
            2, 8, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.TRUCK_TRANSPORT, new UnitProfile(
            3, 5, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.TANK_HUNTER, new UnitProfile(
            4, 4, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.ARTILLERY, new UnitProfile(
            5, 5, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.TANK_BATTLE, new UnitProfile(
            6, 3, 10, 100, 10, 10, 1, 1,
            UnitAttackType.RANGERMELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.ARTILLERY_ROCKET, new UnitProfile(
            7, 5, 10, 100, 10, 10, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));

    }};

    public static final HashMap<UnitType, String> UNIT_RESOURCE_NAMES = new HashMap<UnitType, String>(){{
        put(UnitType.INFANTRY, "infantry");
        put(UnitType.INFANTRY_RPG, "rpg");
        put(UnitType.CAR_HUMVEE, "car");
        put(UnitType.TRUCK_TRANSPORT, "truck_transport");
        put(UnitType.TANK_HUNTER, "hunter_tank");
        put(UnitType.ARTILLERY, "artillery");
        put(UnitType.TANK_BATTLE, "tank");
        put(UnitType.ARTILLERY_ROCKET, "rocketartillery");
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

    public enum UnitType {
        INFANTRY,
        INFANTRY_RPG,
        CAR_HUMVEE,
        TRUCK_TRANSPORT,
        TANK_HUNTER,
        ARTILLERY,
        TANK_BATTLE,
        ARTILLERY_ROCKET,

        SHIP_LANDER,
        SHIP_GUNBOAT,
        SHIP_DESTROYER,
        SHIP_BATTLESHIP,
        SHIP_CARRIER,

        PLANE_PROPELLER,
        PLANE_JET,
        HELICOPTER_CHINOOK,

        INFANTRY_GUERILLA,
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
