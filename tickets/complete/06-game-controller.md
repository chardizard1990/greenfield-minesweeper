# Game Controller & State Management

# Summary
- Create central game controller to orchestrate game flow and manage player interactions.

# Details
- Create GameController class managing game initialization, turn execution, and state transitions
- Implement player input handling (coordinate validation, action type)
- Implement game loop logic for single player vs AI modes
- Track game statistics (time elapsed, moves made, accuracy)
- Create game reset functionality
- Implement save/load game state (optional for MVP, nice-to-have)
- Handle turn-based mechanics properly

# Definition of Done
- GameController initializes games with selected mode
- Player moves are validated before execution
- Game state transitions correctly (ACTIVE -> WON/LOST)
- Game loop runs until win/loss condition
- Statistics are tracked accurately
- Game can be reset to initial state
- Unit tests verify game flow and state transitions

# Story Points
- 5

# Notes
- Observer pattern integration for AI monitoring covered in separate ticket
