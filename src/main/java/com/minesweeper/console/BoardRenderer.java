package com.minesweeper.console;

/**
 * Board renderer for console display.
 * 
 * Renders board state with appropriate symbols:
 * - '?' = unrevealed tile
 * - ' ' = revealed empty tile (0 adjacent mines)
 * - '1-8' = revealed tile with adjacent mine count
 * - 'F' = flagged tile
 * - '*' = mine (shown after game over)
 */
public class BoardRenderer {

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";

    /**
     * Renders the board to a string.
     * 
     * @param board the board to render
     * @param gameRules the game rules (for checking if game is finished)
     * @return formatted board string ready for console display
     */
    public static String render(com.minesweeper.game.Board board, com.minesweeper.game.GameRules gameRules) {
        StringBuilder sb = new StringBuilder();
        
        // Header with column numbers
        sb.append("    ");
        for (int col = 0; col < board.getWidth(); col++) {
            sb.append(String.format("%3d", col));
        }
        sb.append("\n");

        // Separator
        sb.append("   +");
        for (int col = 0; col < board.getWidth(); col++) {
            sb.append("---+");
        }
        sb.append("\n");

        // Board rows
        for (int row = 0; row < board.getHeight(); row++) {
            sb.append(String.format("%2d |", row));

            for (int col = 0; col < board.getWidth(); col++) {
                com.minesweeper.game.Tile tile = board.getTile(row, col);
                sb.append(" ").append(getTileSymbol(tile, board, gameRules)).append(" |");
            }

            sb.append("\n");

            // Row separator
            sb.append("   +");
            for (int col = 0; col < board.getWidth(); col++) {
                sb.append("---+");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Gets the display symbol for a tile.
     * 
     * @param tile the tile to render
     * @param board the board (for adjacent mine counting)
     * @param gameRules the game rules (for checking game state)
     * @return single character representing tile state
     */
    private static String getTileSymbol(com.minesweeper.game.Tile tile,
                                       com.minesweeper.game.Board board,
                                       com.minesweeper.game.GameRules gameRules) {
        // Flagged tiles show 'F'
        if (tile.isFlagged()) {
            return colorize("F", YELLOW);
        }

        // Unrevealed tiles show '?'
        if (tile.isUnrevealed()) {
            return "?";
        }

        // Game over: show mines as '*'
        if (gameRules.getGameState() == com.minesweeper.game.GameState.LOST) {
            if (tile.isMine()) {
                return colorize("*", RED);
            }
        }

        // Revealed tile: show adjacent mine count
        int adjacentMines = board.getAdjacentMineCount(tile.getRow(), tile.getColumn());
        if (adjacentMines == 0) {
            return " "; // Empty space for 0 mines
        } else {
            String number = String.valueOf(adjacentMines);
            // Color high mine counts (7-8) as red
            if (adjacentMines >= 7) {
                return colorize(number, RED);
            } else if (adjacentMines >= 5) {
                return colorize(number, YELLOW);
            }
            return number;
        }
    }

    /**
     * Applies ANSI color code to text.
     * 
     * @param text the text to colorize
     * @param color the ANSI color code
     * @return colored text
     */
    private static String colorize(String text, String color) {
        return color + text + RESET;
    }

    /**
     * Renders a simple status line with game information.
     * 
     * @param gameRules the game rules
     * @return formatted status string
     */
    public static String renderStatus(com.minesweeper.game.GameRules gameRules) {
        com.minesweeper.game.Board board = gameRules.getBoard();
        return String.format(
                "State: %s | Unrevealed: %d | Flagged: %d | Remaining: %d",
                gameRules.getGameState(),
                board.getUnrevealedTileCount(),
                board.getFlaggedTileCount(),
                gameRules.getRemainingMines()
        );
    }
}
