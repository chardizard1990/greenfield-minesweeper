# Ticket 12: MVC/MVVM Pattern Setup

## Objective
Establish a robust MVC/MVVM pattern foundation for the GUI layer with data binding support, observable properties, and clean state management for all scene controllers.

## Requirements

### 1. ViewModel Base Class
- [ ] Create abstract `ViewModel` base class
- [ ] Implement observable property support
- [ ] Add lifecycle management (initialize, cleanup)
- [ ] Support data binding between Model and View

### 2. Observable Property System
- [ ] Create `ObservableProperty<T>` class for reactive properties
- [ ] Implement listeners/observers for property changes
- [ ] Support two-way binding
- [ ] Thread-safe property updates

### 3. Data Binding Utilities
- [ ] Create binding utilities for common UI patterns
- [ ] Implement string formatting bindings
- [ ] Support conditional bindings
- [ ] Add property change listeners

### 4. State Management
- [ ] Update `BaseSceneController` to work with ViewModels
- [ ] Implement state passing between scenes
- [ ] Add event bus or observer pattern for cross-scene communication
- [ ] Support scene data preservation on navigation

### 5. Example Implementation
- [ ] Update one controller (e.g., MainMenuController) to demonstrate pattern
- [ ] Show how to bind UI elements to ViewModel properties
- [ ] Demonstrate state updates and property changes
- [ ] Document the pattern for future controllers

### 6. Testing Foundation
- [ ] Ensure ViewModel can be tested independently
- [ ] Add basic unit tests for ViewModel and property system
- [ ] Verify existing 198 tests still pass

## Success Criteria

- ✅ ViewModel pattern is clean and reusable
- ✅ Observable properties work for all data types
- ✅ Scene controllers easily bind to ViewModels
- ✅ No game logic is duplicated; GUI is pure presentation layer
- ✅ Pattern is well-documented for other controllers to follow
- ✅ All 198 MVP tests still pass
- ✅ Code is readable with helpful comments

## Technical Notes

- Keep ViewModels lightweight and focused on state management
- ViewModels should NOT contain UI logic or JavaFX imports (except for Platform.runLater if needed)
- Use Java's built-in observer pattern or create simple listener interfaces
- Ensure thread safety for property updates (UI updates on JavaFX thread)
- Property changes should trigger UI updates automatically

## Files to Create/Modify

**New Files:**
- `src/main/java/com/minesweeper/gui/viewmodel/ViewModel.java`
- `src/main/java/com/minesweeper/gui/viewmodel/ObservableProperty.java`
- `src/main/java/com/minesweeper/gui/viewmodel/PropertyBinding.java`
- `src/main/java/com/minesweeper/gui/viewmodel/ObserverList.java`
- `src/test/java/com/minesweeper/gui/viewmodel/ViewModelTest.java`
- `src/test/java/com/minesweeper/gui/viewmodel/ObservablePropertyTest.java`

**Modified Files:**
- `src/main/java/com/minesweeper/gui/BaseSceneController.java` (add ViewModel support)
- `src/main/java/com/minesweeper/gui/scenes/MainMenuController.java` (example implementation)

## Definition of Done

1. ViewModel base class created and documented
2. ObservableProperty system working for all data types
3. BaseSceneController updated to support ViewModels
4. MainMenuController refactored to demonstrate pattern
5. Unit tests pass for ViewModel and property system
6. All 198 MVP tests still passing
7. Code is readable with comments explaining the pattern
8. Summary document created with implementation details
