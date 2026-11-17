package com.minesweeper.observer;

import com.minesweeper.game.GameState;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock observer for testing game events.
 * 
 * Records all events for verification in tests.
 */
public class MockGameObserver implements GameObserver {

    private final List<String> events = new ArrayList<>();

    @Override
    public void onTileRevealed(int row, int col, int adjacentMines, boolean isMine) {
        events.add("TILE_REVEALED:" + row + "," + col + "," + adjacentMines + "," + isMine);
    }

    @Override
    public void onTileFlagged(int row, int col, boolean flagged) {
        events.add("TILE_FLAGGED:" + row + "," + col + "," + flagged);
    }

    @Override
    public void onGameStateChanged(GameState newState) {
        events.add("STATE_CHANGED:" + newState);
    }

    @Override
    public void onPlayerMove(String playerId, int row, int col, String actionType, String reasoning) {
        events.add("PLAYER_MOVE:" + playerId + ":" + row + "," + col + ":" + actionType);
    }

    @Override
    public void onGameEnded(String winner, String reason) {
        events.add("GAME_ENDED:" + winner + ":" + reason);
    }

    @Override
    public void onAIAnalysis(String analysisStep) {
        events.add("AI_ANALYSIS:" + analysisStep);
    }

    /**
     * Gets all recorded events.
     * 
     * @return list of event strings
     */
    public List<String> getEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Checks if an event was recorded.
     * 
     * @param eventPattern partial event string to match
     * @return true if event was recorded
     */
    public boolean hasEvent(String eventPattern) {
        return events.stream().anyMatch(e -> e.contains(eventPattern));
    }

    /**
     * Gets the count of a specific event type.
     * 
     * @param eventType event type prefix to count
     * @return count of events matching type
     */
    public int countEventType(String eventType) {
        return (int) events.stream()
            .filter(e -> e.startsWith(eventType))
            .count();
    }

    /**
     * Clears all recorded events.
     */
    public void clear() {
        events.clear();
    }

}
