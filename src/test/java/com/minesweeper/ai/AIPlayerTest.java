package com.minesweeper.ai;

import com.minesweeper.game.Board;
import com.minesweeper.game.GameMode;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.GameState;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the AIPlayer class.
 * Tests autonomous gameplay, move tracking, statistics, and game outcomes.
 */
@DisplayName("AI Player Tests")
public class AIPlayerTest {

    private Board board;
    private GameRules gameRules;
    private AIPlayer aiPlayer;

    @BeforeEach
    public void setUp() {
        // Create a small board for faster test execution
        board = new Board(8, 8, 8);
        gameRules = new GameRules(board);
        aiPlayer = new AIPlayer("TestAI", new EasyAIStrategy(new Random(42)), gameRules);
    }

    @Test
    @DisplayName("AI player should have correct name")
    public void testAIPlayerName() {
        assertEquals("TestAI", aiPlayer.getName());
    }

    @Test
    @DisplayName("AI player should track difficulty level")
    public void testAIPlayerDifficulty() {
        assertEquals(AIDifficulty.EASY, aiPlayer.getDifficulty());
    }

    @Test
    @DisplayName("AI player should track game rules reference")
    public void testAIPlayerGameRulesReference() {
        assertSame(gameRules, aiPlayer.getGameRules());
    }

    @Test
    @DisplayName("AI player should track move history")
    public void testAIPlayerMoveHistory() {
        // Initially empty
        assertTrue(aiPlayer.getMoveHistory().isEmpty());

        // After playing a game
        aiPlayer.playGame();

        List<AIMove> history = aiPlayer.getMoveHistory();
        assertFalse(history.isEmpty(), "Move history should contain moves after game");

        // All moves should have required information
        for (AIMove move : history) {
            assertNotNull(move.getReasoning());
            assertNotNull(move.getMoveType());
            assertTrue(move.getConfidence() >= 0.0 && move.getConfidence() <= 1.0);
        }
    }

    @Test
    @DisplayName("AI player should update win/loss statistics")
    public void testAIPlayerStatistics() {
        assertEquals(0, aiPlayer.getGamesWon());
        assertEquals(0, aiPlayer.getGamesLost());
        assertEquals(0, aiPlayer.getTotalGamesPlayed());

        // Play a game
        aiPlayer.playGame();

        assertEquals(1, aiPlayer.getTotalGamesPlayed());
        assertTrue(
                aiPlayer.getGamesWon() == 1 || aiPlayer.getGamesLost() == 1,
                "Either win or loss should be recorded"
        );
    }

    @Test
    @DisplayName("AI player should calculate win rate correctly")
    public void testAIPlayerWinRate() {
        assertEquals(0.0, aiPlayer.getWinRate());

        // After one game, win rate is either 0% or 100%
        aiPlayer.playGame();
        double winRate = aiPlayer.getWinRate();
        assertTrue(winRate == 0.0 || winRate == 100.0, "Win rate should be 0% or 100% after one game");
    }

    @Test
    @DisplayName("AI player should clear move history")
    public void testClearMoveHistory() {
        aiPlayer.playGame();
        assertFalse(aiPlayer.getMoveHistory().isEmpty());

        aiPlayer.clearMoveHistory();
        assertTrue(aiPlayer.getMoveHistory().isEmpty());
    }

    @Test
    @DisplayName("AI player constructor should validate inputs")
    public void testAIPlayerConstructorValidation() {
        assertThrows(IllegalArgumentException.class, () ->
                new AIPlayer(null, new EasyAIStrategy(), gameRules),
                "Should reject null name"
        );

        assertThrows(IllegalArgumentException.class, () ->
                new AIPlayer("", new EasyAIStrategy(), gameRules),
                "Should reject empty name"
        );

        assertThrows(IllegalArgumentException.class, () ->
                new AIPlayer("AI", null, gameRules),
                "Should reject null strategy"
        );

        assertThrows(IllegalArgumentException.class, () ->
                new AIPlayer("AI", new EasyAIStrategy(), null),
                "Should reject null gameRules"
        );
    }

