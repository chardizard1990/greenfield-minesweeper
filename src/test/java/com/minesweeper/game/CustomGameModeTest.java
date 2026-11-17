package com.minesweeper.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CustomGameMode class.
 * Tests custom configuration creation, validation, and edge cases.
 */
@DisplayName("CustomGameMode Tests")
public class CustomGameModeTest {

    @Test
    @DisplayName("Should create valid custom mode")
    public void testValidCustomMode() {
        CustomGameMode mode = new CustomGameMode(15, 15, 30);
        assertEquals(15, mode.getWidth());
        assertEquals(15, mode.getHeight());
        assertEquals(30, mode.getMineCount());
        assertEquals(225, mode.getTotalTiles());
        assertEquals("Custom", mode.getName());
    }

    @Test
    @DisplayName("Should create custom mode with custom name")
    public void testCustomModeWithName() {
        CustomGameMode mode = new CustomGameMode(10, 10, 20, "My Game");
        assertEquals("My Game", mode.getName());
    }

    @Test
    @DisplayName("Should use default name for null or empty name")
    public void testDefaultNameHandling() {
        CustomGameMode mode1 = new CustomGameMode(10, 10, 20, null);
        assertEquals("Custom", mode1.getName());

        CustomGameMode mode2 = new CustomGameMode(10, 10, 20, "   ");
        assertEquals("Custom", mode2.getName());
    }

    @Test
    @DisplayName("Should reject width below minimum")
    public void testWidthBelowMinimum() {
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(0, 10, 5));
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(-5, 10, 5));
    }

    @Test
    @DisplayName("Should reject width above maximum")
    public void testWidthAboveMaximum() {
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(51, 10, 5));
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(100, 10, 5));
    }

    @Test
    @DisplayName("Should reject height below minimum")
    public void testHeightBelowMinimum() {
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(10, 0, 5));
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(10, -5, 5));
    }

    @Test
    @DisplayName("Should reject height above maximum")
    public void testHeightAboveMaximum() {
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(10, 51, 5));
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(10, 100, 5));
    }

    @Test
    @DisplayName("Should reject negative mine count")
    public void testNegativeMineCount() {
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(10, 10, -1));
    }

    @Test
    @DisplayName("Should reject mine count >= total tiles")
    public void testTooManyMines() {
        // 10x10 = 100 tiles, can have at most 99 mines
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(10, 10, 100));
        assertThrows(IllegalArgumentException.class, () -> new CustomGameMode(10, 10, 101));
    }

    @Test
    @DisplayName("Should accept mine count at maximum allowed")
    public void testMaximumAllowedMines() {
        // 10x10 = 100 tiles, 99 mines is maximum allowed
        CustomGameMode mode = new CustomGameMode(10, 10, 99);
        assertEquals(99, mode.getMineCount());
    }

    @Test
    @DisplayName("Should accept minimum dimensions")
    public void testMinimumDimensions() {
        // 1x1 board with 0 mines is valid
        CustomGameMode mode = new CustomGameMode(1, 1, 0);
        assertEquals(1, mode.getWidth());
        assertEquals(1, mode.getHeight());
        assertEquals(1, mode.getTotalTiles());
    }

    @Test
    @DisplayName("Should accept maximum dimensions")
    public void testMaximumDimensions() {
        // 50x50 board with many mines is valid
        CustomGameMode mode = new CustomGameMode(50, 50, 1000);
        assertEquals(50, mode.getWidth());
        assertEquals(50, mode.getHeight());
        assertEquals(2500, mode.getTotalTiles());
    }

    @Test
    @DisplayName("Should calculate mine density correctly")
    public void testMineDensity() {
        CustomGameMode mode = new CustomGameMode(20, 20, 80); // 80/400 = 20%
        double density = mode.getMineDensity();
        assertEquals(20.0, density, 0.1);
    }

    @Test
    @DisplayName("Should format toString correctly")
    public void testToString() {
        CustomGameMode mode = new CustomGameMode(15, 15, 50, "Test Mode");
        String str = mode.toString();
        assertTrue(str.contains("Test Mode"));
        assertTrue(str.contains("15x15"));
        assertTrue(str.contains("50 mines"));
    }

    @Test
    @DisplayName("Should implement equals correctly")
    public void testEquals() {
        CustomGameMode mode1 = new CustomGameMode(10, 10, 20);
        CustomGameMode mode2 = new CustomGameMode(10, 10, 20);
        CustomGameMode mode3 = new CustomGameMode(10, 10, 30);
        CustomGameMode mode4 = new CustomGameMode(15, 10, 20);

        // Same dimensions should be equal
        assertEquals(mode1, mode2);

        // Different mine count should not be equal
        assertNotEquals(mode1, mode3);

        // Different dimensions should not be equal
        assertNotEquals(mode1, mode4);

        // Should not equal null
        assertNotEquals(mode1, null);

        // Should not equal different type
        assertNotEquals(mode1, "10x10");
    }

    @Test
    @DisplayName("Should implement hashCode consistently")
    public void testHashCode() {
        CustomGameMode mode1 = new CustomGameMode(10, 10, 20);
        CustomGameMode mode2 = new CustomGameMode(10, 10, 20);

        // Equal objects should have equal hash codes
        assertEquals(mode1.hashCode(), mode2.hashCode());
    }

    @Test
    @DisplayName("Should work in hash-based collections")
    public void testHashBasedCollections() {
        CustomGameMode mode1 = new CustomGameMode(10, 10, 20);
        CustomGameMode mode2 = new CustomGameMode(10, 10, 20);

        // Both should be treated as the same key in a HashMap
        java.util.Map<CustomGameMode, String> map = new java.util.HashMap<>();
        map.put(mode1, "first");
        map.put(mode2, "second");

        // Should have only one entry (same hash and equals)
        assertEquals(1, map.size());
        assertEquals("second", map.get(mode1));
    }

    @Test
    @DisplayName("Should handle asymmetric mine/tile configurations")
    public void testAsymmetricDimensions() {
        CustomGameMode mode = new CustomGameMode(5, 20, 30); // 100 tiles, 30% density
        assertEquals(5, mode.getWidth());
        assertEquals(20, mode.getHeight());
        assertEquals(100, mode.getTotalTiles());
        assertEquals(30.0, mode.getMineDensity(), 0.1);
    }
}
