# Ticket 11: Base Application Structure & Scene Management

## Summary
Implement the foundational application structure with scene management system to enable navigation between different game screens.

## Details
- Create SceneManager class for managing scene transitions
- Implement Scene enumeration for different screens
- Create base controller pattern for all scenes
- Implement application state management
- Create scene registry/factory
- Support passing data between scenes
- Implement navigation stack for back button support

### Scenes to Support
- Main Menu (future Ticket 16 implementation)
- Mode Selection (future Ticket 16 implementation)
- Game Board (future Ticket 14 implementation)
- Game Over (future Ticket 18 implementation)
- Settings (future Ticket 21 implementation)
- Exit confirmation dialogs

### SceneManager Features
- Load scene from FXML or code
- Switch between scenes with transitions
- Maintain scene history for back navigation
- Pass parameters between scenes
- Support scene lifecycle (onCreate, onClose, onShow)

### Design Pattern
- **MVC Pattern:** Model-View-Controller separation
- **Scene Controller Base Class:** Abstract base for all scene controllers
- **Data Objects:** Parameter passing between scenes
- **Stage Management:** Single primary stage, multiple scenes

## Definition of Done
- [ ] SceneManager class implemented
- [ ] Scene enumeration created
- [ ] BaseSceneController abstract class created
- [ ] Application state management system working
- [ ] Scene transitions working without issues
- [ ] Data can be passed between scenes
- [ ] Navigation history/back support implemented
- [ ] All 198 MVP tests still pass
- [ ] Application builds and runs without errors

## Story Points
- 5

## Notes
- Keep scene management separate from game logic
- Support both FXML and programmatic scene creation
- Each scene should be independent and reusable
- Plan for async scene loading (future optimization)

## Acceptance Criteria
1. SceneManager successfully switches between scenes
2. Data can be passed and retrieved between scenes
3. Navigation stack works (back button support)
4. No breaking changes to existing code
5. All 198 tests still pass
6. Placeholder scenes for all planned screens
