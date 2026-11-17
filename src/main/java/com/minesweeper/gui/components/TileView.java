package com.minesweeper.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.minesweeper.gui.viewmodel.TileViewModel;
import com.minesweeper.gui.viewmodel.TileViewModel.TileState;

/**
 * Visual component representing a single Minesweeper tile.
 * 
 * This is a reusable JavaFX control that displays a tile in various states:
 * - Unrevealed: 3D button appearance
 * - Revealed: flat background with number (1-8) or blank
 * - Flagged: indicates suspected mine
 * - Mine: shown when game ends
 * 
 * The component uses a TileViewModel to manage state reactively.
 * When the ViewModel's state changes, the view automatically updates.
 * 
 * Example usage:
 * <pre>
 *   TileViewModel vm = new TileViewModel(0, 0);
 *   TileView tile = new TileView(vm);
 *   
 *   tile.setOnTileClicked((row, col) -> gameController.reveal(row, col));
 *   tile.setOnTileFlagged((row, col) -> gameController.flag(row, col));
 *   
 *   vm.setRevealed(3); // Shows number 3
 * </pre>
 */
public class TileView extends StackPane {
    
    private static final int DEFAULT_TILE_SIZE = 30;
    private static final String MINE_SYMBOL = "💣";
    private static final String FLAG_SYMBOL = "🚩";
    
    private final TileViewModel viewModel;
    private final int tileSize;
    
    // UI Components
    private final Label numberLabel;
    private final Label symbolLabel;
    
    // Callbacks
    private TileClickListener onTileClicked;
    private TileFlagListener onTileFlagged;
    
    /**
     * Create a tile view with default size.
     * 
     * @param viewModel The ViewModel for this tile
     */
    public TileView(TileViewModel viewModel) {
        this(viewModel, DEFAULT_TILE_SIZE);
    }
    
    /**
     * Create a tile view with custom size.
     * 
     * @param viewModel The ViewModel for this tile
     * @param tileSize Size in pixels (width and height)
     */
    public TileView(TileViewModel viewModel, int tileSize) {
        this.viewModel = viewModel;
        this.tileSize = tileSize;
        
        // Set fixed size
        setPrefSize(tileSize, tileSize);
        setMinSize(tileSize, tileSize);
        setMaxSize(tileSize, tileSize);
        
        // Create labels
        this.numberLabel = new Label();
        this.symbolLabel = new Label();
        
        // Setup layout
        setupLayout();
        
        // Bind to ViewModel
        bindToViewModel();
        
        // Setup interaction
        setupMouseHandlers();
        
        // Initial render
        updateTileAppearance();
    }
    
