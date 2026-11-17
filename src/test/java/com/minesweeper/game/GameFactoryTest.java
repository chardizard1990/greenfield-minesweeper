package com.minesweeper.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameFactory class.
 * Tests game creation from preset modes and custom configurations.
 */
@DisplayName("GameFactory Tests")
public class GameFactoryTest {

    @Test
    @DisplayName("Should create EASY mode game")
    public void testCreateEasyGame() {
        GameRules game = GameFactory.createGame(GameMode.EASY);

        assertNotNull(game);
        assertEquals(GameState.ACTIVE, game.getGameState());
        assertEquals(10, game.getBoard().getWidth());
        assertEquals(10, game.getBoard().getHeight());
        assertEquals(10, game.getBoard().getMineCount());
        assertEquals(10, game.getRemainingMines());
    }

    @Test
    @DisplayName("Should create MEDIUM mode game")
    public void testCreateMediumGame() {
        GameRules game = GameFactory.createGame(GameMode.MEDIUM);

        assertNotNull(game);
        assertEquals(GameState.ACTIVE, game.getGameState());
        assertEquals(20, game.getBoard().getWidth());
        assertEquals(20, game.getBoard().getHeight());
        assertEquals(75, game.getBoard().getMineCount());
        assertEquals(75, game.getRemainingMines());
    }

    @Test
    @DisplayName("Should create HARD mode game")
    public void testCreateHardGame() {
        GameRules game = GameFactory.createGame(GameMode.HARD);

        assertNotNull(game);
        assertEquals(GameState.ACTIVE, game.getGameState());
        assertEquals(30, game.getBoard().getWidth());
        assertEquals(30, game.getBoard().getHeight());
        assertEquals(150, game.getBoard().getMineCount());
        assertEquals(150, game.getRemainingMines());
    }

    @Test
    @DisplayName("Should throw exception for null GameMode")
    public void testNullGameModeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGame((GameMode) null));
    }

    @Test
    @DisplayName("Should create game from CustomGameMode")
    public void testCreateGameFromCustomMode() {
        CustomGameMode customMode = new CustomGameMode(15, 15, 30);
        GameRules game = GameFactory.createGame(customMode);

        assertNotNull(game);
        assertEquals(GameState.ACTIVE, game.getGameState());
        assertEquals(15, game.getBoard().getWidth());
        assertEquals(15, game.getBoard().getHeight());
        assertEquals(30, game.getBoard().getMineCount());
        assertEquals(30, game.getRemainingMines());
    }

    @Test
    @DisplayName("Should throw exception for null CustomGameMode")
    public void testNullCustomGameModeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createGame((CustomGameMode) null));
    }

    @Test
    @DisplayName("Should create custom game with dimensions and mine count")
    public void testCreateCustomGameWithDimensions() {
        GameRules game = GameFactory.createCustomGame(12, 12, 25);

        assertNotNull(game);
        assertEquals(GameState.ACTIVE, game.getGameState());
        assertEquals(12, game.getBoard().getWidth());
        assertEquals(12, game.getBoard().getHeight());
        assertEquals(25, game.getBoard().getMineCount());
    }

    @Test
    @DisplayName("Should create custom game with dimensions, mines, and name")
    public void testCreateCustomGameWithName() {
        GameRules game = GameFactory.createCustomGame(10, 10, 20, "My Custom Game");

        assertNotNull(game);
        assertEquals(GameState.ACTIVE, game.getGameState());
        assertEquals(10, game.getBoard().getWidth());
        assertEquals(10, game.getBoard().getHeight());
        assertEquals(20, game.getBoard().getMineCount());
    }

    @Test
    @DisplayName("Should validate inputs in custom game creation")
    public void testCustomGameValidation() {
        // Invalid dimensions should throw
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createCustomGame(0, 10, 5));
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createCustomGame(10, 51, 5));

        // Too many mines should throw
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createCustomGame(5, 5, 25));

        // Negative mines should throw
        assertThrows(IllegalArgumentException.class, () -> GameFactory.createCustomGame(10, 10, -1));
    }

    @Test
    @DisplayName("Should create games ready for play")
    public void testCreatedGamesPlayable() {
        GameRules easyGame = GameFactory.createGame(GameMode.EASY);
        GameRules hardGame = GameFactory.createGame(GameMode.HARD);

        // Games should be in ACTIVE state and playable
        assertEquals(GameState.ACTIVE, easyGame.getGameState());
        assertEquals(GameState.ACTIVE, hardGame.getGameState());

        // Should be able to access board
        assertNotNull(easyGame.getBoard());
        assertNotNull(hardGame.getBoard());

        // Should have valid tile counts
        assertTrue(easyGame.getBoard().getUnrevealedTileCount() > 0);
        assertTrue(hardGame.getBoard().getUnrevealedTileCount() > 0);
    }

    @Test
    @DisplayName("Should create independent game instances")
    public void testIndependentGameInstances() {
        GameRules game1 = GameFactory.createGame(GameMode.EASY);
        GameRules game2 = GameFactory.createGame(GameMode.EASY);

        // Different instances
        assertNotSame(game1, game2);
        assertNotSame(game1.getBoard(), game2.getBoard());

        // Changes to one don't affect the other
        int initialCount1 = game1.getBoard().getUnrevealedTileCount();
        int initialCount2 = game2.getBoard().getUnrevealedTileCount();

        // Both should have same unrevealed count initially
        assertEquals(initialCount1, initialCount2);
    }

    @Test
    @DisplayName("Should handle all preset modes")
    public void testAllPresetModes() {
        for (GameMode mode : GameMode.values()) {
            GameRules game = GameFactory.createGame(mode);
            assertNotNull(game);
            assertEquals(GameState.ACTIVE, game.getGameState());
            assertEquals(mode.getMineCount(), game.getRemainingMines());
            assertEquals(mode.getWidth(), game.getBoard().getWidth());
            assertEquals(mode.getHeight(), game.getBoard().getHeight());
        }
    }

    @Test
    @DisplayName("Should create games with correct mine counts")
    public void testCorrectMineCounts() {
        GameRules easyGame = GameFactory.createGame(GameMode.EASY);
        int easyMineCount = 0;
        for (int row = 0; row < easyGame.getBoard().getHeight(); row++) {
            for (int col = 0; col < easyGame.getBoard().getWidth(); col++) {
                if (easyGame.getBoard().getTile(row, col).isMine()) {
                    easyMineCount++;
                }
            }
        }
        assertEquals(10, easyMineCount, "EASY mode should have exactly 10 mines");

        GameRules hardGame = GameFactory.createGame(GameMode.HARD);
        int hardMineCount = 0;
        for (int row = 0; row < hardGame.getBoard().getHeight(); row++) {
            for (int col = 0; col < hardGame.getBoard().getWidth(); col++) {
                if (hardGame.getBoard().getTile(row, col).isMine()) {
                    hardMineCount++;
                }
            }
        }
        assertEquals(150, hardMineCount, "HARD mode should have exactly 150 mines");
    }
}
