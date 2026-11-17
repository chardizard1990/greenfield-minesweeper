package com.minesweeper.game;

/**
 * Represents the result of a tile reveal action.
 * 
 * This immutable class captures:
 * - Whether the reveal was successful
 * - The new game state after the reveal
 * - The tile that was revealed
 * - Any additional message/reason for the outcome
 */
public class RevealResult {

    /**
     * Enum representing different reveal outcomes.
     */
    public enum Outcome {
        /**
         * Tile was successfully revealed (and was safe).
         */
        SUCCESS,

        /**
         * Tile was not revealed because it's already been revealed.
         */
        ALREADY_REVEALED,

        /**
         * Tile was not revealed because it's flagged (player protection).
         */
        TILE_FLAGGED,

        /**
         * Tile was revealed but it contained a mine - game over.
         */
        HIT_MINE,

        /**
         * Game is not active (already won or lost).
         */
        GAME_NOT_ACTIVE,

        /**
         * Coordinates are invalid (out of bounds).
         */
        INVALID_COORDINATES
    }

    private final Outcome outcome;
    private final GameState newGameState;
    private final Tile revealedTile;
    private final String message;

    /**
     * Creates a reveal result.
     *
     * @param outcome the outcome of the reveal attempt
     * @param newGameState the resulting game state
     * @param revealedTile the tile that was revealed (may be null for failed attempts)
     * @param message a descriptive message explaining the outcome
     */
    public RevealResult(Outcome outcome, GameState newGameState, Tile revealedTile, String message) {
        this.outcome = outcome;
        this.newGameState = newGameState;
        this.revealedTile = revealedTile;
        this.message = message;
    }

    // Accessors
    public Outcome getOutcome() {
        return outcome;
    }

    public GameState getNewGameState() {
        return newGameState;
    }

    public Tile getRevealedTile() {
        return revealedTile;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Checks if the reveal was successful.
     * @return true if outcome is SUCCESS, false otherwise
     */
    public boolean isSuccess() {
        return outcome == Outcome.SUCCESS;
    }

    /**
     * Checks if the reveal resulted in hitting a mine.
     * @return true if outcome is HIT_MINE, false otherwise
     */
    public boolean isHitMine() {
        return outcome == Outcome.HIT_MINE;
    }

    @Override
    public String toString() {
        return String.format("RevealResult(%s, state=%s, msg=%s)",
                outcome, newGameState, message);
    }
}
