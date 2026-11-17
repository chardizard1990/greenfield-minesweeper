# 🎮 Quick Start Guide - Greenfield Minesweeper

## Running the Game

### Option 1: Interactive GUI (Recommended)
```bash
cd greenfield-minesweeper
mvn javafx:run
```
This launches the new JavaFX graphical interface with:
- Preset and custom board selection
- Left-click to reveal; right-click to flag or unflag
- New game, restart, mine count, elapsed time and status

The settings screen is a placeholder, and game animations are not yet wired in.
Custom boards accept widths and heights from 1 to 50, with fewer mines than tiles.
Large boards can be scrolled.

### Option 2: Console Gameplay (Classic)
```bash
cd greenfield-minesweeper
java -cp target/classes com.minesweeper.console.ConsoleUI
```

### Option 3: View Automated Demo
```bash
java -cp target/classes com.minesweeper.demo.FullGameplayDemo
```

### Option 4: Run All Tests
```bash
mvn test
```

---

## Console: How to Play

### Main Menu
```
1. Play vs AI           - Play against an AI opponent
2. Play Practice Mode   - Play solo to practice
3. How to Play          - Read the instructions
4. Exit                 - Close the game
```

### Difficulty Selection
```
1. Easy   (10x10 board,  10 mines)
2. Medium (20x20 board, 75 mines)
3. Hard   (30x30 board, 150 mines)
```

### AI Difficulty (vs AI mode only)
```
1. Easy   - AI makes random moves
2. Normal - AI analyzes board strategically
```

### Input Format
When prompted to make a move:
```
Enter row (0-9): [type row number]
Enter column (0-9): [type column number]
Action - (R)eveal or (F)lag?: [type R or F]
```

---

## Board Symbols

| Symbol | Meaning |
|--------|---------|
| `?` | Unrevealed tile (not yet clicked) |
| `F` | Flagged tile (you marked as mine) |
| ` ` (space) | Revealed safe tile with 0 adjacent mines |
| `1-8` | Number showing adjacent mines nearby |
| `*` | Mine (shown when game ends) |

---

## Strategy Tips

1. **Opening Move**: Click in a corner or center - statistically more likely to reveal safe tiles
2. **Number Analysis**: 
   - A "1" with one unrevealed neighbor → that neighbor must be a mine
   - A "2" with two unrevealed neighbors → both are likely mines
3. **Flagging**: Flag suspicious mines to keep track of your assumptions
4. **Cascading**: Clicking safe tiles with 0 adjacent mines automatically reveals neighbors

---

## Game States

- **ACTIVE**: Game is in progress, make your move
- **WON**: 🎉 All safe tiles revealed! You won!
- **LOST**: 💥 Hit a mine! Game over.

---

## Example Walkthrough

```
╔════════════════════════════════════════╗
║   Greenfield Minesweeper - Console MVP  ║
╚════════════════════════════════════════╝

┌─ Main Menu ─────────────────────────────┐
│ 1. Play vs AI                           │
│ 2. Play Practice Mode (No AI)           │
│ 3. How to Play                          │
│ 4. Exit                                 │
└─────────────────────────────────────────┘

Choice (1-4): 1
(You choose "Play vs AI")

┌─ Difficulty Selection ──────────────────┐
│ 1. Easy   (10x10, 10 mines)             │
│ 2. Medium (20x20, 75 mines)             │
│ 3. Hard   (30x30, 150 mines)            │
└─────────────────────────────────────────┐

Choice (1-3): 1
(You choose "Easy")

┌─ AI Difficulty ─────────────────────────┐
│ 1. Easy   (Random moves)                │
│ 2. Normal (Strategic analysis)          │
└─────────────────────────────────────────┘

Choice (1-2): 2
(You choose "Normal AI")

✓ Game started: EASY vs AI (NORMAL)

📋 Initial Board:

      0  1  2  3  4  5  6  7  8  9
   +---+---+---+---+---+---+---+---+---+---+
 0 | ? | ? | ? | ? | ? | ? | ? | ? | ? | ? |
   +---+---+---+---+---+---+---+---+---+---+
 ... (more rows)
 9 | ? | ? | ? | ? | ? | ? | ? | ? | ? | ? |
   +---+---+---+---+---+---+---+---+---+---+

┌─ Status ────────────────────────────────┐
│ Mines Left:     10                      │
│ Tiles Revealed:  0 / 90                 │
└─────────────────────────────────────────┘

Enter row (or 'q' to quit): 0
Enter column: 0
Action - (R)eveal or (F)lag?: R

✓ Revealed tile at (0,0) with 0 adjacent mines

... (board updates with revealed tiles)

🤖 AI is thinking...
🤖 Tile at (3,4) revealed with 2 adjacent mines

... (game continues with alternating moves)
```

