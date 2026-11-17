package com.minesweeper.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import com.minesweeper.gui.viewmodel.GameBoardViewModel;
import com.minesweeper.gui.viewmodel.TileViewModel;

/**
 * Visual component for displaying the Minesweeper game board.
 * 
 * This component creates a grid of TileView components based on the
 * GameBoardViewModel. It handles layout, sizing, and event wiring.
 * 
 * Features:
 * - Dynamic grid sizing (10x10, 20x20, 30x30, custom)
 * - Responsive layout that adapts to window size
 * - Maintains square tiles and square grid
 * - Click events wired to callback listeners
 * - Reactive updates when ViewModel state changes
 * 
 * Example usage:
 * <pre>
 *   GameBoardViewModel boardVM = new GameBoardViewModel(10, 10);
 *   GameBoardView boardView = new GameBoardView(boardVM);
 *   
 *   boardVM.addTileBoardListener(new GameBoardViewModel.TileBoardListener() {
 *       public void onTileClicked(int row, int col) {
 *           gameController.reveal(row, col);
 *       }
 *       public void onTileFlagged(int row, int col) {
 *           gameController.flag(row, col);
 *       }
 *   });
 * </pre>
 */
public class GameBoardView extends VBox {
    
    private static final int TILE_GAP = 1; // 1px gap between tiles
    private static final int DEFAULT_TILE_SIZE = 30;
    
    private final GameBoardViewModel viewModel;
    private final GridPane gridPane;
    private final TileView[][] tileViews;
    
    /**
     * Create a game board view with default tile size.
     * 
     * @param viewModel The GameBoardViewModel for this board
     */
    public GameBoardView(GameBoardViewModel viewModel) {
        this(viewModel, DEFAULT_TILE_SIZE);
    }
    
    /**
     * Create a game board view with custom tile size.
     * 
     * @param viewModel The GameBoardViewModel for this board
     * @param tileSize Size of each tile in pixels
     */
    public GameBoardView(GameBoardViewModel viewModel, int tileSize) {
        this.viewModel = viewModel;
        this.tileViews = new TileView[viewModel.getBoardHeight()][viewModel.getBoardWidth()];
        
        // Setup layout
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #F5F5F5;");
        setPadding(new Insets(10));
        
        // Create grid pane
        this.gridPane = createGridPane(tileSize);
        getChildren().add(gridPane);
        
        // Bind to ViewModel updates
        bindToViewModel();
    }
    
    /**
     * Create and populate the grid pane with tile views.
     */
    private GridPane createGridPane(int tileSize) {
        GridPane grid = new GridPane();
        grid.setHgap(TILE_GAP);
        grid.setVgap(TILE_GAP);
        grid.setStyle("-fx-background-color: #999999; -fx-padding: 2;");
        grid.setAlignment(Pos.CENTER);
        
        // Add all tiles to grid
        for (int row = 0; row < viewModel.getBoardHeight(); row++) {
            for (int col = 0; col < viewModel.getBoardWidth(); col++) {
                TileViewModel tileVM = viewModel.getTile(row, col);
                TileView tileView = new TileView(tileVM, tileSize);
                
                // Wire up callbacks
                tileView.setOnTileClicked((r, c) -> viewModel.notifyTileClicked(r, c));
                tileView.setOnTileFlagged((r, c) -> viewModel.notifyTileFlagged(r, c));
                
                // Add to grid
                grid.add(tileView, col, row);
                tileViews[row][col] = tileView;
            }
        }
        
        return grid;
    }
    
    /**
     * Bind UI updates to ViewModel property changes.
     */
    private void bindToViewModel() {
        // Listen for game state changes
        viewModel.gameStateProperty().addListener((oldVal, newVal) -> {
            updateGameStateVisuals();
        });
        
        // Listen for revealed count changes
        viewModel.revealedCountProperty().addListener((oldVal, newVal) -> {
            // Update any UI that shows revealed count
        });
    }
    
    /**
     * Update visuals based on current game state.
     */
    private void updateGameStateVisuals() {
        GameBoardViewModel.GameState state = viewModel.getGameState();
        
        switch (state) {
            case ACTIVE:
                // Allow tile interactions
                setDisabled(false);
                break;
            case WON:
                // Disable interactions, show win state
                setDisabled(false);
                // Could add overlay or styling here
                break;
            case LOST:
                // Disable interactions, show loss state
                setDisabled(false);
                // Could add overlay or styling here
                break;
        }
    }
    
    /**
     * Get a specific tile view.
     * 
     * @param row Row index
     * @param col Column index
     * @return The TileView at that position, or null if out of bounds
     */
    public TileView getTileView(int row, int col) {
        if (row >= 0 && row < viewModel.getBoardHeight() &&
            col >= 0 && col < viewModel.getBoardWidth()) {
            return tileViews[row][col];
        }
        return null;
    }
    
    /**
     * Get the underlying grid pane component.
     */
    public GridPane getGridPane() {
        return gridPane;
    }
    
    /**
     * Get the ViewModel for this board.
     */
    public GameBoardViewModel getViewModel() {
        return viewModel;
    }
    
    /**
     * Play reveal animation for a single tile.
     * 
     * @param row Row index
     * @param col Column index
     */
    public void playTileRevealAnimation(int row, int col) {
        TileView tileView = getTileView(row, col);
        if (tileView != null) {
            tileView.playRevealAnimation();
        }
    }
    
    /**
     * Play cascade animation for revealed tiles.
     * 
     * When a tile with 0 adjacent mines is revealed, surrounding safe tiles
     * are revealed with staggered animation for visual effect.
     * 
     * @param originRow Origin tile row
     * @param originCol Origin tile column
     * @param revealedTiles 2D boolean array indicating which tiles were revealed
     */
    public void playCascadeAnimation(int originRow, int originCol, boolean[][] revealedTiles) {
        int height = viewModel.getBoardHeight();
        int width = viewModel.getBoardWidth();
        
        // Find all tiles in cascade and calculate distance from origin
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (revealedTiles[row][col] && (row != originRow || col != originCol)) {
                    // Calculate Manhattan distance for cascade effect
                    int distance = Math.abs(row - originRow) + Math.abs(col - originCol);
                    
                    // Schedule animation with delay based on distance
                    javafx.animation.PauseTransition delay = 
                        new javafx.animation.PauseTransition(
                            javafx.util.Duration.millis(distance * 50)
                        );
                    
                    int finalRow = row;
                    int finalCol = col;
                    delay.setOnFinished(e -> playTileRevealAnimation(finalRow, finalCol));
                    delay.play();
                }
            }
        }
    }
    
    /**
     * Play explosion animation for a mine tile.
     * 
     * @param row Row index
     * @param col Column index
     */
    public void playMineExplosion(int row, int col) {
        TileView tileView = getTileView(row, col);
        if (tileView != null) {
            tileView.playExplosionAnimation();
        }
    }
    
    /**
     * Play flag animation when tile is flagged.
     * 
     * @param row Row index
     * @param col Column index
     */
    public void playFlagAnimation(int row, int col) {
        TileView tileView = getTileView(row, col);
        if (tileView != null) {
            tileView.playFlagAnimation();
        }
    }
    
    /**
     * Play unflag animation when tile is unflagged.
     * 
     * @param row Row index
     * @param col Column index
     */
    public void playUnflagAnimation(int row, int col) {
        TileView tileView = getTileView(row, col);
        if (tileView != null) {
            tileView.playUnflagAnimation();
        }
    }
}
