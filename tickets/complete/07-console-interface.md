# Console User Interface

# Summary
- Implement a text-based console interface for MVP to prove game logic works end-to-end.

# Details
- Create menu system for mode selection (Easy, Medium, Hard, Custom)
- Implement console display of board state:
  - Unrevealed tiles shown as '?'
  - Revealed empty tiles shown as ' ' (space)
  - Revealed numbered tiles show bomb count (1-8)
  - Flagged tiles shown as 'F'
  - Game over reveals bombs as '*'
- Implement player input parsing (row, column, action type: reveal/flag)
- Display game status messages (remaining bombs, game won/lost, invalid moves)
- Implement game loop in console (menu -> mode selection -> play -> end)
- Display game statistics on completion

# Definition of Done
- Main menu displays mode selection options
- Board renders correctly in console showing all tile states
- Player can input coordinate and action type successfully
- Game displays appropriate feedback for each action
- Game displays win/loss message and final board state
- Statistics display at end of game (moves, time, etc)
- Console UI is clean and readable
- Integration tests verify full game flow from menu to completion

# Story Points
- 5

# Notes
- Console implementation proves all game logic before GUI is built
- Focus on clarity and usability for testing
