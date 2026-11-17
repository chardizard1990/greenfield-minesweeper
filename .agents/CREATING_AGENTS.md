# Creating Custom Agents for Greenfield Minesweeper

This guide explains how to create custom agents that are scoped locally to the Greenfield Minesweeper repository.

## What are Agents?

Agents are specialized AI assistants that run in separate context windows, allowing you to delegate specific tasks while maintaining focus on other work. They can run synchronously (wait for completion) or asynchronously (run in the background).

## Repository-Scoped Agents

The `.agents` folder in this repository contains guides and documentation for custom agents available only within Greenfield Minesweeper. This keeps your team's specialized workflows centralized and consistent.

## Supported Agent Types

### Built-in Agent Types

- **`explore`** — Read-only exploration for research requiring separate context
  - Best for: Investigating multiple independent threads, analyzing codebase sections
  - Tools: Read-only file/search tools only
  
- **`task`** — Runs commands, tests, builds, lints, installs
  - Best for: Verbose operations like builds, test suites, linting
  - Tools: CLI tools, file operations
  
- **`general-purpose`** — Full-capability agent for implementation and debugging
  - Best for: Complex coding tasks, bug fixes, refactoring, feature implementation
  - Tools: All available tools
  
- **`code-review`** — Specialized code review agent
  - Best for: Reviewing staged/unstaged changes, branch diffs, pull requests
  - Tools: Read-only code inspection, linting analysis
  
- **`rubber-duck`** — Reviews plans and implementations for logic errors
  - Best for: Validating algorithm correctness, catching edge cases, design review
  - Tools: Read-only inspection, logic analysis
  
- **`research`** — Thorough GitHub and web research with citations
  - Best for: Gathering background on technologies, analyzing trends, finding solutions
  - Tools: GitHub, web search, citations
  
- **`security-review`** — Specialized security vulnerability review
  - Best for: Security audits and vulnerability assessment
  - Tools: Read-only inspection, security-specific analysis

### Custom Agents for Greenfield Minesweeper

You can define custom agents tailored to this project's needs. Consider creating agents for:

- **GameDesigner** — Agent focused on game logic, rules validation, and mechanics testing
- **TestEngineer** — Agent specialized in test coverage, edge cases, and test strategy
- **FrontendDeveloper** — Agent focused on GUI implementation and UI/UX
- **BackendDeveloper** — Agent focused on game logic, AI opponent, and core mechanics
- **QA** — Agent for comprehensive testing and bug hunting

## How to Define a Custom Agent

Custom agents for this repository are defined by specifying them when you invoke an agent through the **task** tool. When you invoke an agent, specify:

- **`agent_type`** — The agent type (use built-in or custom name)
- **`name`** — A short, descriptive name for this agent instance
- **`prompt`** — Your task description; include complete context
- **`description`** — 3-5 word UI intent (e.g., "Review game logic")
- **`mode`** — `sync` (wait for completion) or `background` (run independently)

## Example: Invoking an Agent

```
task(
  agent_type: "general-purpose",
  name: "Implement Minesweeper Logic",
  prompt: "Implement the core minesweeper game logic...",
  description: "Implement core game logic",
  mode: "sync"
)
```

For background mode (e.g., running tests):
```
task(
  agent_type: "task",
  name: "Run Test Suite",
  prompt: "Run all tests with coverage reporting...",
  description: "Run all tests",
  mode: "background"
)
```

## Best Practices

1. **Clear Scope** — Define what the agent should do, not how it should do it
2. **Provide Context** — Include relevant file paths, error messages, and requirements from requirements.md
3. **Use Appropriate Type** — Match the agent type to the task complexity
4. **Bounded Work** — Give clear stopping conditions and success criteria
5. **Independent Tasks** — Use multiple agents for parallel work rather than sequential tasks
6. **Review Results** — Always validate the agent's output before merging
7. **Document Decisions** — Save summaries in the `summaries/` folder for future reference

## When to Use Agents vs. Direct Tools

| Situation | Use Agent | Use Direct Tools |
|-----------|-----------|-----------------|
| Simple file inspection | ❌ | ✅ grep, view |
| Complex feature implementation | ✅ | ❌ |
| Running test suite (long) | ✅ background | ❌ |
| Code review (small diff) | ❌ | ✅ direct read |
| Extensive refactoring | ✅ | ❌ |
| Algorithm validation | ✅ rubber-duck | ❌ |
| Single bug fix | Depends on complexity | Often direct |

## Monitoring Agents

- **Synchronous agents** — Block until completion; use for critical path work
- **Asynchronous agents** — Run in background; use while you do other work
- **Multiple agents** — Can run in parallel; useful for independent tasks like UI and game logic

After an async agent completes, you'll be notified. Use `read_agent` to fetch the full output.

## Repository-Specific Considerations

Greenfield Minesweeper is a game development project with:
- Core game logic in `code/`
- Configuration and requirements in `requirements.md`
- Tests (location TBD based on tech stack)
- Project management in `tickets/`

Custom agents might specialize in:
- Game mechanic validation and correctness
- AI opponent behavior testing
- GUI/UX improvements and layout
- Cross-platform compatibility testing
- Minesweeper game rules compliance

## Project Modes to Understand

The game supports multiple difficulty modes that agents should be aware of:
- **Easy** — 10x10 board with 1 bomb
- **Medium** — 20x20 board with 2 bombs
- **Hard** — 30x30 board with 3 bombs
- **Custom** — User-defined dimensions and bomb count

## Logging and Summaries

After completing agent work, consider documenting results:
- Agent logs are stored in `.agents/logs/`
- Create summaries in `.agents/summaries/` for future reference
- Link summaries to related tickets in `tickets/summaries/`

## See Also

- [USING_AGENTS.md](./USING_AGENTS.md) — How to invoke and work with agents
- [../readme.md](../readme.md) — Project overview
- [../requirements.md](../requirements.md) — Full project requirements
- [../tickets/](../tickets/) — Ticket system and project tracking
