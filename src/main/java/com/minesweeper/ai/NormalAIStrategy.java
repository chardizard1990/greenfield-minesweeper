package com.minesweeper.ai;

import com.minesweeper.game.Board;
import com.minesweeper.game.GameRules;
import com.minesweeper.game.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * Normal difficulty AI strategy for Minesweeper.
 * 
 * Strategy:
 * - Flags tiles identified as definitely mines
 * - Reveals tiles identified as definitely safe
 * - Uses logical deduction: analyzes patterns from adjacent bomb counts
 * - Randomly selects among equally-safe options when no definite moves
 * - Starts with center tile for maximum information gain
 */
public class NormalAIStrategy implements AIStrategy {

    private final Random random;
    private boolean hasStarted = false;

    public NormalAIStrategy() {
        this.random = new Random();
    }

    public NormalAIStrategy(Random random) {
        this.random = random;
    }

    @Override
    public AIDifficulty getDifficulty() {
        return AIDifficulty.NORMAL;
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

        // Priority 1: Flag definite mines
        Optional<AIMove> flagMove = findDefiniteMineToFlag(board);
        if (flagMove.isPresent()) {
            return flagMove;
        }

        // Priority 2: Reveal definite safe tiles
        Optional<AIMove> safeMove = findDefiniteSafeToReveal(board);
        if (safeMove.isPresent()) {
            return safeMove;
        }

        // Priority 3: Find logically deduced moves
        Optional<AIMove> deducedMove = findDeducedMove(board);
        if (deducedMove.isPresent()) {
            return deducedMove;
        }

        // Fallback: Random unrevealed tile
        return findRandomUnrevealedTile(board);
    }

    /**
     * Finds a tile that is definitely a mine based on adjacent bomb counts.
     * 
     * @param board the current board
     * @return Optional containing a flag move, or empty if none found
     */
    private Optional<AIMove> findDefiniteMineToFlag(Board board) {
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);

                if (!tile.isRevealed()) {
                    continue;
                }

                int adjacentMineCount = board.getAdjacentMineCount(row, col);
                if (adjacentMineCount == 0) {
                    continue;
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

                // All unrevealed must be mines if flagged + unrevealed count matches mine count
                if (flaggedTiles.size() + unrevealedTiles.size() == adjacentMineCount) {
                    if (!unrevealedTiles.isEmpty()) {
                        Tile mineToFlag = unrevealedTiles.get(0);
                        return Optional.of(new AIMove(
                                mineToFlag.getRow(), mineToFlag.getColumn(),
                                MoveType.FLAG,
                                String.format(
                                        "Definite mine - all adjacent unrevealed tiles must be mines for revealed tile at (%d,%d)",
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
     * Finds a tile that is definitely safe.
     * A tile is definitely safe when: a revealed tile already has all its adjacent mines flagged,
     * so all remaining unrevealed adjacent tiles must be safe.
     * 
     * @param board the current board
     * @return Optional containing a reveal move, or empty if none found
     */
    private Optional<AIMove> findDefiniteSafeToReveal(Board board) {
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);

                if (!tile.isRevealed()) {
                    continue;
                }

                int adjacentMineCount = board.getAdjacentMineCount(row, col);
                List<Tile> adjacentTiles = board.getAdjacentTiles(row, col);

                int flaggedCount = 0;
                Tile firstUnrevealed = null;

                for (Tile adjacent : adjacentTiles) {
                    if (adjacent.isFlagged()) {
                        flaggedCount++;
                    } else if (adjacent.isUnrevealed() && firstUnrevealed == null) {
                        firstUnrevealed = adjacent;
                    }
                }

                // If all adjacent mines are flagged, unrevealed tiles are safe
                if (flaggedCount == adjacentMineCount && firstUnrevealed != null) {
                    return Optional.of(new AIMove(
                            firstUnrevealed.getRow(), firstUnrevealed.getColumn(),
                            MoveType.REVEAL,
                            String.format(
                                    "Definitely safe - all adjacent mines flagged for revealed tile at (%d,%d)",
                                    row, col
                            ),
                            1.0
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds moves based on logical deduction from board patterns.
     * Analyzes constraints and probabilities to identify moves with high confidence.
     * 
     * @param board the current board
     * @return Optional containing a deduced move, or empty if none found with high confidence
     */
    private Optional<AIMove> findDeducedMove(Board board) {
        List<AIMove> goodMoves = new ArrayList<>();

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);

                if (!tile.isUnrevealed()) {
                    continue;
                }

                // Count how many revealed neighbors with mine information
                List<Tile> adjacentTiles = board.getAdjacentTiles(row, col);
                Set<Integer> possibleAdjacentMines = new HashSet<>();

                for (Tile neighbor : adjacentTiles) {
                    if (neighbor.isRevealed()) {
                        int mineCount = board.getAdjacentMineCount(neighbor.getRow(), neighbor.getColumn());
                        possibleAdjacentMines.add(mineCount);
                    }
                }

                // If all adjacent revealed tiles have 0 adjacent mines, this tile is safe
                if (possibleAdjacentMines.size() == 1 && possibleAdjacentMines.contains(0)) {
                    goodMoves.add(new AIMove(
                            row, col,
                            MoveType.REVEAL,
                            "Adjacent to 0-mine region",
                            0.9
                    ));
                }
            }
        }

        if (!goodMoves.isEmpty()) {
            return Optional.of(goodMoves.get(random.nextInt(goodMoves.size())));
        }

        return Optional.empty();
    }

    /**
     * Finds a random unrevealed tile to reveal.
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
