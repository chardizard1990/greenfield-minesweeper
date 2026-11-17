# Ticket 17: Game Controls - Summary

Request: add controls and live game information to the board.

- Added new game, restart and settings navigation.
- Display remaining mines, elapsed time and active/won/lost status.
- Stop the timer and board interaction on game end; restart preserves the chosen
  dimensions and mine count and resets the board, counters and timer.
- Fixed mine-hit statistics so a loss records the move and freezes elapsed time.
- Added game-session and desktop GUI flow/timer tests.

Follow-up: settings functionality and pause are outside this ticket; the
settings button opens the existing placeholder scene.