    /**
     * Setup the layout with layered components.
     */
    private void setupLayout() {
        // Clear and rebuild children
        getChildren().clear();
        
        // Add number label for revealed tiles
        numberLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        numberLabel.setPrefSize(tileSize, tileSize);
        numberLabel.setAlignment(Pos.CENTER);
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, getFontSize()));
        
        // Add symbol label for flags and mines
        symbolLabel.setPrefSize(tileSize, tileSize);
        symbolLabel.setAlignment(Pos.CENTER);
        symbolLabel.setFont(Font.font("Arial", getFontSize()));
        
        getChildren().addAll(numberLabel, symbolLabel);
    }
    
    /**
     * Calculate font size based on tile size.
     */
    private int getFontSize() {
        return Math.max(8, tileSize / 2);
    }
    
    /**
     * Bind UI updates to ViewModel property changes.
     */
    private void bindToViewModel() {
        // Listen for tile state changes
        viewModel.tileStateProperty().addListener((oldVal, newVal) -> {
            updateTileAppearance();
        });
        
        // Listen for adjacent mine count changes
        viewModel.adjacentMineCountProperty().addListener((oldVal, newVal) -> {
            updateTileAppearance();
        });
    }
    
    /**
     * Setup mouse event handlers for clicking and right-clicking.
     */
    private void setupMouseHandlers() {
        setOnMouseClicked(this::handleMouseClick);
        
        // Add CSS hover effects
        setStyle("-fx-cursor: hand;");
    }
    
    /**
     * Handle mouse click events (left and right).
     */
    private void handleMouseClick(MouseEvent event) {
        if (viewModel.getTileState() == TileState.UNREVEALED) {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Left click - reveal
                if (onTileClicked != null) {
                    onTileClicked.onTileClicked(viewModel.getRow(), viewModel.getCol());
                }
                event.consume();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // Right click - flag
                if (onTileFlagged != null) {
                    onTileFlagged.onTileFlagged(viewModel.getRow(), viewModel.getCol());
                }
                event.consume();
            }
        }
    }
    
    /**
     * Update the visual appearance based on the current tile state.
     */
    private void updateTileAppearance() {
        TileState state = viewModel.getTileState();
        
        // Clear labels
        numberLabel.setText("");
        symbolLabel.setText("");
        numberLabel.setStyle("");
        symbolLabel.setStyle("");
        
        switch (state) {
            case UNREVEALED:
                renderUnrevealed();
                break;
            case REVEALED:
                renderRevealed();
                break;
            case FLAGGED:
                renderFlagged();
                break;
            case MINE:
                renderMine();
                break;
        }
    }
    
    /**
     * Render unrevealed tile (3D button appearance).
     */
    private void renderUnrevealed() {
        BackgroundFill fill = new BackgroundFill(
            Color.web("#C0C0C0"),
            new CornerRadii(3),
            Insets.EMPTY
        );
        setBackground(new Background(fill));
        numberLabel.setStyle("-fx-border-color: white white #808080 #808080; -fx-border-width: 2;");
        setStyle("-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 1, 1);");
    }
    
    /**
     * Render revealed tile with number or blank.
     */
    private void renderRevealed() {
        BackgroundFill fill = new BackgroundFill(
            Color.web("#DFDFDF"),
            new CornerRadii(0),
            Insets.EMPTY
        );
        setBackground(new Background(fill));
        
        int count = viewModel.getAdjacentMineCount();
        if (count > 0) {
            numberLabel.setText(String.valueOf(count));
            // Color code the numbers
            String color = getNumberColor(count);
            numberLabel.setStyle("-fx-text-fill: " + color + ";");
        }
        setStyle("-fx-cursor: default;");
    }
    
    /**
     * Get color for numbered tiles (1-8).
     */
    private String getNumberColor(int number) {
        switch (number) {
            case 1:
                return "#0000FF"; // Blue
            case 2:
                return "#008000"; // Green
            case 3:
                return "#FF0000"; // Red
            case 4:
                return "#000080"; // Dark Blue
            case 5:
                return "#800000"; // Maroon
            case 6:
                return "#008080"; // Teal
            case 7:
                return "#000000"; // Black
            case 8:
                return "#808080"; // Gray
            default:
                return "#000000";
        }
    }
    
    /**
     * Render flagged tile (unrevealed but marked).
     */
    private void renderFlagged() {
        BackgroundFill fill = new BackgroundFill(
            Color.web("#C0C0C0"),
            new CornerRadii(3),
            Insets.EMPTY
        );
        setBackground(new Background(fill));
        symbolLabel.setText(FLAG_SYMBOL);
        symbolLabel.setStyle("-fx-text-fill: #FF0000;");
        setStyle("-fx-cursor: hand;");
    }
    
    /**
     * Render mine tile (revealed as mine on game end).
     */
    private void renderMine() {
        BackgroundFill fill = new BackgroundFill(
            Color.web("#FF6B6B"),
            new CornerRadii(0),
            Insets.EMPTY
        );
        setBackground(new Background(fill));
        symbolLabel.setText(MINE_SYMBOL);
        setStyle("-fx-cursor: default;");
    }
    
    // Callback setters
    
    /**
     * Set callback for when tile is left-clicked (reveal).
     */
    public void setOnTileClicked(TileClickListener listener) {
        this.onTileClicked = listener;
    }
    
    /**
     * Set callback for when tile is right-clicked (flag).
     */
    public void setOnTileFlagged(TileFlagListener listener) {
        this.onTileFlagged = listener;
    }
    
    // Callback interfaces
    
    /**
     * Callback for tile click (reveal) events.
     */
    @FunctionalInterface
    public interface TileClickListener {
        void onTileClicked(int row, int col);
    }
    
    /**
     * Callback for tile flag (right-click) events.
     */
    @FunctionalInterface
    public interface TileFlagListener {
        void onTileFlagged(int row, int col);
    }
    
    // Getters
    
    public TileViewModel getViewModel() {
        return viewModel;
    }
    
    public int getTileSize() {
        return tileSize;
    }
}
