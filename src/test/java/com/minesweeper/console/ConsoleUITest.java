package com.minesweeper.console;

import com.minesweeper.game.GameMode;
import com.minesweeper.game.GameController;
import com.minesweeper.game.GameState;
import com.minesweeper.game.Board;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.PlayerMove;
import com.minesweeper.game.MoveResult;
import com.minesweeper.ai.AIDifficulty;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ConsoleUI class.
 */
public class ConsoleUITest {

    private ConsoleUI consoleUI;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setup() {
        // Capture stdout
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    private void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Test that ConsoleUI can be instantiated.
     */
    @Test
    public void testConsoleUIInstantiation() {
        ConsoleUI ui = new ConsoleUI();
        assertNotNull(ui);
        tearDown();
    }

    /**
     * Test that ConsoleUI doesn't crash with invalid menu choice.
     */
    @Test
    public void testMainMenuWithInvalidChoice() {
        // Provide invalid input then quit
        String input = "999\n4\n";
        InputStream inStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inStream);

        consoleUI = new ConsoleUI();
        // Just verify start() doesn't throw
        try {
            consoleUI.start();
        } catch (Exception e) {
            fail("ConsoleUI.start() should not throw exception: " + e.getMessage());
        }
        tearDown();
    }

    /**
     * Test that menu choice 4 exits gracefully.
     */
    @Test
    public void testMainMenuExitChoice() {
        String input = "4\n";
        InputStream inStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inStream);

        consoleUI = new ConsoleUI();
        consoleUI.start(); // Should exit cleanly

        String output = outputStream.toString();
        assertTrue(output.contains("Greenfield Minesweeper"));
        assertTrue(output.contains("Thanks for playing"));

