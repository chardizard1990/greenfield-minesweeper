package com.minesweeper.gui.viewmodel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TileViewModel.
 */
public class TileViewModelTest {
    
    private TileViewModel tileViewModel;
    
    @BeforeEach
    public void setUp() {
        tileViewModel = new TileViewModel(5, 7);
    }
    
    @Test
    public void testInitialState() {
        assertEquals(5, tileViewModel.getRow());
        assertEquals(7, tileViewModel.getCol());
        assertEquals(TileViewModel.TileState.UNREVEALED, tileViewModel.getTileState());
        assertEquals(0, tileViewModel.getAdjacentMineCount());
        assertFalse(tileViewModel.isFlagged());
        assertFalse(tileViewModel.isRevealed());
    }
    
    @Test
    public void testSetRevealed() {
        tileViewModel.setRevealed(3);
        
        assertEquals(TileViewModel.TileState.REVEALED, tileViewModel.getTileState());
        assertEquals(3, tileViewModel.getAdjacentMineCount());
        assertTrue(tileViewModel.isRevealed());
        assertFalse(tileViewModel.isFlagged());
    }
    
    @Test
    public void testSetMine() {
        tileViewModel.setMine();
        
        assertEquals(TileViewModel.TileState.MINE, tileViewModel.getTileState());
        assertTrue(tileViewModel.isRevealed());
        assertFalse(tileViewModel.isFlagged());
    }
    
    @Test
    public void testToggleFlag() {
        // Flag unrevealed tile
        tileViewModel.setFlagged(true);
        
        assertEquals(TileViewModel.TileState.FLAGGED, tileViewModel.getTileState());
        assertTrue(tileViewModel.isFlagged());
        assertFalse(tileViewModel.isRevealed());
        
        // Unflag
        tileViewModel.setFlagged(false);
        
        assertEquals(TileViewModel.TileState.UNREVEALED, tileViewModel.getTileState());
        assertFalse(tileViewModel.isFlagged());
    }
    
    @Test
    public void testAdjacentMineCountValidation() {
        // Valid counts
        tileViewModel.setAdjacentMineCount(0);
        assertEquals(0, tileViewModel.getAdjacentMineCount());
        
        tileViewModel.setAdjacentMineCount(8);
        assertEquals(8, tileViewModel.getAdjacentMineCount());
        
        // Invalid counts (should not change)
        int current = tileViewModel.getAdjacentMineCount();
        tileViewModel.setAdjacentMineCount(-1);
        assertEquals(current, tileViewModel.getAdjacentMineCount());
        
        tileViewModel.setAdjacentMineCount(9);
        assertEquals(current, tileViewModel.getAdjacentMineCount());
    }
    
    @Test
    public void testPropertyChangeNotification() {
        // Track changes to tile state
        final TileViewModel.TileState[] changedTo = {null};
        
        tileViewModel.tileStateProperty().addListener((old, newVal) -> {
            changedTo[0] = newVal;
        });
        
        tileViewModel.setRevealed(5);
        assertEquals(TileViewModel.TileState.REVEALED, changedTo[0]);
        
        tileViewModel.setMine();
        assertEquals(TileViewModel.TileState.MINE, changedTo[0]);
    }
    
    @Test
    public void testConsistency() {
        // Test that isFlagged and isRevealed stay consistent with tileState
        
        // Start unrevealed
        assertFalse(tileViewModel.isFlagged());
        assertFalse(tileViewModel.isRevealed());
        
        // Flag it
        tileViewModel.setFlagged(true);
        assertTrue(tileViewModel.isFlagged());
        assertFalse(tileViewModel.isRevealed());
        
        // Unflag and reveal
        tileViewModel.setFlagged(false);
        tileViewModel.setRevealed(0);
        assertFalse(tileViewModel.isFlagged());
        assertTrue(tileViewModel.isRevealed());
        
        // Reveal as mine
        tileViewModel.setMine();
        assertFalse(tileViewModel.isFlagged());
        assertTrue(tileViewModel.isRevealed());
    }
    
    @Test
    public void testMultipleStateTransitions() {
        // Unrevealed -> Flagged -> Unflagged -> Revealed -> Mine
        
        tileViewModel.setFlagged(true);
        assertEquals(TileViewModel.TileState.FLAGGED, tileViewModel.getTileState());
        
        tileViewModel.setFlagged(false);
        assertEquals(TileViewModel.TileState.UNREVEALED, tileViewModel.getTileState());
        
        tileViewModel.setRevealed(2);
        assertEquals(TileViewModel.TileState.REVEALED, tileViewModel.getTileState());
        
        tileViewModel.setMine();
        assertEquals(TileViewModel.TileState.MINE, tileViewModel.getTileState());
    }
}
