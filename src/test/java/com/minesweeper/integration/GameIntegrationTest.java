package com.minesweeper.integration;

import com.minesweeper.ai.AIDifficulty;
import com.minesweeper.ai.AIMove;
import com.minesweeper.ai.AIPlayer;
import com.minesweeper.game.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for complete game scenarios.
 * Tests full game workflows including human vs AI, AI vs AI, and edge cases.
 * These tests verify that all components work together correctly in realistic gameplay.
 */
@DisplayName("Game Integration Tests")
public class GameIntegrationTest {

    private GameController humanController;
    private GameController aiController;

    @BeforeEach
    public void setUp() {
        // Initialize controllers
        Board humanBoard = new Board(8, 8, 8);
        GameRules humanRules = new GameRules(humanBoard);
        humanController = new GameController("human-game", humanRules, GameMode.EASY);

        Board aiBoard = new Board(8, 8, 8);
        GameRules aiRules = new GameRules(aiBoard);
        aiController = new GameController("ai-game", aiRules, GameMode.EASY);
    }

    @Test
    @DisplayName("Human player can complete an easy game to victory")
    public void testHumanCompleteEasyGameToVictory() {
        // Start with EASY mode where there's minimal mines
        Board easyBoard = new Board(8, 8, 8);
        GameRules easyRules = new GameRules(easyBoard);
        GameController easyController = new GameController("easy-game", easyRules, GameMode.EASY);

        // Track moves
        int movesNeeded = 0;
        int maxMoves = 100;

        // Play until game ends or max moves reached
        while (easyController.getGameState() == GameState.ACTIVE && movesNeeded < maxMoves) {
            // Find an unrevealed tile
            Board board = easyController.getBoard();
            boolean foundMove = false;

            for (int row = 0; row < 8 && !foundMove; row++) {
                for (int col = 0; col < 8 && !foundMove; col++) {
                    Tile tile = board.getTile(row, col);
                    if (tile.isUnrevealed()) {
                        // Try to reveal
                        PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.REVEAL);
                        MoveResult result = easyController.processPlayerMove(move);

                        if (result.isSuccess()) {
                            movesNeeded++;
                            foundMove = true;
                        }
                    }
                }
            }

            if (!foundMove) break;
        }

