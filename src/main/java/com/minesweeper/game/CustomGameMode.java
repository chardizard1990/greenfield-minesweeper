package com.minesweeper.game;

/**
 * Represents a custom game configuration with user-defined dimensions and mine count.
 * 
 * This class allows players to create custom board configurations beyond the preset modes.
 * All inputs are validated to ensure reasonable bounds and valid configurations.
 */
public class CustomGameMode {

    // Validation constants for reasonable board sizes
    private static final int MIN_WIDTH = 1;
    private static final int MAX_WIDTH = 50;
    private static final int MIN_HEIGHT = 1;
    private static final int MAX_HEIGHT = 50;

    private final int width;
    private final int height;
    private final int mineCount;
    private final String name;

    /**
     * Creates a custom game mode with the specified dimensions and mine count.
     * 
     * Validation rules:
     * - Width and height must be between 1 and 50
     * - Mine count must be non-negative
     * - Mine count must be less than total tiles (at least one safe tile)
     *
     * @param width the board width in tiles
     * @param height the board height in tiles
     * @param mineCount the number of mines on the board
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public CustomGameMode(int width, int height, int mineCount) {
        this(width, height, mineCount, "Custom");
    }

    /**
     * Creates a custom game mode with a custom name.
     *
     * @param width the board width in tiles
     * @param height the board height in tiles
     * @param mineCount the number of mines on the board
     * @param name the custom name for this mode
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public CustomGameMode(int width, int height, int mineCount, String name) {
        validateDimensions(width, height);
        validateMineCount(mineCount, width * height);
        
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        this.name = (name != null && !name.trim().isEmpty()) ? name : "Custom";
    }

    /**
     * Validates board dimensions are within acceptable bounds.
     * @throws IllegalArgumentException if dimensions are invalid
     */
    private void validateDimensions(int width, int height) {
        if (width < MIN_WIDTH || width > MAX_WIDTH) {
            throw new IllegalArgumentException(
                    String.format("Width must be between %d and %d, got %d",
                            MIN_WIDTH, MAX_WIDTH, width)
            );
        }
        if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Height must be between %d and %d, got %d",
                            MIN_HEIGHT, MAX_HEIGHT, height)
            );
        }
    }

    /**
     * Validates mine count is reasonable for the board size.
     * @throws IllegalArgumentException if mine count is invalid
     */
    private void validateMineCount(int mineCount, int totalTiles) {
        if (mineCount < 0) {
            throw new IllegalArgumentException("Mine count cannot be negative");
        }
        if (mineCount >= totalTiles) {
            throw new IllegalArgumentException(
                    String.format("Cannot place %d mines on %d-tile board (max: %d)",
                            mineCount, totalTiles, totalTiles - 1)
            );
        }
    }

    // Accessors
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMineCount() {
        return mineCount;
    }

    public String getName() {
        return name;
    }

    /**
     * Gets the total number of tiles on this board.
     * @return width × height
     */
    public int getTotalTiles() {
        return width * height;
    }

    /**
     * Gets the mine density percentage for this configuration.
     * @return percentage of tiles that contain mines (0-100)
     */
    public double getMineDensity() {
        return (double) mineCount / getTotalTiles() * 100;
    }

    @Override
    public String toString() {
        return String.format("%s (%dx%d, %d mines, %.1f%%)",
                name, width, height, mineCount, getMineDensity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomGameMode)) return false;
        CustomGameMode that = (CustomGameMode) o;
        return width == that.width && height == that.height && mineCount == that.mineCount;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(width, height, mineCount);
    }
}
