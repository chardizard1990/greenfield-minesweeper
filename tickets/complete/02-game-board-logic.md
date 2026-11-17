# Game Board & Mine Logic

# Summary
- Implement the core game board data structure and mine placement logic.

# Details
- Create Board class to represent the game grid
- Implement mine placement algorithm (random placement for all difficulty levels)
- Create Tile class to represent individual board cells with states (unrevealed, revealed, flagged)
- Implement logic to track bombs at specific coordinates
- Create utility methods to get adjacent tiles and bomb counts
- Support configurable board dimensions (10x10, 20x20, 30x30, custom sizes)

# Definition of Done
- Board can be initialized with custom width, height, and bomb count
- Mines are randomly placed and verified
- Tiles can track their state (unrevealed/revealed/flagged)
- getAdjacentTiles() returns correct neighboring tiles
- getAdjacentBombCount() correctly counts bombs around a tile
- Board state can be queried for debugging
- Unit tests cover board initialization and mine placement

# Story Points
- 5

# Notes
- Consider immutability or defensive copying for board queries
