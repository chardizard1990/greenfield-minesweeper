package com.minesweeper.gui.scenes;

import com.minesweeper.game.GameMode;
import com.minesweeper.gui.SceneManager;
import com.minesweeper.gui.components.GameBoardView;
import com.minesweeper.gui.components.TileView;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(named = "javafx.integration", matches = "true")
class GameFlowTest {
    @BeforeAll
    static void startToolkit() throws InterruptedException {
        CountDownLatch started = new CountDownLatch(1);
        Platform.startup(() -> {
            Platform.setImplicitExit(false);
            started.countDown();
        });
        assertTrue(started.await(10, TimeUnit.SECONDS));
    }

    @AfterAll
    static void stopToolkit() {
        Platform.exit();
    }

    @Test
    void modesCustomValidationControlsAndNavigationWork() throws Exception {
        runOnFx(() -> {
            Stage stage = new Stage();
            try {
                SceneManager manager = SceneManager.getInstance();
                manager.initialize(stage);
                manager.clearHistory();
                manager.registerScene(SceneManager.SceneType.MAIN_MENU, new MainMenuController());
                manager.registerScene(SceneManager.SceneType.MODE_SELECTION, new ModeSelectionController());
                manager.registerScene(SceneManager.SceneType.GAME_BOARD, new GameBoardController());
                manager.registerScene(SceneManager.SceneType.SETTINGS, new SettingsController());
                manager.switchToScene(SceneManager.SceneType.MAIN_MENU);
                stage.show();
                buttonByText(stage, "New Game").fire();

                for (GameMode mode : GameMode.values()) {
                    button(stage, "#mode-" + mode.name().toLowerCase()).fire();
                    GameBoardView board = board(stage);
                    assertEquals(mode.getWidth(), board.getViewModel().getBoardWidth());
                    assertEquals(mode.getHeight(), board.getViewModel().getBoardHeight());
                    assertEquals("Mines remaining: " + mode.getMineCount(),
                            label(stage, "#remaining-mines").getText());
                    button(stage, "#new-game").fire();
                }

                AtomicReference<Throwable> dialogFailure = new AtomicReference<>();
                Platform.runLater(() -> {
                    DialogPane dialog = null;
                    try {
                        for (Window window : Window.getWindows()) {
                            if (window != stage && window.isShowing()
                                    && window.getScene().getRoot() instanceof DialogPane) {
                                dialog = (DialogPane) window.getScene().getRoot();
                                break;
                            }
                        }
                        assertNotNull(dialog);
                        TextField width = (TextField) dialog.lookup("#custom-width");
                        TextField height = (TextField) dialog.lookup("#custom-height");
                        TextField mines = (TextField) dialog.lookup("#custom-mines");
                        ButtonType start = dialog.getButtonTypes().stream()
                                .filter(type -> type.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                                .findFirst().orElseThrow(AssertionError::new);
                        width.setText("0");
                        ((Button) dialog.lookupButton(start)).fire();
                        assertFalse(((Label) dialog.lookup("#custom-error")).getText().isEmpty());
                        width.setText("abc");
                        ((Button) dialog.lookupButton(start)).fire();
                        assertTrue(((Label) dialog.lookup("#custom-error")).getText().contains("whole numbers"));
                        width.setText("2");
                        height.setText("2");
                        mines.setText("4");
                        ((Button) dialog.lookupButton(start)).fire();
                        assertFalse(((Label) dialog.lookup("#custom-error")).getText().isEmpty());
                        mines.setText("0");
                        ((Button) dialog.lookupButton(start)).fire();
                    } catch (Throwable failure) {
                        dialogFailure.set(failure);
                        if (dialog != null) {
                            ((Stage) dialog.getScene().getWindow()).close();
                        }
                    }
                });
                button(stage, "#mode-custom").fire();
                if (dialogFailure.get() != null) {
                    throw new AssertionError(dialogFailure.get());
                }
                GameBoardView board = board(stage);
                assertEquals(2, board.getViewModel().getBoardWidth());
                assertEquals(2, board.getViewModel().getBoardHeight());
                TileView tile = board.getTileView(0, 0);
                click(tile, MouseButton.SECONDARY);
                assertEquals("Mines remaining: -1", label(stage, "#remaining-mines").getText());
                click(tile, MouseButton.SECONDARY);
                assertEquals("Mines remaining: 0", label(stage, "#remaining-mines").getText());
                click(tile, MouseButton.PRIMARY);
                assertEquals("Status: WON", label(stage, "#game-status").getText());
                assertTrue(board.getGridPane().isMouseTransparent());
                button(stage, "#restart").fire();
                assertEquals("Status: ACTIVE", label(stage, "#game-status").getText());
                assertFalse(board.getGridPane().isMouseTransparent());
                assertEquals("Time: 00:00", label(stage, "#elapsed-time").getText());
                button(stage, "#settings").fire();
                buttonByText(stage, "Back").fire();
                assertSame(board, board(stage));
                assertEquals("Status: ACTIVE", label(stage, "#game-status").getText());
            } finally {
                stage.close();
            }
        });
    }

    @Test
    void timerAdvancesAndFreezesAfterWin() throws Exception {
        AtomicReference<Stage> stageRef = new AtomicReference<>();
        try {
            runOnFx(() -> {
                Stage stage = new Stage();
                stageRef.set(stage);
                SceneManager manager = SceneManager.getInstance();
                manager.initialize(stage);
                manager.clearHistory();
                manager.registerScene(SceneManager.SceneType.GAME_BOARD, new GameBoardController());
                manager.switchToScene(SceneManager.SceneType.GAME_BOARD,
                        com.minesweeper.gui.viewmodel.GameSession.forCustom(
                                new com.minesweeper.game.CustomGameMode(1, 1, 0)));
                stage.show();
            });
            Thread.sleep(1500);
            AtomicReference<String> finishedTime = new AtomicReference<>();
            runOnFx(() -> {
                Stage stage = stageRef.get();
                assertNotEquals("Time: 00:00", label(stage, "#elapsed-time").getText());
                click(board(stage).getTileView(0, 0), MouseButton.PRIMARY);
                assertEquals("Status: WON", label(stage, "#game-status").getText());
                finishedTime.set(label(stage, "#elapsed-time").getText());
            });
            Thread.sleep(1200);
            runOnFx(() -> assertEquals(finishedTime.get(),
                    label(stageRef.get(), "#elapsed-time").getText()));
        } finally {
            runOnFx(() -> {
                if (stageRef.get() != null) {
                    stageRef.get().close();
                }
            });
        }
    }

    @Test
    void mineHitDisplaysLossAndPreventsFurtherInteraction() throws Exception {
        runOnFx(() -> {
            Stage stage = new Stage();
            try {
                com.minesweeper.gui.viewmodel.GameSession session =
                        com.minesweeper.gui.viewmodel.GameSession.forCustom(
                                new com.minesweeper.game.CustomGameMode(2, 1, 1));
                SceneManager manager = SceneManager.getInstance();
                manager.initialize(stage);
                manager.clearHistory();
                manager.registerScene(SceneManager.SceneType.GAME_BOARD, new GameBoardController());
                manager.switchToScene(SceneManager.SceneType.GAME_BOARD, session);
                stage.show();
                int mineColumn = session.getController().getBoard().getTile(0, 0).isMine() ? 0 : 1;
                GameBoardView board = board(stage);
                click(board.getTileView(0, mineColumn), MouseButton.PRIMARY);
                assertEquals("Status: LOST", label(stage, "#game-status").getText());
                assertTrue(board.getGridPane().isMouseTransparent());
                assertTrue(session.getController().getStatistics().getEndTime() > 0);
            } finally {
                stage.close();
            }
        });
    }

    private static void runOnFx(Runnable action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(action, null);
        Platform.runLater(task);
        task.get(30, TimeUnit.SECONDS);
    }

    private static Button button(Stage stage, String selector) {
        return (Button) stage.getScene().lookup(selector);
    }

    private static Label label(Stage stage, String selector) {
        return (Label) stage.getScene().lookup(selector);
    }

    private static Button buttonByText(Stage stage, String text) {
        return stage.getScene().getRoot().lookupAll(".button").stream()
                .map(node -> (Button) node)
                .filter(button -> text.equals(button.getText()))
                .findFirst().orElseThrow(AssertionError::new);
    }

    private static GameBoardView board(Stage stage) {
        return (GameBoardView) ((ScrollPane) stage.getScene().lookup("#board-scroll")).getContent();
    }

    private static void click(Node node, MouseButton button) {
        Event.fireEvent(node, new MouseEvent(MouseEvent.MOUSE_CLICKED, 1, 1, 1, 1, button, 1,
                false, false, false, false, false, false, false,
                false, false, false, null));
    }
}
