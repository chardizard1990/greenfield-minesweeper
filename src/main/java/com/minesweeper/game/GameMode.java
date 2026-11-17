package com.minesweeper.game;

/**
 * Represents a Minesweeper difficulty mode with preset board configurations.
 * 
 * Each mode defines specific board dimensions and mine counts.
 * This enum provides standard presets; custom configurations can be created
 * using the custom() factory method.
 */
public enum GameMode {
    /**
     * Easy difficulty: 10x10 board with 10 mines (~10% mine density).
     * Suitable for beginners and casual play.
     */
    EASY("Easy", "10x10 board with 10 mines - Perfect for beginners", 10, 10, 10),

    /**
     * Medium difficulty: 20x20 board with 75 mines (~19% mine density).
     * Suitable for experienced players.
     */
    MEDIUM("Medium", "20x20 board with 75 mines - For experienced players", 20, 20, 75),

    /**
     * Hard difficulty: 30x30 board with 150 mines (~17% mine density).
     * Suitable for expert players.
     */
    HARD("Hard", "30x30 board with 150 mines - Extreme challenge", 30, 30, 150);

    private final String displayName;
    private final String description;
    private final int width;
    private final int height;
    private final int mineCount;

    /**
     * Creates a game mode with preset dimensions and mine count.
     *
     * @param displayName the user-friendly name for this mode
     * @param description a detailed description of the mode
     * @param width the board width in tiles
     * @param height the board height in tiles
     * @param mineCount the number of mines on the board
     */
    GameMode(String displayName, String description, int width, int height, int mineCount) {
        this.displayName = displayName;
        this.description = description;
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
    }

    // Accessors
    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMineCount() {
        return mineCount;
    }

    /**
     * Gets the total number of tiles on this board.
     * @return width × height
     */
    public int getTotalTiles() {
        return width * height;
    }

    /**
     * Gets the mine density percentage for this mode.
     * @return percentage of tiles that contain mines (0-100)
     */
    public double getMineDensity() {
        return (double) mineCount / getTotalTiles() * 100;
    }

    @Override
    public String toString() {
        return String.format("%s (%dx%d, %d mines, %.1f%%)",
                displayName, width, height, mineCount, getMineDensity());
    }
}
