package com.minesweeper.game;

import com.minesweeper.ai.AIPlayer;
import com.minesweeper.ai.AIDifficulty;
import com.minesweeper.ai.AIMove;
import com.minesweeper.ai.AIPlayerFactory;
import com.minesweeper.ai.MoveType;

import java.util.Optional;

/**
 * Central game controller orchestrating game flow and player interactions.
 * 
 * Responsibilities:
 * - Initialize and manage games
 * - Handle player moves (validation and execution)
 * - Manage game state transitions
 * - Track game statistics
 * - Support both human and AI players
 * - Provide game reset functionality
 * 
 * This class acts as a facade, coordinating between:
 * - GameRules (game logic and state)
 * - GameStatistics (move tracking and analytics)
 * - AIPlayer (autonomous gameplay)
 */
public class GameController {

    private final String gameId;
    private GameRules gameRules;
    private GameStatistics statistics;
    private GameMode gameMode;
    private CustomGameMode customGameMode;
    private AIPlayer aiPlayer;
    private boolean isHumanVsAI;

    /**
     * Creates a game controller for a new game.
     *
     * @param gameId identifier for this game session
     * @param gameRules the game rules for this session
     * @param gameMode the difficulty/mode this game uses
     * @throws IllegalArgumentException if any parameter is null
     */
    public GameController(String gameId, GameRules gameRules, GameMode gameMode) {
        this(gameId, gameRules, gameMode, null);
    }

