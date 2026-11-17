package com.minesweeper.gui.viewmodel;

/**
 * ViewModel for a single tile in the Minesweeper game board.
 * 
 * This manages the presentation state of a tile without any game logic.
 * The actual game logic remains in the game module (100% reused from MVP).
 * 
 * Observable properties allow the TileView to automatically update
 * whenever the tile state changes.
 */
public class TileViewModel extends ViewModel {
    
    // Enum for tile state
    public enum TileState {
        UNREVEALED,  // Not yet clicked
        REVEALED,    // Clicked and safe (0-8 adjacent mines)
        FLAGGED,     // Right-clicked (assumed mine)
        MINE         // Revealed as a mine (game end)
    }
    
    // Observable properties
    private final ObservableProperty<TileState> tileState;
    private final ObservableProperty<Integer> adjacentMineCount;
    private final ObservableProperty<Boolean> isFlagged;
    private final ObservableProperty<Boolean> isRevealed;
    
    // Position on board (for reference)
    private final int row;
    private final int col;
    
    /**
     * Create a new tile view model.
     * 
     * @param row Row position on board
     * @param col Column position on board
     */
    public TileViewModel(int row, int col) {
        this.row = row;
        this.col = col;
        this.tileState = new ObservableProperty<>(TileState.UNREVEALED);
        this.adjacentMineCount = new ObservableProperty<>(0);
        this.isFlagged = new ObservableProperty<>(false);
        this.isRevealed = new ObservableProperty<>(false);
    }
    
    // Getters for position
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    // State properties with getters
    
    public ObservableProperty<TileState> tileStateProperty() {
        return tileState;
    }
    
    public TileState getTileState() {
        return tileState.getValue();
    }
    
    public void setTileState(TileState state) {
        tileState.setValue(state);
        updateFlaggedAndRevealed();
    }
    
    public ObservableProperty<Integer> adjacentMineCountProperty() {
        return adjacentMineCount;
    }
    
    public Integer getAdjacentMineCount() {
        return adjacentMineCount.getValue();
    }
    
    public void setAdjacentMineCount(int count) {
        if (count >= 0 && count <= 8) {
            adjacentMineCount.setValue(count);
        }
    }
    
    public ObservableProperty<Boolean> isFlaggedProperty() {
        return isFlagged;
    }
    
    public Boolean isFlagged() {
        return isFlagged.getValue();
    }
    
    public void setFlagged(boolean flagged) {
        isFlagged.setValue(flagged);
        if (flagged) {
            tileState.setValue(TileState.FLAGGED);
        } else if (tileState.getValue() == TileState.FLAGGED) {
            tileState.setValue(TileState.UNREVEALED);
        }
    }
    
    public ObservableProperty<Boolean> isRevealedProperty() {
        return isRevealed;
    }
    
    public Boolean isRevealed() {
        return isRevealed.getValue();
    }
    
    public void setRevealed(int adjacentMines) {
        isRevealed.setValue(true);
        setAdjacentMineCount(adjacentMines);
        tileState.setValue(TileState.REVEALED);
    }
    
    public void setMine() {
        isRevealed.setValue(true);
        tileState.setValue(TileState.MINE);
    }
    
    /**
     * Update isFlagged and isRevealed based on current tileState.
     * This keeps the boolean properties in sync with the state enum.
     */
    private void updateFlaggedAndRevealed() {
        switch (tileState.getValue()) {
            case UNREVEALED:
                isFlagged.setValue(false);
                isRevealed.setValue(false);
                break;
            case REVEALED:
                isFlagged.setValue(false);
                isRevealed.setValue(true);
                break;
            case FLAGGED:
                isFlagged.setValue(true);
                isRevealed.setValue(false);
                break;
            case MINE:
                isFlagged.setValue(false);
                isRevealed.setValue(true);
                break;
        }
    }
    
    @Override
    public String toString() {
        return String.format("TileVM[%d,%d]:%s(%d)", row, col, tileState.getValue(), adjacentMineCount.getValue());
    }
}
