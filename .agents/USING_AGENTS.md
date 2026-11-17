# Using Agents in Greenfield Minesweeper

This guide explains how to invoke and work with agents in this repository.

## Quick Start

### Invoking an Agent

Use the **`task` tool** to spawn an agent:

```javascript
task(
  name: "Agent Name",
  agent_type: "general-purpose",
  prompt: "Your task here, with full context",
  description: "Brief 3-5 word description",
  mode: "sync"
)
```

### Synchronous vs. Asynchronous

- **`mode: "sync"`** — Wait for the agent to complete; use for blocking work
- **`mode: "background"`** — Agent runs independently; you get notified when done

## Common Use Cases

### 1. Implement Core Game Logic

```javascript
task(
  name: "Implement Minesweeper Logic",
  agent_type: "general-purpose",
  prompt: `
    Implement the core minesweeper game logic in code/.
    
    Requirements from requirements.md:
    - Support Easy (10x10, 1 bomb), Medium (20x20, 2 bombs), Hard (30x30, 3 bombs)
    - Support Custom mode with user-defined dimensions
    - Calculate proximity numbers for revealed tiles
    - Handle game state transitions (playing, won, lost)
    - Flood-fill algorithm for empty tiles
    
    Success criteria:
    - All unit tests pass
    - Game rules validated by tests
    - Logic separable from GUI
  `,
  description: "Implement core game logic",
  mode: "sync"
)
```

### 2. Build GUI Implementation

```javascript
task(
  name: "Build Minesweeper GUI",
  agent_type: "general-purpose",
  prompt: `
    Implement the GUI for minesweeper in code/.
    
    Requirements:
    - Display board as grid with tiles
    - Show proximity numbers from adjacent bombs
    - Highlight revealed, flagged, and unrevealed tiles
    - Provide buttons for game modes (Easy, Medium, Hard, Custom)
    - Display game status (playing, won, lost)
    - Custom mode: input fields for board dimensions and bomb count
    
    Use the most appropriate GUI framework for the chosen language.
  `,
  description: "Build GUI interface",
  mode: "sync"
)
```

### 3. Code Review

```javascript
task(
  name: "Review Minesweeper Logic",
  agent_type: "code-review",
  prompt: `
    Review the minesweeper game logic implementation in code/ for:
    - Correctness of bomb placement and proximity calculations
    - Edge cases (board boundaries, repeated clicks, flagging)
    - Performance with large boards (30x30)
    - Code quality and maintainability
  `,
  description: "Review game logic",
  mode: "sync"
)
```

### 4. Run Long Tests in Background

```javascript
task(
  name: "Full Test Suite",
  agent_type: "task",
  prompt: `
    Run all unit tests for the minesweeper project.
    
    Commands depend on the tech stack:
    - If Python: python -m pytest
    - If JavaScript: npm test
    - If Java: mvn clean test
    - If C#: dotnet test
    
    Report all failures, coverage percentage, and time taken.
  `,
  description: "Run all tests",
  mode: "background"
)
```

Then continue with other work. When done, you'll be notified and can read results.

### 5. AI Opponent Development

```javascript
task(
  name: "Implement AI Observer",
  agent_type: "general-purpose",
  prompt: `
    Implement an observer mode AI that plays minesweeper automatically.
    
    Requirements from requirements.md:
    - AI should reveal tiles strategically
    - Start with corners (lower bomb probability)
    - Use proximity numbers to infer safe tiles
    - Learn from revealed information
    - Support difficulty levels (easy random, medium strategic, hard advanced)
    
    Create observerable hooks so the GUI can watch and display AI moves.
  `,
  description: "Implement AI opponent",
  mode: "sync"
)
```

### 6. Plan Release Features

```javascript
task(
  name: "Plan MVP Completion",
  agent_type: "Planner",
  prompt: `
    Given the requirements in requirements.md, create a prioritized
    implementation plan for the MVP:
    
    - Console version proving logic works
    - Multiple game modes (Easy, Medium, Hard, Custom)
    - Unit test coverage
    - Observer mode for AI
    
    Break into phases with dependencies and effort estimates.
  `,
  description: "Plan MVP roadmap",
  mode: "sync"
)
```

### 7. Test Strategy and Coverage

```javascript
task(
  name: "Design Test Coverage",
  agent_type: "Tester",
  prompt: `
    Design comprehensive test coverage for minesweeper:
    
    - Unit tests for game logic
    - Edge cases (1x1 board, all bombs, no bombs, custom sizes)
    - Game modes verification
    - Proximity calculation accuracy
    - AI opponent strategy validation
    
    What test cases are essential? What's the minimum coverage?
  `,
  description: "Design test coverage",
  mode: "sync"
)
```

