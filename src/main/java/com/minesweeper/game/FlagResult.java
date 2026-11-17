package com.minesweeper.game;

/**
 * Represents the result of a tile flag/unflag action.
 * 
 * This immutable class captures:
 * - Whether the flag operation was successful
 * - Whether the tile is now flagged
 * - Any message/reason for the outcome
 */
public class FlagResult {

    /**
     * Enum representing different flag operation outcomes.
     */
    public enum Outcome {
        /**
         * Tile was successfully flagged or unflagged.
         */
        SUCCESS,

        /**
         * Tile cannot be flagged because it's already been revealed.
         */
        TILE_REVEALED,

        /**
         * Game is not active (already won or lost).
         */
        GAME_NOT_ACTIVE,

        /**
         * Coordinates are invalid (out of bounds).
         */
        INVALID_COORDINATES,

        /**
         * An unexpected error occurred.
         */
        ERROR
    }

    private final Outcome outcome;
    private final boolean isFlaggedNow;
    private final String message;

    /**
     * Creates a flag result.
     *
     * @param outcome the outcome of the flag operation
     * @param isFlaggedNow whether the tile is now flagged (true) or not flagged (false)
     * @param message a descriptive message explaining the outcome
     */
    public FlagResult(Outcome outcome, boolean isFlaggedNow, String message) {
        this.outcome = outcome;
        this.isFlaggedNow = isFlaggedNow;
        this.message = message;
    }

    // Accessors
    public Outcome getOutcome() {
        return outcome;
    }

    public boolean isFlaggedNow() {
        return isFlaggedNow;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Checks if the flag operation was successful.
     * @return true if outcome is SUCCESS, false otherwise
     */
    public boolean isSuccess() {
        return outcome == Outcome.SUCCESS;
    }

    @Override
    public String toString() {
        return String.format("FlagResult(%s, flagged=%s, msg=%s)",
                outcome, isFlaggedNow, message);
    }
}