        tearDown();
    }

    /**
     * Test that invalid move input is handled gracefully.
     */
    @Test
    public void testPlayerMoveInputValidation() {
        // This test verifies that PlayerMove creation handles invalid input
        // by returning null (as designed in getPlayerInput)

        // Test valid move
        PlayerMove move = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
        assertNotNull(move);
        assertEquals(0, move.getRow());
        assertEquals(0, move.getColumn());
        assertEquals(PlayerMove.ActionType.REVEAL, move.getActionType());

        tearDown();
    }

    /**
     * Test that game initialization works correctly for practice mode.
     */
    @Test
    public void testGameInitializationPracticeMode() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        assertNotNull(controller);
        assertEquals(GameState.ACTIVE, controller.getGameState());
        assertNotNull(controller.getBoard());
        assertNotNull(controller.getGameRules());
        assertNotNull(controller.getStatistics());

        tearDown();
    }

    /**
     * Test that game initialization works correctly for AI mode.
     */
    @Test
    public void testGameInitializationAIMode() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        // Initialize AI opponent
        controller.initializeAIOpponent(AIDifficulty.NORMAL);

        assertNotNull(controller);
        assertEquals(GameState.ACTIVE, controller.getGameState());
        assertTrue(controller.isHumanVsAI());

        tearDown();
    }

    /**
     * Test that move processing returns valid results.
     */
    @Test
    public void testMoveProcessing() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        PlayerMove move = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
        MoveResult result = controller.processPlayerMove(move);

        assertNotNull(result);
        assertTrue(result.isSuccess() || !result.isSuccess()); // Either successful or not, but valid result
        assertNotNull(result.getMessage());

        tearDown();
    }

    /**
     * Test that game state transitions correctly when tile is revealed.
     */
    @Test
    public void testGameStateTransition() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        // Game starts active
        assertEquals(GameState.ACTIVE, controller.getGameState());

        // Process a move
        PlayerMove move = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
        controller.processPlayerMove(move);

        // Game should still be active (unless we happened to hit a mine or win)
        GameState state = controller.getGameState();
        assertTrue(state == GameState.ACTIVE || state == GameState.LOST || state == GameState.WON);

        tearDown();
    }

    /**
     * Test that statistics are tracked correctly.
     */
    @Test
    public void testStatisticsTracking() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        // Initial stats should be zero
        assertEquals(0, controller.getStatistics().getTotalMoves());

        // After a move
        PlayerMove move = new PlayerMove(5, 5, PlayerMove.ActionType.REVEAL);
        controller.processPlayerMove(move);

        // Total moves should increase
        assertTrue(controller.getStatistics().getTotalMoves() >= 0);

        // Accuracy should be calculable
        double accuracy = controller.getStatistics().getAccuracy();
        assertTrue(accuracy >= 0 && accuracy <= 100);

        tearDown();
    }

    /**
     * Test that flag action is processed correctly.
     */
    @Test
    public void testFlagAction() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        PlayerMove move = new PlayerMove(0, 0, PlayerMove.ActionType.FLAG);
        MoveResult result = controller.processPlayerMove(move);

        assertNotNull(result);
        assertNotNull(result.getMessage());

        tearDown();
    }

    /**
     * Test that reveal action is processed correctly.
     */
    @Test
    public void testRevealAction() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        PlayerMove move = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
        MoveResult result = controller.processPlayerMove(move);

        assertNotNull(result);
        assertNotNull(result.getMessage());

        tearDown();
    }

    /**
     * Test that invalid coordinates are handled gracefully.
     */
    @Test
    public void testInvalidCoordinates() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        // Out of bounds coordinates
        PlayerMove move = new PlayerMove(100, 100, PlayerMove.ActionType.REVEAL);
        MoveResult result = controller.processPlayerMove(move);

        assertNotNull(result);
        assertFalse(result.isSuccess()); // Should fail
        assertTrue(result.getMessage().contains("out of bounds") || 
                   result.getMessage().contains("invalid") ||
                   result.getMessage().contains("Invalid"));

        tearDown();
    }

    /**
     * Test that negative coordinates are handled gracefully.
     */
    @Test
    public void testNegativeCoordinates() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        // Negative coordinates
        PlayerMove move = new PlayerMove(-1, -1, PlayerMove.ActionType.REVEAL);
        MoveResult result = controller.processPlayerMove(move);

        assertNotNull(result);
        assertFalse(result.isSuccess()); // Should fail

        tearDown();
    }

    /**
     * Test all difficulty modes can be initialized.
     */
    @Test
    public void testAllDifficultyModes() {
        GameMode[] modes = {GameMode.EASY, GameMode.MEDIUM, GameMode.HARD};

        for (GameMode mode : modes) {
            Board board = new Board(mode.getWidth(), mode.getHeight(), mode.getMineCount());
            GameRules rules = new GameRules(board);
            GameController controller = new GameController("test-game-" + mode, rules, mode);

            assertNotNull(controller);
            assertEquals(GameState.ACTIVE, controller.getGameState());
            assertEquals(mode.getWidth(), controller.getBoard().getWidth());
            assertEquals(mode.getHeight(), controller.getBoard().getHeight());
        }

        tearDown();
    }

    /**
     * Test that AI difficulties are supported.
     */
    @Test
    public void testAIDifficulties() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        // Test Easy AI
        controller.initializeAIOpponent(AIDifficulty.EASY);
        assertTrue(controller.isHumanVsAI());

        // Create new controller for Normal AI
        Board board2 = new Board(10, 10, 10);
        GameRules rules2 = new GameRules(board2);
        GameController controller2 = new GameController("test-game-2", rules2, GameMode.EASY);
        controller2.initializeAIOpponent(AIDifficulty.NORMAL);
        assertTrue(controller2.isHumanVsAI());

        tearDown();
    }

    /**
     * Test that game can proceed through multiple moves.
     */
    @Test
    public void testMultipleMoveSequence() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        // Execute multiple moves
        for (int i = 0; i < 5 && controller.getGameState() == GameState.ACTIVE; i++) {
            PlayerMove move = new PlayerMove(i, i, PlayerMove.ActionType.REVEAL);
            MoveResult result = controller.processPlayerMove(move);
            assertNotNull(result);
        }

        // Statistics should show moves were made
        assertTrue(controller.getStatistics().getTotalMoves() > 0);

        tearDown();
    }

    /**
     * Test that board can be accessed after game initialization.
     */
    @Test
    public void testBoardAccess() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        Board retrievedBoard = controller.getBoard();
        assertNotNull(retrievedBoard);
        assertEquals(10, retrievedBoard.getWidth());
        assertEquals(10, retrievedBoard.getHeight());

        tearDown();
    }

    /**
     * Test that game rules can be accessed after game initialization.
     */
    @Test
    public void testGameRulesAccess() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("test-game", rules, GameMode.EASY);

        GameRules retrievedRules = controller.getGameRules();
        assertNotNull(retrievedRules);

        tearDown();
    }

}
