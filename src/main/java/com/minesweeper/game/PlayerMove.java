package com.minesweeper.game;

import com.minesweeper.ai.AIPlayer;

/**
 * Represents a move action that a player wants to make.
 * 
 * This class encapsulates the player's action before validation.
 */
public class PlayerMove {

    public enum ActionType {
        REVEAL,  // Reveal a tile
        FLAG     // Toggle flag on a tile
    }

    private final int row;
    private final int column;
    private final ActionType actionType;

    /**
     * Creates a player move.
     *
     * @param row the row coordinate
     * @param column the column coordinate
     * @param actionType the type of action (REVEAL or FLAG)
     * @throws IllegalArgumentException if actionType is null
     */
    public PlayerMove(int row, int column, ActionType actionType) {
        if (actionType == null) {
            throw new IllegalArgumentException("Action type cannot be null");
        }
        this.row = row;
        this.column = column;
        this.actionType = actionType;
    }

    // Accessors
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public String toString() {
        return String.format("PlayerMove(%d,%d)[%s]", row, column, actionType);
    }
}
