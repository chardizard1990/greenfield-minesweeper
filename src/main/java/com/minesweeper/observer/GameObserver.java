package com.minesweeper.observer;

import com.minesweeper.game.GameState;

/**
 * Interface for observing game events.
 * 
 * Implementations can listen to and react to:
 * - Game state changes
 * - Tile reveals with mine counts
 * - Flagging actions
 * - Wins/losses
 * - AI decision-making
 * 
 * This enables spectator modes, logging, and replay functionality.
 */
public interface GameObserver {

    /**
     * Called when a tile is revealed.
     * 
     * @param row row of the revealed tile
     * @param col column of the revealed tile
     * @param adjacentMines number of adjacent mines (0-8)
     * @param isMine whether the tile is a mine
     */
    void onTileRevealed(int row, int col, int adjacentMines, boolean isMine);

    /**
     * Called when a tile is flagged.
     * 
     * @param row row of the flagged tile
     * @param col column of the flagged tile
     * @param flagged true if flagged, false if unflagged
     */
    void onTileFlagged(int row, int col, boolean flagged);

    /**
     * Called when game state changes.
     * 
     * @param newState the new game state (ACTIVE, WON, LOST)
     */
    void onGameStateChanged(GameState newState);

    /**
     * Called when a player makes a move (human or AI).
     * 
     * @param playerId identifier of the player making the move
     * @param row row coordinate
     * @param col column coordinate
     * @param actionType "REVEAL" or "FLAG"
     * @param reasoning optional explanation of why this move was made
     */
    void onPlayerMove(String playerId, int row, int col, String actionType, String reasoning);

    /**
     * Called when game ends.
     * 
     * @param winner identifier of winning player (or null for loss)
     * @param reason description of game end (e.g., "Hit mine at (5,5)")
     */
    void onGameEnded(String winner, String reason);

    /**
     * Called periodically during AI analysis.
     * Useful for showing "AI is thinking..." messages.
     * 
     * @param analysisStep description of what AI is analyzing
     */
    void onAIAnalysis(String analysisStep);

}
