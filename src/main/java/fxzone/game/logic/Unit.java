package fxzone.game.logic;

import fxzone.controller.ingame.InGameUiController;
import fxzone.engine.handler.AssetHandler;
import fxzone.engine.utils.Direction;
import fxzone.engine.utils.FxUtils;
import fxzone.engine.utils.GeometryUtils;
import fxzone.engine.utils.ZoneMediaPlayer;
import fxzone.game.logic.Codex.UnitAttackType;
import fxzone.game.logic.Codex.UnitSuperType;
import fxzone.game.logic.Codex.UnitType;
import fxzone.game.logic.serializable.UnitSerializable;
import fxzone.game.render.GameObjectUiUnitHealth;
import fxzone.game.render.GameObjectUnit;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import javafx.scene.Group;

public class Unit extends TileSpaceObject{

    /**
     * Tile position in map.
     * Alters from actual position when unit is moving.
     */
    private int visualTileX, visualTileY;

    private boolean inVision;

    /**
     * Identifies what kind of unit this is.
     * E. g. "Battle Tank" or "RPG Infantry"
     */
    private final UnitType unitType;

    /**
     * Graphical representation. Superclass already has a pointer, this one specifies it further.
     */
    GameObjectUnit gameObjectUnit;

    /**
     * Graphical indicator of this units health.
     */
    GameObjectUiUnitHealth gameObjectUiUnitHealth;

    private UnitStance unitStance = UnitStance.NORMAL;

    private UnitState unitState = UnitState.NEUTRAL;

    /**
     * Whether this unit can be selected for commands or if its blacked out.
     * Closely related to UnitStates NEUTRAL & BLACKED_OUT
     */
    private boolean actionableThisTurn = true;

    private ArrayDeque<Point> movePath;

    /**
     * As the unit moves between tiles, this is the direction of the next neighboring tile along its path.
     */
    private Direction directionNextPointOnMovePath;

    private boolean hasAttackCommandAfterMoving;
    private Point pointToAttackAfterMoving;
    private Unit currentlyAttackedUnit;
    private Unit lastAttackedUnit;
    private boolean waitForAttackAfterMoving;

    /*
    TRANSPORT
     */
    private ArrayList<Unit> transportLoadedUnits;
    private boolean disappearIntoTransportAfterMoving;
    private Unit transportedBy;

    /*
    CONSTRUCTION (AIRCRAFT CARRIER
     */
    private ConstructionMenu constructionMenu;

    private int ownerId;

    /*
    GAME BALANCE
     */
    private int statMaxHealth;
    private int statRemainingHealth;
    private int statRemainingHealthOnAttack;

    /*
    SOUND
     */
    private ZoneMediaPlayer mediaPlayerMovement;
    private ZoneMediaPlayer mediaPlayerGunshot;
    private ZoneMediaPlayer mediaPlayerOnSelect;

    /*
    NET
     */
    private final int unitId;

    /*
    DEBUG
     */
    static final boolean verbose = true;

