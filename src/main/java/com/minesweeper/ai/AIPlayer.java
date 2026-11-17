package com.minesweeper.ai;

import com.minesweeper.game.FlagResult;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.GameState;
import com.minesweeper.game.RevealResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents an AI player that can autonomously play Minesweeper.
 * 
 * Features:
 * - Plays complete games autonomously using a chosen strategy
 * - Tracks all moves and reasoning for analysis/replay
 * - Supports configurable difficulty levels (EASY, NORMAL, HARD)
 * - Implements best-practice move execution
 * 
 * The AI:
 * 1. Gets the next move from its strategy
 * 2. Executes the move (flag, unflag, or reveal)
 * 3. Records move history for analysis
 * 4. Repeats until the game ends
 */
public class AIPlayer {

    private final String name;
    private final AIStrategy strategy;
    private final GameRules gameRules;
    private final List<AIMove> moveHistory;
    private int gamesWon = 0;
    private int gamesLost = 0;

    /**
     * Creates an AI player with the specified strategy.
     *
     * @param name the name/identifier for this AI player
     * @param strategy the AI strategy to use for decision making
     * @param gameRules the game rules instance to play against
     * @throws IllegalArgumentException if any parameter is null
     */
    public AIPlayer(String name, AIStrategy strategy, GameRules gameRules) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        if (gameRules == null) {
            throw new IllegalArgumentException("GameRules cannot be null");
        }

        this.name = name;
        this.strategy = strategy;
        this.gameRules = gameRules;
        this.moveHistory = new ArrayList<>();
    }

    // Accessors
    public String getName() {
        return name;
    }

    public AIStrategy getStrategy() {
        return strategy;
    }

    public AIDifficulty getDifficulty() {
        return strategy.getDifficulty();
    }

    public GameRules getGameRules() {
        return gameRules;
    }

    public List<AIMove> getMoveHistory() {
        return new ArrayList<>(moveHistory); // Defensive copy
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getTotalGamesPlayed() {
        return gamesWon + gamesLost;
    }

    public double getWinRate() {
        int total = getTotalGamesPlayed();
        if (total == 0) {
            return 0.0;
        }
        return (double) gamesWon / total * 100.0;
    }

    /**
     * Executes one complete game, making moves until the game ends.
     * Tracks all moves and updates win/loss statistics.
     * 
     * @return true if the AI won, false if it lost
     * @throws IllegalStateException if game is not in ACTIVE state
     */
    public boolean playGame() {
        if (gameRules.getGameState() != GameState.ACTIVE) {
            throw new IllegalStateException("Game is not in ACTIVE state. Cannot start new game with existing GameRules.");
        }

        moveHistory.clear();
        int maxFailedMoves = 100; // Safeguard against infinite loops
        int consecutiveFailures = 0;

        while (gameRules.getGameState() == GameState.ACTIVE) {
            Optional<AIMove> nextMove = strategy.getNextMove(gameRules);

            if (!nextMove.isPresent()) {
                // No valid move available but game is still active - shouldn't happen
                break;
            }

            AIMove move = nextMove.get();
            moveHistory.add(move);

            // Execute the move
            boolean moveSucceeded = executeMoveInternal(move);

            if (!moveSucceeded) {
                // Move execution failed - track consecutive failures
                consecutiveFailures++;
                if (consecutiveFailures > maxFailedMoves) {
                    // Too many failures - break to avoid infinite loop
                    break;
                }
                continue;
            }

            // Reset failure counter on successful move
            consecutiveFailures = 0;
        }

        // Record outcome
        GameState finalState = gameRules.getGameState();
        if (finalState == GameState.WON) {
            gamesWon++;
            return true;
        } else if (finalState == GameState.LOST) {
            gamesLost++;
            return false;
        }

        return false;
    }

    /**
     * Executes a single move and returns whether it succeeded.
     * 
     * @param move the move to execute
     * @return true if move was valid and executed, false if move failed
     */
    private boolean executeMoveInternal(AIMove move) {
        switch (move.getMoveType()) {
            case REVEAL:
                RevealResult revealResult = gameRules.revealTile(move.getRow(), move.getColumn());
                return revealResult.isSuccess();

            case FLAG:
                FlagResult flagResult = gameRules.toggleFlag(move.getRow(), move.getColumn());
                // Flag result is successful if toggle was successful
                // Check if tile is now flagged (would only be true if toggle succeeded)
                return flagResult.isSuccess() && flagResult.isFlaggedNow();

            case UNFLAG:
                FlagResult unflagResult = gameRules.toggleFlag(move.getRow(), move.getColumn());
                // Unflag is successful if toggle succeeded and tile is NOT now flagged
                return unflagResult.isSuccess() && !unflagResult.isFlaggedNow();

            default:
                return false;
        }
    }

    /**
     * Clears the move history for this player.
     * Useful when replaying games or resetting state.
     */
    public void clearMoveHistory() {
        moveHistory.clear();
    }

    /**
     * Gets a formatted summary of the last game played.
     * 
     * @return string describing the last game's outcome and statistics
     */
    public String getLastGameSummary() {
        if (moveHistory.isEmpty()) {
            return "No games played yet";
        }

        GameState finalState = gameRules.getGameState();
        String outcome = finalState == GameState.WON ? "WON" : "LOST";
        return String.format(
                "%s played %s game in %d moves",
                name, outcome, moveHistory.size()
        );
    }

    @Override
    public String toString() {
        return String.format(
                "AIPlayer(%s, difficulty=%s, wins=%d, losses=%d, winRate=%.1f%%)",
                name, strategy.getDifficulty(), gamesWon, gamesLost, getWinRate()
        );
    }
}
