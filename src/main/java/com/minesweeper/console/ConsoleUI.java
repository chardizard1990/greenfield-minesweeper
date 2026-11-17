package com.minesweeper.console;

import com.minesweeper.game.GameMode;
import com.minesweeper.game.Board;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.GameController;
import com.minesweeper.game.PlayerMove;
import com.minesweeper.game.MoveResult;
import com.minesweeper.ai.AIDifficulty;

import java.util.Scanner;

/**
 * Console-based user interface for Minesweeper.
 * 
 * Provides:
 * - Interactive menu system
 * - Board display and updates
 * - Player input handling
 * - Game statistics display
 * - Full game loop management
 */
public class ConsoleUI {

    private final Scanner scanner;
    private GameController gameController;
    private boolean playingVsAI;
    private long gameStartTime;

    /**
     * Creates a new console UI.
     */
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.playingVsAI = false;
    }

    /**
     * Starts the main application loop.
     */
    public void start() {
        printWelcome();

        boolean running = true;
        while (running) {
            int choice = showMainMenu();

            switch (choice) {
                case 1:
                    playGame();
                    break;
                case 2:
                    playGameVsAI();
                    break;
                case 3:
                    printInstructions();
                    break;
                case 4:
                    running = false;
                    printGoodbye();
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.\n");
            }
        }

        scanner.close();
    }

    /**
     * Displays the welcome message.
     */
    private void printWelcome() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   Greenfield Minesweeper - Console MVP  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    /**
     * Displays the main menu and returns user choice.
     * 
     * @return menu choice (1-4)
     */
    private int showMainMenu() {
        System.out.println("┌─ Main Menu ─────────────────────────────┐");
        System.out.println("│ 1. Play vs AI                           │");
        System.out.println("│ 2. Play Practice Mode (No AI)           │");
        System.out.println("│ 3. How to Play                          │");
        System.out.println("│ 4. Exit                                 │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("\nChoice (1-4): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return choice;
        } catch (NumberFormatException e) {
            return -1; // Invalid input
        }
    }

    /**
     * Displays difficulty selection menu.
     * 
     * @return selected GameMode
     */
    private GameMode showDifficultyMenu() {
        System.out.println("\n┌─ Difficulty Selection ──────────────────┐");
        System.out.println("│ 1. Easy   (10x10, 10 mines)             │");
        System.out.println("│ 2. Medium (20x20, 75 mines)             │");
        System.out.println("│ 3. Hard   (30x30, 150 mines)            │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("\nChoice (1-3): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1:
                    return GameMode.EASY;
                case 2:
                    return GameMode.MEDIUM;
                case 3:
                    return GameMode.HARD;
                default:
                    return GameMode.EASY;
            }
        } catch (NumberFormatException e) {
            return GameMode.EASY;
        }
    }

    /**
     * Displays AI difficulty selection menu.
     * 
     * @return selected AI difficulty
     */
    private AIDifficulty showAIDifficultyMenu() {
        System.out.println("\n┌─ AI Difficulty ─────────────────────────┐");
        System.out.println("│ 1. Easy   (Random moves)                │");
        System.out.println("│ 2. Normal (Strategic analysis)          │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("\nChoice (1-2): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1:
                    return AIDifficulty.EASY;
                case 2:
                    return AIDifficulty.NORMAL;
                default:
                    return AIDifficulty.NORMAL;
            }
        } catch (NumberFormatException e) {
            return AIDifficulty.NORMAL;
        }
    }

    /**
     * Main game loop for single-player practice mode.
     */
    private void playGame() {
        GameMode mode = showDifficultyMenu();
        initializeGame(mode, false, null);

        gameLoop();
    }

    /**
     * Main game loop for vs AI mode.
     */
    private void playGameVsAI() {
        GameMode mode = showDifficultyMenu();
        AIDifficulty aiDifficulty = showAIDifficultyMenu();
        initializeGame(mode, true, aiDifficulty);

        gameLoop();
    }

    /**
     * Initializes a new game with the specified settings.
     * 
     * @param mode the game difficulty mode
     * @param vsAI whether playing against AI
     * @param aiDifficulty AI difficulty (null if not vs AI)
     */
    private void initializeGame(GameMode mode, boolean vsAI, AIDifficulty aiDifficulty) {
        Board board = new Board(mode.getWidth(), mode.getHeight(), mode.getMineCount());
        GameRules rules = new GameRules(board);
        gameController = new GameController("console-game", rules, mode);

        if (vsAI && aiDifficulty != null) {
            gameController.initializeAIOpponent(aiDifficulty);
            playingVsAI = true;
            System.out.println("\n✓ Game started: " + mode + " vs AI (" + aiDifficulty + ")\n");
        } else {
            playingVsAI = false;
            System.out.println("\n✓ Game started: " + mode + " (Practice Mode)\n");
        }

        gameStartTime = System.currentTimeMillis();
    }

    /**
     * Main game loop - handles player turns and game flow.
     */
    private void gameLoop() {
        while (gameController.getGameState() == com.minesweeper.game.GameState.ACTIVE) {
            // Display board
            System.out.println(BoardRenderer.render(gameController.getBoard(), gameController.getGameRules()));
            System.out.println(BoardRenderer.renderStatus(gameController.getGameRules()));

            // Get player input
            PlayerMove move = getPlayerInput();
            if (move == null) {
                continue;
            }

            // Process move
            MoveResult result = gameController.processPlayerMove(move);

            System.out.println();
            if (result.isSuccess()) {
                System.out.println("✓ " + result.getMessage());
            } else {
                System.out.println("❌ " + result.getMessage());
            }

            System.out.println();

            // AI turn (if vs AI and game still active)
            if (playingVsAI && gameController.getGameState() == com.minesweeper.game.GameState.ACTIVE) {
                try {
                    Thread.sleep(500); // Brief pause before AI move
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("🤖 AI is thinking...");
                MoveResult aiResult = gameController.executeAIMove();
                System.out.println("🤖 " + aiResult.getMessage() + "\n");
            }
        }

        // Game ended
        displayGameEnd();
    }

    /**
     * Gets player input for the next move.
     * Handles row, column, and action type input.
     * 
     * @return PlayerMove if valid input, null if invalid
     */
    private PlayerMove getPlayerInput() {
        System.out.print("Enter row (or 'q' to quit): ");
        String rowInput = scanner.nextLine().trim();

        if (rowInput.equalsIgnoreCase("q")) {
            System.out.println("\n👋 Game abandoned.\n");
            return null;
        }

        try {
            int row = Integer.parseInt(rowInput);
            System.out.print("Enter column: ");
            int column = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Action - (R)eveal or (F)lag? ");
            String action = scanner.nextLine().trim().toUpperCase();

            PlayerMove.ActionType actionType;
            if (action.equals("R")) {
                actionType = PlayerMove.ActionType.REVEAL;
            } else if (action.equals("F")) {
                actionType = PlayerMove.ActionType.FLAG;
            } else {
                System.out.println("❌ Invalid action. Use R or F.\n");
                return null;
            }

            return new PlayerMove(row, column, actionType);

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter numbers.\n");
            return null;
        }
    }

    /**
     * Displays the game end screen and statistics.
     */
    private void displayGameEnd() {
        System.out.println("\n" + BoardRenderer.render(gameController.getBoard(), gameController.getGameRules()));

        // Show result
        if (gameController.getGameState() == com.minesweeper.game.GameState.WON) {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║           🎉 YOU WON! 🎉               ║");
            System.out.println("╚════════════════════════════════════════╝\n");
        } else {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║           💥 GAME OVER 💥               ║");
            System.out.println("╚════════════════════════════════════════╝\n");
        }

        // Display statistics
        displayStatistics();
        System.out.println();
    }

    /**
     * Displays detailed game statistics.
     */
    private void displayStatistics() {
        com.minesweeper.game.GameStatistics stats = gameController.getStatistics();

        System.out.println("┌─ Statistics ────────────────────────────┐");
        System.out.println(String.format("│ Total Moves:        %2d", stats.getTotalMoves()));
        System.out.println(String.format("│ Flags Placed:       %2d (Correct: %d)", 
                stats.getFlagsPlaced(), stats.getFlagsCorrect()));
        System.out.println(String.format("│ Reveals Made:       %2d (Correct: %d)", 
                stats.getRevealsMade(), stats.getRevealsCorrect()));
        System.out.println(String.format("│ Accuracy:          %.1f%%", stats.getAccuracy()));
        System.out.println(String.format("│ Time Elapsed:        %ds", stats.getElapsedTimeSeconds()));
        System.out.println("└─────────────────────────────────────────┘");
    }

    /**
     * Displays how to play instructions.
     */
    private void printInstructions() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           How to Play                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("OBJECTIVE:");
        System.out.println("  Reveal all non-mine tiles without hitting a mine.\n");

        System.out.println("BOARD SYMBOLS:");
        System.out.println("  ?  = Unrevealed tile");
        System.out.println("  F  = Flagged tile (you marked as mine)");
        System.out.println("     = Revealed safe tile (no adjacent mines)");
        System.out.println("  1-8= Revealed tile with adjacent mine count");
        System.out.println("  *  = Mine (shown when game ends)\n");

        System.out.println("HOW TO PLAY:");
        System.out.println("  1. Enter the row number (0-based)");
        System.out.println("  2. Enter the column number (0-based)");
        System.out.println("  3. Choose action: Reveal (R) or Flag (F)\n");

        System.out.println("STRATEGY:");
        System.out.println("  - Use the numbers to deduce safe tiles");
        System.out.println("  - Flag tiles you think are mines");
        System.out.println("  - If revealed tile shows 0, all adjacent are safe\n");

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Displays goodbye message.
     */
    private void printGoodbye() {
        System.out.println("\n👋 Thanks for playing Greenfield Minesweeper!");
        System.out.println("See you next time!\n");
    }

    /**
     * Main entry point for console application.
     * 
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}