    /**
     * Constructor
     *
     * @param x              game logical tile position in the map
     * @param y              game logical tile position in the map
     */
    public Unit(UnitType unitType, int x, int y, int unitId) {
        super(x, y);
        this.unitType = unitType;
        this.unitId = unitId;
    }
    public Unit(UnitSerializable unitSerializable, double tileRenderSize, Group group, Game game){
        super(unitSerializable);
        this.unitType = unitSerializable.unitType;
        this.ownerId = unitSerializable.ownerId;

        this.unitId = unitSerializable.unitId;

        java.awt.Color playerColor = null;
        try {
            playerColor = FxUtils.toAwtColor(game.getPlayer(ownerId).getColor());
        }catch (NullPointerException e){
            System.err.println(this+" Initialized without owner color");
            // Set owner id to 0 if owner-less
            this.ownerId = 0;
        }

        this.gameObjectUnit = new GameObjectUnit(
            unitType, x, y, tileRenderSize, group, playerColor
        );
        this.gameObjectInTileSpace = this.gameObjectUnit;

        this.gameObjectUiUnitHealth = new GameObjectUiUnitHealth(x, y, tileRenderSize, group);

        this.transportLoadedUnits = new ArrayList<>();
        initializeStats();
        initializeMediaPlayer();
    }
    private void initializeStats(){
        this.statMaxHealth = Codex.getUnitProfile(unitType).HEALTH;
        this.statRemainingHealth = this.statMaxHealth;
    }
    private void initializeMediaPlayer(){
        //TODO Improve very rudimentary sound system
        this.mediaPlayerMovement = new ZoneMediaPlayer(AssetHandler.getSoundMovement(unitType));
        this.mediaPlayerMovement.setRate((1 / (2 * InGameUiController.TOTAL_UNIT_MOVEMENT_INTERVAL)) * 1);

        this.mediaPlayerGunshot = new ZoneMediaPlayer(AssetHandler.getSoundGunshot(unitType));

        if(mediaPlayerGunshot.getMediaPlayer() != null){
            mediaPlayerGunshot.getMediaPlayer().setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayerGunshot.stop();
                }
            });
        }

        this.mediaPlayerOnSelect = new ZoneMediaPlayer(AssetHandler.getSoundOnSelect(unitType));
    }

    public UnitType getUnitType(){
        return unitType;
    }
    public enum UnitState {
        NEUTRAL,
        MOVING,
        BLACKED_OUT,
        ATTACKING,
        COUNTERATTACKING,
        MOVED_AND_WAITING_FOR_ATTACK,
        IN_TRANSPORT
    }
    public enum UnitStance {
        NORMAL,
        MOVE_1,
        MOVE_2,
        ATTACK
    }

    /**
     * Switch between the different images of a unit.
     */
    public void setStance(UnitStance unitStance){
        this.unitStance = unitStance;
        gameObjectUnit.setStance(unitStance);
    }
    public void switchStanceOnMove(){
        if(this.unitStance == UnitStance.MOVE_1){
            setStance(UnitStance.MOVE_2);
        } else if(this.unitStance == UnitStance.MOVE_2){
            setStance(UnitStance.MOVE_1);
        }
    }

    /**
     * During a game turn, receive a move command and start moving across the map.
     * @param path the path of tiles this unit will take
     */
    public UnitState moveCommand(ArrayDeque<Point> path, Game game, Point pointToAttack, boolean waitForAttack, boolean enterTransport){
        /* TODO Remove second condition, it is temporary for testing */
        if(unitState == UnitState.NEUTRAL && path.size() <= Codex.getUnitProfile(this.unitType).SPEED){
            this.movePath = path;
            Point oldPosition = new Point(x, y);
            Point finalPosition = new Point(x, y);
            directionNextPointOnMovePath = Direction.NONE;
            if(! path.isEmpty()){
                finalPosition = new Point(path.peekLast());
                directionNextPointOnMovePath = GeometryUtils.getPointToPointDirection(oldPosition, path.peek());
                setFacingDirection(directionNextPointOnMovePath);

                //If unit is moved away from tile with building, reset that buildings capture progress
                if(game.getMap().getTiles()[x][y].hasBuildingOnTile()){
                    game.getMap().getTiles()[x][y].getBuildingOnTile().setStatCaptureProgress(0);
                }
            }

            if(!enterTransport){
                setPositionInMap(finalPosition.x, finalPosition.y, game.getMap());
            } else {
                game.getMap().getTiles()[this.x][this.y].setUnitOnTile(null);
                this.x = finalPosition.x;
                this.y = finalPosition.y;
            }
            for(Unit transportedUnit : transportLoadedUnits){
                transportedUnit.x = this.x;
                transportedUnit.y = this.y;
            }

            setPositionInMapVisual(oldPosition.x, oldPosition.y, game.getMap());

            //If an attack is included in the command, the unit will perform it after moving to the destination.
            if(pointToAttack != null){
                this.hasAttackCommandAfterMoving = true;
                this.pointToAttackAfterMoving = pointToAttack;
            }
            this.waitForAttackAfterMoving = waitForAttack;
            actionableThisTurn = false;

            if(enterTransport){
                Unit transporter = game.getMap().getTiles()[finalPosition.x][finalPosition.y].getUnitOnTile();
                if(transporter == null){
                    System.err.println(this+" [moveCommand] ERROR Could not find transporter to enter");
                } else {
                    this.disappearIntoTransportAfterMoving = true;
                    transporter.loadToTransport(this);
                    this.transportedBy = transporter;
                }
            }


            if(verbose) System.out.println(this+" received a move command");

            //If path is empty, movement is ended immediately and the unit goes into attack immediately
            if(path.isEmpty()){
                onMovementEnd(game.getMap());
            } else {
                mediaPlayerMovement.play();
                unitState = UnitState.MOVING;
            }

            return this.unitState;
        }
        else if(unitState == UnitState.MOVED_AND_WAITING_FOR_ATTACK && path.isEmpty() && pointToAttack != null && !waitForAttack && !enterTransport){
            this.hasAttackCommandAfterMoving = true;
            this.pointToAttackAfterMoving = pointToAttack;
            if(verbose) System.out.println(this+" received attack command while moved and waiting for attack");
            onMovementEnd(game.getMap());
            return this.unitState;
        }
        else if(unitState == UnitState.IN_TRANSPORT && pointToAttack == null && !waitForAttack && !enterTransport && !path.isEmpty()){
            // Exit transport normal (limited)
            transportedBy.getTransportLoadedUnits().remove(this);
            unitStateToNeutral();
            this.movePath = path;
            Point oldPosition = new Point(transportedBy.getX(), transportedBy.getY());
            Point finalPosition = new Point(path.peekLast());
            directionNextPointOnMovePath = GeometryUtils.getPointToPointDirection(oldPosition, path.peek());
            setFacingDirection(directionNextPointOnMovePath);

            super.setPositionInMap(finalPosition.x, finalPosition.y, game.getMap());
            game.getMap().getTiles()[finalPosition.x][finalPosition.y].setUnitOnTile(this);
            this.gameObjectUiUnitHealth.setPositionInMap(finalPosition.x, finalPosition.y, game.getMap());
            setPositionInMapVisual(oldPosition.x, oldPosition.y, game.getMap());
            setVisible(true);

            this.hasAttackCommandAfterMoving = false;
            this.waitForAttackAfterMoving = false;
            this.actionableThisTurn = false;

            if(verbose) System.out.println(this+" received an exit transport move command");

            mediaPlayerMovement.play();
            unitState = UnitState.MOVING;

            return this.unitState;
        }
        else if(unitState == UnitState.IN_TRANSPORT && !enterTransport && !path.isEmpty() && Codex.getUnitProfile(this).SUPERTYPE == UnitSuperType.AIRCRAFT_PLANE){
            // Exit transport plane (unlimited)
            transportedBy.getTransportLoadedUnits().remove(this);
            unitStateToNeutral();
            this.movePath = path;
            Point oldPosition = new Point(transportedBy.getX(), transportedBy.getY());
            Point finalPosition = new Point(path.peekLast());
            directionNextPointOnMovePath = GeometryUtils.getPointToPointDirection(oldPosition, path.peek());
            setFacingDirection(directionNextPointOnMovePath);

            super.setPositionInMap(finalPosition.x, finalPosition.y, game.getMap());
            game.getMap().getTiles()[finalPosition.x][finalPosition.y].setUnitOnTile(this);
            this.gameObjectUiUnitHealth.setPositionInMap(finalPosition.x, finalPosition.y, game.getMap());
            setPositionInMapVisual(oldPosition.x, oldPosition.y, game.getMap());
            setVisible(true);

            if(pointToAttack != null){
                this.hasAttackCommandAfterMoving = true;
                this.pointToAttackAfterMoving = pointToAttack;
            }
            this.waitForAttackAfterMoving = waitForAttack;
            actionableThisTurn = false;

            if(verbose) System.out.println(this+" received an exit transport move command for planes");

            mediaPlayerMovement.play();
            unitState = UnitState.MOVING;

            return this.unitState;
        }
        else {
            System.err.println("[UNIT "+unitType+"] received a move command it can't perform");
            return null;
        }
    }

    /**
     * Move to the next tile in queued path.
     * @return UnitState.MOVING if this unit is still moving afterwards
     */
    public UnitState performFullTileMove(Map map){
        if(unitState == UnitState.MOVING){
            Point nextPoint = movePath.poll();
            if(nextPoint == null){
                onMovementEnd(map);
                return this.unitState;
            } else {
                setPositionInMapVisual(nextPoint.x, nextPoint.y, map);
                boolean continueMove = (movePath.peek() != null);
                if(!continueMove){
                    onMovementEnd(map);
                } else{
                    directionNextPointOnMovePath = GeometryUtils.getPointToPointDirection(nextPoint, movePath.peek());
                    setFacingDirection(directionNextPointOnMovePath);
                }

                return this.unitState;
            }
        }
        return this.unitState;
    }
    public AttackResult performFinishAttack(Map map){
        if(unitState == UnitState.ATTACKING || unitState == UnitState.COUNTERATTACKING){
            return onAttackEnd(map);
        } else {
            System.err.println("[UNIT "+unitType+"] [performFinishAttack] ERROR: Unit is not attacking");
            return new AttackResult(true, 0);
        }
    }
    private AttackResult onAttackHitBy(Unit attackingUnit){
        int damage = Codex.calculateDamageOnAttack(attackingUnit, this);
        int hpBeforeHit = Codex.getUnitHealthDigit(this);
        boolean survivedTheAttack = this.changeStatRemainingHealth(-damage);
        int hpAfterHit = Codex.getUnitHealthDigit(this);
        int visualDamage = hpBeforeHit - hpAfterHit;
        AttackResult attackResult = new AttackResult(survivedTheAttack, -visualDamage);
        return attackResult;
    }
    public boolean changeStatRemainingHealth(int healthDelta){
        this.statRemainingHealth += healthDelta;
        if(statRemainingHealth > statMaxHealth){
            statRemainingHealth = statMaxHealth;
        }
        if(verbose) System.out.println(this+" now has "+statRemainingHealth+" HP remaining");
        gameObjectUiUnitHealth.updateUnitHealth((double) statRemainingHealth / (double) statMaxHealth);
        return statRemainingHealth > 0;
    }

    public class AttackResult {
        public final boolean attackedUnitSurvived;
        public final int hpChange;
        AttackResult(boolean attackedUnitSurvived, int hpChange){
            this.attackedUnitSurvived = attackedUnitSurvived;
            this.hpChange = hpChange;
        }
    }

    /**
     * While moving, increase in-between-tile-offset to graphically represent the unit moving from one tile to a neighboring tile.
     * @param fractionOfTile at what position between tiles should this unit be placed
     */
    public void performInBetweenTileMove(double fractionOfTile, Map map){
        if(unitState == UnitState.MOVING){
            double tileCenterOffsetX, tileCenterOffsetY;
            switch(directionNextPointOnMovePath){
                case RIGHT: tileCenterOffsetX = fractionOfTile; tileCenterOffsetY = 0; break;
                case DOWN: tileCenterOffsetX = 0; tileCenterOffsetY = fractionOfTile; break;
                case LEFT: tileCenterOffsetX = -fractionOfTile; tileCenterOffsetY = 0; break;
                case UP: tileCenterOffsetX = 0; tileCenterOffsetY = -fractionOfTile; break;
                default: tileCenterOffsetX = 0; tileCenterOffsetY = 0; break;
            }
            gameObjectUnit.setTileCenterOffset(tileCenterOffsetX, tileCenterOffsetY, visualTileX, visualTileY, map);
        } else {
            System.err.println("[UNIT "+unitType+"] [performInBetweenTileMove] Unit is not in MOVING state");
        }
    }
    public void performAttack(double fractionOfAttack, Map map){
        //TODO
        // Similar to moving the unit itself in between tiles, here a bullet of sorts will be moved/adjusted.
    }

    /**
     * Render methods are overwritten to include the UI health indicator object in render operations.
     */
    @Override
    public void setPositionInMap(int x, int y, Map map){
        map.getTiles()[this.x][this.y].setUnitOnTile(null);
        super.setPositionInMap(x, y, map);
        map.getTiles()[x][y].setUnitOnTile(this);
        this.gameObjectUiUnitHealth.setPositionInMap(x, y, map);
    }
    @Override
    public void changeTileRenderSize(Map map){
        super.changeTileRenderSize(map);
        this.gameObjectUiUnitHealth.changeTileRenderSize(x, y, map);
    }
    @Override
    public void setGraphicalOffset(double offsetX, double offsetY){
        super.setGraphicalOffset(offsetX, offsetY);
        this.gameObjectUiUnitHealth.setOffset(offsetX, offsetY);
    }
    private void setFacingDirection(Direction direction){
        if(direction == Direction.LEFT){
            gameObjectUnit.setFacingLeft(true);
        } else if(direction == Direction.RIGHT){
            gameObjectUnit.setFacingLeft(false);
        }
    }

    /**
     * Used while the unit is performing its move command. The unit will update it's position only visually while actually
     * being on the destination tile.
     */
    private void setPositionInMapVisual(int x, int y, Map map){
        visualTileX = x;
        visualTileY = y;
        gameObjectUnit.setTileCenterOffset(0, 0, x, y, map);
    }
    public int getVisualTileX(){
        return visualTileX;
    }
    public int getVisualTileY(){
        return visualTileY;
    }

    public UnitState getUnitState(){
        return unitState;
    }

    public int getOwnerId(){
        return ownerId;
    }
    public void setOwnerId(int ownerId){
        this.ownerId = ownerId;
    }
    public boolean hasOwner(){
        return this.ownerId != 0;
    }

    private void onMovementEnd(Map map){
        cleanUpMovingState(map);
        if(hasAttackCommandAfterMoving){
            if(verbose) System.out.println(this+" on movement end, going into attack");
            setStance(UnitStance.ATTACK);
            setFacingDirection(GeometryUtils.getPointToPointDirection(new Point(x, y), pointToAttackAfterMoving));
            Unit attackedUnit = map.getTiles()[pointToAttackAfterMoving.x][pointToAttackAfterMoving.y].getUnitOnTile();
            currentlyAttackedUnit = attackedUnit;
            statRemainingHealthOnAttack = statRemainingHealth;
            mediaPlayerGunshot.play();
            unitStateToAttacking();
        }
        else if(disappearIntoTransportAfterMoving){
            disappearIntoTransportAfterMoving = false;
            setStance(UnitStance.NORMAL);
            unitStateToInTransport();
        }
        else {
            setStance(UnitStance.NORMAL);
            if(actionableThisTurn){
                unitStateToNeutral();
            } else{
                if(waitForAttackAfterMoving){
                    waitForAttackAfterMoving = false;
                    unitState = UnitState.MOVED_AND_WAITING_FOR_ATTACK;
                } else {
                    unitStateToBlackedOut();
                }
            }
        }
    }
    private AttackResult onAttackEnd(Map map){
        //TODO
        // This is a temporary and rudimentary attack script
        AttackResult attackResult = new AttackResult(true, 0);
        if(currentlyAttackedUnit != null){
            attackResult = currentlyAttackedUnit.onAttackHitBy(this);
            this.lastAttackedUnit = currentlyAttackedUnit;
        } else {
            System.err.println(this + " could not find enemy to attack");
        }
        setStance(UnitStance.NORMAL);
        if(actionableThisTurn){
            unitStateToNeutral();
        } else{
            unitStateToBlackedOut();
        }

        return attackResult;
    }

    /**
     * This unit is attacked, presumably during not its turn.
     * @return whether this unit will be counterattacking
     */
    public boolean onAttacked(Unit attackingUnit){
        if(
            Codex.getUnitProfile(attackingUnit).ATTACKTYPE == UnitAttackType.RANGED ||
            Codex.getUnitProfile(this).ATTACKTYPE == UnitAttackType.RANGED ||
            (GeometryUtils.getPointToPointDistance(new Point(attackingUnit.getX(), attackingUnit.getY()), new Point(this.getX(), this.getY())) > Codex.getUnitProfile(this).MAXRANGE) ||
            !Codex.canHit(this, attackingUnit)
        ){
            return false;
        }
        setStance(UnitStance.ATTACK);
        setFacingDirection(GeometryUtils.getPointToPointDirection(new Point(x, y), new Point(attackingUnit.getX(), attackingUnit.getY())));
        this.currentlyAttackedUnit = attackingUnit;
        statRemainingHealthOnAttack = statRemainingHealth;
        this.unitState = UnitState.COUNTERATTACKING;
        return true;
    }
    public Unit getCurrentlyAttackedUnit(){
        return currentlyAttackedUnit;
    }
    public Unit getLastAttackedUnit(){
        return lastAttackedUnit;
    }
    private void unitStateToNeutral(){
        gameObjectUnit.setBlackedOut(false);
        waitForAttackAfterMoving = false;
        unitState = UnitState.NEUTRAL;
    }
    private void unitStateToBlackedOut(){
        gameObjectUnit.setBlackedOut(true);
        waitForAttackAfterMoving = false;
        unitState = UnitState.BLACKED_OUT;
    }
    private void unitStateToAttacking(){
        this.hasAttackCommandAfterMoving = false;
        unitState = UnitState.ATTACKING;
    }
    private void unitStateToInTransport(){
        this.setVisible(false);
        unitState = UnitState.IN_TRANSPORT;
    }
    private void cleanUpMovingState(Map map){
        gameObjectUnit.setTileCenterOffset(0, 0, x, y, map);
        mediaPlayerOnSelect.stop();
        mediaPlayerMovement.stop();
    }

    public void setActionableThisTurn(boolean actionable){
        if(actionable){
            if(unitState == UnitState.BLACKED_OUT){
                unitStateToNeutral();
            }
        } else {
            if(unitState == UnitState.NEUTRAL){
                unitStateToBlackedOut();
            }
        }
        this.actionableThisTurn = actionable;
    }

    /**
     * Called when this unit is killed.
     * Remove all graphical game objects.
     */
    @Override
    public void onRemoval(Group group){
        super.onRemoval(group);
        gameObjectUiUnitHealth.removeSelfFromRoot(group);
    }
    @Override
    public void setVisible(boolean visible){
        if(visible && !inVision){
            return;
        }
        super.setVisible(visible);
        if(this.isDamaged()){
            gameObjectUiUnitHealth.setVisible(visible);
        }
    }
    public void setInVision(boolean inVision){
        this.inVision = inVision;
        if(!inVision){
            setVisible(false);
        } else if(inVision && unitState != UnitState.IN_TRANSPORT){
            setVisible(true);
        }
    }

    public boolean isDamaged(){
        return statRemainingHealth < statMaxHealth;
    }
    public int getRemainingHealthOnAttack(){
        return statRemainingHealthOnAttack;
    }
    public int getStatRemainingHealth(){
        return statRemainingHealth;
    }
    public int getStatMaxHealth(){
        return statMaxHealth;
    }

    @Override
    public String toString(){
        return "[UNIT "+unitType+" "+unitId+"]";
    }

    /**
     * If any building is on the same tile as this unit, that building is captured.
     */
    public void doCaptureAtEndOfTurn(Game game){
        Building buildingToCapture = game.getMap().getTiles()[x][y].getBuildingOnTile();
        if(buildingToCapture != null){
            if(buildingToCapture.getOwnerId() != this.ownerId && Codex.canCapture(this)){
                buildingToCapture.captureAtEndOfTurn(
                    Codex.getUnitHealthDigit((double) statRemainingHealth / (double) statMaxHealth),
                    this.ownerId,
                    game
                );
            }
        }
    }

    public void onSelect(){
        setStance(UnitStance.MOVE_1);

        mediaPlayerOnSelect.play();
    }
    public void onDeselect(){
        mediaPlayerOnSelect.stop();
        if(unitState == UnitState.MOVING && waitForAttackAfterMoving){
            waitForAttackAfterMoving = false;
            return;
        }
        setStance(UnitStance.NORMAL);
        if(unitState == UnitState.MOVED_AND_WAITING_FOR_ATTACK){
            if(actionableThisTurn){
                unitStateToNeutral();
            } else {
                unitStateToBlackedOut();
            }
        }
    }

    public boolean canTransportLoad(Unit unit){
        return Codex.canTransport(this, unit) && this.transportLoadedUnits.size() < Codex.getTransportCapacity(this);
    }
    public void loadToTransport(Unit unit){
        transportLoadedUnits.add(unit);
    }
    public void unloadFromTransport(Unit unit){
        if(!transportLoadedUnits.remove(unit)){
            System.err.println(this+" [unloadFromTransport] ERROR Unit left transport that was not loaded up for transport");
        }
    }
    public ArrayList<Unit> getTransportLoadedUnits(){
        return transportLoadedUnits;
    }

    public int getUnitId(){
        return unitId;
    }

    public ConstructionMenu getConstructionMenu(){
        if(constructionMenu == null) System.err.println(this+" [getConstructionMenu] ERROR Unit has no construction menu");
        return constructionMenu;
    }
    public void setConstructionMenu(ConstructionMenu constructionMenu){
        this.constructionMenu = constructionMenu;
    }

    public void setTransportedBy(Unit transporter){
        this.transportedBy = transporter;
        if(this.transportedBy != null){
            unitStateToInTransport();
        }
    }
}
