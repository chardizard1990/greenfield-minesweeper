package com.minesweeper.gui.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Minesweeper game board.
 * 
 * This manages the presentation state of the entire board without any game logic.
 * The game logic remains in the game module (100% reused from MVP).
 * 
 * This ViewModel holds a 2D grid of TileViewModels and coordinates updates
 * when the underlying game state changes.
 */
public class GameBoardViewModel extends ViewModel {
    
    public enum GameState {
        ACTIVE,   // Game in progress
        WON,      // All safe tiles revealed
        LOST      // Hit a mine
    }
    
    // Board dimensions
    private final int boardWidth;
    private final int boardHeight;
    
    // 2D array of tile view models
    private final TileViewModel[][] tiles;
    
    // Observable properties
    private final ObservableProperty<GameState> gameState;
    private final ObservableProperty<Integer> revealedCount;
    private final ObservableProperty<Integer> flaggedCount;
    private final ObservableProperty<Integer> totalMines;
    
    // Event listeners for tile interactions
    private List<TileBoardListener> tileBoardListeners = new ArrayList<>();
    
    /**
     * Create a game board view model.
     * 
     * @param width Number of columns
     * @param height Number of rows
     */
    public GameBoardViewModel(int width, int height) {
        this.boardWidth = width;
        this.boardHeight = height;
        this.tiles = new TileViewModel[height][width];
        this.gameState = new ObservableProperty<>(GameState.ACTIVE);
        this.revealedCount = new ObservableProperty<>(0);
        this.flaggedCount = new ObservableProperty<>(0);
        this.totalMines = new ObservableProperty<>(0);
        
        // Initialize all tiles
        initializeTiles();
    }
    
    /**
     * Initialize the tile grid with unrevealed tiles.
     */
    private void initializeTiles() {
        for (int row = 0; row < boardHeight; row++) {
            for (int col = 0; col < boardWidth; col++) {
                tiles[row][col] = new TileViewModel(row, col);
            }
        }
    }
    
    // Getters for board dimensions
    
    public int getBoardWidth() {
        return boardWidth;
    }
    
    public int getBoardHeight() {
        return boardHeight;
    }
    
    // Tile access
    
    public TileViewModel getTile(int row, int col) {
        if (row >= 0 && row < boardHeight && col >= 0 && col < boardWidth) {
            return tiles[row][col];
        }
        return null;
    }
    
    public TileViewModel[][] getTiles() {
        return tiles;
    }
    
    // State properties
    
    public ObservableProperty<GameState> gameStateProperty() {
        return gameState;
    }
    
    public GameState getGameState() {
        return gameState.getValue();
    }
    
    public void setGameState(GameState state) {
        gameState.setValue(state);
    }
    
    public ObservableProperty<Integer> revealedCountProperty() {
        return revealedCount;
    }
    
    public Integer getRevealedCount() {
        return revealedCount.getValue();
    }
    
    public void setRevealedCount(int count) {
        revealedCount.setValue(count);
    }
    
    public ObservableProperty<Integer> flaggedCountProperty() {
        return flaggedCount;
    }
    
    public Integer getFlaggedCount() {
        return flaggedCount.getValue();
    }
    
    public void setFlaggedCount(int count) {
        flaggedCount.setValue(count);
    }
    
    public ObservableProperty<Integer> totalMinesProperty() {
        return totalMines;
    }
    
    public Integer getTotalMines() {
        return totalMines.getValue();
    }
    
    public void setTotalMines(int count) {
        totalMines.setValue(count);
    }
    
    // Event listener management for tile interactions
    
    public void addTileBoardListener(TileBoardListener listener) {
        if (listener != null) {
            tileBoardListeners.add(listener);
        }
    }
    
    public void removeTileBoardListener(TileBoardListener listener) {
        tileBoardListeners.remove(listener);
    }
    
    public void clearTileBoardListeners() {
        tileBoardListeners.clear();
    }
    
    /**
     * Notify all listeners that a tile was clicked (to be revealed).
     */
    public void notifyTileClicked(int row, int col) {
        for (TileBoardListener listener : tileBoardListeners) {
            try {
                listener.onTileClicked(row, col);
            } catch (Exception e) {
                System.err.println("Error notifying tile click listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Notify all listeners that a tile was flagged.
     */
    public void notifyTileFlagged(int row, int col) {
        for (TileBoardListener listener : tileBoardListeners) {
            try {
                listener.onTileFlagged(row, col);
            } catch (Exception e) {
                System.err.println("Error notifying tile flag listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Reset the board to initial state (all unrevealed).
     */
    public void resetBoard() {
        for (int row = 0; row < boardHeight; row++) {
            for (int col = 0; col < boardWidth; col++) {
                tiles[row][col] = new TileViewModel(row, col);
            }
        }
        setGameState(GameState.ACTIVE);
        setRevealedCount(0);
        setFlaggedCount(0);
    }
    
    @Override
    public String toString() {
        return String.format("GameBoardVM[%dx%d]:%s", boardWidth, boardHeight, gameState.getValue());
    }
    
    /**
     * Listener interface for tile board interactions.
     */
    @FunctionalInterface
    public interface TileBoardListener {
        /**
         * Called when a tile is clicked and should be revealed.
         * 
         * @param row Row of the clicked tile
         * @param col Column of the clicked tile
         */
        void onTileClicked(int row, int col);
        
        /**
         * Called when a tile is flagged.
         * 
         * @param row Row of the flagged tile
         * @param col Column of the flagged tile
         */
        default void onTileFlagged(int row, int col) {
            // Default implementation does nothing
        }
    }
}
