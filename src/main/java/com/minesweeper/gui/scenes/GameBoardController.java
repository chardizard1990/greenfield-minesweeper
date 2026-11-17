package com.minesweeper.gui.scenes;

import com.minesweeper.game.MoveResult;
import com.minesweeper.game.PlayerMove;
import com.minesweeper.gui.BaseSceneController;
import com.minesweeper.gui.SceneManager;
import com.minesweeper.gui.components.GameBoardView;
import com.minesweeper.gui.viewmodel.GameBoardViewModel;
import com.minesweeper.gui.viewmodel.GameSession;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** Manages a playable game board and its controls. */
public class GameBoardController extends BaseSceneController {
    private final BorderPane root = new BorderPane();
    private final Label mines = new Label();
    private final Label time = new Label();
    private final Label status = new Label();
    private final Label message = new Label();
    private final Timeline clock;
    private GameSession session;

    public GameBoardController() {
        Button newGame = new Button("New game");
        newGame.setId("new-game");
        newGame.setOnAction(event -> goBack());
        Button restart = new Button("Restart");
        restart.setId("restart");
        restart.setOnAction(event -> restartGame());
        Button settings = new Button("Settings");
        settings.setId("settings");
        settings.setOnAction(event -> navigateTo(SceneManager.SceneType.SETTINGS));
        mines.setId("remaining-mines");
        time.setId("elapsed-time");
        status.setId("game-status");
        message.setId("game-message");
        HBox controls = new HBox(10, newGame, restart, settings);
        HBox information = new HBox(24, mines, time, status);
        controls.setAlignment(Pos.CENTER);
        information.setAlignment(Pos.CENTER);
        VBox header = new VBox(10, controls, information, message);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(12));
        root.setTop(header);
        root.setStyle("-fx-background-color: #f5f5f5;");
        scene = new Scene(root, 800, 600);
        clock = new Timeline(new KeyFrame(Duration.millis(250), event -> updateTime()));
        clock.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void onSceneShown(Object data) {
        if (data instanceof GameSession) {
            session = (GameSession) data;
            showBoard();
        } else if (data != null || session == null) {
            throw new IllegalArgumentException("A game session is required to show the board");
        }
        updateInformation();
        if (session.getController().getGameState() == com.minesweeper.game.GameState.ACTIVE) {
            clock.play();
        }
    }

    @Override
    public void onSceneHidden() {
        clock.stop();
    }

    private void showBoard() {
        GameBoardViewModel board = session.getBoard();
        board.addTileBoardListener(new GameBoardViewModel.TileBoardListener() {
            @Override
            public void onTileClicked(int row, int col) {
                makeMove(row, col, PlayerMove.ActionType.REVEAL);
            }

            @Override
            public void onTileFlagged(int row, int col) {
                makeMove(row, col, PlayerMove.ActionType.FLAG);
            }
        });
        ScrollPane scroll = new ScrollPane(new GameBoardView(board));
        scroll.setId("board-scroll");
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setPannable(true);
        root.setCenter(scroll);
        message.setText("");
    }

    private void makeMove(int row, int col, PlayerMove.ActionType action) {
        MoveResult result = session.move(row, col, action);
        message.setText(result.isSuccess() ? "" : result.getMessage());
        updateInformation();
        if (session.getController().getGameState() != com.minesweeper.game.GameState.ACTIVE) {
            clock.stop();
        }
    }

    private void restartGame() {
        session.restart();
        message.setText("");
        updateInformation();
        clock.playFromStart();
    }

    private void updateInformation() {
        mines.setText("Mines remaining: " + session.getController().getGameRules().getRemainingMines());
        status.setText("Status: " + session.getController().getGameState());
        updateTime();
    }

    private void updateTime() {
        long seconds = session.getController().getStatistics().getElapsedTimeSeconds();
        time.setText(String.format("Time: %02d:%02d", seconds / 60, seconds % 60));
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
