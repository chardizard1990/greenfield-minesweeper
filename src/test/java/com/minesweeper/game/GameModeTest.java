package com.minesweeper.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameMode enum and difficulty configurations.
 */
@DisplayName("GameMode Tests")
public class GameModeTest {

    @Test
    @DisplayName("Should have EASY mode with 10x10 and 10 mines")
    public void testEasyMode() {
        GameMode easy = GameMode.EASY;
        assertEquals("Easy", easy.getDisplayName());
        assertEquals(10, easy.getWidth());
        assertEquals(10, easy.getHeight());
        assertEquals(10, easy.getMineCount());
        assertEquals(100, easy.getTotalTiles());
    }

    @Test
    @DisplayName("Should have MEDIUM mode with 20x20 and 75 mines")
    public void testMediumMode() {
        GameMode medium = GameMode.MEDIUM;
        assertEquals("Medium", medium.getDisplayName());
        assertEquals(20, medium.getWidth());
        assertEquals(20, medium.getHeight());
        assertEquals(75, medium.getMineCount());
        assertEquals(400, medium.getTotalTiles());
    }

    @Test
    @DisplayName("Should have HARD mode with 30x30 and 150 mines")
    public void testHardMode() {
        GameMode hard = GameMode.HARD;
        assertEquals("Hard", hard.getDisplayName());
        assertEquals(30, hard.getWidth());
        assertEquals(30, hard.getHeight());
        assertEquals(150, hard.getMineCount());
        assertEquals(900, hard.getTotalTiles());
    }

    @Test
    @DisplayName("Should calculate mine density correctly")
    public void testMineDensity() {
        GameMode easy = GameMode.EASY;
        double easyDensity = easy.getMineDensity();
        assertEquals(10.0, easyDensity, 0.1); // 10/100 = 10%

        GameMode medium = GameMode.MEDIUM;
        double mediumDensity = medium.getMineDensity();
        assertEquals(18.75, mediumDensity, 0.1); // 75/400 = 18.75%

        GameMode hard = GameMode.HARD;
        double hardDensity = hard.getMineDensity();
        assertEquals(16.67, hardDensity, 0.1); // 150/900 = 16.67%
    }

    @Test
    @DisplayName("Should provide meaningful descriptions")
    public void testModeDescriptions() {
        for (GameMode mode : GameMode.values()) {
            assertNotNull(mode.getDescription());
            assertTrue(mode.getDescription().length() > 0);
        }
    }

    @Test
    @DisplayName("Should format toString correctly")
    public void testToString() {
        GameMode easy = GameMode.EASY;
        String str = easy.toString();
        assertTrue(str.contains("Easy"));
        assertTrue(str.contains("10x10"));
        assertTrue(str.contains("10 mines"));
    }
}