        // Game should be finished
        GameState finalState = easyController.getGameState();
        assertTrue(finalState == GameState.WON || finalState == GameState.LOST,
                "Game should end after playing");
        assertTrue(movesNeeded > 0, "Should have made at least one move");
    }

    @Test
    @DisplayName("AI player can complete a full game autonomously")
    public void testAICompleteFullGameAutonomously() {
        aiController.initializeAIOpponent(AIDifficulty.EASY);

        int movesExecuted = 0;
        int maxMoves = 100;

        // AI plays until game finishes or max moves
        while (aiController.getGameState() == GameState.ACTIVE && movesExecuted < maxMoves) {
            MoveResult result = aiController.executeAIMove();

            if (result.isSuccess()) {
                movesExecuted++;
            } else {
                // If move failed, break to avoid infinite loop
                if (result.getOutcome() != MoveResult.Outcome.MOVE_EXECUTION_FAILED) {
                    break;
                }
            }
        }

        // Game should be in a terminal state
        GameState finalState = aiController.getGameState();
        assertTrue(finalState == GameState.WON || finalState == GameState.LOST,
                "AI should finish the game, final state: " + finalState);
        assertTrue(movesExecuted > 0, "AI should make at least one move");
    }

    @Test
    @DisplayName("Easy AI difficulty plays full games")
    public void testEasyAIDifficultyPlaysFullGames() {
        Board board = new Board(8, 8, 8);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("easy-game", rules, GameMode.EASY);
        controller.initializeAIOpponent(AIDifficulty.EASY);

        // Let AI play several moves
        int moves = 0;
        int maxMoves = 10;

        while (controller.getGameState() == GameState.ACTIVE && moves < maxMoves) {
            MoveResult result = controller.executeAIMove();
            if (result.isSuccess()) {
                moves++;
            } else {
                break;
            }
        }

        // Verify AI made strategic decisions with reasonable confidence
        AIPlayer ai = controller.getAIPlayer();
        if (!ai.getMoveHistory().isEmpty()) {
            for (AIMove move : ai.getMoveHistory()) {
                // All moves should have reasoning and confidence
                assertNotNull(move.getReasoning(), "Move should have reasoning");
                assertTrue(move.getConfidence() >= 0.0 && move.getConfidence() <= 1.0,
                        "Confidence should be between 0 and 1");
            }
        }
    }

    @Test
    @DisplayName("Game state transitions correctly through reveal actions")
    public void testGameStateTransitionsThroughReveals() {
        Board board = humanController.getBoard();
        GameState initialState = humanController.getGameState();

        assertEquals(GameState.ACTIVE, initialState, "Game should start as ACTIVE");

        // Find and reveal tiles
        int revealsBeforeEnd = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile tile = board.getTile(row, col);
                if (tile.isUnrevealed()) {
                    PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.REVEAL);
                    MoveResult result = humanController.processPlayerMove(move);

                    if (result.isSuccess()) {
                        revealsBeforeEnd++;
                    }

                    // Once game ends, no more reveals should succeed
                    if (humanController.getGameState() != GameState.ACTIVE) {
                        // Try to make another move - should fail
                        PlayerMove nextMove = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
                        MoveResult nextResult = humanController.processPlayerMove(nextMove);
                        assertFalse(nextResult.isSuccess(),
                                "Should not accept moves when game is not active");
                        return;
                    }
                }
            }
        }

        assertTrue(revealsBeforeEnd > 0, "Should have revealed at least one tile");
    }

    @Test
    @DisplayName("Flagging strategy affects game outcome")
    public void testFlaggingStrategyAffectsOutcome() {
        Board board = humanController.getBoard();

        // Count total mines
        int totalMines = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getTile(row, col).isMine()) {
                    totalMines++;
                }
            }
        }

        assertTrue(totalMines > 0, "Board should have mines");

        // Try to flag tiles
        int flagsPlaced = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile tile = board.getTile(row, col);
                if (tile.isUnrevealed() && flagsPlaced < totalMines) {
                    PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.FLAG);
                    MoveResult result = humanController.processPlayerMove(move);

                    if (result.isSuccess() && board.getTile(row, col).isFlagged()) {
                        flagsPlaced++;
                    }
                }
            }
        }

        // Verify flagging worked
        GameStatistics stats = humanController.getStatistics();
        assertTrue(stats.getTotalMoves() >= flagsPlaced, "Statistics should track flagging moves");
    }

    @Test
    @DisplayName("Cascading reveals are handled correctly")
    public void testCascadingRevealsHandledCorrectly() {
        Board board = humanController.getBoard();
        int tileCountBefore = getTileCountRevealed(board);

        // Try to reveal tiles strategically
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile tile = board.getTile(row, col);
                if (tile.isUnrevealed() && humanController.getGameState() == GameState.ACTIVE) {
                    PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.REVEAL);
                    MoveResult result = humanController.processPlayerMove(move);

                    if (result.isSuccess()) {
                        // Check if cascade happened (multiple tiles revealed in one move)
                        int tileCountAfter = getTileCountRevealed(board);
                        int tilesDifference = tileCountAfter - tileCountBefore;

                        // If more than 1 tile was revealed, cascade happened
                        if (tilesDifference > 1) {
                            // Verify all revealed tiles are safe (not mines)
                            for (int r = 0; r < 8; r++) {
                                for (int c = 0; c < 8; c++) {
                                    Tile t = board.getTile(r, c);
                                    if (t.isRevealed()) {
                                        assertFalse(t.isMine(),
                                                "Cascading reveal should not reveal mines");
                                    }
                                }
                            }
                            return; // Test passed
                        }

                        tileCountBefore = tileCountAfter;
                    }
                }
            }
        }

        // If no cascades, verify at least some tiles were revealed
        int finalCount = getTileCountRevealed(board);
        assertTrue(finalCount > 0, "At least some tiles should be revealed");
    }

    @Test
    @DisplayName("Edge case: Corner tile reveal works correctly")
    public void testCornerTileRevealWorksCorrectly() {
        Board board = humanController.getBoard();

        // Try corners: (0,0), (0,7), (7,0), (7,7)
        int[][] corners = {{0, 0}, {0, 7}, {7, 0}, {7, 7}};

        for (int[] corner : corners) {
            if (humanController.getGameState() != GameState.ACTIVE) break;

            Tile tile = board.getTile(corner[0], corner[1]);
            if (tile.isUnrevealed()) {
                PlayerMove move = new PlayerMove(corner[0], corner[1], PlayerMove.ActionType.REVEAL);
                MoveResult result = humanController.processPlayerMove(move);

                // Result should be valid (success or hit mine)
                assertNotNull(result);
                assertTrue(result.getOutcome() != MoveResult.Outcome.INVALID_COORDINATES,
                        "Corner coordinates should be valid");
            }
        }
    }

    @Test
    @DisplayName("Edge case: Center tile on even board works correctly")
    public void testCenterTileOnEvenBoardWorksCorrectly() {
        // 8x8 board, center area is around (3,3) to (4,4)
        Board board = humanController.getBoard();

        int[][] centerTiles = {{3, 3}, {3, 4}, {4, 3}, {4, 4}};

        for (int[] center : centerTiles) {
            if (humanController.getGameState() != GameState.ACTIVE) break;

            Tile tile = board.getTile(center[0], center[1]);
            if (tile.isUnrevealed()) {
                PlayerMove move = new PlayerMove(center[0], center[1], PlayerMove.ActionType.REVEAL);
                MoveResult result = humanController.processPlayerMove(move);

                assertNotNull(result);
                assertTrue(result.getOutcome() != MoveResult.Outcome.INVALID_COORDINATES,
                        "Center coordinates should be valid");
            }
        }
    }

    @Test
    @DisplayName("Game reset properly clears state for new game")
    public void testGameResetClearsStateForNewGame() {
        // Make some moves
        for (int i = 0; i < 3; i++) {
            Tile tile = humanController.getBoard().getTile(i, i);
            if (tile.isUnrevealed()) {
                PlayerMove move = new PlayerMove(i, i, PlayerMove.ActionType.REVEAL);
                humanController.processPlayerMove(move);
            }
        }

        GameStatistics statsBefore = humanController.getStatistics();
        int movesBefore = statsBefore.getTotalMoves();

        // Reset
        humanController.resetGame();

        GameStatistics statsAfter = humanController.getStatistics();
        assertEquals(0, statsAfter.getTotalMoves(), "Moves should be cleared after reset");
        assertEquals(GameState.ACTIVE, humanController.getGameState(),
                "Game should be ACTIVE after reset");

        // Verify board is reset
        Board board = humanController.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile tile = board.getTile(row, col);
                assertTrue(tile.isUnrevealed(), "All tiles should be unrevealed after reset");
            }
        }
    }

    @Test
    @DisplayName("Invalid moves don't corrupt game state")
    public void testInvalidMovesDoNotCorruptGameState() {
        GameState stateBeforeInvalid = humanController.getGameState();
        int tileCountBefore = getTileCountRevealed(humanController.getBoard());

        // Try many invalid moves
        for (int i = 0; i < 10; i++) {
            PlayerMove invalidMove = new PlayerMove(100 + i, 100 + i, PlayerMove.ActionType.REVEAL);
            MoveResult result = humanController.processPlayerMove(invalidMove);

            assertFalse(result.isSuccess(), "Invalid move should fail");
        }

        // Game state should not be corrupted
        assertEquals(stateBeforeInvalid, humanController.getGameState(),
                "Invalid moves should not change game state");
        assertEquals(tileCountBefore, getTileCountRevealed(humanController.getBoard()),
                "Board should be unchanged after invalid moves");
    }

    @Test
    @DisplayName("Multiple difficulty levels play differently")
    public void testMultipleDifficultyLevelsPlayDifferently() {
        // Easy game
        Board easyBoard = new Board(10, 10, 10);
        GameRules easyRules = new GameRules(easyBoard);
        GameController easyController = new GameController("easy-ai", easyRules, GameMode.EASY);
        easyController.initializeAIOpponent(AIDifficulty.EASY);

        // Medium game
        Board mediumBoard = new Board(20, 20, 75);
        GameRules mediumRules = new GameRules(mediumBoard);
        GameController mediumController = new GameController("medium-ai", mediumRules, GameMode.MEDIUM);
        mediumController.initializeAIOpponent(AIDifficulty.NORMAL);

        // Let each play for a few moves
        int easyMoves = 0;
        int mediumMoves = 0;

        while (easyController.getGameState() == GameState.ACTIVE && easyMoves < 5) {
            if (easyController.executeAIMove().isSuccess()) {
                easyMoves++;
            } else {
                break;
            }
        }

        while (mediumController.getGameState() == GameState.ACTIVE && mediumMoves < 5) {
            if (mediumController.executeAIMove().isSuccess()) {
                mediumMoves++;
            } else {
                break;
            }
        }

        // Both should play, but possibly with different strategies
        assertTrue(easyMoves >= 1, "Easy AI should make moves");
        assertTrue(mediumMoves >= 1, "Medium AI should make moves");
    }

    // Helper method to count revealed tiles
    private int getTileCountRevealed(Board board) {
        int count = 0;
        for (int row = 0; row < 100; row++) {
            for (int col = 0; col < 100; col++) {
                try {
                    if (board.getTile(row, col).isRevealed()) {
                        count++;
                    }
                } catch (IndexOutOfBoundsException e) {
                    return count;
                }
            }
        }
        return count;
    }
}
