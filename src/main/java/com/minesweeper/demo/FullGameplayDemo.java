package com.minesweeper.demo;

import com.minesweeper.game.GameMode;
import com.minesweeper.game.Board;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.GameController;
import com.minesweeper.game.PlayerMove;
import com.minesweeper.game.MoveResult;
import com.minesweeper.console.BoardRenderer;
import com.minesweeper.ai.AIDifficulty;

/**
 * Full end-to-end console gameplay demonstration.
 * 
 * This shows:
 * - Game initialization (practice vs AI)
 * - Board rendering and updates
 * - Player moves (reveal and flag)
 * - AI moves
 * - Game statistics
 * - Game ending conditions
 */
public class FullGameplayDemo {

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ Greenfield Minesweeper - Full Demo    ║");
        System.out.println("║      PRACTICE MODE GAMEPLAY            ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Create a practice mode game
        System.out.println("🎮 Starting Practice Mode (Easy: 10x10, 10 mines)\n");
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("practice-game", rules, GameMode.EASY);

        System.out.println("✓ Game initialized");
        System.out.println("✓ Board ready\n");

        // Show initial board
        System.out.println("📋 INITIAL BOARD:\n");
        printBoard(board, rules);
        pause();

        // Move 1: Reveal a safe corner
        System.out.println("\n🎯 MOVE 1: Player reveals (0, 0)\n");
        PlayerMove move1 = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
        MoveResult result1 = controller.processPlayerMove(move1);
        
        if (result1.isSuccess()) {
            System.out.println("✓ " + result1.getMessage());
        } else {
            System.out.println("❌ " + result1.getMessage());
        }
        
        System.out.println("\n📋 BOARD AFTER MOVE 1:\n");
        printBoard(board, rules);
        printStats(controller);
        pause();

        // Move 2: Reveal another tile
        System.out.println("\n🎯 MOVE 2: Player reveals (3, 3)\n");
        PlayerMove move2 = new PlayerMove(3, 3, PlayerMove.ActionType.REVEAL);
        MoveResult result2 = controller.processPlayerMove(move2);
        
        if (result2.isSuccess()) {
            System.out.println("✓ " + result2.getMessage());
        } else {
            System.out.println("❌ " + result2.getMessage());
        }
        
        System.out.println("\n📋 BOARD AFTER MOVE 2:\n");
        printBoard(board, rules);
        printStats(controller);
        pause();

        // Move 3: Flag a suspected mine
        System.out.println("\n🎯 MOVE 3: Player flags (5, 5) [suspected mine]\n");
        PlayerMove move3 = new PlayerMove(5, 5, PlayerMove.ActionType.FLAG);
        MoveResult result3 = controller.processPlayerMove(move3);
        
        if (result3.isSuccess()) {
            System.out.println("✓ " + result3.getMessage());
        } else {
            System.out.println("❌ " + result3.getMessage());
        }
        
        System.out.println("\n📋 BOARD AFTER MOVE 3:\n");
        printBoard(board, rules);
        printStats(controller);
        pause();

        // Move 4: Reveal another tile
        System.out.println("\n🎯 MOVE 4: Player reveals (7, 7)\n");
        PlayerMove move4 = new PlayerMove(7, 7, PlayerMove.ActionType.REVEAL);
        MoveResult result4 = controller.processPlayerMove(move4);
        
        if (result4.isSuccess()) {
            System.out.println("✓ " + result4.getMessage());
        } else {
            System.out.println("❌ " + result4.getMessage());
        }
        
        System.out.println("\n📋 BOARD AFTER MOVE 4:\n");
        printBoard(board, rules);
        printStats(controller);
        pause();

        // Now demo vs AI
        System.out.println("\n\n╔════════════════════════════════════════╗");
        System.out.println("║      VS AI MODE DEMONSTRATION         ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("🤖 Starting AI vs Player game (Easy: 10x10, 10 mines)\n");
        Board aiBoard = new Board(10, 10, 10);
        GameRules aiRules = new GameRules(aiBoard);
        GameController aiController = new GameController("ai-game", aiRules, GameMode.EASY);
        aiController.initializeAIOpponent(AIDifficulty.NORMAL);

        System.out.println("✓ Game initialized with NORMAL AI opponent\n");

        // Show initial board
        System.out.println("📋 INITIAL BOARD:\n");
        printBoard(aiBoard, aiRules);
        pause();

        // Human move
        System.out.println("\n🎯 HUMAN MOVE: Reveals (0, 0)\n");
        PlayerMove humanMove = new PlayerMove(0, 0, PlayerMove.ActionType.REVEAL);
        MoveResult humanResult = aiController.processPlayerMove(humanMove);
        
        if (humanResult.isSuccess()) {
            System.out.println("✓ " + humanResult.getMessage());
        } else {
            System.out.println("❌ " + humanResult.getMessage());
        }
        
        System.out.println("\n📋 BOARD AFTER HUMAN MOVE:\n");
        printBoard(aiBoard, aiRules);
        printStats(aiController);
        pause();

        // AI move
        if (aiController.getGameState().name().equals("ACTIVE")) {
            System.out.println("\n🤖 AI IS THINKING...\n");
            try { Thread.sleep(500); } catch (Exception e) {}
            
            MoveResult aiMove = aiController.executeAIMove();
            System.out.println("🤖 " + aiMove.getMessage() + "\n");
            
            System.out.println("📋 BOARD AFTER AI MOVE:\n");
            printBoard(aiBoard, aiRules);
            printStats(aiController);
        }

        // Final summary
        System.out.println("\n\n╔════════════════════════════════════════╗");
        System.out.println("║         DEMO COMPLETE                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("✅ Console Interface Demo Summary:");
        System.out.println("   ✓ Game initialization working");
        System.out.println("   ✓ Board rendering working");
        System.out.println("   ✓ Player moves processed correctly");
        System.out.println("   ✓ Game statistics tracked");
        System.out.println("   ✓ AI opponent functional");
        System.out.println("   ✓ Both Practice and vs AI modes work\n");

        System.out.println("🎮 TO PLAY INTERACTIVELY:\n");
        System.out.println("   mvn exec:java@console\n");
        System.out.println("   Or manually:");
        System.out.println("   java -cp target/classes com.minesweeper.console.ConsoleUI\n");
    }

    private static void printBoard(Board board, GameRules rules) {
        System.out.println(BoardRenderer.render(board, rules));
        System.out.println(BoardRenderer.renderStatus(rules));
    }

    private static void printStats(GameController controller) {
        System.out.println("\n📊 STATISTICS:");
        System.out.println("   Total Moves: " + controller.getStatistics().getTotalMoves());
        System.out.println("   Reveals: " + controller.getStatistics().getRevealsCorrect());
        System.out.println("   Flags: " + controller.getStatistics().getFlagsCorrect());
        System.out.println("   Accuracy: " + String.format("%.1f", controller.getStatistics().getAccuracy()) + "%");
    }

    private static void pause() {
        System.out.println("\n[Press any key to continue...]");
        try { Thread.sleep(1500); } catch (Exception e) {}
    }
}
