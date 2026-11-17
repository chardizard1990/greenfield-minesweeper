package com.minesweeper.gui.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;

import com.minesweeper.gui.viewmodel.GameBoardViewModel;
import com.minesweeper.gui.viewmodel.TileViewModel;

/**
 * Unit tests for GameBoardView component.
 * Note: Most tests are disabled because they require JavaFX initialization.
 * Visual rendering tests require running with javafx:run or in an IDE.
 */
public class GameBoardViewTest {
    
    private GameBoardViewModel viewModel;
    
    @BeforeEach
    public void setUp() {
        viewModel = new GameBoardViewModel(10, 10);
    }
    
    @Test
    public void testGameBoardViewModelCreation() {
        // Test that we can create a ViewModel for the board
        assertNotNull(viewModel);
        assertEquals(10, viewModel.getBoardWidth());
        assertEquals(10, viewModel.getBoardHeight());
    }
    
    @Test
    @Disabled("Requires JavaFX initialization - visual tests require javafx:run")
    public void testGameBoardViewInitialization() {
        // This test is disabled in headless environment
        // GameBoardView requires JavaFX to be properly initialized
    }
    
    @Test
    @Disabled("Requires JavaFX initialization - visual tests require javafx:run")
    public void testTileGridCreation() {
        // This test is disabled in headless environment
    }
    
    @Test
    @Disabled("Requires JavaFX initialization - visual tests require javafx:run")
    public void testEventWiring() {
        // This test is disabled in headless environment
    }
    
    @Test
    public void testGameBoardViewModelIntegration() {
        // This test doesn't require JavaFX, only the ViewModel
        GameBoardViewModel board = new GameBoardViewModel(20, 20);
        assertEquals(20, board.getBoardWidth());
        assertEquals(20, board.getBoardHeight());
        
        // Test that tiles exist
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                TileViewModel tile = board.getTile(row, col);
                assertNotNull(tile);
            }
        }
    }
    
    @Test
    public void testDifferentBoardSizes() {
        // Test various board sizes
        GameBoardViewModel[] boards = {
            new GameBoardViewModel(10, 10),
            new GameBoardViewModel(20, 20),
            new GameBoardViewModel(30, 30),
            new GameBoardViewModel(15, 12)
        };
        
        for (GameBoardViewModel board : boards) {
            assertEquals(board.getBoardWidth() * board.getBoardHeight(), 
                        countTiles(board));
        }
    }
    
    /**
     * Count the number of tiles in a board.
     */
    private int countTiles(GameBoardViewModel board) {
        int count = 0;
        for (int row = 0; row < board.getBoardHeight(); row++) {
            for (int col = 0; col < board.getBoardWidth(); col++) {
                if (board.getTile(row, col) != null) {
                    count++;
                }
            }
        }
        return count;
    }
}
