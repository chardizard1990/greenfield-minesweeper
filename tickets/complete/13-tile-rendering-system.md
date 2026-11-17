# Ticket 13: Tile Rendering System

## Objective
Implement a reusable TileView component that visually represents a Minesweeper tile in all its states (unrevealed, revealed, flagged, mine). This is the core visual building block for the game board.

## Requirements

### 1. Tile State Representation
- [ ] Create TileView component extending Region or Pane
- [ ] Support all tile states:
  - Unrevealed (empty)
  - Revealed with 0 adjacent mines (blank)
  - Revealed with 1-8 adjacent mines (numbered)
  - Flagged (unrevealed but marked as mine)
  - Mine (revealed on game end)
- [ ] Display appropriate visuals for each state

### 2. Tile Styling
- [ ] Professional CSS styling for all states
- [ ] Color scheme:
  - Unrevealed: 3D raised button appearance (light gray)
  - Revealed (0 mines): flat light background
  - Revealed (1-8 mines): colored text (1=blue, 2=green, 3=red, etc.)
  - Flagged: flag symbol with background
  - Mine: red background with mine symbol
- [ ] Hover effects for unrevealed tiles
- [ ] Active state when clicked

### 3. TileViewModel
- [ ] Create TileViewModel for each tile's state
- [ ] Observable properties:
  - `tileState` (unrevealed, revealed, flagged, mine)
  - `adjacentMineCount` (0-8)
  - `isFlagged`
  - `isRevealed`
- [ ] No dependencies on game logic (pure presentation)

### 4. User Interaction Support
- [ ] Handle left-click (reveal tile)
- [ ] Handle right-click (toggle flag)
- [ ] Provide callbacks for game controller
- [ ] Visual feedback on mouse enter/exit
- [ ] Disable interactions when appropriate

### 5. Configuration
- [ ] Configurable tile size (default 30px)
- [ ] Responsive font sizing based on tile size
- [ ] Configurable colors if needed
- [ ] Support different visual themes (future)

## Success Criteria

- ✅ TileView renders correctly for all 5 states
- ✅ Tiles are visually distinct and professional looking
- ✅ Hover effects provide visual feedback
- ✅ TileViewModel properly manages tile state
- ✅ Click/right-click handlers work correctly
- ✅ Tile size is configurable
- ✅ Code is well-documented with examples
- ✅ No game logic in view or viewmodel (separation of concerns)

## Technical Notes

- TileView should use StackPane or similar to layer background, number, and flag
- Use ObservableProperty from Ticket 12 for tile state management
- CSS styling for clean separation of appearance from logic
- Number display should adapt to tile size (smaller tiles = no number)
- Flag symbol can be Unicode (🚩) or CSS-based

## Files to Create/Modify

**New Files:**
- `src/main/java/com/minesweeper/gui/components/TileView.java`
- `src/main/java/com/minesweeper/gui/viewmodel/TileViewModel.java`
- `src/main/java/com/minesweeper/gui/resources/tile-styles.css`
- `src/test/java/com/minesweeper/gui/components/TileViewTest.java`
- `src/test/java/com/minesweeper/gui/viewmodel/TileViewModelTest.java`

## Definition of Done

1. TileView component created and renders all states
2. TileViewModel manages tile state through observable properties
3. Click and right-click events properly handled
4. Professional CSS styling applied
5. Unit tests verify state transitions
6. Example usage shown in documentation
7. No game logic in view/viewmodel layers
8. All 216 existing tests still passing
9. Summary document created
10. Code is readable with helpful comments
