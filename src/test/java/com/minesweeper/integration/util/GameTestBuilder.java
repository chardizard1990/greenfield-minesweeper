package com.minesweeper.integration.util;

import com.minesweeper.game.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for constructing consistent test scenarios and game configurations.
 * Provides builders and factories for creating boards, controllers, and game scenarios.
 */
public class GameTestBuilder {

    // Board configuration helper
    public static class BoardBuilder {
        private int width = 8;
        private int height = 8;
        private int mines = 8;

        public BoardBuilder withWidth(int width) {
            this.width = width;
            return this;
        }

        public BoardBuilder withHeight(int height) {
            this.height = height;
            return this;
        }

        public BoardBuilder withMines(int mines) {
            this.mines = mines;
            return this;
        }

        // Easy board: low mine count
        public Board buildEasy() {
            return new Board(8, 8, 8);
        }

        // Normal board: medium mine count
        public Board buildNormal() {
            return new Board(12, 12, 30);
        }

        // Hard board: high mine count
        public Board buildHard() {
            return new Board(16, 16, 40);
        }

        // Custom board
        public Board build() {
            return new Board(width, height, mines);
        }
    }

    // Game controller configuration helper
    public static class GameControllerBuilder {
        private Board board;
        private GameMode mode = GameMode.EASY;
        private String gameId = "test-game";

        public GameControllerBuilder withBoard(Board board) {
            this.board = board;
            return this;
        }

        public GameControllerBuilder withMode(GameMode mode) {
            this.mode = mode;
            return this;
        }

        public GameControllerBuilder withGameId(String gameId) {
            this.gameId = gameId;
            return this;
        }

        public GameController build() {
            if (board == null) {
                board = new BoardBuilder().build();
            }
            GameRules rules = new GameRules(board);
            return new GameController(gameId, rules, mode);
        }
    }

    // Test scenario templates
    public static class ScenarioBuilder {

        /**
         * Creates a simple win scenario:
         * - Small board with few mines
         * - Human can win by revealing all safe tiles
         */
        public static GameController buildSimpleWinScenario() {
            Board board = new BoardBuilder().buildEasy();
            GameRules rules = new GameRules(board);
            return new GameController("win-scenario", rules, GameMode.EASY);
        }

        /**
         * Creates a challenging AI scenario:
         * - Larger board with many mines
         * - AI needs to use strategy to avoid mines
         */
        public static GameController buildChallengeAIScenario() {
            Board board = new BoardBuilder().buildHard();
            GameRules rules = new GameRules(board);
            GameController controller = new GameController("ai-challenge", rules, GameMode.HARD);
            return controller;
        }

        /**
         * Creates a quick game scenario:
         * - Very small board for fast tests
         */
        public static GameController buildQuickGameScenario() {
            Board board = new Board(5, 5, 5);
            GameRules rules = new GameRules(board);
            return new GameController("quick-game", rules, GameMode.EASY);
        }

