package com.minesweeper.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Tile class.
 * Tests tile state management, flagging, and revealing.
 */
@DisplayName("Tile Tests")
public class TileTest {

    private Tile safeTile;
    private Tile mineTile;

    @BeforeEach
    public void setUp() {
        safeTile = new Tile(0, 0, false);
        mineTile = new Tile(0, 1, true);
    }

    @Test
    @DisplayName("Should create tile with correct initial state")
    public void testTileInitialization() {
        assertEquals(0, safeTile.getRow());
        assertEquals(0, safeTile.getColumn());
        assertFalse(safeTile.isMine());
        assertEquals(Tile.State.UNREVEALED, safeTile.getState());
        assertTrue(safeTile.isUnrevealed());
    }

    @Test
    @DisplayName("Should correctly identify mine tiles")
    public void testMineTile() {
        assertTrue(mineTile.isMine());
        assertFalse(safeTile.isMine());
    }

    @Test
    @DisplayName("Should reveal tile when reveal() is called")
    public void testRevealTile() {
        safeTile.reveal();
        assertEquals(Tile.State.REVEALED, safeTile.getState());
        assertTrue(safeTile.isRevealed());
        assertFalse(safeTile.isUnrevealed());
    }

    @Test
    @DisplayName("Should toggle flag on unrevealed tile")
    public void testFlagUnrevealedTile() {
        // Flag the tile
        boolean isFlagged = safeTile.toggleFlag();
        assertTrue(isFlagged);
        assertEquals(Tile.State.FLAGGED, safeTile.getState());
        assertTrue(safeTile.isFlagged());

        // Unflag the tile
        isFlagged = safeTile.toggleFlag();
        assertFalse(isFlagged);
        assertEquals(Tile.State.UNREVEALED, safeTile.getState());
        assertFalse(safeTile.isFlagged());
    }

    @Test
    @DisplayName("Should throw exception when flagging revealed tile")
    public void testFlagRevealedTileThrowsException() {
        safeTile.reveal();
        assertThrows(IllegalStateException.class, safeTile::toggleFlag);
    }

    @Test
    @DisplayName("Should handle flagging mine tiles")
    public void testFlagMineTile() {
        mineTile.toggleFlag();
        assertTrue(mineTile.isFlagged());
        assertEquals(Tile.State.FLAGGED, mineTile.getState());
    }

    @Test
    @DisplayName("Should maintain correct state through multiple operations")
    public void testStateTransitions() {
        // Start unrevealed
        assertTrue(safeTile.isUnrevealed());

        // Flag it
        safeTile.toggleFlag();
        assertTrue(safeTile.isFlagged());
        assertFalse(safeTile.isUnrevealed());

        // Unflag it
        safeTile.toggleFlag();
        assertTrue(safeTile.isUnrevealed());
        assertFalse(safeTile.isFlagged());

        // Reveal it
        safeTile.reveal();
        assertTrue(safeTile.isRevealed());
        assertFalse(safeTile.isUnrevealed());
    }
}
