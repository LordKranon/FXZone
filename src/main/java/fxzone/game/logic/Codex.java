package fxzone.game.logic;

import fxzone.engine.handler.AssetHandler;
import fxzone.game.logic.Tile.TileType;
import fxzone.game.logic.Unit.UnitState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Codex {

    public static final int BUILDING_CAPTURE_TOTAL = 20;

    public static final int PLAYER_STARTING_CASH = 200;

    public static final HashMap<UnitType, UnitProfile> UNIT_PROFILE_VALUES = new HashMap<UnitType, UnitProfile>() {{
        put(UnitType.INFANTRY, new UnitProfile(
            0, "Rifle Infantry",
            3, 3, 100, 3, 0, 1, 1,
            1.5, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_INFANTRY,
            UnitSuperType.LAND_INFANTRY,
            80
        ));
        put(UnitType.INFANTRY_RPG, new UnitProfile(
            1, "RPG Infantry",
            3, 3, 100, 4, 0, 1, 1,
            .5, 1.5, 1.5,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_INFANTRY,
            UnitSuperType.LAND_INFANTRY,
            90
        ));
        put(UnitType.CAR_HUMVEE, new UnitProfile(
            2, "Humvee",
            7, 4, 100, 4, 2, 1, 1,
            1.5, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE,
            160
        ));
        put(UnitType.TRUCK_TRANSPORT, new UnitProfile(
            3, "Supply Truck",
            4, 3, 100, 0, 1, 1, 1,
            1, 1, 1,
            UnitAttackType.PACIFIST,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE,
            200
        ));
        put(UnitType.TANK_HUNTER, new UnitProfile(
            4, "Hunter Tank",
            5, 4, 100, 6, 5, 1, 1,
            1, 1.25, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.LAND_VEHICLE,
            240
        ));
        put(UnitType.ARTILLERY, new UnitProfile(
            5, "Artillery",
            3, 3, 100, 6, 4, 3, 5,
            1.5, 1, 1,
            UnitAttackType.RANGED,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE,
            200
        ));
        put(UnitType.TANK_BATTLE, new UnitProfile(
            6, "Battle Tank",
            4, 3, 100, 9, 7, 1, 2,
            1, 1, 1.5,
            UnitAttackType.RANGERMELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.LAND_VEHICLE,
            360
        ));
        put(UnitType.ARTILLERY_ROCKET, new UnitProfile(
            7, "Rocket Artillery",
            3, 3, 100, 6, 2, 4, 6,
            1, 1.5, 1.5,
            UnitAttackType.RANGED,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE,
            300
        ));
        put(UnitType.SHIP_LANDER, new UnitProfile(
            8, "Landing Craft",
            5, 3, 100, 0, 4, 1, 1,
            1, 1, 1,
            UnitAttackType.PACIFIST,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.SHIP_SMALL,
            100
        ));
        put(UnitType.SHIP_GUNBOAT, new UnitProfile(
            9, "Gunboat",
            6, 4, 100, 5, 2, 1, 1,
            1.5, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.SHIP_SMALL,
            110
        ));
        put(UnitType.SHIP_DESTROYER, new UnitProfile(
            10, "Destroyer",
            5, 4, 100, 6, 5, 1, 1,
            1, 1.5, 1.5,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.SHIP_LARGE,
            260
        ));
        put(UnitType.SHIP_BATTLESHIP, new UnitProfile(
            11, "Battleship",
            3, 3, 100, 7, 7, 1, 4,
            1, 1, 1.5,
            UnitAttackType.RANGERMELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.SHIP_LARGE,
            400
        ));
        put(UnitType.SHIP_CARRIER, new UnitProfile(
            12, "Aircraft Carrier",
            3, 3, 100, 0, 5, 1, 1,
            1, 1, 1,
            UnitAttackType.PACIFIST,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.SHIP_LARGE,
            500
        ));

        put(UnitType.PLANE_PROPELLER, new UnitProfile(
            13, "Recon Plane",
            7, 5, 100, 4, 2, 1, 1,
            1.25, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.AIRCRAFT_PLANE,
            240
        ));
        put(UnitType.PLANE_JET, new UnitProfile(
            13, "Fighter Jet",
            8, 5, 100, 9, 4, 1, 1,
            1, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.AIRCRAFT_PLANE,
            300
        ));
        put(UnitType.HELICOPTER_CHINOOK, new UnitProfile(
            13, "Transport Heli",
            6, 4, 100, 0, 3, 1, 1,
            1, 1, 1,
            UnitAttackType.PACIFIST,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.AIRCRAFT_HELICOPTER,
            100
        ));
        put(UnitType.HELICOPTER_APACHE, new UnitProfile(
            13, "Attack Heli",
            5, 4, 100, 5, 2, 1, 1,
            1, 1.25, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.AIRCRAFT_HELICOPTER,
            250
        ));

    }};
    public static final HashMap<UnitType, String> UNIT_DESCRIPTIONS =  new HashMap<>(){{
        put(UnitType.INFANTRY, "DMG 3  DFN 0  SPD 3  VIS 3\nStrong vs Infantry\nWeak vs Vehicles");
        put(UnitType.INFANTRY_RPG, "DMG 4  DFN 0  SPD 3  VIS 3\nStrong vs Light Vehicles\nWeak vs Infantry");
        put(UnitType.CAR_HUMVEE, "DMG 4  DFN 2  SPD 7  VIS 4\nVery fast and a good Scout\nVery Strong vs Infantry\nWeak vs Tanks");
        put(UnitType.TRUCK_TRANSPORT, "DMG 0  DFN 1  SPD 4  VIS 3\nGenerates income when moving to cities\nWeak vs RPGs and all Vehicles");
        put(UnitType.TANK_HUNTER, "DMG 6  DFN 5  SPD 5  VIS 4\nFast and very powerful\nVery Strong vs Light Vehicles");
        put(UnitType.ARTILLERY, "DMG 6  DFN 4  SPD 3  VIS 3\nFires over a 5 tile range\nCan't hit targets that are too close\nBonus Damage vs Infantry");
        put(UnitType.TANK_BATTLE, "DMG 9  DFN 7  SPD 4  VIS 3\nSuper powerful\nCan fire over 2 tiles, out-ranging other units");
        put(UnitType.ARTILLERY_ROCKET, "DMG 6  DFN 2  SPD 3  VIS 3\nFires over a 6 tile range\nCan't hit targets that are too close\nBonus Damage vs Vehicles");

        put(UnitType.SHIP_LANDER, "DMG 0  DFN 4  SPD 5  VIS 3\nTransports units over water\nHolds up to 2 Land Units");
        put(UnitType.SHIP_GUNBOAT, "DMG 5  DFN 2  SPD 6  VIS 4\nVery fast and a good Scout\nVery Strong vs Infantry");
        put(UnitType.SHIP_DESTROYER, "DMG 6  DFN 5  SPD 5  VIS 4\nFast and very powerful\nStrong vs other Ships\nStrong vs Aircraft");
        put(UnitType.SHIP_BATTLESHIP, "DMG 7  DFN 7  SPD 3  VIS 3\nSuper powerful\nCan fire over 4 tiles,\nout-ranging other ships and striking at Land Units");
        put(UnitType.SHIP_CARRIER, "DMG 0  DFN 5  SPD 3  VIS 3\nCarries and launches aircraft\nWeak vs other Ships");

        put(UnitType.PLANE_PROPELLER, "DMG 4  DFN 2  SPD 7  VIS 5\nVery fast and a good Scout\nEffective vs Land Units");
        put(UnitType.PLANE_JET, "DMG 9  DFN 4  SPD 8  VIS 5\nCan only attack other aircraft\nExtremely Strong vs other Aircraft");
        put(UnitType.HELICOPTER_CHINOOK, "DMG 0  DFN 3  SPD 6  VIS 4\nTransports Infantry\nHolds up to 2 Infantry Units");
        put(UnitType.HELICOPTER_APACHE, "DMG 5  DFN 2  SPD 5  VIS 4\nFast and very powerful\nExtremely Strong vs Land Units");
    }};
    public static final HashMap<BuildingType, String> BUILDING_NAMES = new HashMap<>(){{
        put(BuildingType.CITY, "City");
        put(BuildingType.FACTORY, "Factory");
        put(BuildingType.PORT, "Port");
        put(BuildingType.AIRPORT, "Airport");
    }};
    public static final HashMap<TileType, String> TILE_NAMES = new HashMap<>(){{
        put(TileType.PLAINS, "Plains");
        put(TileType.WATER, "Water");
        put(TileType.BEACH, "Beach");
        put(TileType.FOREST, "Forest");
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
        put(UnitType.SHIP_LANDER, "ship_lander");
        put(UnitType.SHIP_GUNBOAT, "ship_gunboat");
        put(UnitType.SHIP_DESTROYER, "ship_destroyer");
        put(UnitType.SHIP_BATTLESHIP, "ship_battleship");
        put(UnitType.SHIP_CARRIER, "ship_carrier");

        put(UnitType.PLANE_PROPELLER, "plane_1");
        put(UnitType.PLANE_JET, "plane_jet");
        put(UnitType.HELICOPTER_CHINOOK, "helicopter_chinook");
        put(UnitType.HELICOPTER_APACHE, "helicopter_apache");
    }};
    public static final HashMap<BuildingType, String> BUILDING_RESOURCE_NAMES = new HashMap<BuildingType, String>(){{
        put(BuildingType.CITY, "city");
        put(BuildingType.FACTORY, "factory");
        put(BuildingType.PORT, "port");
        put(BuildingType.AIRPORT, "airport");
    }};

    public static final List<UnitType> BUILDABLE_UNIT_TYPES_FACTORY = Arrays.asList(
        UnitType.INFANTRY,
        UnitType.INFANTRY_RPG,
        UnitType.CAR_HUMVEE,
        //UnitType.TRUCK_TRANSPORT,
        UnitType.TANK_HUNTER,
        UnitType.ARTILLERY,
        UnitType.TANK_BATTLE,
        UnitType.ARTILLERY_ROCKET
    );
    public static final List<UnitType> BUILDABLE_UNIT_TYPES_PORT = Arrays.asList(
        UnitType.SHIP_LANDER,
        UnitType.SHIP_GUNBOAT,
        UnitType.SHIP_DESTROYER,
        UnitType.SHIP_BATTLESHIP,
        UnitType.SHIP_CARRIER
    );
    public static final List<UnitType> BUILDABLE_UNIT_TYPES_AIRPORT = Arrays.asList(
        UnitType.HELICOPTER_CHINOOK,
        UnitType.HELICOPTER_APACHE,
        UnitType.PLANE_PROPELLER,
        UnitType.PLANE_JET
    );

    public static class UnitProfile{
        public String NAME;
        public int ID, SPEED, VISION, HEALTH, DAMAGE, DEFENSE, MINRANGE, MAXRANGE;
        public int COST;
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
            UnitSuperType SUPERTYPE,
            int COST
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

            this.COST = COST;
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

        SHIP_LANDER,
        SHIP_GUNBOAT,
        SHIP_DESTROYER,
        SHIP_BATTLESHIP,
        SHIP_CARRIER,

        PLANE_PROPELLER,
        PLANE_JET,
        HELICOPTER_CHINOOK,
        HELICOPTER_APACHE,
/*
        INFANTRY_GUERRILLA,
        CAR_PICKUP,
 */
    }
    public enum UnitSuperType{
        LAND_INFANTRY,
        LAND_VEHICLE,
        AIRCRAFT_PLANE,
        AIRCRAFT_HELICOPTER,
        SHIP_LARGE,
        SHIP_SMALL,
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

    public enum TileSuperType {
        TS_LAND,
        TS_BEACH,
        TS_WATER
    }
    public static TileSuperType getTileSuperType(TileType tileType){
        switch (tileType){
            case PLAINS:
            case FOREST:
                return TileSuperType.TS_LAND;
            case BEACH:
                return TileSuperType.TS_BEACH;
            case WATER:
                return TileSuperType.TS_WATER;
            default:
                System.err.println("[CODEX] ERROR Can't determine tile super type, invalid tile type");
                return null;
        }
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

    public static int getUnitHealthDigit(double fractionOfMaxHealthRemaining){
        if(fractionOfMaxHealthRemaining >= 1){
            return 10;
        } else if(fractionOfMaxHealthRemaining <= 0){
            return 0;
        } else {
            int hpDigit = (int)(fractionOfMaxHealthRemaining * 10.0);
            if (hpDigit == 0){
                hpDigit = 1;
            }
            return hpDigit;
        }
    }
    public static int getUnitHealthDigit(Unit unit){
        return getUnitHealthDigit((double)(unit.getStatRemainingHealth())/(double)(unit.getStatMaxHealth()));
    }

    public static boolean tileTypeFitsUnitSuperType(Tile tile, Unit unit){
        TileType tileType = tile.getTileType();
        UnitSuperType unitSuperType = Codex.getUnitProfile(unit).SUPERTYPE;
        TileSuperType tileSuperType = getTileSuperType(tileType);
        return
            (unitSuperType == UnitSuperType.LAND_INFANTRY && tileSuperType != TileSuperType.TS_WATER) ||
                (unitSuperType == UnitSuperType.LAND_VEHICLE && (tileSuperType == TileSuperType.TS_LAND || tileSuperType == TileSuperType.TS_BEACH)) ||
                (unitSuperType == UnitSuperType.SHIP_SMALL && (tileSuperType == TileSuperType.TS_WATER || tileSuperType == TileSuperType.TS_BEACH || (tile.hasBuildingOnTile() && tile.getBuildingOnTile().getBuildingType()==BuildingType.PORT))) ||
                (unitSuperType == UnitSuperType.SHIP_LARGE && (tileSuperType == TileSuperType.TS_WATER || (tile.hasBuildingOnTile() && tile.getBuildingOnTile().getBuildingType()==BuildingType.PORT))) ||
                (unitSuperType == UnitSuperType.AIRCRAFT_HELICOPTER || unitSuperType == UnitSuperType.AIRCRAFT_PLANE);
    }

    public static boolean canTransport(Unit transporter, Unit loaded){
        if(transporter.getUnitType() == UnitType.SHIP_LANDER){
            return getUnitProfile(loaded).SUPERTYPE == UnitSuperType.LAND_INFANTRY || getUnitProfile(loaded).SUPERTYPE == UnitSuperType.LAND_VEHICLE;
        } else if(transporter.getUnitType() == UnitType.SHIP_CARRIER){
            return getUnitProfile(loaded).SUPERTYPE == UnitSuperType.AIRCRAFT_PLANE || getUnitProfile(loaded).SUPERTYPE == UnitSuperType.AIRCRAFT_HELICOPTER;
        } else if(transporter.getUnitType() == UnitType.HELICOPTER_CHINOOK){
            return getUnitProfile(loaded).SUPERTYPE == UnitSuperType.LAND_INFANTRY;
        } else {
            return false;
        }
    }
    public static int getTransportCapacity(Unit unit){
        switch (unit.getUnitType()){
            case HELICOPTER_CHINOOK:
            case SHIP_LANDER:
            case SHIP_CARRIER:
                return 2;
            default:
                return 0;
        }
    }

    public static boolean canHit(Unit offender, Unit defender){
        UnitSuperType atk = getUnitProfile(offender).SUPERTYPE;
        UnitSuperType def = getUnitProfile(defender).SUPERTYPE;
        if(def == UnitSuperType.AIRCRAFT_PLANE || def == UnitSuperType.AIRCRAFT_HELICOPTER){
            return atk == UnitSuperType.AIRCRAFT_PLANE || atk == UnitSuperType.AIRCRAFT_HELICOPTER || offender.getUnitType() == UnitType.SHIP_DESTROYER;
        }
        else {
            return offender.getUnitType() != UnitType.PLANE_JET;
        }
    }
    public static boolean canCapture(Unit unit){
        UnitSuperType superType = getUnitProfile(unit).SUPERTYPE;
        return !(superType == UnitSuperType.AIRCRAFT_PLANE);
    }
}
