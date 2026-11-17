# Greenfield Minesweeper - Build & Development Guide

## Project Overview
AI-generated Minesweeper game implemented in Java with a layered architecture supporting both console (MVP) and GUI (final) versions.

## Technology Stack
- **Language**: Java 11+
- **Build Tool**: Maven 3.6+
- **Testing**: JUnit 5, Mockito
- **Console MVP**: Standard Java
- **GUI Version**: JavaFX (future)

## Project Structure
```
greenfield-minesweeper/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/java/com/minesweeper/
│   │   ├── game/                    # Core game logic (100% reusable)
│   │   │   ├── Board.java
│   │   │   ├── GameRules.java
│   │   │   ├── DifficultyMode.java
│   │   │   ├── Cell.java
│   │   │   └── GameState.java
│   │   ├── ai/                      # AI engine
│   │   │   ├── AIPlayer.java
│   │   │   └── MoveStrategy.java
│   │   ├── observer/                # Observer pattern for spectator mode
│   │   │   ├── GameObserver.java
│   │   │   └── GameEvent.java
│   │   ├── console/                 # Console UI (MVP only)
│   │   │   ├── ConsoleGame.java
│   │   │   └── ConsoleUI.java
│   │   └── Main.java                # Entry point
│   ├── resources/
│   │   └── config/
│   │       └── game-config.properties
│   └── test/java/com/minesweeper/
│       ├── game/
│       │   ├── BoardTest.java
│       │   ├── GameRulesTest.java
│       │   └── DifficultyModeTest.java
│       ├── ai/
│       │   └── AIPlayerTest.java
│       └── console/
│           └── ConsoleGameTest.java
└── README.md

```

## Getting Started

### Prerequisites
- Java 11 or higher: `java -version`
- Maven 3.6 or higher: `mvn -version`

### Build the Project
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package

# Run the application
java -cp target/greenfield-minesweeper-0.1.0-MVP.jar com.minesweeper.console.ConsoleGame
```

### IDE Setup

#### IntelliJ IDEA
1. Open project root in IntelliJ
2. Mark `src/main/java` as Sources Root
3. Mark `src/test/java` as Test Sources Root
4. Maven should auto-configure dependencies

#### VS Code
1. Install "Extension Pack for Java" (Microsoft)
2. Install "Maven for Java" (Microsoft)
3. Open project root; VS Code will detect Maven configuration
4. Run/Debug via VS Code's Java Test Explorer

## Development Workflow

### Running Tests
```bash
# All tests
mvn test

# Single test class
mvn test -Dtest=BoardTest

# Single test method
mvn test -Dtest=BoardTest#testMinePlacement
```

### Code Organization

**Game Logic Layer** (100% reusable across versions):
- `com.minesweeper.game.*` - Core game rules, board, difficulty modes
- `com.minesweeper.ai.*` - AI player engine
- `com.minesweeper.observer.*` - Event/observer pattern for decoupling

**UI Layer** (replaceable):
- `com.minesweeper.console.*` - Console interface for MVP

## Layered Architecture

This project uses **clean architecture** to enable code reuse between MVP and final versions:

```
┌─────────────────────────┐
│   UI Layer (Replaceable)│
│  ┌────────────────────┐ │
│  │ Console (MVP)      │ │
│  │ JavaFX (Final)     │ │
│  └────────────────────┘ │
└──────────┬──────────────┘
           │ Observers/Events
┌──────────▼──────────────┐
│  Game Logic Layer       │
│  (100% Reusable)        │
│  ┌────────────────────┐ │
│  │ Game Rules         │ │
│  │ Board Management   │ │
│  │ AI Engine          │ │
│  │ Difficulty Modes   │ │
│  └────────────────────┘ │
└─────────────────────────┘
```

**Key Principle**: Game logic **never imports** UI libraries. Communication happens through observers and events.

## CI/CD Pipeline (Future)

- GitHub Actions for automated testing on PR/commit
- Code coverage reporting with JaCoCo
- Automated builds for release versions

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `mvn: command not found` | Ensure Maven is installed and in PATH |
| `No compiler is provided` | Ensure JAVA_HOME environment variable is set |
| Tests fail to run | Run `mvn clean compile test` to rebuild |
| Cannot find main class | Verify `com.minesweeper.console.ConsoleGame` exists |

## Next Steps
- Ticket #2: Implement Game Board Logic
- Ticket #3: Implement Game Rules Engine
- Ticket #4: Implement Difficulty Modes
- Tickets #5-#9: Continue in dependency order

