package com.minesweeper.ai;

import com.minesweeper.game.GameRules;
import java.util.Optional;

/**
 * Interface for AI strategies in Minesweeper.
 * Implementations provide different decision-making algorithms.
 */
public interface AIStrategy {

    /**
     * Decides the next move based on the current game state.
     * 
     * @param gameRules the current game rules and state
     * @return an Optional containing the next move, or empty if no good move available
     */
    Optional<AIMove> getNextMove(GameRules gameRules);

    /**
     * Gets the difficulty level of this strategy.
     * 
     * @return the difficulty level
     */
    AIDifficulty getDifficulty();
}