---

## Keyboard Commands

- **Numbers 0-9**: Row/column input
- **R**: Reveal action
- **F**: Flag action
- **q**: Quit during gameplay
- **Numbers 1-4**: Menu selection

---

## Winning Conditions

**You Win When**:
- All non-mine tiles are revealed
- No mines are revealed
- Flagged mines are correct (bonus for accuracy)

**You Lose When**:
- You click on a tile with a mine 💣
- Game displays all mines at end

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Invalid coordinates" | Coordinates must be 0-9 (or higher for bigger boards) |
| "Tile is already revealed" | That tile was already clicked; try a different one |
| "Cannot flag revealed tile" | Only unrevealed tiles can be flagged |
| Game won't start | Ensure you're in the correct directory and Maven is installed |

---

## File Structure

```
greenfield-minesweeper/
├── src/main/java/com/minesweeper/
│   ├── gui/                        ← NEW: JavaFX GUI Layer
│   │   ├── GuiApplication.java       ← Main GUI entry point
│   │   ├── SceneManager.java         ← Scene navigation system
│   │   ├── BaseSceneController.java  ← Base for all scene controllers
│   │   ├── scenes/                   ← Individual scene controllers
│   │   │   ├── MainMenuController.java
│   │   │   ├── ModeSelectionController.java
│   │   │   ├── GameBoardController.java
│   │   │   ├── GameOverController.java
│   │   │   ├── SettingsController.java
│   │   │   └── MainMenuViewModel.java ← Example ViewModel
│   │   └── viewmodel/                ← Observable properties & data binding
│   │       ├── ViewModel.java
│   │       ├── ObservableProperty.java
│   │       └── PropertyBinding.java
│   ├── console/
│   │   ├── ConsoleUI.java          ← Classic console interface
│   │   └── BoardRenderer.java       ← Board display formatting
│   ├── game/
│   │   ├── GameController.java      ← Game orchestration
│   │   ├── GameRules.java           ← Game logic
│   │   ├── Board.java               ← Board state
│   │   └── ... (other game classes)
│   ├── ai/
│   │   ├── AIPlayer.java            ← AI opponent
│   │   ├── AIStrategy.java          ← Strategy interface
│   │   └── ... (other AI classes)
│   └── demo/
│       ├── ConsoleGameDemo.java     ← Simple demo
│       └── FullGameplayDemo.java    ← Full gameplay demo
└── src/test/java/com/minesweeper/
    ├── gui/viewmodel/               ← NEW: ViewModel unit tests
    │   ├── ObservablePropertyTest.java
    │   ├── PropertyBindingTest.java
    │   └── ViewModelTest.java
    └── ... (other test suites)
```

---

## Project Status

The GUI supports solo preset and custom games with a playable board and game
controls. AI play, settings, the separate results screen, and tile animation
integration remain future work.

The standard `mvn test` run skips desktop-dependent GUI tests. On a machine
with a desktop session, run them with:

```bash
mvn test -Dtest=GameFlowTest -Djavafx.integration=true
```

Some existing console and integration tests use random boards and can fail
intermittently.

---

## Need Help?

1. Run `java -cp target/classes com.minesweeper.console.ConsoleUI`
2. Select option 3 "How to Play" in the main menu
3. Read the in-game instructions
4. Try the demo with `java -cp target/classes com.minesweeper.demo.FullGameplayDemo`

---

**Have fun playing Greenfield Minesweeper! 🎮**
