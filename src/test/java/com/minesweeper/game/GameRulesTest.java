package com.minesweeper.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GameRules class.
 * Tests game state management, reveal logic, cascading reveals, flagging, and win/loss conditions.
 */
@DisplayName("GameRules Tests")
public class GameRulesTest {

    private GameRules gameRules;
    private Board board;

    @BeforeEach
    public void setUp() {
        // Create a small 5x5 board with 3 mines for testing
        // Seeded for reproducible mine placement
        Random random = new Random(42);
        board = new Board(5, 5, 3, random);
        gameRules = new GameRules(board);
    }

    @Test
    @DisplayName("Should initialize game in ACTIVE state")
    public void testGameInitialization() {
        assertEquals(GameState.ACTIVE, gameRules.getGameState());
        assertEquals(3, gameRules.getRemainingMines());
        assertEquals(0, gameRules.getFlaggedMineCount());
    }

    @Test
    @DisplayName("Should throw exception if board is null")
    public void testNullBoardThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new GameRules(null));
    }

    @Test
    @DisplayName("Should reveal safe tile successfully")
    public void testRevealSafeTile() {
        // Find a safe tile (not a mine)
        int safeRow = -1, safeCol = -1;
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.getTile(row, col).isMine()) {
                    safeRow = row;
                    safeCol = col;
                    break;
                }
            }
            if (safeRow >= 0) break;
        }

        RevealResult result = gameRules.revealTile(safeRow, safeCol);
        
        assertTrue(result.isSuccess());
        assertFalse(result.isHitMine());
        assertEquals(GameState.ACTIVE, result.getNewGameState());
        assertTrue(board.getTile(safeRow, safeCol).isRevealed());
    }

    @Test
    @DisplayName("Should hit mine and transition to LOST state")
    public void testHitMine() {
        // Find a mine
        int mineRow = -1, mineCol = -1;
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (board.getTile(row, col).isMine()) {
                    mineRow = row;
                    mineCol = col;
                    break;
                }
            }
            if (mineRow >= 0) break;
        }

        RevealResult result = gameRules.revealTile(mineRow, mineCol);

        assertTrue(result.isHitMine());
        assertEquals(RevealResult.Outcome.HIT_MINE, result.getOutcome());
        assertEquals(GameState.LOST, result.getNewGameState());
        assertEquals(GameState.LOST, gameRules.getGameState());
        assertTrue(board.getTile(mineRow, mineCol).isRevealed());
    }

    @Test
    @DisplayName("Should prevent revealing already revealed tile")
    public void testRevealAlreadyRevealedTile() {
        // Find and reveal a safe tile
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.getTile(row, col).isMine()) {
                    // First reveal
                    RevealResult result1 = gameRules.revealTile(row, col);
                    assertTrue(result1.isSuccess());

                    // Try to reveal same tile again
                    RevealResult result2 = gameRules.revealTile(row, col);
                    assertEquals(RevealResult.Outcome.ALREADY_REVEALED, result2.getOutcome());
                    return;
                }
            }
        }
    }

    @Test
    @DisplayName("Should prevent revealing flagged tile")
    public void testPreventRevealingFlaggedTile() {
        // Find a safe tile
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.getTile(row, col).isMine()) {
                    // Flag it
                    FlagResult flagResult = gameRules.toggleFlag(row, col);
                    assertTrue(flagResult.isSuccess());
                    assertTrue(flagResult.isFlaggedNow());

                    // Try to reveal it
                    RevealResult revealResult = gameRules.revealTile(row, col);
                    assertEquals(RevealResult.Outcome.TILE_FLAGGED, revealResult.getOutcome());
                    return;
                }
            }
        }
    }

    @Test
    @DisplayName("Should handle cascading reveal for 0-mine tiles")
    public void testCascadingReveal() {
        // Create a board with minimal mines for predictable cascading
        Board emptyBoard = new Board(5, 5, 1, new Random(42));
        GameRules rules = new GameRules(emptyBoard);

        // Try to find a tile with 0 adjacent mines and reveal it
        for (int row = 0; row < emptyBoard.getHeight(); row++) {
            for (int col = 0; col < emptyBoard.getWidth(); col++) {
                if (!emptyBoard.getTile(row, col).isMine()) {
                    int adjacentMines = emptyBoard.getAdjacentMineCount(row, col);
                    if (adjacentMines == 0) {
                        // This tile should trigger cascading reveal
                        RevealResult result = rules.revealTile(row, col);
                        assertTrue(result.isSuccess());

                        // Multiple tiles should now be revealed (cascade effect)
                        int revealedCount = 0;
                        for (int r = 0; r < emptyBoard.getHeight(); r++) {
                            for (int c = 0; c < emptyBoard.getWidth(); c++) {
                                if (emptyBoard.getTile(r, c).isRevealed()) {
                                    revealedCount++;
                                }
                            }
                        }
                        // Should reveal more than just the one tile
                        assertTrue(revealedCount > 1, "Cascading reveal should reveal multiple tiles");
                        return;
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Should flag and unflag tiles correctly")
    public void testToggleFlag() {
        // Find a safe tile
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.getTile(row, col).isMine()) {
                    // Flag it
                    FlagResult flagResult = gameRules.toggleFlag(row, col);
                    assertTrue(flagResult.isSuccess());
                    assertTrue(flagResult.isFlaggedNow());
                    assertEquals(2, gameRules.getRemainingMines()); // 3 - 1 flagged

                    // Unflag it
                    FlagResult unflagResult = gameRules.toggleFlag(row, col);
                    assertTrue(unflagResult.isSuccess());
                    assertFalse(unflagResult.isFlaggedNow());
                    assertEquals(3, gameRules.getRemainingMines()); // Back to 3

                    return;
                }
            }
        }
    }

    @Test
    @DisplayName("Should prevent flagging revealed tiles")
    public void testPreventFlaggingRevealedTile() {
        // Find a safe tile and reveal it
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.getTile(row, col).isMine()) {
                    // Reveal it
                    gameRules.revealTile(row, col);

                    // Try to flag it
                    FlagResult result = gameRules.toggleFlag(row, col);
                    assertEquals(FlagResult.Outcome.TILE_REVEALED, result.getOutcome());
                    return;
                }
            }
        }
    }

    @Test
    @DisplayName("Should prevent actions when game is not active")
    public void testPreventActionsWhenGameNotActive() {
        // Find a mine and hit it to end game
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (board.getTile(row, col).isMine()) {
                    gameRules.revealTile(row, col);
                    assertEquals(GameState.LOST, gameRules.getGameState());

                    // Try to reveal another tile
                    RevealResult revealResult = gameRules.revealTile((row + 1) % board.getHeight(), col);
                    assertEquals(RevealResult.Outcome.GAME_NOT_ACTIVE, revealResult.getOutcome());

                    // Try to flag a tile
                    FlagResult flagResult = gameRules.toggleFlag((row + 2) % board.getHeight(), col);
                    assertEquals(FlagResult.Outcome.GAME_NOT_ACTIVE, flagResult.getOutcome());

                    return;
                }
            }
        }
    }

    @Test
    @DisplayName("Should handle invalid coordinates")
    public void testInvalidCoordinates() {
        RevealResult result = gameRules.revealTile(-1, 0);
        assertEquals(RevealResult.Outcome.INVALID_COORDINATES, result.getOutcome());

        result = gameRules.revealTile(5, 5);
        assertEquals(RevealResult.Outcome.INVALID_COORDINATES, result.getOutcome());

        FlagResult flagResult = gameRules.toggleFlag(10, 10);
        assertEquals(FlagResult.Outcome.INVALID_COORDINATES, flagResult.getOutcome());
    }

    @Test
    @DisplayName("Should correctly identify safe tiles")
    public void testIsSafeMethod() {
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                boolean isSafe = gameRules.isSafe(row, col);
                boolean isMine = board.getTile(row, col).isMine();
                assertNotEquals(isSafe, isMine, "isSafe should be opposite of isMine");
            }
        }

        // Test out of bounds
        assertFalse(gameRules.isSafe(-1, 0));
        assertFalse(gameRules.isSafe(5, 5));
    }

    @Test
    @DisplayName("Should reveal all tiles when game is lost")
    public void testRevealAllTiles() {
        gameRules.revealAllTiles();

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                assertTrue(board.getTile(row, col).isRevealed());
            }
        }
    }

    @Test
    @DisplayName("Should track remaining mines when flagging")
    public void testRemainingMinesTracking() {
        assertEquals(3, gameRules.getRemainingMines());

        // Flag 2 safe tiles
        int flaggedCount = 0;
        for (int row = 0; row < board.getHeight() && flaggedCount < 2; row++) {
            for (int col = 0; col < board.getWidth() && flaggedCount < 2; col++) {
                if (!board.getTile(row, col).isMine()) {
                    gameRules.toggleFlag(row, col);
                    flaggedCount++;
                    assertEquals(3 - flaggedCount, gameRules.getRemainingMines());
                }
            }
        }
    }

    @Test
    @DisplayName("Should correctly identify when game is won")
    public void testWinCondition() {
        // Create a 2x2 board with 1 mine - easier to win
        Board smallBoard = new Board(2, 2, 1, new Random(42));
        GameRules rules = new GameRules(smallBoard);

        // Reveal all non-mine tiles
        boolean gameWon = false;
        for (int row = 0; row < smallBoard.getHeight(); row++) {
            for (int col = 0; col < smallBoard.getWidth(); col++) {
                if (!smallBoard.getTile(row, col).isMine()) {
                    RevealResult result = rules.revealTile(row, col);
                    if (result.getNewGameState() == GameState.WON) {
                        gameWon = true;
                        break;
                    }
                }
            }
            if (gameWon) break;
        }

        // We should have won at some point
        assertTrue(gameWon || rules.getGameState() == GameState.WON,
                "Should win when all non-mine tiles are revealed");
    }

    @Test
    @DisplayName("Should handle game state transitions correctly")
    public void testGameStateTransitions() {
        // Start: ACTIVE
        assertEquals(GameState.ACTIVE, gameRules.getGameState());

        // Can stay ACTIVE through safe reveals
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.getTile(row, col).isMine() && 
                    board.getAdjacentMineCount(row, col) > 0) {
                    // This tile is safe but has adjacent mines - good for testing
                    RevealResult result = gameRules.revealTile(row, col);
                    if (result.isSuccess()) {
                        assertEquals(GameState.ACTIVE, gameRules.getGameState());
                    }
                    return;
                }
            }
        }
    }

    @Test
    @DisplayName("Should provide meaningful messages for all outcomes")
    public void testOutcomeMessages() {
        // Find safe tile and get success message
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.getTile(row, col).isMine()) {
                    RevealResult result = gameRules.revealTile(row, col);
                    assertNotNull(result.getMessage());
                    assertTrue(result.getMessage().length() > 0);

                    FlagResult flagResult = gameRules.toggleFlag(row + 1, col);
                    assertNotNull(flagResult.getMessage());
                    assertTrue(flagResult.getMessage().length() > 0);
                    return;
                }
            }
        }
    }
}