    /**
     * Creates a controller for a custom configuration without labelling it as a preset.
     */
    public static GameController forCustomGame(String gameId, CustomGameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Custom game mode cannot be null");
        }
        return new GameController(gameId, GameFactory.createGame(mode), null, mode);
    }

    private GameController(String gameId, GameRules gameRules, GameMode gameMode,
                           CustomGameMode customGameMode) {
        if (gameId == null || gameId.isEmpty()) {
            throw new IllegalArgumentException("Game ID cannot be null or empty");
        }
        if (gameRules == null) {
            throw new IllegalArgumentException("GameRules cannot be null");
        }
        if (gameMode == null && customGameMode == null) {
            throw new IllegalArgumentException("GameMode cannot be null");
        }

        this.gameId = gameId;
        this.gameRules = gameRules;
        this.gameMode = gameMode;
        this.customGameMode = customGameMode;
        this.statistics = new GameStatistics();
        this.isHumanVsAI = false;
    }

    // Accessors
    public String getGameId() {
        return gameId;
    }

    public GameRules getGameRules() {
        return gameRules;
    }

    /** Returns the preset mode, or null for a custom game. */
    public GameMode getGameMode() {
        return gameMode;
    }

    /** Returns the custom configuration, or null for a preset game. */
    public CustomGameMode getCustomGameMode() {
        return customGameMode;
    }

    public GameState getGameState() {
        return gameRules.getGameState();
    }

    public GameStatistics getStatistics() {
        return statistics;
    }

    public AIPlayer getAIPlayer() {
        return aiPlayer;
    }

    public boolean isHumanVsAI() {
        return isHumanVsAI;
    }

    public Board getBoard() {
        return gameRules.getBoard();
    }

    /**
     * Initializes an AI opponent for this game.
     * Sets up the game for human vs AI mode.
     * 
     * @param aiDifficulty the difficulty level for the AI
     * @throws IllegalStateException if game has already started (moves made)
     */
    public void initializeAIOpponent(AIDifficulty aiDifficulty) {
        if (statistics.getTotalMoves() > 0) {
            throw new IllegalStateException("Cannot initialize AI after game has started");
        }

        this.aiPlayer = AIPlayerFactory.createAI("AI-" + aiDifficulty, aiDifficulty, gameRules);
        this.isHumanVsAI = true;
    }

    /**
     * Processes a player move.
     * 
     * - Validates coordinates and game state
     * - Executes the move through GameRules
     * - Updates statistics
     * - Returns detailed result
     * 
     * @param move the player's move
     * @return MoveResult with outcome and details
     */
    public MoveResult processPlayerMove(PlayerMove move) {
        // Check game state first
        if (gameRules.getGameState() != GameState.ACTIVE) {
            return new MoveResult(
                    MoveResult.Outcome.GAME_NOT_ACTIVE,
                    gameRules.getGameState(),
                    "Game is not active - " + gameRules.getGameState(),
                    null, null
            );
        }

        // Validate coordinates
        if (!gameRules.getBoard().isWithinBounds(move.getRow(), move.getColumn())) {
            return new MoveResult(
                    MoveResult.Outcome.INVALID_COORDINATES,
                    gameRules.getGameState(),
                    String.format("Invalid coordinates: (%d,%d)", move.getRow(), move.getColumn()),
                    null, null
            );
        }

        // Execute the move
        switch (move.getActionType()) {
            case REVEAL:
                return handleRevealMove(move);
            case FLAG:
                return handleFlagMove(move);
            default:
                return new MoveResult(
                        MoveResult.Outcome.MOVE_EXECUTION_FAILED,
                        gameRules.getGameState(),
                        "Unknown action type: " + move.getActionType(),
                        null, null
                );
        }
    }

    /**
     * Handles a reveal action.
     * 
     * @param move the reveal move
     * @return MoveResult with execution details
     */
    private MoveResult handleRevealMove(PlayerMove move) {
        RevealResult revealResult = gameRules.revealTile(move.getRow(), move.getColumn());

        if (!revealResult.isSuccess() && !revealResult.isHitMine()) {
            return new MoveResult(
                    MoveResult.Outcome.MOVE_EXECUTION_FAILED,
                    gameRules.getGameState(),
                    revealResult.getMessage(),
                    revealResult, null
            );
        }

        // Track statistics
        statistics.recordRevealAction(revealResult.isHitMine());

        // Check if game ended
        if (gameRules.getGameState() != GameState.ACTIVE) {
            statistics.finishGame();
        }

        return new MoveResult(
                MoveResult.Outcome.SUCCESS,
                gameRules.getGameState(),
                revealResult.getMessage(),
                revealResult, null
        );
    }

    /**
     * Handles a flag toggle action.
     * 
     * @param move the flag move
     * @return MoveResult with execution details
     */
    private MoveResult handleFlagMove(PlayerMove move) {
        FlagResult flagResult = gameRules.toggleFlag(move.getRow(), move.getColumn());

        if (!flagResult.isSuccess()) {
            return new MoveResult(
                    MoveResult.Outcome.MOVE_EXECUTION_FAILED,
                    gameRules.getGameState(),
                    flagResult.getMessage(),
                    null, flagResult
            );
        }

        // Track statistics
        // Check if the flagged tile is actually a mine
        Tile tile = gameRules.getBoard().getTile(move.getRow(), move.getColumn());
        statistics.recordFlagAction(flagResult.isFlaggedNow(), tile.isMine());

        return new MoveResult(
                MoveResult.Outcome.SUCCESS,
                gameRules.getGameState(),
                flagResult.getMessage(),
                null, flagResult
        );
    }

    /**
     * Executes one move by the AI opponent.
     * Only valid for human vs AI games.
     * 
     * @return MoveResult of the AI's move
     * @throws IllegalStateException if no AI opponent is configured
     * @throws IllegalStateException if game is not active
     */
    public MoveResult executeAIMove() {
        if (!isHumanVsAI) {
            throw new IllegalStateException("No AI opponent configured");
        }

        if (gameRules.getGameState() != GameState.ACTIVE) {
            throw new IllegalStateException("Game is not active");
        }

        // Get AI's next move
        Optional<AIMove> nextMove = aiPlayer.getStrategy().getNextMove(gameRules);

        if (!nextMove.isPresent()) {
            throw new IllegalStateException("AI could not determine next move");
        }

        AIMove aiMove = nextMove.get();

        // Convert AI move to PlayerMove and execute
        PlayerMove.ActionType actionType = convertAIMoveType(aiMove.getMoveType());
        PlayerMove playerMove = new PlayerMove(aiMove.getRow(), aiMove.getColumn(), actionType);

        return processPlayerMove(playerMove);
    }

    /**
     * Converts an AI move type to a player move action type.
     * 
     * @param aiMoveType the AI's move type
     * @return equivalent PlayerMove action type
     */
    private PlayerMove.ActionType convertAIMoveType(MoveType aiMoveType) {
        switch (aiMoveType) {
            case REVEAL:
                return PlayerMove.ActionType.REVEAL;
            case FLAG:
            case UNFLAG:
                return PlayerMove.ActionType.FLAG;
            default:
                throw new IllegalArgumentException("Unknown AI move type: " + aiMoveType);
        }
    }

    /**
     * Resets the game to initial state.
     * Creates a new board with same dimensions and mine count.
     * Clears all statistics and game state.
     * 
     * @throws IllegalArgumentException if configuration is invalid
     */
    public void resetGame() {
        Board oldBoard = gameRules.getBoard();
        Board newBoard = new Board(
                oldBoard.getWidth(),
                oldBoard.getHeight(),
                oldBoard.getMineCount()
        );

        gameRules = new GameRules(newBoard);
        statistics = new GameStatistics();

        // Reinitialize AI if present
        if (isHumanVsAI && aiPlayer != null) {
            AIDifficulty difficulty = aiPlayer.getDifficulty();
            aiPlayer = AIPlayerFactory.createAI("AI-" + difficulty, difficulty, gameRules);
        }
    }

    /**
     * Gets a summary of the current game state.
     * 
     * @return formatted string with game details
     */
    public String getGameSummary() {
        Board board = gameRules.getBoard();
        return String.format(
                "Game[%s]: %s | Board: %dx%d, Mines: %d | " +
                "Unrevealed: %d | Flagged: %d | Remaining: %d | %s",
                gameId,
                gameRules.getGameState(),
                board.getWidth(), board.getHeight(), board.getMineCount(),
                board.getUnrevealedTileCount(),
                board.getFlaggedTileCount(),
                gameRules.getRemainingMines(),
                statistics.toString()
        );
    }

    @Override
    public String toString() {
        return getGameSummary();
    }
}
