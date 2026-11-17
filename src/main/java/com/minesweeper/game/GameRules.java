package com.minesweeper.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Implements the core rules and mechanics of Minesweeper.
 * 
 * GameRules manages:
 * - Game state (ACTIVE, WON, LOST)
 * - Tile reveal logic with cascading for empty regions
 * - Tile flagging with mine count tracking
 * - Win/loss condition detection
 * - Move validation
 * 
 * This class follows a functional approach - methods return outcomes and don't throw exceptions.
 * This makes it suitable for both console and GUI implementations.
 */
public class GameRules {

    private final Board board;
    private GameState gameState;
    private int remainingMines;

    /**
     * Creates a new game with the given board.
     * Game starts in ACTIVE state with all mines remaining to be found.
     *
     * @param board the board to play on
     * @throws IllegalArgumentException if board is null
     */
    public GameRules(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        this.board = board;
        this.gameState = GameState.ACTIVE;
        this.remainingMines = board.getMineCount();
    }

    // Accessors
    public Board getBoard() {
        return board;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getRemainingMines() {
        return remainingMines;
    }

    public int getFlaggedMineCount() {
        return board.getFlaggedTileCount();
    }

    public int getUnrevealedTileCount() {
        return board.getUnrevealedTileCount();
    }

    /**
     * Attempts to reveal a tile at the given coordinates.
     * 
     * If the tile is safe and has no adjacent mines, automatically reveals all adjacent tiles
     * recursively (cascading reveal). This is the flood-fill behavior players expect.
     *
     * @param row the row of the tile to reveal
     * @param column the column of the tile to reveal
     * @return a RevealResult containing the outcome and new game state
     */
    public RevealResult revealTile(int row, int column) {
        // Validate game state
        if (gameState != GameState.ACTIVE) {
            return new RevealResult(
                    RevealResult.Outcome.GAME_NOT_ACTIVE,
                    gameState,
                    null,
                    String.format("Cannot reveal tile - game is %s", gameState)
            );
        }

        // Validate coordinates
        if (!board.isWithinBounds(row, column)) {
            return new RevealResult(
                    RevealResult.Outcome.INVALID_COORDINATES,
                    gameState,
                    null,
                    String.format("Invalid coordinates: (%d,%d) outside board (%dx%d)",
                            row, column, board.getHeight(), board.getWidth())
            );
        }

        Tile tile = board.getTile(row, column);

        // Check if already revealed
        if (tile.isRevealed()) {
            return new RevealResult(
                    RevealResult.Outcome.ALREADY_REVEALED,
                    gameState,
                    tile,
                    String.format("Tile at (%d,%d) is already revealed", row, column)
            );
        }

        // Check if flagged (players can't reveal flagged tiles - protection mechanism)
        if (tile.isFlagged()) {
            return new RevealResult(
                    RevealResult.Outcome.TILE_FLAGGED,
                    gameState,
                    tile,
                    String.format("Tile at (%d,%d) is flagged - cannot reveal", row, column)
            );
        }

        // Hit a mine - game over immediately
        if (tile.isMine()) {
            tile.reveal();
            gameState = GameState.LOST;
            return new RevealResult(
                    RevealResult.Outcome.HIT_MINE,
                    gameState,
                    tile,
                    String.format("HIT! Mine revealed at (%d,%d) - Game Over!", row, column)
            );
        }

        // Safe tile - reveal it and check for cascading reveal
        tile.reveal();
        int adjacentMines = board.getAdjacentMineCount(row, column);

        // If this tile has no adjacent mines, perform cascading reveal
        if (adjacentMines == 0) {
            performCascadingReveal(row, column);
        }

        // Check win condition
        if (checkWinCondition()) {
            gameState = GameState.WON;
            return new RevealResult(
                    RevealResult.Outcome.SUCCESS,
                    gameState,
                    tile,
                    "Game WON! All non-mine tiles revealed"
            );
        }

        return new RevealResult(
                RevealResult.Outcome.SUCCESS,
                gameState,
                tile,
                String.format("Revealed tile at (%d,%d) with %d adjacent mines", row, column, adjacentMines)
        );
    }

    /**
     * Performs a cascading (flood-fill) reveal starting from a tile with 0 adjacent mines.
     * Uses BFS to reveal all connected safe regions.
     * 
     * Algorithm:
     * 1. Queue the starting tile
     * 2. For each tile in queue:
     *    - Reveal adjacent unrevealed tiles
     *    - If an adjacent tile has 0 mines, queue it for processing
     *    - Stop at tiles with 1+ adjacent mines (they form boundary)
     *
     * @param startRow the row of the tile with 0 adjacent mines
     * @param startColumn the column of the tile with 0 adjacent mines
     */
    private void performCascadingReveal(int startRow, int startColumn) {
        // Track visited tiles to avoid processing same tile twice
        Set<String> visited = new HashSet<>();
        Queue<String> toProcess = new LinkedList<>();

        String startKey = startRow + "," + startColumn;
        toProcess.offer(startKey);
        visited.add(startKey);

        while (!toProcess.isEmpty()) {
            String key = toProcess.poll();
            String[] coords = key.split(",");
            int row = Integer.parseInt(coords[0]);
            int column = Integer.parseInt(coords[1]);

            // Get adjacent tiles
            java.util.List<Tile> adjacent = board.getAdjacentTiles(row, column);

            for (Tile adjTile : adjacent) {
                int adjRow = adjTile.getRow();
                int adjCol = adjTile.getColumn();
                String adjKey = adjRow + "," + adjCol;

                // Skip if already visited
                if (visited.contains(adjKey)) {
                    continue;
                }

                visited.add(adjKey);

                // Skip if already revealed
                if (adjTile.isRevealed()) {
                    continue;
                }

                // Skip flagged tiles (players flagged them, so protect them)
                if (adjTile.isFlagged()) {
                    continue;
                }

                // Reveal this adjacent tile
                adjTile.reveal();

                // If this tile also has 0 adjacent mines, queue it for processing
                // Otherwise it forms a boundary and we stop here
                int adjacentMineCount = board.getAdjacentMineCount(adjRow, adjCol);
                if (adjacentMineCount == 0) {
                    toProcess.offer(adjKey);
                }
            }
        }
    }

    /**
     * Toggles the flag state of a tile.
     * Flagging indicates the player believes this tile contains a mine.
     * Flagged tiles cannot be revealed (player protection).
     * Remaining mine count is updated when flagging/unflagging.
     *
     * @param row the row of the tile to flag/unflag
     * @param column the column of the tile to flag/unflag
     * @return a FlagResult containing the outcome
     */
    public FlagResult toggleFlag(int row, int column) {
        // Validate game state
        if (gameState != GameState.ACTIVE) {
            return new FlagResult(
                    FlagResult.Outcome.GAME_NOT_ACTIVE,
                    false,
                    String.format("Cannot flag tile - game is %s", gameState)
            );
        }

        // Validate coordinates
        if (!board.isWithinBounds(row, column)) {
            return new FlagResult(
                    FlagResult.Outcome.INVALID_COORDINATES,
                    false,
                    String.format("Invalid coordinates: (%d,%d)", row, column)
            );
        }

        Tile tile = board.getTile(row, column);

        // Can't flag revealed tiles
        if (tile.isRevealed()) {
            return new FlagResult(
                    FlagResult.Outcome.TILE_REVEALED,
                    false,
                    String.format("Cannot flag revealed tile at (%d,%d)", row, column)
            );
        }

        try {
            boolean isFlaggedNow = tile.toggleFlag();

            // Update remaining mines count
            if (isFlaggedNow) {
                remainingMines--;
            } else {
                remainingMines++;
            }

            String outcome = isFlaggedNow ? "flagged" : "unflagged";
            return new FlagResult(
                    FlagResult.Outcome.SUCCESS,
                    isFlaggedNow,
                    String.format("Tile at (%d,%d) %s. Remaining mines: %d",
                            row, column, outcome, remainingMines)
            );
        } catch (IllegalStateException e) {
            return new FlagResult(
                    FlagResult.Outcome.ERROR,
                    false,
                    e.getMessage()
            );
        }
    }

    /**
     * Checks if the win condition has been met.
     * Win condition: All non-mine tiles have been revealed.
     * 
     * Calculation: Total unrevealed tiles = unrevealed + flagged tiles
     *              Non-mine tiles = total tiles - mine count
     *              Win when: unrevealed count == mine count (only mines left unrevealed)
     *
     * @return true if all non-mine tiles are revealed, false otherwise
     */
    private boolean checkWinCondition() {
        // Count of unrevealed tiles includes both UNREVEALED and FLAGGED states
        int unrevealedCount = board.getUnrevealedTileCount();
        
        // If unrevealed count equals mine count, only mines are left unrevealed
        // Therefore all non-mine tiles have been revealed - WIN!
        return unrevealedCount == board.getMineCount();
    }

    /**
     * Reveals all tiles on the board.
     * Used when game is lost to show the player all mines.
     * Does not change game state - must already be LOST.
     */
    public void revealAllTiles() {
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);
                if (!tile.isRevealed()) {
                    tile.reveal();
                }
            }
        }
    }

    /**
     * Checks if a tile at given coordinates is safe (not a mine).
     * Returns false if coordinates are invalid.
     * Public utility for game analysis and AI.
     *
     * @param row the row to check
     * @param column the column to check
     * @return true if tile is not a mine, false if it's a mine or coordinates invalid
     */
    public boolean isSafe(int row, int column) {
        if (!board.isWithinBounds(row, column)) {
            return false;
        }
        return !board.getTile(row, column).isMine();
    }

    @Override
    public String toString() {
        return String.format("GameRules(state=%s, unrevealed=%d, flagged=%d, remainingMines=%d)",
                gameState, board.getUnrevealedTileCount(), board.getFlaggedTileCount(), remainingMines);
    }
}