## Working with Agent Results

### For Synchronous Agents

The agent runs and returns output immediately:

```javascript
// Agent finishes and you see the result
result = task(mode: "sync", ...)
// Use result.output, result.success, etc.
```

### For Background Agents

The agent runs independently:

```javascript
// Start the agent
task(
  name: "Build Project",
  agent_type: "task",
  prompt: "npm run build",
  mode: "background"
)

// Meanwhile, you continue with other tools
// When done, you get a notification
// Then read the results
read_agent(agent_id: "...", wait: false)
```

## Agent Communication

### Single-Turn Agents

Most agents complete in one turn. They receive your task description and return results.

### Multi-Turn Agents (Advanced)

For complex work, you can send follow-up messages:

```javascript
// Start long-running work
agent = task(
  name: "Build Game MVP",
  agent_type: "general-purpose",
  prompt: "Start implementing the console version...",
  mode: "background"
)

// Wait for it to do initial work, then follow up
write_agent(agent_id: agent.id, message: "Now add comprehensive unit tests for all modes")

// Read the updated results
read_agent(agent_id: agent.id)
```

## Delegation Best Practices

### ✅ DO

- **Be specific** — Include file paths, error messages, and exact requirements
- **Provide context** — Quote relevant parts of requirements.md
- **Use appropriate agents** — Match task complexity to agent capability
- **Batch independent work** — Run multiple agents in parallel (GUI + logic)
- **Stop agents early** — If an agent is stuck, check status and restart
- **Document decisions** — Save summaries in `.agents/summaries/`
- **Link to tickets** — Reference tickets in `tickets/` when delegating

### ❌ DON'T

- **Chain trivial tasks** — Don't delegate a simple grep or file read
- **Stack agents** — Don't have an agent launch another agent
- **Re-check direct work** — Agents shouldn't re-verify your own tool calls
- **Vague prompts** — "Fix the game" is too undefined; cite requirements
- **Forget context** — Include mode definitions, tech stack details, success criteria

## Repository Structure for Agents

Agents in Greenfield Minesweeper should be aware of:

```
code/                           — Source code
  <language-dependent structure>
  
tests/                          — Test files
  <language-dependent structure>
  
tickets/
  incomplete/                   — Open tasks
  complete/                     — Finished work
  summaries/                    — Detailed ticket summaries
  
.agents/
  summaries/                    — Agent work summaries
  logs/                         — Agent execution logs
  CREATING_AGENTS.md            — This guide
  USING_AGENTS.md               — How to use agents
  
requirements.md                 — Game requirements
readme.md                       — Project overview
```

## Project Context for Agents

When delegating work, remind agents of key requirements:

**MVP Features:**
- Console version proving logic works
- Multiple modes: Easy (10x10, 1 bomb), Medium (20x20, 2 bombs), Hard (30x30, 3 bombs), Custom
- Unit test coverage
- Observer mode watching AI play

**Final Version:**
- GUI implementation
- All modes from MVP
- Proximity numbers displayed on tiles

**Technology:** No language restrictions; use most appropriate tech

## Monitoring Agent Health

After spawning an agent, you can check its status. If an agent is taking too long or producing unexpected results:

1. Check its current status
2. Review any error output
3. Consider stopping and restarting with refined instructions

## Example Workflow: Minesweeper MVP

1. **Plan** (Planner agent) — Break MVP into phases
2. **Logic** (General-purpose agent) — Implement core game logic
3. **Tests** (Task agent) — Run comprehensive tests
4. **Console** (General-purpose agent) — Build console interface
5. **Review** (Code-review agent) — Validate implementation
6. **AI** (General-purpose agent) — Implement AI observer
7. **Final Test** (Task agent) — Full regression test suite

Each agent focuses on one component while you coordinate and verify results.

## Tips for Success

1. **Start with logic** — Game logic is the foundation; get it right first
2. **Test early** — Implement tests alongside logic, not after
3. **Provide requirements** — Always reference `requirements.md` in prompts
4. **Break large tasks** — Implement console version before GUI
5. **Keep context fresh** — Include recent error messages and test failures
6. **Document decisions** — Save a summary after each major phase completes

## See Also

- [CREATING_AGENTS.md](./CREATING_AGENTS.md) — How to create custom agents
- [../readme.md](../readme.md) — Project overview
- [../requirements.md](../requirements.md) — Full game requirements
- [../tickets/](../tickets/) — Ticket system and tracking
