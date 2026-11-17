package com.minesweeper.game;

/**
 * Factory for creating Minesweeper games with specific difficulty modes.
 * 
 * This factory encapsulates game creation logic, ensuring consistent initialization
 * and providing a central point for game configuration.
 * 
 * Usage:
 * - GameRules game = GameFactory.createGame(GameMode.EASY);
 * - GameRules game = GameFactory.createGame(new CustomGameMode(15, 15, 30));
 */
public class GameFactory {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static factory methods.
     */
    private GameFactory() {
        throw new AssertionError("Cannot instantiate GameFactory");
    }

    /**
     * Creates a new game with the specified preset difficulty mode.
     * 
     * The board will be initialized with the predefined dimensions and mine count
     * for the given difficulty level.
     *
     * @param mode the difficulty mode (EASY, MEDIUM, HARD)
     * @return a GameRules instance ready for play
     * @throws IllegalArgumentException if mode is null
     */
    public static GameRules createGame(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Game mode cannot be null");
        }

        Board board = new Board(mode.getWidth(), mode.getHeight(), mode.getMineCount());
        return new GameRules(board);
    }

    /**
     * Creates a new game with the specified custom configuration.
     * 
     * This method allows players to create games with custom board dimensions
     * and mine counts beyond the preset difficulty levels.
     * 
     * Input validation is performed by CustomGameMode constructor.
     *
     * @param customMode the custom game configuration
     * @return a GameRules instance ready for play
     * @throws IllegalArgumentException if customMode is null
     */
    public static GameRules createGame(CustomGameMode customMode) {
        if (customMode == null) {
            throw new IllegalArgumentException("Custom game mode cannot be null");
        }

        Board board = new Board(customMode.getWidth(), customMode.getHeight(), customMode.getMineCount());
        return new GameRules(board);
    }

    /**
     * Creates a new game with custom dimensions and mine count.
     * 
     * Convenience method that creates both the CustomGameMode and GameRules.
     * Input validation is performed by CustomGameMode constructor.
     *
     * @param width the board width in tiles
     * @param height the board height in tiles
     * @param mineCount the number of mines
     * @return a GameRules instance ready for play
     * @throws IllegalArgumentException if parameters are invalid
     */
    public static GameRules createCustomGame(int width, int height, int mineCount) {
        CustomGameMode customMode = new CustomGameMode(width, height, mineCount);
        return createGame(customMode);
    }

    /**
     * Creates a new game with custom dimensions, mine count, and name.
     * 
     * Convenience method that creates both the CustomGameMode and GameRules.
     * Input validation is performed by CustomGameMode constructor.
     *
     * @param width the board width in tiles
     * @param height the board height in tiles
     * @param mineCount the number of mines
     * @param name the custom name for this game configuration
     * @return a GameRules instance ready for play
     * @throws IllegalArgumentException if parameters are invalid
     */
    public static GameRules createCustomGame(int width, int height, int mineCount, String name) {
        CustomGameMode customMode = new CustomGameMode(width, height, mineCount, name);
        return createGame(customMode);
    }
}
