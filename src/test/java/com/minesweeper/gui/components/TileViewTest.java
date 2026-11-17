package com.minesweeper.gui.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;

import com.minesweeper.gui.viewmodel.TileViewModel;
import com.minesweeper.gui.viewmodel.TileViewModel.TileState;

/**
 * Unit tests for TileView component.
 * Note: Most tests are disabled because they require JavaFX initialization.
 * Visual rendering tests require running with javafx:run or in an IDE.
 */
public class TileViewTest {
    
    private TileViewModel viewModel;
    
    @BeforeEach
    public void setUp() {
        viewModel = new TileViewModel(0, 0);
    }
    
    @Test
    public void testTileViewModelCreation() {
        // Test that we can create a ViewModel for a tile
        assertNotNull(viewModel);
        assertEquals(0, viewModel.getRow());
        assertEquals(0, viewModel.getCol());
    }
    
    @Test
    @Disabled("Requires JavaFX initialization - visual tests require javafx:run")
    public void testTileViewInitialization() {
        // This test is disabled in headless environment
        // TileView requires JavaFX to be properly initialized
    }
    
    @Test
    @Disabled("Requires JavaFX initialization - visual tests require javafx:run")
    public void testDefaultTileSize() {
        // This test is disabled in headless environment
    }
    
    @Test
    @Disabled("Requires JavaFX initialization - visual tests require javafx:run")
    public void testCallbackRegistration() {
        // This test is disabled in headless environment
    }
    
    @Test
    public void testViewModelPropertyBinding() {
        // This test doesn't require JavaFX, only the ViewModel
        viewModel.setRevealed(5);
        assertEquals(TileState.REVEALED, viewModel.getTileState());
        
        viewModel.setMine();
        assertEquals(TileState.MINE, viewModel.getTileState());
    }
    
    @Test
    public void testTilePositionTracking() {
        TileViewModel vm1 = new TileViewModel(3, 7);
        
        assertEquals(3, vm1.getRow());
        assertEquals(7, vm1.getCol());
    }
}
