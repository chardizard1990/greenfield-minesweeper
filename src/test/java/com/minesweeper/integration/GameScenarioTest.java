package com.minesweeper.integration;

import com.minesweeper.ai.AIDifficulty;
import com.minesweeper.game.*;
import com.minesweeper.integration.util.GameTestBuilder.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Additional integration tests using the GameTestBuilder utilities.
 * Tests specific game scenarios using consistent test framework helpers.
 */
@DisplayName("Game Scenario Tests with Builder")
public class GameScenarioTest {

    @Test
    @DisplayName("Builder creates valid game controller")
    public void testBuilderCreatesValidGameController() {
        GameController controller = new GameControllerBuilder()
                .withGameId("builder-test")
                .withMode(GameMode.EASY)
                .build();

        assertNotNull(controller);
        assertEquals("builder-test", controller.getGameId());
        assertEquals(GameMode.EASY, controller.getGameMode());
        assertEquals(GameState.ACTIVE, controller.getGameState());
    }

    @Test
    @DisplayName("BoardAnalyzer correctly counts tiles")
    public void testBoardAnalyzerCountsTiles() {
        Board board = new BoardBuilder().buildEasy();
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("count-test", rules, GameMode.EASY);

        // All tiles should start unrevealed
        int unrevealed = BoardAnalyzer.countUnrevealedTiles(board);
        assertEquals(64, unrevealed, "8x8 board should have 64 unrevealed tiles");

        int revealed = BoardAnalyzer.countRevealedTiles(board);
        assertEquals(0, revealed, "No tiles should be revealed initially");

        int mines = BoardAnalyzer.countMines(board);
        assertTrue(mines > 0, "Board should have mines");

        int safe = BoardAnalyzer.countSafeTiles(board);
        assertEquals(64 - mines, safe, "Safe tiles count should match");
    }

    @Test
    @DisplayName("GameScenarioHelper plays to coverage percentage")
    public void testGameScenarioHelperPlaysToCoverage() {
        GameController controller = ScenarioBuilder.buildSimpleWinScenario();
        Board board = controller.getBoard();

        // Play to 25% coverage (lower target for stability)
        GameScenarioHelper.playToCoverage(controller, 25.0);

        double coverage = BoardAnalyzer.calculateCoverage(board);
        // Should have made progress
        assertTrue(coverage > 0.0, "Coverage should be greater than 0%");
    }

    @Test
    @DisplayName("GameScenarioHelper plays to completion")
    public void testGameScenarioHelperPlaysToCompletion() {
        GameController controller = ScenarioBuilder.buildQuickGameScenario();

        // Play to completion
        GameScenarioHelper.playToCompletion(controller);

        // Game should be finished
        GameState state = controller.getGameState();
        assertTrue(state == GameState.WON || state == GameState.LOST,
                "Game should be completed");
    }