        /**
         * Creates multiple games for comparative testing
         */
        public static List<GameController> buildMultipleGames(int count) {
            List<GameController> games = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Board board = new BoardBuilder().build();
                GameRules rules = new GameRules(board);
                GameController controller = new GameController("game-" + i, rules, GameMode.EASY);
                games.add(controller);
            }
            return games;
        }
    }

    // Helper methods for game state inspection
    public static class BoardAnalyzer {

        /**
         * Count unrevealed tiles on board
         */
        public static int countUnrevealedTiles(Board board) {
            int count = 0;
            for (int row = 0; row < board.getHeight(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (board.getTile(row, col).isUnrevealed()) {
                        count++;
                    }
                }
            }
            return count;
        }

        /**
         * Count revealed tiles on board
         */
        public static int countRevealedTiles(Board board) {
            int count = 0;
            for (int row = 0; row < board.getHeight(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (board.getTile(row, col).isRevealed()) {
                        count++;
                    }
                }
            }
            return count;
        }

        /**
         * Count flagged tiles on board
         */
        public static int countFlaggedTiles(Board board) {
            int count = 0;
            for (int row = 0; row < board.getHeight(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (board.getTile(row, col).isFlagged()) {
                        count++;
                    }
                }
            }
            return count;
        }

        /**
         * Count mines on board
         */
        public static int countMines(Board board) {
            int count = 0;
            for (int row = 0; row < board.getHeight(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (board.getTile(row, col).isMine()) {
                        count++;
                    }
                }
            }
            return count;
        }

        /**
         * Count safe tiles on board
         */
        public static int countSafeTiles(Board board) {
            int count = 0;
            for (int row = 0; row < board.getHeight(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (!board.getTile(row, col).isMine()) {
                        count++;
                    }
                }
            }
            return count;
        }

        /**
         * Find first unrevealed tile coordinates
         */
        public static int[] findFirstUnrevealedTile(Board board) {
            for (int row = 0; row < board.getHeight(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (board.getTile(row, col).isUnrevealed()) {
                        return new int[]{row, col};
                    }
                }
            }
            return null;
        }

        /**
         * Calculate coverage percentage (revealed tiles / total tiles)
         */
        public static double calculateCoverage(Board board) {
            int revealed = countRevealedTiles(board);
            int total = board.getHeight() * board.getWidth();
            if (total == 0) return 0.0;
            return (double) revealed / total * 100.0;
        }
    }

    // Helper methods for game scenario setup
    public static class GameScenarioHelper {

        /**
         * Play game to a specific coverage percentage
         */
        public static void playToCoverage(GameController controller, double targetCoverage) {
            Board board = controller.getBoard();
            int maxMoves = 1000;
            int moves = 0;

            while (controller.getGameState() == GameState.ACTIVE && 
                   BoardAnalyzer.calculateCoverage(board) < targetCoverage && 
                   moves < maxMoves) {
                
                int[] tile = BoardAnalyzer.findFirstUnrevealedTile(board);
                if (tile == null) break;

                PlayerMove move = new PlayerMove(tile[0], tile[1], PlayerMove.ActionType.REVEAL);
                MoveResult result = controller.processPlayerMove(move);

                if (result.isSuccess()) {
                    moves++;
                } else if (result.getOutcome() != MoveResult.Outcome.MOVE_EXECUTION_FAILED) {
                    break;
                }
            }
        }

        /**
         * Play game until it ends
         */
        public static void playToCompletion(GameController controller) {
            Board board = controller.getBoard();
            int maxMoves = 1000;
            int moves = 0;

            while (controller.getGameState() == GameState.ACTIVE && moves < maxMoves) {
                int[] tile = BoardAnalyzer.findFirstUnrevealedTile(board);
                if (tile == null) break;

                PlayerMove move = new PlayerMove(tile[0], tile[1], PlayerMove.ActionType.REVEAL);
                MoveResult result = controller.processPlayerMove(move);

                if (result.isSuccess()) {
                    moves++;
                } else if (result.getOutcome() != MoveResult.Outcome.MOVE_EXECUTION_FAILED) {
                    break;
                }
            }
        }

        /**
         * Flag a specific percentage of unrevealed tiles
         */
        public static void flagPercentage(GameController controller, double percentage) {
            Board board = controller.getBoard();
            int flagCount = 0;
            int targetFlags = (int) (BoardAnalyzer.countUnrevealedTiles(board) * percentage / 100.0);

            for (int row = 0; row < board.getHeight() && flagCount < targetFlags; row++) {
                for (int col = 0; col < board.getWidth() && flagCount < targetFlags; col++) {
                    Tile tile = board.getTile(row, col);
                    if (tile.isUnrevealed() && !tile.isFlagged()) {
                        PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.FLAG);
                        MoveResult result = controller.processPlayerMove(move);
                        if (result.isSuccess()) {
                            flagCount++;
                        }
                    }
                }
            }
        }

        /**
         * Play AI for N moves
         */
        public static int playAI(GameController controller, int moveCount) {
            int movesExecuted = 0;

            while (controller.getGameState() == GameState.ACTIVE && movesExecuted < moveCount) {
                MoveResult result = controller.executeAIMove();
                if (result.isSuccess()) {
                    movesExecuted++;
                } else if (result.getOutcome() != MoveResult.Outcome.MOVE_EXECUTION_FAILED) {
                    break;
                }
            }

            return movesExecuted;
        }
    }
}
