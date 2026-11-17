package com.minesweeper.observer;

import com.minesweeper.game.GameState;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Console implementation of GameObserver for spectating games.
 * 
 * Features:
 * - Displays AI moves in real-time with reasoning
 * - Tracks move history
 * - Shows game state changes
 * - Displays AI analysis steps
 * - Formats output for console readability
 */
public class ConsoleGameSpectator implements GameObserver {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    private final String spectatorId;
    private final List<MoveRecord> moveHistory = new ArrayList<>();
    private boolean verbose;
    private int moveCount = 0;

    /**
     * Creates a new console spectator.
     * 
     * @param spectatorId identifier for this spectator
     */
    public ConsoleGameSpectator(String spectatorId) {
        this.spectatorId = spectatorId;
        this.verbose = true;
    }

    /**
     * Creates a new console spectator with verbosity control.
     * 
     * @param spectatorId identifier for this spectator
     * @param verbose whether to print detailed output
     */
    public ConsoleGameSpectator(String spectatorId, boolean verbose) {
        this.spectatorId = spectatorId;
        this.verbose = verbose;
    }

    @Override
    public void onTileRevealed(int row, int col, int adjacentMines, boolean isMine) {
        if (isMine && verbose) {
            System.out.println("💣 MINE HIT at (" + row + ", " + col + ")");
        }
    }

    @Override
    public void onTileFlagged(int row, int col, boolean flagged) {
        // Recorded in move history
    }

    @Override
    public void onGameStateChanged(GameState newState) {
        if (verbose) {
            System.out.println("\n🎮 Game State: " + newState);
        }
    }

    @Override
    public void onPlayerMove(String playerId, int row, int col, String actionType, String reasoning) {
        moveCount++;
        
        MoveRecord record = new MoveRecord(
            moveCount,
            playerId,
            row,
            col,
            actionType,
            reasoning,
            LocalDateTime.now()
        );
        moveHistory.add(record);

        if (verbose) {
            System.out.println("\n📍 Move #" + moveCount + ": " + playerId);
            System.out.println("   Position: (" + row + ", " + col + ")");
            System.out.println("   Action: " + actionType);
            if (reasoning != null && !reasoning.isEmpty()) {
                System.out.println("   Reasoning: " + reasoning);
            }
        }
    }

    @Override
    public void onGameEnded(String winner, String reason) {
        if (verbose) {
            System.out.println("\n╔════════════════════════════════════════╗");
            if (winner != null) {
                String winMsg = "🎉 " + winner + " WINS!";
                System.out.println("║ " + winMsg);
            } else {
                System.out.println("║ 💥 GAME OVER");
            }
            System.out.println("║ " + reason);
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("\nTotal Moves: " + moveCount);
        }
    }

    @Override
    public void onAIAnalysis(String analysisStep) {
        if (verbose) {
            System.out.println("🤖 Analyzing: " + analysisStep);
        }
    }

    /**
     * Displays move history in formatted table.
     */
    public void displayMoveHistory() {
        System.out.println("\n┌─ MOVE HISTORY ──────────────────────────┐");
        System.out.println("│ # │ Player │ Position │ Action │ Reasoning");
        System.out.println("├───┼────────┼──────────┼────────┼─────────────");
        
        for (MoveRecord record : moveHistory) {
            String position = "(" + record.row + "," + record.col + ")";
            String player = record.playerId.substring(0, Math.min(6, record.playerId.length()));
            String action = record.actionType.substring(0, Math.min(6, record.actionType.length()));
            String reasoning = record.reasoning != null ? record.reasoning : "-";
            if (reasoning.length() > 20) {
                reasoning = reasoning.substring(0, 17) + "...";
            }
            
            System.out.printf("│ %2d│ %-6s │ %8s │ %-6s │ %s%n", 
                record.moveNumber, player, position, action, reasoning);
        }
        
        System.out.println("└───┴────────┴──────────┴────────┴─────────────");
    }

    /**
     * Gets the total number of moves recorded.
     * 
     * @return move count
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Gets a copy of the move history.
     * 
     * @return list of move records
     */
    public List<MoveRecord> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }

    /**
     * Clears the move history.
     */
    public void clearHistory() {
        moveHistory.clear();
        moveCount = 0;
    }

    /**
     * Sets verbosity for this spectator.
     * 
     * @param verbose true for detailed output, false for silent
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Record of a single move.
     */
    public static class MoveRecord {
        public final int moveNumber;
        public final String playerId;
        public final int row;
        public final int col;
        public final String actionType;
        public final String reasoning;
        public final LocalDateTime timestamp;

        MoveRecord(int moveNumber, String playerId, int row, int col, String actionType, 
                   String reasoning, LocalDateTime timestamp) {
            this.moveNumber = moveNumber;
            this.playerId = playerId;
            this.row = row;
            this.col = col;
            this.actionType = actionType;
            this.reasoning = reasoning;
            this.timestamp = timestamp;
        }
    }

}
