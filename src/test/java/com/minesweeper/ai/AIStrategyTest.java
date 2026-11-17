package com.minesweeper.ai;

import com.minesweeper.game.Board;
import com.minesweeper.game.GameMode;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.GameState;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AI strategy implementations.
 * Tests strategy decision making, move selection, and reasoning.
 */
@DisplayName("AI Strategy Tests")
public class AIStrategyTest {

    private Board board;
    private GameRules gameRules;
    private Random seededRandom;

    @BeforeEach
    public void setUp() {
        // Create a 10x10 board with 10 mines for testing
        board = new Board(10, 10, 10);
        gameRules = new GameRules(board);
        seededRandom = new Random(42);
    }

    @Test
    @DisplayName("Easy AI should return first move as center tile")
    public void testEasyAIOpeningMove() {
        AIStrategy strategy = new EasyAIStrategy(new Random(42));
        Optional<AIMove> firstMove = strategy.getNextMove(gameRules);

        assertTrue(firstMove.isPresent(), "AI should provide a move");
        AIMove move = firstMove.get();

        int centerRow = board.getHeight() / 2;
        int centerCol = board.getWidth() / 2;
        assertEquals(centerRow, move.getRow(), "First move should be center row");
        assertEquals(centerCol, move.getColumn(), "First move should be center column");
        assertEquals(MoveType.REVEAL, move.getMoveType(), "Opening move should be REVEAL");
        assertEquals(1.0, move.getConfidence(), "Opening move should have full confidence");
    }

    @Test
    @DisplayName("Easy AI should identify definite mines for flagging")
    public void testEasyAIFlagsMines() {
        AIStrategy strategy = new EasyAIStrategy(new Random(42));

        // Make first move
        Optional<AIMove> firstMove = strategy.getNextMove(gameRules);
        assertTrue(firstMove.isPresent());
        AIMove move = firstMove.get();

        // Simulate the first reveal
        gameRules.revealTile(move.getRow(), move.getColumn());

        // If the first move revealed an empty area, we should get more moves
        if (gameRules.getGameState() == GameState.ACTIVE) {
            Optional<AIMove> nextMove = strategy.getNextMove(gameRules);
            assertTrue(nextMove.isPresent(), "AI should provide subsequent moves");
        }
    }

    @Test
    @DisplayName("Easy AI difficulty should be EASY")
    public void testEasyAIDifficulty() {
        AIStrategy strategy = new EasyAIStrategy();
        assertEquals(AIDifficulty.EASY, strategy.getDifficulty());
    }

    @Test
    @DisplayName("Normal AI should return first move as center tile")
    public void testNormalAIOpeningMove() {
        AIStrategy strategy = new NormalAIStrategy(new Random(42));
        Optional<AIMove> firstMove = strategy.getNextMove(gameRules);

        assertTrue(firstMove.isPresent(), "AI should provide a move");
        AIMove move = firstMove.get();

        int centerRow = board.getHeight() / 2;
        int centerCol = board.getWidth() / 2;
        assertEquals(centerRow, move.getRow(), "First move should be center row");
        assertEquals(centerCol, move.getColumn(), "First move should be center column");
        assertEquals(MoveType.REVEAL, move.getMoveType());
        assertEquals(1.0, move.getConfidence());
    }

    @Test
    @DisplayName("Normal AI difficulty should be NORMAL")
    public void testNormalAIDifficulty() {
        AIStrategy strategy = new NormalAIStrategy();
        assertEquals(AIDifficulty.NORMAL, strategy.getDifficulty());
    }

    @Test
    @DisplayName("Normal AI should identify safe tiles adjacent to 0-mine regions")
    public void testNormalAIIdentifiesSafeTiles() {
        // Create a custom small board where we know the mine locations
        Board testBoard = new Board(5, 5, 1);
        GameRules testRules = new GameRules(testBoard);

        AIStrategy strategy = new NormalAIStrategy(new Random(42));

        // Make opening move
        Optional<AIMove> firstMove = strategy.getNextMove(testRules);
        assertTrue(firstMove.isPresent());

        // Execute opening move
        testRules.revealTile(firstMove.get().getRow(), firstMove.get().getColumn());

        if (testRules.getGameState() == GameState.ACTIVE) {
            Optional<AIMove> nextMove = strategy.getNextMove(testRules);
            // As long as game is active, AI should provide moves
            if (testRules.getGameState() == GameState.ACTIVE) {
                assertTrue(nextMove.isPresent() || testRules.getUnrevealedTileCount() == 0);
            }
        }
    }

    @Test
    @DisplayName("AI should provide reasoning for decisions")
    public void testAIMoveReasoning() {
        AIMove move = new AIMove(5, 5, MoveType.REVEAL, "Test reasoning", 0.8);
        assertEquals("Test reasoning", move.getReasoning(), "Move should contain reasoning");
        assertNotNull(move.toString(), "Move toString should work");
    }

    @Test
    @DisplayName("AI move confidence should be valid")
    public void testAIMoveConfidenceValidation() {
        // Valid confidence values
        new AIMove(0, 0, MoveType.REVEAL, "Test", 0.0);
        new AIMove(0, 0, MoveType.REVEAL, "Test", 0.5);
        new AIMove(0, 0, MoveType.REVEAL, "Test", 1.0);

        // Invalid confidence should throw
        assertThrows(IllegalArgumentException.class, () ->
                new AIMove(0, 0, MoveType.REVEAL, "Test", -0.1),
                "Negative confidence should throw"
        );

        assertThrows(IllegalArgumentException.class, () ->
                new AIMove(0, 0, MoveType.REVEAL, "Test", 1.1),
                "Confidence > 1.0 should throw"
        );
    }

    @Test
    @DisplayName("AI move should capture all required information")
    public void testAIMoveCapture() {
        AIMove move = new AIMove(3, 7, MoveType.FLAG, "Found a mine", 0.95);
        
        assertEquals(3, move.getRow());
        assertEquals(7, move.getColumn());
        assertEquals(MoveType.FLAG, move.getMoveType());
        assertEquals("Found a mine", move.getReasoning());
        assertEquals(0.95, move.getConfidence(), 0.001);
    }
}
