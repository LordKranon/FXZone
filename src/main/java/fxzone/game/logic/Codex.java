package fxzone.game.logic;

import fxzone.game.logic.Game.CustomGameRules;
import fxzone.game.logic.Tile.TileType;
import fxzone.game.logic.Unit.UnitState;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.scene.paint.Color;

public class Codex {

    public static final int TOTAL_CAMPAIGN_COLUMNS = 4;
    public static final int TOTAL_CAMPAIGN_MISSIONS_PER_COLUMN = 5;
    public static final int TOTAL_CAMPAIGN_PAGES = 2;

    public static final int BUILDING_CAPTURE_TOTAL = 20;

    public static final int BUILDING_HEALING_TOTAL = 20;

    public static final int CITY_CASH_GENERATION = 50;

    public static final int PLAYER_STARTING_CASH = 100;

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
            390
        ));
        put(UnitType.ARTILLERY_ROCKET, new UnitProfile(
            7, "Rocket Artillery",
            3, 3, 100, 6, 2, 4, 6,
            1, 1.5, 1.5,
            UnitAttackType.RANGED,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE,
            280
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
            450
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
            14, "Fighter Jet",
            8, 5, 100, 9, 4, 1, 1,
            1, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.AIRCRAFT_PLANE,
            300
        ));
        put(UnitType.HELICOPTER_CHINOOK, new UnitProfile(
            15, "Transport Heli",
            6, 4, 100, 0, 3, 1, 1,
            1, 1, 1,
            UnitAttackType.PACIFIST,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.AIRCRAFT_HELICOPTER,
            100
        ));
        put(UnitType.HELICOPTER_APACHE, new UnitProfile(
            16, "Attack Heli",
            5, 4, 100, 5, 2, 1, 1,
            1, 1.25, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.AIRCRAFT_HELICOPTER,
            250
        ));

        put(UnitType.INFANTRY_AA, new UnitProfile(
            17, "AA Infantry",
            3, 3, 100, 5, 0, 1, 1,
            .5, 1.7, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_INFANTRY,
            UnitSuperType.LAND_INFANTRY,
            100
        ));
        put(UnitType.TANK_AA, new UnitProfile(
            18, "Anti-Air Tank",
            5, 4, 100, 3, 3, 1, 1,
            1.5, 1, 1,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE,
            190
        ));
        put(UnitType.CAR_PICKUP, new UnitProfile(
            19, "Pick-Up",
            6, 4, 100, 3, 2, 1, 1,
            1.5, 1, 1.25,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.LAND_VEHICLE,
            150
        ));
        put(UnitType.TANK_IFV, new UnitProfile(
            20, "BMP", //oder: Marder, Schützenpanzer, Bradley
            5, 3, 100, 5, 4, 1, 1,
            1.2, 1, 1.2,
            UnitAttackType.MELEE,
            UnitArmorClass.ARMORCLASS_HEAVY_ARMOR,
            UnitSuperType.LAND_VEHICLE,
            220
        ));
        put(UnitType.PLANE_BOMBER, new UnitProfile(
            21, "Bomber",
            6, 3, 100, 9, 3, 1, 1,
            1, 1.2, 1.2,
            UnitAttackType.PACIFIST, // (Special)
            UnitArmorClass.ARMORCLASS_ARMORED,
            UnitSuperType.AIRCRAFT_PLANE,
            300
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
        put(UnitType.PLANE_JET, "DMG 9  DFN 4  SPD 8  VIS 5\nCan only attack other Aircraft\nExtremely Strong vs other Aircraft");
        put(UnitType.HELICOPTER_CHINOOK, "DMG 0  DFN 3  SPD 6  VIS 4\nTransports Infantry\nHolds up to 2 Infantry Units");
        put(UnitType.HELICOPTER_APACHE, "DMG 5  DFN 2  SPD 5  VIS 4\nFast and very powerful\nExtremely Strong vs Land Units");

        put(UnitType.INFANTRY_AA, "DMG 5  DFN 0  SPD 3  VIS 3\nCan only attack Aircraft\nStrong vs Aircraft");
        put(UnitType.TANK_AA, "DMG 6  DFN 3  SPD 5  VIS 4\nExtremely Strong vs Aircraft\nReduced damage vs Land Units");

        put(UnitType.CAR_PICKUP, "DMG 3  DFN 2  SPD 6  VIS 4\nTransports Infantry\nStrong vs Infantry\nWeak vs Tanks");
        put(UnitType.TANK_IFV, "DMG 5  DFN 4  SPD 5  VIS 3\nTransports Infantry\nVery Strong vs Infantry\nStrong vs Light Vehicles");
        put(UnitType.PLANE_BOMBER, "DMG 9  DFN 3  SPD 6  VIS 3\nDevastatingly Strong vs Land Units and Ships\nWeak vs other Aircraft and Anti-Air");

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
        put(TileType.MOUNTAIN, "Mountain");
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
        put(UnitType.SHIP_GUNBOAT, "ship_gunboat2");
        put(UnitType.SHIP_DESTROYER, "ship_destroyer2");
        put(UnitType.SHIP_BATTLESHIP, "ship_battleship2");
        put(UnitType.SHIP_CARRIER, "ship_carrier");

        put(UnitType.PLANE_PROPELLER, "plane_1");
        put(UnitType.PLANE_JET, "plane_jet");
        put(UnitType.HELICOPTER_CHINOOK, "helicopter_chinook");
        put(UnitType.HELICOPTER_APACHE, "helicopter_apache");

        put(UnitType.INFANTRY_AA, "infantry_aa");
        put(UnitType.TANK_AA, "aa_tank");

        put(UnitType.CAR_PICKUP, "car_pickup");
        put(UnitType.TANK_IFV, "bmp");
        put(UnitType.PLANE_BOMBER, "bomber");
    }};
    public static final HashMap<BuildingType, String> BUILDING_RESOURCE_NAMES = new HashMap<BuildingType, String>(){{
        put(BuildingType.CITY, "city");
        put(BuildingType.FACTORY, "factory");
        put(BuildingType.PORT, "port");
        put(BuildingType.AIRPORT, "airport");
    }};
    public static final HashMap<CharacterType, String> CHARACTER_RESOURCE_NAMES = new HashMap<>(){{
        put(CharacterType.SOLDIER, "infantry");
        put(CharacterType.GENERAL, "general");
    }};

    public static final List<UnitType> BUILDABLE_UNIT_TYPES_FACTORY = Arrays.asList(
        UnitType.INFANTRY,
        UnitType.INFANTRY_RPG,

        UnitType.INFANTRY_AA,

        UnitType.CAR_HUMVEE,
        //UnitType.TRUCK_TRANSPORT,
        UnitType.TANK_HUNTER,
        UnitType.ARTILLERY,
        UnitType.TANK_AA,
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
        UnitType.PLANE_JET,
        UnitType.PLANE_BOMBER
    );
    public static final List<UnitType> BUILDABLE_UNIT_TYPES_CARRIER = Arrays.asList(
        UnitType.PLANE_PROPELLER,
        UnitType.PLANE_JET
    );

    public static final List<String> CAMPAIGN_MISSION_NAMES = Arrays.asList(
        "Tutorial",

        "Red Formation",
        "Artillery Line",
        "Reinforcement",
        "Abandoned Factory",
        "Crimson Peninsula",

        "Shallow Strait",
        "Superweapon",
        "Sand Hills",
        "Naval Battle",
        "Beachhead",

        "Whirlwind",
        "Dogfight",
        "Mountain Range",
        "Riverhead Assault",
        "Crossing",

        "Last Stand",
        "Ashen Offensive",
        "Skull Island",
        "Magma Fleet",
        "Human Enemy",

        "Conquest",
        "Bombing Run",
        "?",
        "?",
        "?",

        "?",
        "?",
        "?",
        "?",
        "?",

        "?",
        "?",
        "?",
        "?",
        "?",

        "?",
        "?",
        "?",
        "?",
        "?"
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

        INFANTRY_AA,
        TANK_AA,

        CAR_PICKUP,
        TANK_IFV,
        PLANE_BOMBER,
/*
        INFANTRY_GUERRILLA,

        //Бронетранспорт
        //Боевая Машина Пехоты
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
    public enum CharacterType{
        SOLDIER,
        GENERAL
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
            case MOUNTAIN:
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

        // Add special damage bonus
        // Anti-Air bonus
        if(getUnitProfile(defender).SUPERTYPE == UnitSuperType.AIRCRAFT_HELICOPTER || getUnitProfile(defender).SUPERTYPE == UnitSuperType.AIRCRAFT_PLANE){
            if(offender.getUnitType() == UnitType.TANK_AA){
                rawDamage = 10;
            }
        }

        // Defender does half damage
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
                (unitSuperType == UnitSuperType.LAND_VEHICLE && (tileSuperType == TileSuperType.TS_LAND || tileSuperType == TileSuperType.TS_BEACH) && tileType != TileType.MOUNTAIN) ||
                (unitSuperType == UnitSuperType.SHIP_SMALL && (tileSuperType == TileSuperType.TS_WATER || tileSuperType == TileSuperType.TS_BEACH || (tile.hasBuildingOnTile() && tile.getBuildingOnTile().getBuildingType()==BuildingType.PORT))) ||
                (unitSuperType == UnitSuperType.SHIP_LARGE && (tileSuperType == TileSuperType.TS_WATER || (tile.hasBuildingOnTile() && tile.getBuildingOnTile().getBuildingType()==BuildingType.PORT))) ||
                (unitSuperType == UnitSuperType.AIRCRAFT_HELICOPTER || unitSuperType == UnitSuperType.AIRCRAFT_PLANE);
    }

    public static boolean canTransport(Unit transporter, Unit loaded){
        if(transporter.getUnitType() == UnitType.SHIP_LANDER){
            return getUnitProfile(loaded).SUPERTYPE == UnitSuperType.LAND_INFANTRY || getUnitProfile(loaded).SUPERTYPE == UnitSuperType.LAND_VEHICLE;
        } else if(transporter.getUnitType() == UnitType.SHIP_CARRIER){
            return getUnitProfile(loaded).SUPERTYPE == UnitSuperType.AIRCRAFT_PLANE || getUnitProfile(loaded).SUPERTYPE == UnitSuperType.AIRCRAFT_HELICOPTER;
        } else if(transporter.getUnitType() == UnitType.HELICOPTER_CHINOOK || transporter.getUnitType() == UnitType.CAR_PICKUP || transporter.getUnitType() == UnitType.TANK_IFV){
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
            case CAR_PICKUP:
            case TANK_IFV:
                return 2;
            default:
                return 0;
        }
    }

    public static boolean canHit(Unit offender, Unit defender){
        UnitSuperType atk = getUnitProfile(offender).SUPERTYPE;
        UnitSuperType def = getUnitProfile(defender).SUPERTYPE;
        if(def == UnitSuperType.AIRCRAFT_PLANE || def == UnitSuperType.AIRCRAFT_HELICOPTER){
            return (offender.getUnitType() != UnitType.PLANE_BOMBER) && (atk == UnitSuperType.AIRCRAFT_PLANE || atk == UnitSuperType.AIRCRAFT_HELICOPTER || offender.getUnitType() == UnitType.SHIP_DESTROYER || offender.getUnitType() == UnitType.INFANTRY_AA || offender.getUnitType() == UnitType.TANK_AA);
        }
        else {
            return offender.getUnitType() != UnitType.PLANE_JET && offender.getUnitType() != UnitType.INFANTRY_AA;
        }
    }
    public static boolean canCounterAttack(Unit unit){
        UnitAttackType attackType = getUnitProfile(unit).ATTACKTYPE;
        if(attackType == UnitAttackType.RANGED || attackType == UnitAttackType.PACIFIST || unit.getUnitType() == UnitType.PLANE_BOMBER){
            return false;
        } else {
            return true;
        }
    }
    public static boolean canCapture(Unit unit){
        UnitSuperType superType = getUnitProfile(unit).SUPERTYPE;
        return !(superType == UnitSuperType.AIRCRAFT_PLANE || superType == UnitSuperType.AIRCRAFT_HELICOPTER);
    }

    public static boolean hasConstructionMode(Unit unit){
        return unit.getUnitType() == UnitType.SHIP_CARRIER;
    }

    public static List<UnitType> getBuildableUnitTypes(BuildingType buildingType){
        switch (buildingType){
            case FACTORY:
                return BUILDABLE_UNIT_TYPES_FACTORY;
            case PORT:
                return BUILDABLE_UNIT_TYPES_PORT;
            case AIRPORT:
                return BUILDABLE_UNIT_TYPES_AIRPORT;
            case CITY:
            default:
                System.err.println("[CODEX] [getBuildableUnitTypes] ERROR Could not retrieve buildable unit types");
                return null;
        }
    }
    public static List<UnitType> getCustomBuildableUnitTypesOfCampaign(BuildingType buildingType, CustomGameRules customGameRules){
        switch (buildingType){
            case FACTORY:
                return customGameRules.getBuildableUnitTypesFactory();
            case PORT:
                return customGameRules.getBuildableUnitTypesPort();
            case AIRPORT:
                return customGameRules.getBuildableUnitTypesAirport();
            case CITY:
            default:
                System.err.println("[CODEX] [getCustomBuildableUnitTypesOfCampaign] ERROR Could not retrieve buildable unit types");
                return null;
        }
    }

    public static CustomGameRules getCustomGameRulesOfCampaign(int mission) {
        if(mission >= 0 && mission <= 3){
            return new CustomGameRules(
                50,
                Arrays.asList(UnitType.INFANTRY, UnitType.INFANTRY_RPG),
                Arrays.asList(UnitType.SHIP_GUNBOAT),
                Arrays.asList(UnitType.PLANE_PROPELLER)
            );
        } else if(mission >= 4 && mission <= 10){
            return new CustomGameRules(
                50,
                Arrays.asList(UnitType.INFANTRY, UnitType.INFANTRY_RPG, UnitType.CAR_HUMVEE, UnitType.TANK_HUNTER, UnitType.ARTILLERY),
                Arrays.asList(UnitType.SHIP_GUNBOAT),
                Arrays.asList(UnitType.PLANE_PROPELLER)
            );
        } else if(mission >= 11 && mission <= 15){
            return new CustomGameRules(
                50,
                Arrays.asList(UnitType.INFANTRY, UnitType.INFANTRY_RPG, UnitType.CAR_HUMVEE, UnitType.TANK_HUNTER, UnitType.ARTILLERY, UnitType.TANK_AA),
                Arrays.asList(UnitType.SHIP_GUNBOAT),
                Arrays.asList(UnitType.PLANE_PROPELLER, UnitType.PLANE_JET)
            );
        } else if(mission >= 16 && mission <= 19){
            return new CustomGameRules(
                50,
                Arrays.asList(UnitType.INFANTRY, UnitType.INFANTRY_RPG, UnitType.CAR_HUMVEE, UnitType.TANK_HUNTER, UnitType.ARTILLERY, UnitType.TANK_AA),
                Arrays.asList(UnitType.SHIP_GUNBOAT),
                Arrays.asList(UnitType.PLANE_PROPELLER)
            );
        } else if(mission == 20){
            return new CustomGameRules(
                50,
                Arrays.asList(UnitType.INFANTRY, UnitType.INFANTRY_RPG, UnitType.CAR_HUMVEE, UnitType.TANK_HUNTER, UnitType.ARTILLERY, UnitType.TANK_AA, UnitType.TANK_BATTLE, UnitType.ARTILLERY_ROCKET),
                Arrays.asList(UnitType.SHIP_GUNBOAT),
                Arrays.asList(UnitType.HELICOPTER_APACHE, UnitType.PLANE_PROPELLER, UnitType.PLANE_JET)
            );
        } else if(mission == 21){
            return new CustomGameRules(
                50,
                Arrays.asList(UnitType.INFANTRY, UnitType.INFANTRY_RPG, UnitType.CAR_HUMVEE, UnitType.TANK_HUNTER, UnitType.ARTILLERY, UnitType.TANK_BATTLE, UnitType.ARTILLERY_ROCKET),
                Arrays.asList(UnitType.SHIP_GUNBOAT),
                Arrays.asList(UnitType.PLANE_PROPELLER, UnitType.PLANE_JET)
            );
        } else if(mission >= 22 && mission <= 25){
            return new CustomGameRules(
                50,
                Arrays.asList(UnitType.INFANTRY, UnitType.INFANTRY_RPG, UnitType.CAR_HUMVEE, UnitType.TANK_HUNTER, UnitType.ARTILLERY, UnitType.TANK_AA, UnitType.TANK_BATTLE, UnitType.ARTILLERY_ROCKET),
                Arrays.asList(UnitType.SHIP_GUNBOAT),
                Arrays.asList(UnitType.PLANE_PROPELLER, UnitType.PLANE_JET)
            );
        } else {
            return new CustomGameRules(
                CITY_CASH_GENERATION,
                BUILDABLE_UNIT_TYPES_FACTORY,
                BUILDABLE_UNIT_TYPES_PORT,
                BUILDABLE_UNIT_TYPES_AIRPORT
            );
        }
    }
    public static Player getEnemyPlayerOfCampaignMission(int mission){
        if(mission >= 0 && mission <= 5){
            return new Player("Marauders", Color.web("#a08000"), 2, "horn_scary");
        } else if(mission >= 6 && mission <= 10){
            return new Player("Neoglow Coast Guard", Color.web("#0080a0"), 2, "horn_scary");
        } else if(mission >= 11 && mission <= 15){
            return new Player("Free Ponzia Army", Color.web("#40a040"), 2, "horn_scary");
        } else if(mission >= 16 && mission <= 18){
            return new Player("Quicksilver Army", Color.web("#000000"), 2, "horn_scary");
        } else if(mission == 19){
            return new Player("Quicksilver Fleet", Color.web("#000000"), 2, "horn_scary");
        } else if(mission == 20){
            return new Player("Dr. Axel Stoll", Color.web("#0000ff"), 2, "horn_scary");
        } else if(mission >= 21 && mission <= 25){
            return new Player("Quicksilver Wing", Color.web("#000000"), 2, "horn_scary");
        }
        else {
            return new Player("Enemy", Color.web("#000000"), 2, "horn_scary");
        }
    }

    private static String readLineFromFile(String filePath, int lineNumber) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int currentLine = 0;

            while ((line = reader.readLine()) != null) {
                if (currentLine == lineNumber) {
                    reader.close();
                    return line;
                }
                currentLine++;
            }
        }
        System.err.println("[CODEX] [readLineFromFile] ERROR");
        return null;
    }
    public static String[] getCampaignDialog(int mission){
        try {
            String line = readLineFromFile("./fxzone/campaign/campaign_dialog.csv", mission);
            return line.split(";");
        } catch (IOException | NullPointerException e) {
            System.err.println("[CODEX] [getCampaignDialog] ERROR");
            return null;
        }
    }
    public static boolean getCampaignDialogScreenSideLeft(int mission){
        switch (mission){
            case 7:
            case 9:
            case 13:
            case 19:
                return false;
            default:
                return true;
        }
    }
    public static CharacterType getCampaignDialogCharacterType(int mission){
        switch (mission){
            case 9:
            case 10:
            case 19:
                return CharacterType.GENERAL;
            default:
                return CharacterType.SOLDIER;
        }
    }
}
