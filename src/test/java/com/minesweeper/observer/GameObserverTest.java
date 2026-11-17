package com.minesweeper.observer;

import com.minesweeper.game.GameState;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the observer pattern implementation.
 */
public class GameObserverTest {

    private GameEventBroadcaster broadcaster;
    private MockGameObserver observer1;
    private MockGameObserver observer2;

    @BeforeEach
    public void setup() {
        broadcaster = new GameEventBroadcaster();
        observer1 = new MockGameObserver();
        observer2 = new MockGameObserver();
    }

    /**
     * Test that observers can be subscribed.
     */
    @Test
    public void testSubscribeObserver() {
        broadcaster.subscribe(observer1);
        assertEquals(1, broadcaster.getObserverCount());
    }

    /**
     * Test that null observers are rejected.
     */
    @Test
    public void testSubscribeNullObserverFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            broadcaster.subscribe(null);
        });
    }

    /**
     * Test that duplicate subscriptions are ignored.
     */
    @Test
    public void testDuplicateSubscriptionIgnored() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer1);
        assertEquals(1, broadcaster.getObserverCount());
    }

    /**
     * Test that observers can be unsubscribed.
     */
    @Test
    public void testUnsubscribeObserver() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer2);
        assertEquals(2, broadcaster.getObserverCount());

        broadcaster.unsubscribe(observer1);
        assertEquals(1, broadcaster.getObserverCount());
    }

    /**
     * Test that all observers can be cleared.
     */
    @Test
    public void testClearAllObservers() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer2);
        assertEquals(2, broadcaster.getObserverCount());

        broadcaster.clearObservers();
        assertEquals(0, broadcaster.getObserverCount());
    }

    /**
     * Test tile reveal events are broadcast to all observers.
     */
    @Test
    public void testBroadcastTileRevealed() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer2);

        broadcaster.broadcastTileRevealed(5, 5, 3, false);

        assertTrue(observer1.hasEvent("TILE_REVEALED"));
        assertTrue(observer2.hasEvent("TILE_REVEALED"));
        assertEquals(1, observer1.countEventType("TILE_REVEALED"));
        assertEquals(1, observer2.countEventType("TILE_REVEALED"));
    }

    /**
     * Test tile flag events are broadcast to all observers.
     */
    @Test
    public void testBroadcastTileFlagged() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer2);

        broadcaster.broadcastTileFlagged(3, 3, true);

        assertTrue(observer1.hasEvent("TILE_FLAGGED"));
        assertTrue(observer2.hasEvent("TILE_FLAGGED"));
    }

    /**
     * Test game state change events are broadcast.
     */
    @Test
    public void testBroadcastGameStateChanged() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer2);

        broadcaster.broadcastGameStateChanged(GameState.LOST);

        assertTrue(observer1.hasEvent("STATE_CHANGED:LOST"));
        assertTrue(observer2.hasEvent("STATE_CHANGED:LOST"));
    }

    /**
     * Test player move events are broadcast with reasoning.
     */
    @Test
    public void testBroadcastPlayerMove() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer2);

        broadcaster.broadcastPlayerMove("HUMAN", 2, 4, "REVEAL", "Corner strategy");

        assertTrue(observer1.hasEvent("PLAYER_MOVE"));
        assertTrue(observer2.hasEvent("PLAYER_MOVE"));
        assertTrue(observer1.hasEvent("HUMAN"));
    }

    /**
     * Test game end events are broadcast.
     */
    @Test
    public void testBroadcastGameEnded() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer2);

        broadcaster.broadcastGameEnded("HUMAN", "Won by revealing all safe tiles");

        assertTrue(observer1.hasEvent("GAME_ENDED"));
        assertTrue(observer2.hasEvent("GAME_ENDED"));
    }

    /**
     * Test AI analysis events are broadcast.
     */
    @Test
    public void testBroadcastAIAnalysis() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer2);

        broadcaster.broadcastAIAnalysis("Finding definite mines");

        assertTrue(observer1.hasEvent("AI_ANALYSIS"));
        assertTrue(observer2.hasEvent("AI_ANALYSIS"));
    }

    /**
     * Test that unsubscribed observers don't receive events.
     */
    @Test
    public void testUnsubscribedObserversNotified() {
        broadcaster.subscribe(observer1);
        broadcaster.subscribe(observer2);

        broadcaster.broadcastTileRevealed(1, 1, 0, false);
        assertEquals(1, observer1.countEventType("TILE_REVEALED"));

        broadcaster.unsubscribe(observer1);

        broadcaster.broadcastTileRevealed(2, 2, 1, false);
        assertEquals(1, observer1.countEventType("TILE_REVEALED")); // Still 1
        assertEquals(2, observer2.countEventType("TILE_REVEALED")); // Now 2
    }

    /**
     * Test multiple events in sequence.
     */
    @Test
    public void testMultipleEventsSequence() {
        broadcaster.subscribe(observer1);

        broadcaster.broadcastPlayerMove("AI", 0, 0, "REVEAL", "Opening move");
        broadcaster.broadcastTileRevealed(0, 0, 0, false);
        broadcaster.broadcastPlayerMove("AI", 1, 1, "REVEAL", "Cascading");
        broadcaster.broadcastGameStateChanged(GameState.ACTIVE);

        assertEquals(2, observer1.countEventType("PLAYER_MOVE"));
        assertEquals(1, observer1.countEventType("TILE_REVEALED"));
        assertEquals(1, observer1.countEventType("STATE_CHANGED"));
    }

    /**
     * Test ConsoleGameSpectator records moves.
     */
    @Test
    public void testConsoleSpectatorRecordsMoves() {
        ConsoleGameSpectator spectator = new ConsoleGameSpectator("test-spectator", false);
        broadcaster.subscribe(spectator);

        broadcaster.broadcastPlayerMove("HUMAN", 5, 5, "REVEAL", "Strategic");
        broadcaster.broadcastPlayerMove("AI", 3, 3, "FLAG", "Definite mine");

        assertEquals(2, spectator.getMoveCount());
        assertEquals(2, spectator.getMoveHistory().size());
    }

    /**
     * Test ConsoleGameSpectator move history.
     */
    @Test
    public void testConsoleSpectatorMoveHistory() {
        ConsoleGameSpectator spectator = new ConsoleGameSpectator("test-spectator", false);
        broadcaster.subscribe(spectator);

        broadcaster.broadcastPlayerMove("HUMAN", 0, 0, "REVEAL", null);
        broadcaster.broadcastPlayerMove("AI", 2, 2, "FLAG", "High confidence");

        var history = spectator.getMoveHistory();
        assertEquals(2, history.size());
        assertEquals("HUMAN", history.get(0).playerId);
        assertEquals("AI", history.get(1).playerId);
        assertEquals(0, history.get(0).row);
        assertEquals(2, history.get(1).col);
    }

    /**
     * Test ConsoleGameSpectator clears history.
     */
    @Test
    public void testConsoleSpectatorClearHistory() {
        ConsoleGameSpectator spectator = new ConsoleGameSpectator("test-spectator", false);
        broadcaster.subscribe(spectator);

        broadcaster.broadcastPlayerMove("HUMAN", 0, 0, "REVEAL", null);
        assertEquals(1, spectator.getMoveCount());

        spectator.clearHistory();
        assertEquals(0, spectator.getMoveCount());
    }

    /**
     * Test ConsoleGameSpectator verbosity control.
     */
    @Test
    public void testConsoleSpectatorVerbosity() {
        ConsoleGameSpectator quietSpectator = new ConsoleGameSpectator("quiet", false);
        assertNotNull(quietSpectator);
        
        quietSpectator.setVerbose(true);
        quietSpectator.setVerbose(false);
        // Just verify no exceptions
    }

    /**
     * Test that mine reveals are recorded.
     */
    @Test
    public void testMineRevealRecorded() {
        broadcaster.subscribe(observer1);

        broadcaster.broadcastTileRevealed(5, 5, 0, true);

        assertTrue(observer1.hasEvent("TILE_REVEALED"));
        assertTrue(observer1.hasEvent("true")); // isMine = true
    }

    /**
     * Test broadcaster with no observers doesn't crash.
     */
    @Test
    public void testBroadcastWithNoObservers() {
        // Should not throw
        broadcaster.broadcastPlayerMove("TEST", 0, 0, "REVEAL", null);
        broadcaster.broadcastGameStateChanged(GameState.ACTIVE);
        broadcaster.broadcastGameEnded(null, "Test reason");
    }

}
