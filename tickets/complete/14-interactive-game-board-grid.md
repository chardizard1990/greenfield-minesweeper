# Ticket 14: Interactive Game Board Grid

## Objective
Implement a GameBoardView component that displays the full Minesweeper game board as a grid of TileView components. This connects the game logic with the visual presentation through reactive updates.

## Requirements

### 1. GameBoardViewModel
- [ ] Create ViewModel for board state management
- [ ] Observable properties:
  - `boardWidth` - Number of columns
  - `boardHeight` - Number of rows
  - `tiles` - ObservableList of TileViewModels (or 2D array)
  - `gameState` - Current game state (ACTIVE, WON, LOST)
- [ ] Methods to get/create tiles at specific positions
- [ ] No game logic - only board presentation state

### 2. GameBoardView Component
- [ ] Create component extending Pane or GridPane
- [ ] Dynamic grid layout based on boardWidth and boardHeight
- [ ] Add TileView for each tile in the grid
- [ ] Responsive sizing - fills available space
- [ ] Maintain aspect ratio (square tiles in square grid)

### 3. Event Wiring
- [ ] Collect click events from all TileView components
- [ ] Wire events to external callbacks:
  - `onTileRevealed(row, col)` - pass to GameController.reveal()
  - `onTileFlagged(row, col)` - pass to GameController.flag()
- [ ] Provide listener interfaces for game controller integration

### 4. Game State Integration
- [ ] Connect to game board state from GameController
- [ ] Listen for board updates from game events
- [ ] Update TileViewModels when game state changes
- [ ] Display tile counts and game status

### 5. Visual Layout
- [ ] Responsive grid that scales with window size
- [ ] Proper spacing between tiles (1-2px gap)
- [ ] Center grid in available space
- [ ] Support different difficulty levels (10x10, 20x20, 30x30, custom)

## Success Criteria

- ✅ Board displays correctly for all difficulty levels
- ✅ Tiles render in correct positions
- ✅ Click events work and trigger callbacks
- ✅ Board updates when game state changes
- ✅ Responsive layout that scales with window
- ✅ No game logic in components (pure presentation)
- ✅ Proper separation between ViewModel and View
- ✅ All existing tests still pass

## Technical Notes

- Use GridPane for automatic tile layout
- Each tile is a TileView component bound to a TileViewModel
- GameBoardViewModel holds 2D array of TileViewModels
- Reactive updates: when GameController updates game board, TileViewModels update automatically
- Consider using VBox/HBox for responsive scaling

## Files to Create/Modify

**New Files:**
- `src/main/java/com/minesweeper/gui/components/GameBoardView.java`
- `src/main/java/com/minesweeper/gui/viewmodel/GameBoardViewModel.java`
- `src/test/java/com/minesweeper/gui/components/GameBoardViewTest.java`
- `src/test/java/com/minesweeper/gui/viewmodel/GameBoardViewModelTest.java`

## Definition of Done

1. GameBoardViewModel created with proper state management
2. GameBoardView displays grid of TileView components
3. Dynamic grid sizing works for all modes
4. Click/flag events properly wired
5. Responsive layout implemented
6. Unit tests verify state management
7. All 230 existing tests still passing
8. Code is readable with helpful comments
9. No game logic in view/viewmodel layers
10. Summary document created
