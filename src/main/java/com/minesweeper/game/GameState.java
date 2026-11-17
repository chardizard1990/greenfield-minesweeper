package com.minesweeper.game;

/**
 * Represents the current state of a Minesweeper game.
 */
public enum GameState {
    /**
     * Game is actively being played. Players can reveal and flag tiles.
     */
    ACTIVE,

    /**
     * Game is won. All non-mine tiles have been revealed.
     */
    WON,

    /**
     * Game is lost. A mine has been revealed.
     */
    LOST
}
