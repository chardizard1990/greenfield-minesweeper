package com.minesweeper.ai;

import com.minesweeper.game.GameRules;
import java.util.Random;

/**
 * Factory for creating AI players with specific difficulty levels and strategies.
 * 
 * This factory encapsulates the creation logic, making it easy to:
 * - Create AI players with different difficulty levels
 * - Provide reproducible AI behavior via seeded Random instances
 * - Add new AI strategies in the future
 */
public class AIPlayerFactory {

    private AIPlayerFactory() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates an AI player with the specified difficulty.
     *
     * @param name the name for the AI player
     * @param difficulty the difficulty level
     * @param gameRules the game to play
     * @return a new AI player configured for the difficulty level
     * @throws IllegalArgumentException if difficulty is HARD (not yet implemented)
     */
    public static AIPlayer createAI(String name, AIDifficulty difficulty, GameRules gameRules) {
        return createAI(name, difficulty, gameRules, new Random());
    }

    /**
     * Creates an AI player with the specified difficulty and seeded Random.
     * Useful for testing and reproducible AI behavior.
     *
     * @param name the name for the AI player
     * @param difficulty the difficulty level
     * @param gameRules the game to play
     * @param random a Random instance (useful for testing)
     * @return a new AI player configured for the difficulty level
     * @throws IllegalArgumentException if difficulty is HARD (not yet implemented)
     */
    public static AIPlayer createAI(String name, AIDifficulty difficulty, GameRules gameRules, Random random) {
        if (difficulty == null) {
            throw new IllegalArgumentException("Difficulty cannot be null");
        }

        AIStrategy strategy;

        switch (difficulty) {
            case EASY:
                strategy = new EasyAIStrategy(random);
                break;
            case NORMAL:
                strategy = new NormalAIStrategy(random);
                break;
            case HARD:
                // HARD strategy not yet implemented
                throw new IllegalArgumentException("HARD difficulty not yet implemented");
            default:
                throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }

        return new AIPlayer(name, strategy, gameRules);
    }
}
