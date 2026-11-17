package com.minesweeper.observer;

import com.minesweeper.game.GameState;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages multiple observers for a game.
 * 
 * This class follows the Observer pattern, allowing multiple listeners
 * to subscribe to game events without coupling to the game logic.
 * 
 * Features:
 * - Add/remove observers
 * - Broadcast events to all observers
 * - Automatic cleanup on game end
 */
public class GameEventBroadcaster {

    private final List<GameObserver> observers = new ArrayList<>();

    /**
     * Subscribes an observer to game events.
     * 
     * @param observer the observer to add
     * @throws IllegalArgumentException if observer is null
     */
    public void subscribe(GameObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Unsubscribes an observer from game events.
     * 
     * @param observer the observer to remove
     */
    public void unsubscribe(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Clears all observers.
     */
    public void clearObservers() {
        observers.clear();
    }

    /**
     * Gets the number of active observers.
     * 
     * @return count of subscribed observers
     */
    public int getObserverCount() {
        return observers.size();
    }

    /**
     * Broadcasts a tile reveal event to all observers.
     * 
     * @param row row of revealed tile
     * @param col column of revealed tile
     * @param adjacentMines adjacent mine count
     * @param isMine whether tile is a mine
     */
    public void broadcastTileRevealed(int row, int col, int adjacentMines, boolean isMine) {
        for (GameObserver observer : observers) {
            observer.onTileRevealed(row, col, adjacentMines, isMine);
        }
    }

    /**
     * Broadcasts a tile flag event to all observers.
     * 
     * @param row row of flagged tile
     * @param col column of flagged tile
     * @param flagged true if flagged, false if unflagged
     */
    public void broadcastTileFlagged(int row, int col, boolean flagged) {
        for (GameObserver observer : observers) {
            observer.onTileFlagged(row, col, flagged);
        }
    }

    /**
     * Broadcasts a game state change event to all observers.
     * 
     * @param newState the new game state
     */
    public void broadcastGameStateChanged(GameState newState) {
        for (GameObserver observer : observers) {
            observer.onGameStateChanged(newState);
        }
    }

    /**
     * Broadcasts a player move event to all observers.
     * 
     * @param playerId identifier of player making move
     * @param row row coordinate
     * @param col column coordinate
     * @param actionType action type (REVEAL or FLAG)
     * @param reasoning optional reasoning for the move
     */
    public void broadcastPlayerMove(String playerId, int row, int col, String actionType, String reasoning) {
        for (GameObserver observer : observers) {
            observer.onPlayerMove(playerId, row, col, actionType, reasoning);
        }
    }

    /**
     * Broadcasts a game end event to all observers.
     * 
     * @param winner identifier of winner or null
     * @param reason reason for game end
     */
    public void broadcastGameEnded(String winner, String reason) {
        for (GameObserver observer : observers) {
            observer.onGameEnded(winner, reason);
        }
    }

    /**
     * Broadcasts an AI analysis event to all observers.
     * 
     * @param analysisStep description of analysis step
     */
    public void broadcastAIAnalysis(String analysisStep) {
        for (GameObserver observer : observers) {
            observer.onAIAnalysis(analysisStep);
        }
    }

}
