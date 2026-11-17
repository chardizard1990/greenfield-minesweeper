package com.minesweeper.gui.viewmodel;

import com.minesweeper.game.Board;
import com.minesweeper.game.CustomGameMode;
import com.minesweeper.game.GameController;
import com.minesweeper.game.GameFactory;
import com.minesweeper.game.GameMode;
import com.minesweeper.game.GameState;
import com.minesweeper.game.MoveResult;
import com.minesweeper.game.PlayerMove;
import com.minesweeper.game.Tile;

/**
 * Connects the game controller to the board's presentation state.
 * The game rules remain the source of truth for every tile and counter.
 */
public class GameSession {
    private final GameController controller;
    private final GameBoardViewModel board;

    private GameSession(GameController controller) {
        this.controller = controller;
        Board gameBoard = controller.getBoard();
        this.board = new GameBoardViewModel(gameBoard.getWidth(), gameBoard.getHeight());
        refresh();
    }

    public static GameSession forPreset(GameMode mode) {
        return new GameSession(new GameController("gui-game", GameFactory.createGame(mode), mode));
    }

    public static GameSession forCustom(CustomGameMode mode) {
        return new GameSession(GameController.forCustomGame("gui-game", mode));
    }

    public GameBoardViewModel getBoard() {
        return board;
    }

    public GameController getController() {
        return controller;
    }

    public MoveResult move(int row, int col, PlayerMove.ActionType action) {
        MoveResult result = controller.processPlayerMove(new PlayerMove(row, col, action));
        refresh();
        return result;
    }

    public void restart() {
        controller.resetGame();
        refresh();
    }

    private void refresh() {
        Board source = controller.getBoard();
        for (int row = 0; row < source.getHeight(); row++) {
            for (int col = 0; col < source.getWidth(); col++) {
                Tile tile = source.getTile(row, col);
                TileViewModel target = board.getTile(row, col);
                if (tile.isRevealed()) {
                    if (tile.isMine()) {
                        target.setTileState(TileViewModel.TileState.MINE);
                    } else {
                        target.setAdjacentMineCount(source.getAdjacentMineCount(row, col));
                        target.setTileState(TileViewModel.TileState.REVEALED);
                    }
                } else if (tile.isFlagged()) {
                    target.setTileState(TileViewModel.TileState.FLAGGED);
                } else {
                    target.setTileState(TileViewModel.TileState.UNREVEALED);
                }
            }
        }
        board.setTotalMines(source.getMineCount());
        board.setFlaggedCount(source.getFlaggedTileCount());
        board.setRevealedCount(source.getWidth() * source.getHeight() - source.getUnrevealedTileCount());
        GameState state = controller.getGameState();
        board.setGameState(GameBoardViewModel.GameState.valueOf(state.name()));
    }
}
