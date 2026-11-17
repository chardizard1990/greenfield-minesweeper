package com.minesweeper.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Represents the Minesweeper game board.
 * 
 * The board is a 2D grid of tiles that can contain mines. It manages:
 * - Tile states and coordinates
 * - Mine placement (random)
 * - Adjacent tile calculations
 * - Adjacent bomb counting
 * 
 * This class follows defensive copying principles - getters return defensive copies
 * to prevent external modification of internal board state.
 */
public class Board {

    private final int width;
    private final int height;
    private final int mineCount;
    private final Tile[][] tiles;
    private final Random random;

    /**
     * Creates a new Minesweeper board with the specified dimensions and mine count.
     * Mines are placed randomly across the board during construction.
     *
     * @param width the number of columns on the board
     * @param height the number of rows on the board
     * @param mineCount the number of mines to place on the board
     * @throws IllegalArgumentException if mineCount >= width * height (not enough space for mines)
     * @throws IllegalArgumentException if width or height is less than 1
     */
    public Board(int width, int height, int mineCount) {
        // Validate dimensions BEFORE creating arrays
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Board dimensions must be at least 1x1");
        }
        if (mineCount < 0) {
            throw new IllegalArgumentException("Mine count cannot be negative");
        }
        if (mineCount >= width * height) {
            throw new IllegalArgumentException(
                    String.format("Cannot place %d mines on a %dx%d board (max: %d)",
                            mineCount, width, height, width * height - 1)
            );
        }

        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        this.tiles = new Tile[height][width];
        this.random = new Random();

        initializeBoard();
        placeMines();
    }

    /**
     * Constructor for testing that accepts a seeded Random for reproducible mine placement.
     *
     * @param width the number of columns on the board
     * @param height the number of rows on the board
     * @param mineCount the number of mines to place on the board
     * @param random a Random instance (useful for testing with fixed seeds)
     */
    protected Board(int width, int height, int mineCount, Random random) {
        // Validate dimensions BEFORE creating arrays
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Board dimensions must be at least 1x1");
        }
        if (mineCount < 0) {
            throw new IllegalArgumentException("Mine count cannot be negative");
        }
        if (mineCount >= width * height) {
            throw new IllegalArgumentException(
                    String.format("Cannot place %d mines on a %dx%d board (max: %d)",
                            mineCount, width, height, width * height - 1)
            );
        }

        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        this.tiles = new Tile[height][width];
        this.random = random;

        initializeBoard();
        placeMines();
    }

    /**
     * Validates that board dimensions are valid.
     * @throws IllegalArgumentException if dimensions are invalid
     */
    private void validateDimensions() {
        // This method is no longer used as validation happens in constructors
        // Kept for reference in case needed in future
    }

    /**
     * Initializes all tiles on the board in the UNREVEALED state without mines.
     */
    private void initializeBoard() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                tiles[row][col] = new Tile(row, col, false);
            }
        }
    }

    /**
     * Randomly places mines on the board.
     * Uses a Fisher-Yates shuffle-like algorithm to ensure uniform distribution.
     */
    private void placeMines() {
        Set<Integer> minePositions = new HashSet<>();

        // Generate random positions until we have enough mines
        while (minePositions.size() < mineCount) {
            int randomIndex = random.nextInt(width * height);
            minePositions.add(randomIndex);
        }

        // Convert flat indices to 2D coordinates and place mines
        for (int index : minePositions) {
            int row = index / width;
            int col = index % width;
            tiles[row][col] = new Tile(row, col, true);
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

    /**
     * Gets the tile at the specified coordinates.
     *
     * @param row the row index (0-based)
     * @param column the column index (0-based)
     * @return the tile at the given coordinates
     * @throws IndexOutOfBoundsException if coordinates are outside board bounds
     */
    public Tile getTile(int row, int column) {
        validateCoordinates(row, column);
        return tiles[row][column];
    }

    /**
     * Checks if the given coordinates are within board bounds.
     *
     * @param row the row index to validate
     * @param column the column index to validate
     * @return true if coordinates are valid, false otherwise
     */
    public boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < height && column >= 0 && column < width;
    }

    /**
     * Validates that coordinates are within board bounds.
     * @throws IndexOutOfBoundsException if coordinates are outside bounds
     */
    private void validateCoordinates(int row, int column) {
        if (!isWithinBounds(row, column)) {
            throw new IndexOutOfBoundsException(
                    String.format("Coordinates (%d,%d) outside board bounds (%dx%d)",
                            row, column, height, width)
            );
        }
    }

    /**
     * Gets all tiles adjacent to the tile at the given coordinates.
     * Adjacent tiles include all 8 surrounding tiles (up, down, left, right, diagonals).
     * Defensive copy: returns a new list each time to prevent external modification.
     *
     * @param row the row index of the center tile
     * @param column the column index of the center tile
     * @return a list of adjacent tiles
     * @throws IndexOutOfBoundsException if center coordinates are outside bounds
     */
    public List<Tile> getAdjacentTiles(int row, int column) {
        validateCoordinates(row, column);
        List<Tile> adjacent = new ArrayList<>();

        // Check all 8 directions: up, down, left, right, and 4 diagonals
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                // Skip the center tile itself
                if (dr == 0 && dc == 0) {
                    continue;
                }

                int newRow = row + dr;
                int newCol = column + dc;

                // Only add if within bounds
                if (isWithinBounds(newRow, newCol)) {
                    adjacent.add(tiles[newRow][newCol]);
                }
            }
        }

        return adjacent; // Defensive copy - new list
    }

    /**
     * Counts the number of adjacent mines to the tile at the given coordinates.
     * This is the number displayed to the player (0-8).
     *
     * @param row the row index of the tile
     * @param column the column index of the tile
     * @return the count of adjacent mines (0-8)
     * @throws IndexOutOfBoundsException if coordinates are outside bounds
     */
    public int getAdjacentMineCount(int row, int column) {
        validateCoordinates(row, column);
        int count = 0;

        for (Tile adjacent : getAdjacentTiles(row, column)) {
            if (adjacent.isMine()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Gets a 2D array representation of the board for debugging/display purposes.
     * Returns true for mines, false for safe tiles.
     * This is defensive - returns a copy of the internal state.
     *
     * @return a copy of the mine layout
     */
    public boolean[][] getMineLayout() {
        boolean[][] layout = new boolean[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                layout[row][col] = tiles[row][col].isMine();
            }
        }
        return layout;
    }

    /**
     * Gets the total number of unrevealed tiles remaining on the board.
     * This includes tiles that are UNREVEALED or FLAGGED (flagged tiles are still unrevealed).
     * @return count of unrevealed tiles
     */
    public int getUnrevealedTileCount() {
        int count = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Tile tile = tiles[row][col];
                // Count both unrevealed and flagged tiles (flagged = unrevealed but marked)
                if (tile.isUnrevealed() || tile.isFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Gets the total number of flagged tiles on the board.
     * @return count of flagged tiles
     */
    public int getFlaggedTileCount() {
        int count = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (tiles[row][col].isFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return String.format("Board(%dx%d, %d mines, %d unrevealed)",
                width, height, mineCount, getUnrevealedTileCount());
    }
}
