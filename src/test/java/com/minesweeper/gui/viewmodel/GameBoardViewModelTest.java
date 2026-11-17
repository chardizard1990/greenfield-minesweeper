package com.minesweeper.gui.viewmodel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameBoardViewModel.
 */
public class GameBoardViewModelTest {
    
    private GameBoardViewModel boardViewModel;
    
    @BeforeEach
    public void setUp() {
        boardViewModel = new GameBoardViewModel(10, 10);
    }
    
    @Test
    public void testInitialization() {
        assertEquals(10, boardViewModel.getBoardWidth());
        assertEquals(10, boardViewModel.getBoardHeight());
        assertEquals(GameBoardViewModel.GameState.ACTIVE, boardViewModel.getGameState());
        assertEquals(0, boardViewModel.getRevealedCount());
        assertEquals(0, boardViewModel.getFlaggedCount());
        assertEquals(0, boardViewModel.getTotalMines());
    }
    
    @Test
    public void testTileCreation() {
        // All tiles should be initialized as unrevealed
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                TileViewModel tile = boardViewModel.getTile(row, col);
                assertNotNull(tile);
                assertEquals(TileViewModel.TileState.UNREVEALED, tile.getTileState());
                assertEquals(row, tile.getRow());
                assertEquals(col, tile.getCol());
            }
        }
    }
    
    @Test
    public void testGetTileOutOfBounds() {
        assertNull(boardViewModel.getTile(-1, 0));
        assertNull(boardViewModel.getTile(0, -1));
        assertNull(boardViewModel.getTile(10, 0));
        assertNull(boardViewModel.getTile(0, 10));
    }
    
    @Test
    public void testGameStateTransitions() {
        boardViewModel.setGameState(GameBoardViewModel.GameState.WON);
        assertEquals(GameBoardViewModel.GameState.WON, boardViewModel.getGameState());
        
        boardViewModel.setGameState(GameBoardViewModel.GameState.LOST);
        assertEquals(GameBoardViewModel.GameState.LOST, boardViewModel.getGameState());
        
        boardViewModel.setGameState(GameBoardViewModel.GameState.ACTIVE);
        assertEquals(GameBoardViewModel.GameState.ACTIVE, boardViewModel.getGameState());
    }
    
    @Test
    public void testGameStatPropertyNotification() {
        final GameBoardViewModel.GameState[] changedTo = {null};
        
        boardViewModel.gameStateProperty().addListener((old, newVal) -> {
            changedTo[0] = newVal;
        });
        
        boardViewModel.setGameState(GameBoardViewModel.GameState.WON);
        assertEquals(GameBoardViewModel.GameState.WON, changedTo[0]);
    }
    
    @Test
    public void testRevealedCountProperty() {
        boardViewModel.setRevealedCount(5);
        assertEquals(5, boardViewModel.getRevealedCount());
        
        boardViewModel.setRevealedCount(50);
        assertEquals(50, boardViewModel.getRevealedCount());
    }
    
    @Test
    public void testFlaggedCountProperty() {
        boardViewModel.setFlaggedCount(3);
        assertEquals(3, boardViewModel.getFlaggedCount());
    }
    
    @Test
    public void testTotalMinesProperty() {
        boardViewModel.setTotalMines(10);
        assertEquals(10, boardViewModel.getTotalMines());
    }
    
    @Test
    public void testResetBoard() {
        // Modify some tiles
        boardViewModel.getTile(0, 0).setRevealed(0);
        boardViewModel.getTile(5, 5).setFlagged(true);
        boardViewModel.setGameState(GameBoardViewModel.GameState.WON);
        boardViewModel.setRevealedCount(50);
        
        // Reset
        boardViewModel.resetBoard();
        
        // Verify reset
        assertEquals(GameBoardViewModel.GameState.ACTIVE, boardViewModel.getGameState());
        assertEquals(0, boardViewModel.getRevealedCount());
        assertEquals(0, boardViewModel.getFlaggedCount());
        
        // All tiles should be unrevealed again
        assertEquals(TileViewModel.TileState.UNREVEALED, boardViewModel.getTile(0, 0).getTileState());
        assertEquals(TileViewModel.TileState.UNREVEALED, boardViewModel.getTile(5, 5).getTileState());
    }
    
    @Test
    public void testTileBoardListenerNotification() {
        final int[] clickedRow = {-1};
        final int[] clickedCol = {-1};
        final int[] flaggedRow = {-1};
        final int[] flaggedCol = {-1};
        
        GameBoardViewModel.TileBoardListener listener = new GameBoardViewModel.TileBoardListener() {
            @Override
            public void onTileClicked(int row, int col) {
                clickedRow[0] = row;
                clickedCol[0] = col;
            }
            
            @Override
            public void onTileFlagged(int row, int col) {
                flaggedRow[0] = row;
                flaggedCol[0] = col;
            }
        };
        
        boardViewModel.addTileBoardListener(listener);
        
        // Trigger notifications
        boardViewModel.notifyTileClicked(3, 7);
        assertEquals(3, clickedRow[0]);
        assertEquals(7, clickedCol[0]);
        
        boardViewModel.notifyTileFlagged(5, 2);
        assertEquals(5, flaggedRow[0]);
        assertEquals(2, flaggedCol[0]);
    }
    
    @Test
    public void testMultipleTileBoardListeners() {
        final int[] listener1Calls = {0};
        final int[] listener2Calls = {0};
        
        boardViewModel.addTileBoardListener((row, col) -> listener1Calls[0]++);
        boardViewModel.addTileBoardListener((row, col) -> listener2Calls[0]++);
        
        boardViewModel.notifyTileClicked(0, 0);
        
        assertEquals(1, listener1Calls[0]);
        assertEquals(1, listener2Calls[0]);
    }
    
    @Test
    public void testRemoveTileBoardListener() {
        final int[] listenerCalls = {0};
        
        GameBoardViewModel.TileBoardListener listener = (row, col) -> listenerCalls[0]++;
        
        boardViewModel.addTileBoardListener(listener);
        boardViewModel.notifyTileClicked(0, 0);
        assertEquals(1, listenerCalls[0]);
        
        boardViewModel.removeTileBoardListener(listener);
        boardViewModel.notifyTileClicked(1, 1);
        assertEquals(1, listenerCalls[0]); // Should not increase
    }
    
    @Test
    public void testDifferentBoardSizes() {
        // Test 20x20
        GameBoardViewModel board20 = new GameBoardViewModel(20, 20);
        assertEquals(20, board20.getBoardWidth());
        assertEquals(20, board20.getBoardHeight());
        
        // Test 30x30
        GameBoardViewModel board30 = new GameBoardViewModel(30, 30);
        assertEquals(30, board30.getBoardWidth());
        assertEquals(30, board30.getBoardHeight());
        
        // Test custom size
        GameBoardViewModel boardCustom = new GameBoardViewModel(15, 12);
        assertEquals(15, boardCustom.getBoardWidth());
        assertEquals(12, boardCustom.getBoardHeight());
    }
}
