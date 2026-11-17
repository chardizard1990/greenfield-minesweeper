package com.minesweeper.gui.viewmodel;

import com.minesweeper.game.CustomGameMode;
import com.minesweeper.game.GameMode;
import com.minesweeper.game.GameState;
import com.minesweeper.game.MoveResult;
import com.minesweeper.game.PlayerMove;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {
    @Test
    void presetAndCustomGamesUseTheirOwnConfigurations() {
        for (GameMode mode : GameMode.values()) {
            GameSession session = GameSession.forPreset(mode);
            assertEquals(mode.getWidth(), session.getBoard().getBoardWidth());
            assertEquals(mode.getHeight(), session.getBoard().getBoardHeight());
            assertEquals(mode.getMineCount(), session.getBoard().getTotalMines());
            assertEquals(mode, session.getController().getGameMode());
        }
        CustomGameMode custom = new CustomGameMode(4, 3, 2);
        GameSession session = GameSession.forCustom(custom);
        assertEquals(4, session.getBoard().getBoardWidth());
        assertEquals(3, session.getBoard().getBoardHeight());
        assertEquals(2, session.getBoard().getTotalMines());
        assertEquals(custom, session.getController().getCustomGameMode());
        assertNull(session.getController().getGameMode());
    }

    @Test
    void flagUnflagAndRestartKeepViewAndRulesInSync() {
        GameSession session = GameSession.forCustom(new CustomGameMode(2, 2, 1));
        session.move(0, 0, PlayerMove.ActionType.FLAG);
        assertEquals(TileViewModel.TileState.FLAGGED, session.getBoard().getTile(0, 0).getTileState());
        assertEquals(0, session.getController().getGameRules().getRemainingMines());
        session.move(0, 0, PlayerMove.ActionType.FLAG);
        assertEquals(TileViewModel.TileState.UNREVEALED, session.getBoard().getTile(0, 0).getTileState());
        assertEquals(1, session.getController().getGameRules().getRemainingMines());

        session.move(0, 0, PlayerMove.ActionType.FLAG);
        TileViewModel existing = session.getBoard().getTile(0, 0);
        session.restart();
        assertSame(existing, session.getBoard().getTile(0, 0));
        assertEquals(TileViewModel.TileState.UNREVEALED, existing.getTileState());
        assertEquals(1, session.getController().getGameRules().getRemainingMines());
        assertEquals(0, session.getController().getStatistics().getTotalMoves());
    }

    @Test
    void cascadeAndWinUpdateEveryTile() {
        GameSession session = GameSession.forCustom(new CustomGameMode(3, 2, 0));
        assertTrue(session.move(0, 0, PlayerMove.ActionType.REVEAL).isSuccess());
        assertEquals(GameBoardViewModel.GameState.WON, session.getBoard().getGameState());
        assertEquals(6, session.getBoard().getRevealedCount());
        for (TileViewModel[] row : session.getBoard().getTiles()) {
            for (TileViewModel tile : row) {
                assertEquals(TileViewModel.TileState.REVEALED, tile.getTileState());
            }
        }
        assertTrue(session.getController().getStatistics().getEndTime() > 0);
    }

    @Test
    void hittingMineEndsGameAndStopsStatisticsTimer() {
        GameSession session = GameSession.forCustom(new CustomGameMode(2, 2, 1));
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                if (session.getController().getBoard().getTile(row, col).isMine()) {
                    MoveResult result = session.move(row, col, PlayerMove.ActionType.REVEAL);
                    assertTrue(result.isSuccess());
                    assertEquals(GameState.LOST, session.getController().getGameState());
                    assertEquals(TileViewModel.TileState.MINE, session.getBoard().getTile(row, col).getTileState());
                    assertTrue(session.getController().getStatistics().getEndTime() > 0);
                    assertEquals(1, session.getController().getStatistics().getTotalMoves());
                    return;
                }
            }
        }
        fail("Expected one mine");
    }
}