    @Test
    @DisplayName("GameScenarioHelper flags tiles correctly")
    public void testGameScenarioHelperFlagsTiles() {
        GameController controller = ScenarioBuilder.buildSimpleWinScenario();
        Board board = controller.getBoard();

        int flaggedBefore = BoardAnalyzer.countFlaggedTiles(board);
        assertEquals(0, flaggedBefore, "No tiles should be flagged initially");

        // Try to flag some tiles
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Tile tile = board.getTile(row, col);
                if (tile.isUnrevealed() && !tile.isFlagged()) {
                    PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.FLAG);
                    controller.processPlayerMove(move);
                }
            }
        }

        int flaggedAfter = BoardAnalyzer.countFlaggedTiles(board);
        // May or may not have flagged tiles depending on board state

        // Verify any flagged tiles are indeed flagged (not revealed and not unrevealed)
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);
                if (tile.isFlagged()) {
                    assertFalse(tile.isRevealed(), "Flagged tiles should not be revealed");
                    assertFalse(tile.isUnrevealed(), "Flagged tiles should not be unrevealed (should be FLAGGED state)");
                }
            }
        }
    }

    @Test
    @DisplayName("GameScenarioHelper plays AI moves")
    public void testGameScenarioHelperPlaysAIMoves() {
        GameController controller = new GameControllerBuilder()
                .withGameId("ai-helper-test")
                .withMode(GameMode.EASY)
                .build();

        controller.initializeAIOpponent(AIDifficulty.EASY);
        // Must reveal at least one tile first
        controller.processPlayerMove(new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL));

        int movesPlayed = GameScenarioHelper.playAI(controller, 5);
        assertTrue(movesPlayed >= 0, "AI should play zero or more moves");
        assertTrue(movesPlayed <= 5, "Should not play more than 5 moves");
    }

    @Test
    @DisplayName("BoardAnalyzer finds unrevealed tiles correctly")
    public void testBoardAnalyzerFindsUnrevealedTile() {
        Board board = new BoardBuilder().buildEasy();

        int[] tile = BoardAnalyzer.findFirstUnrevealedTile(board);
        assertNotNull(tile, "Should find unrevealed tile in new game");
        assertEquals(2, tile.length, "Should return row and column");
        assertTrue(tile[0] >= 0 && tile[0] < 8, "Row should be valid");
        assertTrue(tile[1] >= 0 && tile[1] < 8, "Column should be valid");
    }

    @Test
    @DisplayName("BuilderScenarios create appropriate difficulty boards")
    public void testBuilderScenariosCreateAppropriateDifficulty() {
        GameController easyScenario = ScenarioBuilder.buildSimpleWinScenario();
        GameController hardScenario = ScenarioBuilder.buildChallengeAIScenario();

        // Both should create valid boards
        Board easyBoard = easyScenario.getBoard();
        Board hardBoard = hardScenario.getBoard();

        assertNotNull(easyBoard, "Easy board should be created");
        assertNotNull(hardBoard, "Hard board should be created");

        // Mine density: hard should have higher or equal mine ratio than easy
        int easyMines = BoardAnalyzer.countMines(easyBoard);
        int hardMines = BoardAnalyzer.countMines(hardBoard);

        int easyTiles = easyMines + BoardAnalyzer.countSafeTiles(easyBoard);
        int hardTiles = hardMines + BoardAnalyzer.countSafeTiles(hardBoard);

        double easyMineRatio = (double) easyMines / easyTiles;
        double hardMineRatio = (double) hardMines / hardTiles;

        assertTrue(hardMineRatio >= easyMineRatio, 
                "Hard mode should have higher or equal mine density");
    }

    @Test
    @DisplayName("Multiple games can be created independently")
    public void testMultipleGamesCreatedIndependently() {
        java.util.List<GameController> games = ScenarioBuilder.buildMultipleGames(3);

        assertEquals(3, games.size(), "Should create 3 games");

        // Verify games are independent
        GameController game1 = games.get(0);
        GameController game2 = games.get(1);

        // Make a move in game1
        Board board1 = game1.getBoard();
        int[] tile1 = BoardAnalyzer.findFirstUnrevealedTile(board1);
        if (tile1 != null) {
            PlayerMove move = new PlayerMove(tile1[0], tile1[1], PlayerMove.ActionType.REVEAL);
            game1.processPlayerMove(move);
        }

        // game2 should be unaffected
        Board board2 = game2.getBoard();
        int unrevealed2 = BoardAnalyzer.countUnrevealedTiles(board2);
        assertEquals(64, unrevealed2, "Game2 should still have all tiles unrevealed");
    }

    @Test
    @DisplayName("Coverage calculation is accurate")
    public void testCoverageCalculationIsAccurate() {
        GameController controller = ScenarioBuilder.buildSimpleWinScenario();
        Board board = controller.getBoard();

        double initialCoverage = BoardAnalyzer.calculateCoverage(board);
        assertEquals(0.0, initialCoverage, "Initial coverage should be 0%");

        // Reveal some tiles
        GameScenarioHelper.playToCoverage(controller, 25.0);

        double midCoverage = BoardAnalyzer.calculateCoverage(board);
        assertTrue(midCoverage > 0.0 && midCoverage <= 100.0, 
                "Coverage should be between 0 and 100%");

        // Play to completion
        GameScenarioHelper.playToCompletion(controller);

        double finalCoverage = BoardAnalyzer.calculateCoverage(board);
        assertTrue(finalCoverage >= 0.0 && finalCoverage <= 100.0,
                "Final coverage should be valid percentage");
    }

    @Test
    @DisplayName("Quick game scenario completes very fast")
    public void testQuickGameScenarioCompletesFast() {
        GameController controller = ScenarioBuilder.buildQuickGameScenario();

        long startTime = System.currentTimeMillis();
        GameScenarioHelper.playToCompletion(controller);
        long elapsedTime = System.currentTimeMillis() - startTime;

        // Should complete in less than 5 seconds
        assertTrue(elapsedTime < 5000, 
                "Quick game should complete fast, took: " + elapsedTime + "ms");

        // Game should be finished
        GameState state = controller.getGameState();
        assertTrue(state == GameState.WON || state == GameState.LOST,
                "Quick game should be completed");
    }

    @Test
    @DisplayName("Builder with custom board dimensions")
    public void testBuilderWithCustomBoardDimensions() {
        Board customBoard = new BoardBuilder()
                .withWidth(10)
                .withHeight(10)
                .withMines(10)
                .build();

        GameController controller = new GameControllerBuilder()
                .withBoard(customBoard)
                .build();

        Board board = controller.getBoard();

        int mines = BoardAnalyzer.countMines(board);
        assertTrue(mines > 0 && mines <= 10, "Should have between 1-10 mines");

        int totalBoardTiles = BoardAnalyzer.countMines(board) + BoardAnalyzer.countSafeTiles(board);
        assertEquals(100, totalBoardTiles, "Should have 100 total tiles (10x10)");
    }

    @Test
    @DisplayName("GameStatistics track correctly through gameplay")
    public void testGameStatisticsTrackCorrectlyThroughGameplay() {
        GameController controller = ScenarioBuilder.buildSimpleWinScenario();
        GameStatistics stats = controller.getStatistics();

        assertEquals(0, stats.getTotalMoves(), "Should start with no moves");

        // Make several moves
        int movesRequested = 5;
        int movesMade = 0;

        for (int i = 0; i < movesRequested; i++) {
            Board board = controller.getBoard();
            int[] tile = BoardAnalyzer.findFirstUnrevealedTile(board);

            if (tile != null && controller.getGameState() == GameState.ACTIVE) {
                PlayerMove move = new PlayerMove(tile[0], tile[1], PlayerMove.ActionType.REVEAL);
                MoveResult result = controller.processPlayerMove(move);

                if (result.isSuccess()) {
                    movesMade++;
                }
            }
        }

        // Statistics should be updated
        assertTrue(stats.getTotalMoves() >= movesMade,
                "Statistics should track moves");
    }
}
