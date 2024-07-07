package fxzone.game.logic;

import fxzone.game.logic.Unit.UnitState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Codex {

    public static final HashMap<UnitType, UnitProfile> UNIT_PROFILE_VALUES = new HashMap<UnitType, UnitProfile>() {{
        put(UnitType.INFANTRY, new UnitProfile(
            0, "Infantry",
            3, 5, 100, 3, 0, 1, 1,
            1.5, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_INFANTRY,
            UnitSuperType.LAND_INFANTRY
        ));
        put(UnitType.INFANTRY_RPG, new UnitProfile(
            1, "RPG",
            3, 10, 100, 4, 0, 1, 1,
            .5, 1.5, 1.5,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_INFANTRY,
            UnitSuperType.LAND_INFANTRY
        ));
        put(UnitType.CAR_HUMVEE, new UnitProfile(
            2, "Car",
            7, 10, 100, 4, 2, 1, 1,
            1.5, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.TRUCK_TRANSPORT, new UnitProfile(
            3, "Truck",
            4, 10, 100, 0, 1, 1, 1,
            1, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.TANK_HUNTER, new UnitProfile(
            4, "Hunter Tank",
            5, 10, 100, 6, 5, 1, 1,
            1, 1.25, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.ARTILLERY, new UnitProfile(
            5, "Artillery",
            3, 10, 100, 6, 4, 3, 5,
            1.5, 1, 1,
            UnitAttackType.RANGED,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.TANK_BATTLE, new UnitProfile(
            6, "Battle Tank",
            4, 10, 100, 9, 7, 1, 2,
            1, 1, 1.5,
            UnitAttackType.RANGERMELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.LAND_VEHICLE
        ));
        put(UnitType.ARTILLERY_ROCKET, new UnitProfile(
            7, "Rocket Artillery",
            3, 10, 100, 6, 2, 4, 6,
            1, 1.5, 1.5,
            UnitAttackType.RANGED,
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
    public static final HashMap<BuildingType, String> BUILDING_RESOURCE_NAMES = new HashMap<BuildingType, String>(){{
        put(BuildingType.CITY, "city");
        put(BuildingType.FACTORY, "factory");
        put(BuildingType.PORT, "port");
        put(BuildingType.AIRPORT, "airport");
    }};

    public static final List<UnitType> BUILDABLE_UNIT_TYPES = Arrays.asList(
        UnitType.INFANTRY,
        UnitType.INFANTRY_RPG,
        UnitType.CAR_HUMVEE,
        UnitType.TRUCK_TRANSPORT,
        UnitType.TANK_HUNTER,
        UnitType.ARTILLERY,
        UnitType.TANK_BATTLE,
        UnitType.ARTILLERY_ROCKET
    );

    public static class UnitProfile{
        public String NAME;
        public int ID, SPEED, VISION, HEALTH, DAMAGE, DEFENSE, MINRANGE, MAXRANGE;
        public double DMG_VS_INFANTRY, DMG_VS_ARMORED, DMG_VS_HEAVY;
        public UnitAttackType ATTACKTYPE;
        public UnitArmorClass ARMORCLASS;
        public UnitSuperType SUPERTYPE;
        UnitProfile(
            int ID, String NAME,
            int SPEED, int VISION, int HEALTH, int DAMAGE, int DEFENSE, int MINRANGE, int MAXRANGE,
            double DMG_VS_INFANTRY, double DMG_VS_ARMORED, double DMG_VS_HEAVY,
            UnitAttackType ATTACKTYPE,
            UnitArmorClass ARMORCLASS,
            UnitSuperType SUPERTYPE
        ){
            this.ID = ID;
            this.NAME = NAME;

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

            this.DMG_VS_INFANTRY = DMG_VS_INFANTRY;
            this.DMG_VS_ARMORED = DMG_VS_ARMORED;
            this.DMG_VS_HEAVY = DMG_VS_HEAVY;
        }
    }
    public static UnitProfile getUnitProfile(UnitType unitType){
        return UNIT_PROFILE_VALUES.get(unitType);
    }
    public static UnitProfile getUnitProfile(Unit unit){
        return getUnitProfile(unit.getUnitType());
    }

    public enum BuildingType {
        CITY,
        FACTORY,
        PORT,
        AIRPORT,

        /*
        HQ,
        CITY_COMPLEX,
        NUCLEAR_REACTOR,
        STORAGE,
        TENT
         */
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
/*
        SHIP_LANDER,
        SHIP_GUNBOAT,
        SHIP_DESTROYER,
        SHIP_BATTLESHIP,
        SHIP_CARRIER,

        PLANE_PROPELLER,
        PLANE_JET,
        HELICOPTER_CHINOOK,

        INFANTRY_GUERRILLA,

 */
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

    public static int calculateDamageOnAttack(Unit offender, Unit defender){
        /* From old Zone */

        double offenderRemainingHp = offender.getRemainingHealthOnAttack();
        double offenderMaxHp = getUnitProfile(offender).HEALTH;
        double defenderDefense = getUnitProfile(defender).DEFENSE;

        int rawDamage = getUnitProfile(offender.getUnitType()).DAMAGE;

        if(offender.getUnitState() == UnitState.COUNTERATTACKING){
            rawDamage /= 2;
        }
        double scaledDamage = (double)rawDamage * 10.0 * ((offenderRemainingHp + offenderMaxHp) / (2.0 * offenderMaxHp));

        // Add damage bonuses for armor class
        double damageMultiplier = 1.0;
        switch (getUnitProfile(defender.getUnitType()).ARMORCLASS){
            case ARMORCLASS_INFANTRY: damageMultiplier *= getUnitProfile(offender).DMG_VS_INFANTRY; break;
            case ARMORCLASS_HEAVY_ARMOR: damageMultiplier *= getUnitProfile(offender).DMG_VS_HEAVY;
            case ARMORCLASS_ARMORED: damageMultiplier *= getUnitProfile(offender).DMG_VS_ARMORED; break;
        }
        double increasedDamage = scaledDamage * damageMultiplier;

        double defenseMultiplier = (10.0 - defenderDefense) / (10.0);

        int finalDamage = (int)(increasedDamage * defenseMultiplier);

        System.out.println("[CODEX] "+defender+" is hit for "+finalDamage+" damage");

        return finalDamage;
    }
}
