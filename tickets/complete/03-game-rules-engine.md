# Game Rules & Core Mechanics

# Summary
- Implement the core game rules: revealing tiles, flagging, win/loss detection, and cascading reveals.

# Details
- Implement reveal logic: single tile reveal and flood-fill for empty regions
- Implement flagging/unflagging tiles with bomb count tracking
- Implement win condition: all non-bomb tiles revealed
- Implement loss condition: bomb tile revealed
- Handle cascading reveals when a tile with 0 adjacent bombs is revealed
- Create GameState enum (ACTIVE, WON, LOST)
- Create AttackResult class to represent outcomes of tile reveals

# Definition of Done
- Revealing an empty tile (0 adjacent bombs) cascades and reveals adjacent tiles recursively
- Revealing a bomb ends the game with LOST state
- Revealing all non-bomb tiles results in WON state
- Flagging/unflagging works correctly and prevents revealing flagged tiles
- Remaining bomb count is tracked and updated when flagging/unflagging
- Cannot perform actions (reveal/flag) when game is in WON or LOST state
- Unit tests cover all game rule scenarios including edge cases

# Story Points
- 8

# Notes
- Cascading reveal algorithm is critical for smooth gameplay
