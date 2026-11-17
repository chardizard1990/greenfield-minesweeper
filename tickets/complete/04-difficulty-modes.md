# Game Difficulty Modes

# Summary
- Implement predefined difficulty levels (Easy, Medium, Hard) and custom mode configuration.

# Details
- Create GameMode enum or class with presets:
  - Easy: 10x10 board with 10 bombs
  - Medium: 20x20 board with 75 bombs
  - Hard: 30x30 board with 150 bombs
- Implement CustomMode configuration allowing user-defined dimensions and bomb count
- Create GameFactory or Builder pattern to initialize games based on selected mode
- Validate custom mode inputs (reasonable bounds, bomb count <= total tiles)
- Provide mode metadata (name, description, difficulty rating)

# Definition of Done
- Easy mode initializes 10x10 board with 10 bombs
- Medium mode initializes 20x20 board with 75 bombs
- Hard mode initializes 30x30 board with 150 bombs
- Custom mode accepts width, height, and bomb count parameters
- Input validation prevents invalid configurations
- Factory method creates appropriate game instances
- Unit tests verify all difficulty configurations

# Story Points
- 3

# Notes
- Bomb counts updated from requirements (original was 1,2,3)
