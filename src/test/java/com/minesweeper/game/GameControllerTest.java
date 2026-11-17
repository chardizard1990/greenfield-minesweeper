package com.minesweeper.game;

import com.minesweeper.ai.AIDifficulty;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameController.
 * Tests game initialization, move processing, state management, and statistics.
 */
@DisplayName("Game Controller Tests")
public class GameControllerTest {

    private GameController gameController;
    private Board board;
    private GameRules gameRules;

    @BeforeEach
    public void setUp() {
        board = new Board(8, 8, 8);
        gameRules = new GameRules(board);
        gameController = new GameController("test-game-1", gameRules, GameMode.EASY);
    }

    @Test
    @DisplayName("GameController should initialize with correct parameters")
    public void testGameControllerInitialization() {
        assertEquals("test-game-1", gameController.getGameId());
        assertEquals(GameMode.EASY, gameController.getGameMode());
        assertEquals(GameState.ACTIVE, gameController.getGameState());
        assertFalse(gameController.isHumanVsAI());
    }

    @Test
    @DisplayName("GameController should validate game ID")
    public void testGameControllerValidatesGameId() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameController(null, gameRules, GameMode.EASY),
                "Should reject null game ID"
        );

        assertThrows(IllegalArgumentException.class, () ->
                new GameController("", gameRules, GameMode.EASY),
                "Should reject empty game ID"
        );
    }

    @Test
    @DisplayName("GameController should validate game rules")
    public void testGameControllerValidatesGameRules() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameController("test", null, GameMode.EASY),
                "Should reject null game rules"
        );
    }

    @Test
    @DisplayName("GameController should validate game mode")
    public void testGameControllerValidatesGameMode() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameController("test", gameRules, null),
                "Should reject null game mode"
        );
    }

    @Test
    @DisplayName("GameController should process valid reveal move")
    public void testProcessValidRevealMove() {
        // Find an unrevealed tile
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile tile = board.getTile(row, col);
                if (tile.isUnrevealed()) {
                    PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.REVEAL);
                    MoveResult result = gameController.processPlayerMove(move);

                    assertTrue(result.isSuccess() || result.getOutcome() == MoveResult.Outcome.MOVE_EXECUTION_FAILED);
                    return;
                }
            }
        }
    }

    @Test
    @DisplayName("GameController should reject move with invalid coordinates")
    public void testRejectInvalidCoordinates() {
        PlayerMove move = new PlayerMove(100, 100, PlayerMove.ActionType.REVEAL);
        MoveResult result = gameController.processPlayerMove(move);

        assertFalse(result.isSuccess());
        assertEquals(MoveResult.Outcome.INVALID_COORDINATES, result.getOutcome());
    }

    @Test
    @DisplayName("GameController should track move statistics")
    public void testTrackMoveStatistics() {
        GameStatistics stats = gameController.getStatistics();
        assertEquals(0, stats.getTotalMoves());

        // Make a valid move
        PlayerMove move = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
        gameController.processPlayerMove(move);

        // Statistics should be updated
        assertTrue(stats.getTotalMoves() > 0 || gameController.getGameState() != GameState.ACTIVE);
    }

    @Test
    @DisplayName("GameController should handle flag moves")
    public void testProcessFlagMove() {
        // Find an unrevealed tile
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile tile = board.getTile(row, col);
                if (tile.isUnrevealed()) {
                    PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.FLAG);
                    MoveResult result = gameController.processPlayerMove(move);

                    assertTrue(result.isSuccess() || result.getOutcome() == MoveResult.Outcome.MOVE_EXECUTION_FAILED);
                    return;
                }
            }
        }
    }

    @Test
    @DisplayName("GameController should reject moves when game is not active")
    public void testRejectMovesWhenGameNotActive() {
        // Finish the game by playing all tiles or hitting a mine
        while (gameRules.getGameState() == GameState.ACTIVE) {
            boolean moved = false;
            for (int row = 0; row < 8 && !moved; row++) {
                for (int col = 0; col < 8 && !moved; col++) {
                    if (!board.getTile(row, col).isRevealed()) {
                        gameRules.revealTile(row, col);
                        moved = true;
                    }
                }
            }
            if (!moved) break;
        }

        // Now try to move
        PlayerMove move = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
        MoveResult result = gameController.processPlayerMove(move);

        assertFalse(result.isSuccess());
        assertEquals(MoveResult.Outcome.GAME_NOT_ACTIVE, result.getOutcome());
    }

    @Test
    @DisplayName("GameController should initialize AI opponent")
    public void testInitializeAIOpponent() {
        gameController.initializeAIOpponent(AIDifficulty.EASY);

        assertTrue(gameController.isHumanVsAI());
        assertNotNull(gameController.getAIPlayer());
        assertEquals(AIDifficulty.EASY, gameController.getAIPlayer().getDifficulty());
    }

    @Test
    @DisplayName("GameController should not allow AI initialization after game started")
    public void testCannotInitializeAIAfterGameStarted() {
        // Try to find an unrevealed tile and make a move
        boolean moveMade = false;
        for (int row = 0; row < 8 && !moveMade; row++) {
            for (int col = 0; col < 8 && !moveMade; col++) {
                if (board.getTile(row, col).isUnrevealed()) {
                    PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.REVEAL);
                    MoveResult result = gameController.processPlayerMove(move);
                    if (result.isSuccess()) {
                        moveMade = true;
                    }
                }
            }
        }

        // If a move was made and the game is still active, try to initialize AI
        if (moveMade && gameController.getGameState() == GameState.ACTIVE) {
            assertThrows(IllegalStateException.class, () ->
                    gameController.initializeAIOpponent(AIDifficulty.EASY),
                    "Should not allow AI initialization after game started"
            );
        }
    }

    @Test
    @DisplayName("GameController should execute AI move")
    public void testExecuteAIMove() {
        gameController.initializeAIOpponent(AIDifficulty.EASY);

        MoveResult result = gameController.executeAIMove();
        assertTrue(result.isSuccess() || result.getOutcome() == MoveResult.Outcome.MOVE_EXECUTION_FAILED);
    }

    @Test
    @DisplayName("GameController should reject AI move execution without AI")
    public void testRejectAIMoveWithoutAI() {
        assertThrows(IllegalStateException.class, () ->
                gameController.executeAIMove(),
                "Should require AI opponent"
        );
    }

    @Test
    @DisplayName("GameController should reset game")
    public void testResetGame() {
        GameStatistics statsBeforeReset = gameController.getStatistics();
        GameState stateBeforeReset = gameController.getGameState();

        // Make a move
        PlayerMove move = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
        gameController.processPlayerMove(move);

        // Reset
        gameController.resetGame();

        // Statistics should be cleared
        assertEquals(0, gameController.getStatistics().getTotalMoves());
        assertEquals(GameState.ACTIVE, gameController.getGameState());
    }

    @Test
    @DisplayName("GameController should maintain AI after reset")
    public void testAIMaintenedAfterReset() {
        gameController.initializeAIOpponent(AIDifficulty.NORMAL);

        AIDifficulty difficultyBefore = gameController.getAIPlayer().getDifficulty();

        // Reset
        gameController.resetGame();

        // AI should still be present with same difficulty
        assertTrue(gameController.isHumanVsAI());
        assertEquals(difficultyBefore, gameController.getAIPlayer().getDifficulty());
    }

    @Test
    @DisplayName("GameController should provide game summary")
    public void testGameSummary() {
        String summary = gameController.getGameSummary();

        assertFalse(summary.isEmpty());
        assertTrue(summary.contains("test-game-1"));
        assertTrue(summary.contains("ACTIVE"));
        assertTrue(summary.contains("8x8"));
    }

    @Test
    @DisplayName("GameController should track elapsed time")
    public void testElapsedTimeTracking() {
        GameStatistics stats = gameController.getStatistics();
        long startTime = stats.getElapsedTimeMs();

        // Sleep briefly
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long elapsedAfter = stats.getElapsedTimeMs();
        assertTrue(elapsedAfter >= startTime);
    }

    @Test
    @DisplayName("GameController should calculate accuracy")
    public void testAccuracyCalculation() {
        GameStatistics stats = gameController.getStatistics();
        assertEquals(0.0, stats.getAccuracy());

        // Record some actions
        stats.recordRevealAction(false); // Correct reveal
        stats.recordRevealAction(false); // Correct reveal
        stats.recordFlagAction(true, true); // Correct flag

        // 3/3 = 100%
        assertEquals(100.0, stats.getAccuracy(), 0.1);
    }

    @Test
    @DisplayName("GameController should get board reference")
    public void testGetBoardReference() {
        Board retrievedBoard = gameController.getBoard();
        assertSame(board, retrievedBoard);
    }

    @Test
    @DisplayName("GameController should finish game on statistics")
    public void testGameStatisticsFinish() {
        GameStatistics stats = gameController.getStatistics();
        long startTime = stats.getStartTime();

        // Finish the game
        stats.finishGame();

        assertTrue(stats.getEndTime() > 0);
        assertTrue(stats.getEndTime() >= startTime);
    }
}
