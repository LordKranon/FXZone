package fxzone.game.logic;

/**
 * Affects how inputs are processed and how UI elements behave / what is shown on screen during IN-GAME
 */
public enum TurnState {
    NO_TURN,
    NEUTRAL,
    UNIT_SELECTED,
    BUILDING_SELECTED,

    /**
     * Only for network syncing purposes
     */
    ENDING_TURN,
    BEGINNING_TURN,
}
