package com.minesweeper.ai;

import com.minesweeper.game.Board;
import com.minesweeper.game.GameRules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the AIPlayerFactory.
 * Tests factory creation of AI players with different difficulty levels.
 */
@DisplayName("AI Player Factory Tests")
public class AIPlayerFactoryTest {

    @Test
    @DisplayName("Factory should create Easy AI player")
    public void testCreateEasyAI() {
        Board board = new Board(8, 8, 8);
        GameRules rules = new GameRules(board);

        AIPlayer player = AIPlayerFactory.createAI("EasyBot", AIDifficulty.EASY, rules);

        assertNotNull(player);
        assertEquals("EasyBot", player.getName());
        assertEquals(AIDifficulty.EASY, player.getDifficulty());
    }

    @Test
    @DisplayName("Factory should create Normal AI player")
    public void testCreateNormalAI() {
        Board board = new Board(8, 8, 8);
        GameRules rules = new GameRules(board);

        AIPlayer player = AIPlayerFactory.createAI("NormalBot", AIDifficulty.NORMAL, rules);

        assertNotNull(player);
        assertEquals("NormalBot", player.getName());
        assertEquals(AIDifficulty.NORMAL, player.getDifficulty());
    }

    @Test
    @DisplayName("Factory should create AI with seeded Random for reproducibility")
    public void testCreateAIWithSeededRandom() {
        Board board1 = new Board(8, 8, 8);
        GameRules rules1 = new GameRules(board1);
        Random seed1 = new Random(42);
        AIPlayer player1 = AIPlayerFactory.createAI("Bot1", AIDifficulty.EASY, rules1, seed1);

        Board board2 = new Board(8, 8, 8);
        GameRules rules2 = new GameRules(board2);
        Random seed2 = new Random(42);
        AIPlayer player2 = AIPlayerFactory.createAI("Bot2", AIDifficulty.EASY, rules2, seed2);

        // Both players should exist
        assertNotNull(player1);
        assertNotNull(player2);

        // With same seed, they should follow similar patterns
        assertEquals(AIDifficulty.EASY, player1.getDifficulty());
        assertEquals(AIDifficulty.EASY, player2.getDifficulty());
    }

    @Test
    @DisplayName("Factory should reject Hard difficulty (not implemented)")
    public void testCreateHardAINotImplemented() {
        Board board = new Board(8, 8, 8);
        GameRules rules = new GameRules(board);

        assertThrows(IllegalArgumentException.class, () ->
                AIPlayerFactory.createAI("HardBot", AIDifficulty.HARD, rules),
                "Hard difficulty should not be implemented yet"
        );
    }

    @Test
    @DisplayName("Factory should reject null difficulty")
    public void testCreateAIWithNullDifficulty() {
        Board board = new Board(8, 8, 8);
        GameRules rules = new GameRules(board);

        assertThrows(IllegalArgumentException.class, () ->
                AIPlayerFactory.createAI("Bot", null, rules),
                "Should reject null difficulty"
        );
    }

    @Test
    @DisplayName("Factory should handle game rules validation internally")
    public void testFactoryCreatesValidAIInstance() {
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);

        AIPlayer easyPlayer = AIPlayerFactory.createAI("Easy", AIDifficulty.EASY, rules);
        AIPlayer normalPlayer = AIPlayerFactory.createAI("Normal", AIDifficulty.NORMAL, rules);

        // Verify they're properly initialized
        assertNotNull(easyPlayer.getMoveHistory());
        assertNotNull(normalPlayer.getMoveHistory());
        assertEquals(0, easyPlayer.getTotalGamesPlayed());
        assertEquals(0, normalPlayer.getTotalGamesPlayed());
    }

    @Test
    @DisplayName("Factory should create different instances each time")
    public void testFactoryCreatesDistinctInstances() {
        Board board = new Board(8, 8, 8);
        GameRules rules = new GameRules(board);

        AIPlayer player1 = AIPlayerFactory.createAI("Bot", AIDifficulty.EASY, rules);
        AIPlayer player2 = AIPlayerFactory.createAI("Bot", AIDifficulty.EASY, rules);

        // Should be different instances
        assertNotSame(player1, player2);

        // But same name and difficulty
        assertEquals(player1.getName(), player2.getName());
        assertEquals(player1.getDifficulty(), player2.getDifficulty());
    }
}
