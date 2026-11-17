package com.minesweeper.ai;

import com.minesweeper.game.Board;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Easy difficulty AI strategy for Minesweeper.
 * 
 * Strategy:
 * - Flags tiles identified as definitely mines (all adjacent unrevealed tiles when adjacent mine count is met)
 * - Randomly selects among unrevealed safe tiles
 * - Starts with center tile to maximize information gain
 * - Falls back to random when board analysis provides no insights
 */
public class EasyAIStrategy implements AIStrategy {

    private final Random random;
    private boolean hasStarted = false;

    public EasyAIStrategy() {
        this.random = new Random();
    }

    public EasyAIStrategy(Random random) {
        this.random = random;
    }

    @Override
    public AIDifficulty getDifficulty() {
        return AIDifficulty.EASY;
    }

    @Override
    public Optional<AIMove> getNextMove(GameRules gameRules) {
        Board board = gameRules.getBoard();

        // First move: Start from center to maximize information
        if (!hasStarted) {
            hasStarted = true;
            int centerRow = board.getHeight() / 2;
            int centerCol = board.getWidth() / 2;
            return Optional.of(new AIMove(
                    centerRow, centerCol,
                    MoveType.REVEAL,
                    "Opening move - center tile for maximum information",
                    1.0
            ));
        }

        // Try to flag definite mines
        Optional<AIMove> flagMove = findDefiniteMineToFlag(board);
        if (flagMove.isPresent()) {
            return flagMove;
        }

        // Try to find random unrevealed safe tile
        return findRandomUnrevealedTile(board);
    }

    /**
     * Finds a tile that is definitely a mine based on adjacent bomb counts.
     * A tile is definitely a mine when: all unrevealed adjacent tiles must be mines
     * because the adjacent bomb count equals the number of unrevealed adjacent tiles.
     * 
     * @param board the current board
     * @return Optional containing a flag move, or empty if none found
     */
    private Optional<AIMove> findDefiniteMineToFlag(Board board) {
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);

                // Only analyze revealed tiles with mine count information
                if (!tile.isRevealed()) {
                    continue;
                }

                int adjacentMineCount = board.getAdjacentMineCount(row, col);
                if (adjacentMineCount == 0) {
                    continue; // No adjacent mines to find
                }

                List<Tile> adjacentTiles = board.getAdjacentTiles(row, col);
                List<Tile> unrevealedTiles = new ArrayList<>();
                List<Tile> flaggedTiles = new ArrayList<>();

                for (Tile adjacent : adjacentTiles) {
                    if (adjacent.isUnrevealed()) {
                        unrevealedTiles.add(adjacent);
                    } else if (adjacent.isFlagged()) {
                        flaggedTiles.add(adjacent);
                    }
                }

                // If unrevealed count + flagged count equals adjacent mine count,
                // all unrevealed tiles must be mines
                if (flaggedTiles.size() + unrevealedTiles.size() == adjacentMineCount) {
                    if (!unrevealedTiles.isEmpty()) {
                        Tile mineToFlag = unrevealedTiles.get(0);
                        return Optional.of(new AIMove(
                                mineToFlag.getRow(), mineToFlag.getColumn(),
                                MoveType.FLAG,
                                String.format(
                                        "Definite mine - all adjacent unrevealed tiles must be mines for tile at (%d,%d)",
                                        row, col
                                ),
                                1.0
                        ));
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a random unrevealed tile to reveal.
     * This is the fallback strategy when no definite mines can be identified.
     * 
     * @param board the current board
     * @return Optional containing a reveal move, or empty if no tiles available
     */
    private Optional<AIMove> findRandomUnrevealedTile(Board board) {
        List<Tile> unrevealedTiles = new ArrayList<>();

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);
                if (tile.isUnrevealed()) {
                    unrevealedTiles.add(tile);
                }
            }
        }

        if (unrevealedTiles.isEmpty()) {
            return Optional.empty();
        }

        Tile selected = unrevealedTiles.get(random.nextInt(unrevealedTiles.size()));
        return Optional.of(new AIMove(
                selected.getRow(), selected.getColumn(),
                MoveType.REVEAL,
                "Random unrevealed tile",
                0.5
        ));
    }
}
