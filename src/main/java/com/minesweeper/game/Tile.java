package com.minesweeper.game;

/**
 * Represents a single cell/tile on the Minesweeper board.
 * 
 * Each tile can be in one of three states:
 * - UNREVEALED: Hidden from the player, may contain a mine
 * - REVEALED: Exposed to the player, shows either empty or bomb count
 * - FLAGGED: Marked by player as suspected mine (unrevealed but flagged)
 */
public class Tile {

    /**
     * The state of a tile on the board.
     */
    public enum State {
        UNREVEALED,  // Hidden from player
        REVEALED,    // Exposed to player
        FLAGGED      // Marked by player as suspected mine
    }

    private final int row;
    private final int column;
    private final boolean isMine;
    private State state;

    /**
     * Creates a new tile at the given coordinates.
     * Tiles are initially in UNREVEALED state.
     *
     * @param row the row index of this tile
     * @param column the column index of this tile
     * @param isMine true if this tile contains a mine, false otherwise
     */
    public Tile(int row, int column, boolean isMine) {
        this.row = row;
        this.column = column;
        this.isMine = isMine;
        this.state = State.UNREVEALED;
    }

    // Accessors
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isMine() {
        return isMine;
    }

    public State getState() {
        return state;
    }

    /**
     * Reveals this tile to the player.
     * Once revealed, the tile cannot be un-revealed.
     */
    public void reveal() {
        this.state = State.REVEALED;
    }

    /**
     * Toggles the flagged state of this tile.
     * Can only flag/unflag unrevealed tiles.
     *
     * @return true if the tile is now flagged, false if unflagged
     * @throws IllegalStateException if tile is already revealed
     */
    public boolean toggleFlag() {
        if (state == State.REVEALED) {
            throw new IllegalStateException("Cannot flag a revealed tile");
        }

        if (state == State.FLAGGED) {
            state = State.UNREVEALED;
            return false; // No longer flagged
        } else {
            state = State.FLAGGED;
            return true; // Now flagged
        }
    }

    /**
     * Checks if this tile is in the unrevealed state.
     * @return true if unrevealed, false otherwise
     */
    public boolean isUnrevealed() {
        return state == State.UNREVEALED;
    }

    /**
     * Checks if this tile is in the revealed state.
     * @return true if revealed, false otherwise
     */
    public boolean isRevealed() {
        return state == State.REVEALED;
    }

    /**
     * Checks if this tile is flagged by the player.
     * @return true if flagged, false otherwise
     */
    public boolean isFlagged() {
        return state == State.FLAGGED;
    }

    @Override
    public String toString() {
        return String.format("Tile(%d,%d)[%s,%s]",
                row, column,
                isMine ? "MINE" : "SAFE",
                state);
    }
}