    @Test
    @DisplayName("AI player should track last game summary")
    public void testLastGameSummary() {
        String emptySummary = aiPlayer.getLastGameSummary();
        assertEquals("No games played yet", emptySummary);

        aiPlayer.playGame();
        String summary = aiPlayer.getLastGameSummary();
        
        assertFalse(summary.isEmpty());
        assertTrue(summary.contains("TestAI"));
        assertTrue(summary.contains("WON") || summary.contains("LOST"));
        assertTrue(summary.contains("moves"));
    }

    @Test
    @DisplayName("AI player toString should include stats")
    public void testAIPlayerToString() {
        String str = aiPlayer.toString();
        
        assertFalse(str.isEmpty());
        assertTrue(str.contains("TestAI"));
        assertTrue(str.contains("EASY"));
        assertTrue(str.contains("wins"));
        assertTrue(str.contains("losses"));
    }

    @Test
    @DisplayName("AI player should not play game when not ACTIVE")
    public void testPlayGameWithInactiveGameRules() {
        // Manually play until game ends - but check for already-revealed tiles
        int attempts = 0;
        int maxAttempts = 100;
        
        while (gameRules.getGameState() == GameState.ACTIVE && attempts < maxAttempts) {
            // Try different tiles until one works
            boolean moved = false;
            
            for (int row = 0; row < 8 && !moved; row++) {
                for (int col = 0; col < 8 && !moved; col++) {
                    if (!gameRules.getBoard().getTile(row, col).isRevealed()) {
                        gameRules.revealTile(row, col);
                        moved = true;
                    }
                }
            }
            
            if (!moved) {
                break; // No more unrevealed tiles
            }
            attempts++;
        }

        // Now try to play with non-active game rules
        assertThrows(IllegalStateException.class, () -> aiPlayer.playGame(),
                "Should throw when game is not ACTIVE"
        );
    }

    @Test
    @DisplayName("AI player should handle multiple games")
    public void testMultipleGames() {
        // Play 5 games with different boards
        for (int i = 0; i < 5; i++) {
            Board newBoard = new Board(8, 8, 8);
            GameRules newRules = new GameRules(newBoard);
            AIPlayer player = new AIPlayer("MultiGameAI", new EasyAIStrategy(new Random(100 + i)), newRules);

            player.playGame();
            assertEquals(1, player.getTotalGamesPlayed());
        }
    }

    @Test
    @DisplayName("AI player move history should be defensive copy")
    public void testMoveHistoryDefensiveCopy() {
        aiPlayer.playGame();
        
        List<AIMove> history1 = aiPlayer.getMoveHistory();
        List<AIMove> history2 = aiPlayer.getMoveHistory();

        // Both lists should be independent
        assertNotSame(history1, history2, "getMoveHistory should return new list each time");
        
        // But contain same moves
        assertEquals(history1.size(), history2.size());
    }

    @Test
    @DisplayName("AI player with Normal difficulty should work correctly")
    public void testNormalAIPlayer() {
        Board normalBoard = new Board(8, 8, 8);
        GameRules normalRules = new GameRules(normalBoard);
        AIPlayer normalPlayer = new AIPlayer("NormalAI", new NormalAIStrategy(new Random(42)), normalRules);

        assertEquals(AIDifficulty.NORMAL, normalPlayer.getDifficulty());
        
        normalPlayer.playGame();
        assertEquals(1, normalPlayer.getTotalGamesPlayed());
        assertFalse(normalPlayer.getMoveHistory().isEmpty());
    }

    @Test
    @DisplayName("AI player should complete games autonomously")
    public void testAutonomousGameplay() {
        aiPlayer.playGame();

        // Game should be finished
        GameState finalState = gameRules.getGameState();
        assertTrue(
                finalState == GameState.WON || finalState == GameState.LOST,
                "Game should end in either WIN or LOSS"
        );

        // Should have made moves
        assertFalse(aiPlayer.getMoveHistory().isEmpty());

        // Statistics should be recorded
        assertEquals(1, aiPlayer.getTotalGamesPlayed());
    }

    @Test
    @DisplayName("AI moves should have valid coordinates")
    public void testAIMoveCoordinates() {
        aiPlayer.playGame();

        for (AIMove move : aiPlayer.getMoveHistory()) {
            int row = move.getRow();
            int col = move.getColumn();

            assertTrue(row >= 0 && row < board.getHeight(), "Row should be in bounds");
            assertTrue(col >= 0 && col < board.getWidth(), "Column should be in bounds");
        }
    }
}
