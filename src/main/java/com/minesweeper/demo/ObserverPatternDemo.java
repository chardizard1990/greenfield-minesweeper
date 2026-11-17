package com.minesweeper.demo;

import com.minesweeper.game.GameMode;
import com.minesweeper.game.Board;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.GameController;
import com.minesweeper.observer.GameEventBroadcaster;
import com.minesweeper.observer.ConsoleGameSpectator;
import com.minesweeper.ai.AIDifficulty;

/**
 * Demonstrates the Observer pattern in action.
 * 
 * Shows how spectators can watch and analyze AI gameplay.
 */
public class ObserverPatternDemo {

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ Greenfield Minesweeper - Observer Demo ║");
        System.out.println("║      SPECTATING AI GAMEPLAY            ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Setup observer infrastructure
        GameEventBroadcaster broadcaster = new GameEventBroadcaster();
        ConsoleGameSpectator spectator = new ConsoleGameSpectator("Console-Spectator", true);
        broadcaster.subscribe(spectator);

        System.out.println("👁️  Spectator attached and observing game events\n");

        // Create game
        System.out.println("🎮 Creating game: Easy Mode (10x10, 10 mines)\n");
        Board board = new Board(10, 10, 10);
        GameRules rules = new GameRules(board);
        GameController controller = new GameController("ai-game-1", rules, GameMode.EASY);
        controller.initializeAIOpponent(AIDifficulty.NORMAL);

        System.out.println("✓ Game initialized with NORMAL AI opponent\n");

        // Simulate AI moves with observer events
        System.out.println("═".repeat(42));
        System.out.println("SIMULATING AI GAMEPLAY WITH EVENT NOTIFICATIONS");
        System.out.println("═".repeat(42) + "\n");

        // Move 1
        simulateMove(broadcaster, "AI-Player", 0, 0, "REVEAL", "Opening in corner for maximum safe space");
        pause();

        // Move 2
        simulateMove(broadcaster, "AI-Player", 5, 5, "REVEAL", "Strategic center position");
        pause();

        // Move 3
        simulateMove(broadcaster, "AI-Player", 7, 3, "FLAG", "Definite mine - probability analysis shows 100% confidence");
        pause();

        // Move 4
        simulateMove(broadcaster, "AI-Player", 9, 9, "REVEAL", "Corner exploration");
        pause();

        // End game
        System.out.println("\n🎮 Game State: ACTIVE → WON\n");
        broadcaster.broadcastGameEnded("AI-Player", "All safe tiles revealed successfully");

        // Display move history
        System.out.println("\n");
        spectator.displayMoveHistory();

        // Summary
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         OBSERVER DEMO SUMMARY           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("✅ Observer Pattern Features Demonstrated:");
        System.out.println("   • Multiple observers can listen to events");
        System.out.println("   • Events are broadcast to all subscribers");
        System.out.println("   • Move history is tracked with reasoning");
        System.out.println("   • Game state changes are observable");
        System.out.println("   • AI analysis steps can be logged");
        System.out.println("   • Spectators can analyze AI decisions\n");

        System.out.println("📊 Event Statistics:");
        System.out.println("   Total Moves Recorded: " + spectator.getMoveCount());
        System.out.println("   Move History Size: " + spectator.getMoveHistory().size());
        System.out.println("   Observers Subscribed: 1 (spectator)\n");

        System.out.println("💡 Use Cases:");
        System.out.println("   • Logging all game events to file");
        System.out.println("   • Replay system (can replay from move history)");
        System.out.println("   • Analysis tools (understand AI decisions)");
        System.out.println("   • Leaderboards (track AI performance)");
        System.out.println("   • Multiple UIs (console, GUI, web) watching same game\n");
    }

    private static void simulateMove(GameEventBroadcaster broadcaster, String playerId, 
                                      int row, int col, String action, String reasoning) {
        System.out.println("📍 Move: " + action + " at (" + row + ", " + col + ")");
        broadcaster.broadcastPlayerMove(playerId, row, col, action, reasoning);
        
        // Simulate tile reveal event
        if (action.equals("REVEAL")) {
            broadcaster.broadcastTileRevealed(row, col, row % 3, false); // Mock adjacent mines
            System.out.println("   ✓ Tile revealed\n");
        } else {
            broadcaster.broadcastTileFlagged(row, col, true);
            System.out.println("   ✓ Tile flagged\n");
        }
    }

    private static void pause() {
        try { Thread.sleep(800); } catch (Exception e) {}
    }
}
