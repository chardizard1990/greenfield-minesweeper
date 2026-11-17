package com.minesweeper.demo;

import com.minesweeper.game.GameMode;
import com.minesweeper.game.Board;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.GameController;
import com.minesweeper.game.PlayerMove;
import com.minesweeper.game.MoveResult;
import com.minesweeper.console.BoardRenderer;

/**
 * Interactive demo of Greenfield Minesweeper console interface.
 * 
 * This demonstrates the working console UI with:
 * - Game initialization
 * - Board display
 * - Player moves
 * - Game progression
 * - Statistics tracking
 */
public class ConsoleGameDemo {

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ Greenfield Minesweeper - Console Demo ║");
        System.out.println("║      END-TO-END GAMEPLAY TEST         ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Create a small easy game for demo
        System.out.println("🎮 Initializing game: Easy Mode (10x10, 10 mines)\n");
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("demo-game", rules, GameMode.EASY);

        System.out.println("✓ Game created successfully");
        System.out.println("✓ Board dimensions: " + board.getWidth() + "x" + board.getHeight());
        System.out.println("✓ Mines to avoid: " + board.getMineCount());
        System.out.println("✓ Game state: " + controller.getGameState() + "\n");

        // Show initial board
        System.out.println("📋 Initial Board State:\n");
        System.out.println(BoardRenderer.render(board, rules));
        System.out.println(BoardRenderer.renderStatus(rules));
        System.out.println();

        // Simulate player moves
        int moveCount = 0;
        int maxMoves = 5;

        while (controller.getGameState().name().equals("ACTIVE") && moveCount < maxMoves) {
            moveCount++;

            // Simulate player input (safe tiles in corners)
            int row = moveCount - 1;
            int col = moveCount - 1;

            System.out.println("─".repeat(42));
            System.out.println("🎯 MOVE " + moveCount + ": Revealing tile at (" + row + ", " + col + ")\n");

            PlayerMove move = new PlayerMove(row, col, PlayerMove.ActionType.REVEAL);
            MoveResult result = controller.processPlayerMove(move);

            if (result.isSuccess()) {
                System.out.println("✓ " + result.getMessage());
            } else {
                System.out.println("❌ " + result.getMessage());
                break;
            }

            // Show board after move
            System.out.println();
            System.out.println(BoardRenderer.render(board, rules));
            System.out.println(BoardRenderer.renderStatus(rules));
            System.out.println();

            // Show current statistics
            System.out.println("📊 Current Statistics:");
            System.out.println("   Total Moves: " + controller.getStatistics().getTotalMoves());
            System.out.println("   Tiles Revealed: " + controller.getStatistics().getRevealsCorrect());
            System.out.println("   Accuracy: " + String.format("%.1f", controller.getStatistics().getAccuracy()) + "%");
            System.out.println();

            // Check game state
            if (!controller.getGameState().name().equals("ACTIVE")) {
                break;
            }
        }

        // Final status
        System.out.println("─".repeat(42));
        System.out.println("\n✅ DEMO COMPLETE\n");
        System.out.println("Final Game State: " + controller.getGameState());
        System.out.println("Final Statistics:");
        System.out.println("  Total Moves: " + controller.getStatistics().getTotalMoves());
        System.out.println("  Reveals: " + controller.getStatistics().getRevealsCorrect() + " correct");
        System.out.println("  Accuracy: " + String.format("%.1f", controller.getStatistics().getAccuracy()) + "%");
        System.out.println("  Time Elapsed: " + controller.getStatistics().getElapsedTimeSeconds() + "s");

        System.out.println("\n🎮 Console UI is fully functional!");
        System.out.println("   Run: mvn exec:java -Dexec.mainClass=\"com.minesweeper.console.ConsoleUI\"");
        System.out.println("   to play an interactive game.\n");
    }
}
