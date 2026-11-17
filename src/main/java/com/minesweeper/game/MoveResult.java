package com.minesweeper.game;

/**
 * Result of a player's move attempt.
 * 
 * Encapsulates:
 * - Whether move was valid
 * - The underlying game result (reveal or flag)
 * - Human-readable message
 */
public class MoveResult {

    public enum Outcome {
        SUCCESS,              // Move executed successfully
        INVALID_COORDINATES,  // Coordinates out of bounds
        GAME_NOT_ACTIVE,      // Game already won/lost
        MOVE_EXECUTION_FAILED // Move was invalid (e.g., revealing flagged tile)
    }

    private final Outcome outcome;
    private final GameState newGameState;
    private final String message;
    private final RevealResult revealResult;  // Null if move was flag
    private final FlagResult flagResult;      // Null if move was reveal

    /**
     * Creates a successful move result.
     */
    public MoveResult(Outcome outcome, GameState newGameState, String message,
                     RevealResult revealResult, FlagResult flagResult) {
        this.outcome = outcome;
        this.newGameState = newGameState;
        this.message = message;
        this.revealResult = revealResult;
        this.flagResult = flagResult;
    }

    // Accessors
    public Outcome getOutcome() {
        return outcome;
    }

    public GameState getNewGameState() {
        return newGameState;
    }

    public String getMessage() {
        return message;
    }

    public RevealResult getRevealResult() {
        return revealResult;
    }

    public FlagResult getFlagResult() {
        return flagResult;
    }

    public boolean isSuccess() {
        return outcome == Outcome.SUCCESS;
    }

    @Override
    public String toString() {
        return String.format("MoveResult(%s, state=%s, msg=%s)",
                outcome, newGameState, message);
    }
}
