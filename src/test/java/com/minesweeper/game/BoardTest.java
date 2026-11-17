package com.minesweeper.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Board class.
 * Tests board initialization, mine placement, adjacent tiles, and mine counting.
 */
@DisplayName("Board Tests")
public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        // Create a 10x10 board with 10 mines for testing
        board = new Board(10, 10, 10);
    }

    @Test
    @DisplayName("Should initialize board with correct dimensions")
    public void testBoardInitialization() {
        assertEquals(10, board.getWidth());
        assertEquals(10, board.getHeight());
        assertEquals(10, board.getMineCount());
    }

    @Test
    @DisplayName("Should create small board successfully")
    public void testSmallBoard() {
        Board smallBoard = new Board(3, 3, 2);
        assertEquals(3, smallBoard.getWidth());
        assertEquals(3, smallBoard.getHeight());
        assertEquals(2, smallBoard.getMineCount());
    }

    @Test
    @DisplayName("Should create large board successfully")
    public void testLargeBoard() {
        Board largeBoard = new Board(30, 30, 100);
        assertEquals(30, largeBoard.getWidth());
        assertEquals(30, largeBoard.getHeight());
        assertEquals(100, largeBoard.getMineCount());
    }

    @Test
    @DisplayName("Should throw exception for invalid dimensions")
    public void testInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Board(0, 10, 5));
        assertThrows(IllegalArgumentException.class, () -> new Board(10, 0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Board(-5, 10, 5));
    }

    @Test
    @DisplayName("Should throw exception when too many mines")
    public void testTooManyMines() {
        // 10x10 board can have at most 99 mines
        assertThrows(IllegalArgumentException.class, () -> new Board(10, 10, 100));
        assertThrows(IllegalArgumentException.class, () -> new Board(10, 10, 101));
    }

    @Test
    @DisplayName("Should throw exception for negative mine count")
    public void testNegativeMineCount() {
        assertThrows(IllegalArgumentException.class, () -> new Board(10, 10, -1));
    }

    @Test
    @DisplayName("Should place exactly the specified number of mines")
    public void testMineCountPlacement() {
        // Use seeded random for reproducible placement
        Random random = new Random(42);
        Board testBoard = new Board(10, 10, 15, random);

        int mineCount = 0;
        for (int row = 0; row < testBoard.getHeight(); row++) {
            for (int col = 0; col < testBoard.getWidth(); col++) {
                if (testBoard.getTile(row, col).isMine()) {
                    mineCount++;
                }
            }
        }

        assertEquals(15, mineCount, "Should have exactly 15 mines on board");
    }

    @Test
    @DisplayName("Should retrieve tile at valid coordinates")
    public void testGetTileValidCoordinates() {
        Tile tile = board.getTile(0, 0);
        assertNotNull(tile);
        assertEquals(0, tile.getRow());
        assertEquals(0, tile.getColumn());
    }

    @Test
    @DisplayName("Should throw exception for out-of-bounds coordinates")
    public void testGetTileOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> board.getTile(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getTile(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getTile(10, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getTile(0, 10));
    }

    @Test
    @DisplayName("Should correctly identify in-bounds coordinates")
    public void testIsWithinBounds() {
        assertTrue(board.isWithinBounds(0, 0));
        assertTrue(board.isWithinBounds(5, 5));
        assertTrue(board.isWithinBounds(9, 9));
        assertFalse(board.isWithinBounds(-1, 0));
        assertFalse(board.isWithinBounds(10, 10));
    }

    @Test
    @DisplayName("Should return all 8 adjacent tiles for center tile")
    public void testGetAdjacentTilesCenterTile() {
        List<Tile> adjacent = board.getAdjacentTiles(5, 5);
        assertEquals(8, adjacent.size(), "Center tile should have 8 adjacent tiles");
    }

    @Test
    @DisplayName("Should return 3 adjacent tiles for corner tile")
    public void testGetAdjacentTilesCorner() {
        List<Tile> adjacent = board.getAdjacentTiles(0, 0);
        assertEquals(3, adjacent.size(), "Corner tile should have 3 adjacent tiles");

        // Verify the adjacent tiles are correct
        boolean hasRight = adjacent.stream().anyMatch(t -> t.getRow() == 0 && t.getColumn() == 1);
        boolean hasDown = adjacent.stream().anyMatch(t -> t.getRow() == 1 && t.getColumn() == 0);
        boolean hasDiagonal = adjacent.stream().anyMatch(t -> t.getRow() == 1 && t.getColumn() == 1);

        assertTrue(hasRight && hasDown && hasDiagonal);
    }

    @Test
    @DisplayName("Should return 5 adjacent tiles for edge tile")
    public void testGetAdjacentTilesEdge() {
        List<Tile> adjacent = board.getAdjacentTiles(0, 5);
        assertEquals(5, adjacent.size(), "Edge tile should have 5 adjacent tiles");
    }

    @Test
    @DisplayName("Should throw exception for adjacent tiles query outside bounds")
    public void testGetAdjacentTilesOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> board.getAdjacentTiles(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getAdjacentTiles(10, 5));
    }

    @Test
    @DisplayName("Should correctly count adjacent mines")
    public void testGetAdjacentMineCount() {
        // Create a small board with known mine placement for testing
        Random random = new Random(42);
        Board testBoard = new Board(5, 5, 3, random);

        // Adjacent mine count should be between 0 and 8
        for (int row = 0; row < testBoard.getHeight(); row++) {
            for (int col = 0; col < testBoard.getWidth(); col++) {
                int count = testBoard.getAdjacentMineCount(row, col);
                assertTrue(count >= 0 && count <= 8,
                        String.format("Mine count at (%d,%d) should be 0-8, got %d",
                                row, col, count));
            }
        }
    }

    @Test
    @DisplayName("Should return 0 adjacent mines for safe tile surrounded by safe tiles")
    public void testAdjacentMineCountZero() {
        // Create a 1x1 board with 0 mines
        Board emptyBoard = new Board(1, 1, 0);
        assertEquals(0, emptyBoard.getAdjacentMineCount(0, 0));
    }

    @Test
    @DisplayName("Should throw exception for mine count query outside bounds")
    public void testGetAdjacentMineCountOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> board.getAdjacentMineCount(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getAdjacentMineCount(10, 0));
    }

    @Test
    @DisplayName("Should return defensive copy of mine layout")
    public void testGetMineLayoutDefensiveCopy() {
        boolean[][] layout1 = board.getMineLayout();
        boolean[][] layout2 = board.getMineLayout();

        // Modify first layout
        layout1[0][0] = !layout1[0][0];

        // Second layout should be unchanged (proves it's a copy)
        assertEquals(board.getMineLayout()[0][0], layout2[0][0],
                "Mine layout should return defensive copy");
    }

    @Test
    @DisplayName("Should correctly report unrevealed tile count")
    public void testGetUnrevealedTileCount() {
        // Create a small board with 0 mines for deterministic testing
        Board emptyBoard = new Board(5, 5, 0);
        int total = emptyBoard.getWidth() * emptyBoard.getHeight();
        
        assertEquals(total, emptyBoard.getUnrevealedTileCount(),
                "All tiles should initially be unrevealed");

        // Reveal a tile
        emptyBoard.getTile(0, 0).reveal();
        int expected = total - 1;
        assertEquals(expected, emptyBoard.getUnrevealedTileCount(),
                "Should have one fewer unrevealed tile after reveal");

        // Flag another tile (it's still unrevealed, just marked)
        // Flagging doesn't change unrevealed count
        emptyBoard.getTile(2, 2).toggleFlag();
        assertEquals(expected, emptyBoard.getUnrevealedTileCount(),
                "Flagged tiles are still counted as unrevealed");
        
        // Reveal the flagged tile  
        emptyBoard.getTile(2, 2).reveal();
        assertEquals(expected - 1, emptyBoard.getUnrevealedTileCount(),
                "Revealing a flagged tile decreases unrevealed count");
    }

    @Test
    @DisplayName("Should correctly report flagged tile count")
    public void testGetFlaggedTileCount() {
        assertEquals(0, board.getFlaggedTileCount());

        board.getTile(0, 0).toggleFlag();
        assertEquals(1, board.getFlaggedTileCount());

        board.getTile(1, 1).toggleFlag();
        assertEquals(2, board.getFlaggedTileCount());

        // Unflag a tile
        board.getTile(0, 0).toggleFlag();
        assertEquals(1, board.getFlaggedTileCount());
    }

    @Test
    @DisplayName("Should handle board with no mines")
    public void testBoardWithNoMines() {
        Board emptyBoard = new Board(5, 5, 0);
        assertEquals(0, emptyBoard.getMineCount());

        // No tiles should have mines
        for (int row = 0; row < emptyBoard.getHeight(); row++) {
            for (int col = 0; col < emptyBoard.getWidth(); col++) {
                assertFalse(emptyBoard.getTile(row, col).isMine());
            }
        }
    }

    @Test
    @DisplayName("Should maintain consistent state across operations")
    public void testConsistentBoardState() {
        // Count mines in multiple ways
        int mineCount = 0;
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (board.getTile(row, col).isMine()) {
                    mineCount++;
                }
            }
        }

        assertEquals(board.getMineCount(), mineCount,
                "Mine count should match actual mines on board");
    }
}
