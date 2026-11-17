package com.minesweeper.gui.scenes;

import com.minesweeper.game.CustomGameMode;
import com.minesweeper.game.GameMode;
import com.minesweeper.gui.BaseSceneController;
import com.minesweeper.gui.SceneManager;
import com.minesweeper.gui.viewmodel.GameSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/** Allows a player to start a preset or custom game. */
public class ModeSelectionController extends BaseSceneController {

    public ModeSelectionController() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f0f0f0;");

        Label title = new Label("Choose a difficulty");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        root.getChildren().add(title);
        for (GameMode mode : GameMode.values()) {
            Button button = new Button(mode.getDisplayName() + "  -  "
                    + mode.getWidth() + " x " + mode.getHeight() + ", "
                    + mode.getMineCount() + " mines");
            button.setId("mode-" + mode.name().toLowerCase());
            button.setMinWidth(260);
            button.setOnAction(event -> startGame(GameSession.forPreset(mode)));
            root.getChildren().add(button);
        }

        Button custom = new Button("Custom...");
        custom.setId("mode-custom");
        custom.setMinWidth(260);
        custom.setOnAction(event -> showCustomDialog());
        Button back = new Button("Back");
        back.setOnAction(event -> goBack());
        root.getChildren().addAll(custom, back);
        scene = new Scene(root, 600, 400);
    }

    private void startGame(GameSession session) {
        navigateTo(SceneManager.SceneType.GAME_BOARD, session);
    }

    private void showCustomDialog() {
        Dialog<CustomGameMode> dialog = new Dialog<>();
        dialog.setTitle("Custom game");
        dialog.setHeaderText("Enter board dimensions and number of mines");
        dialog.initOwner(scene.getWindow());
        ButtonType start = new ButtonType("Start", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(start, ButtonType.CANCEL);

        TextField width = new TextField("10");
        TextField height = new TextField("10");
        TextField mines = new TextField("10");
        width.setId("custom-width");
        height.setId("custom-height");
        mines.setId("custom-mines");
        Label error = new Label();
        error.setId("custom-error");
        error.setStyle("-fx-text-fill: #b00020;");
        error.setWrapText(true);
        GridPane fields = new GridPane();
        fields.setHgap(10);
        fields.setVgap(10);
        fields.addRow(0, new Label("Width (1-50)"), width);
        fields.addRow(1, new Label("Height (1-50)"), height);
        fields.addRow(2, new Label("Mines"), mines);
        fields.add(error, 0, 3, 2, 1);
        dialog.getDialogPane().setContent(fields);

        dialog.getDialogPane().lookupButton(start).addEventFilter(
                javafx.event.ActionEvent.ACTION, event -> {
                    try {
                        parseCustomMode(width, height, mines);
                        error.setText("");
                    } catch (IllegalArgumentException ex) {
                        error.setText(ex.getMessage());
                        event.consume();
                    }
                });
        dialog.setResultConverter(button -> button == start
                ? parseCustomMode(width, height, mines) : null);
        dialog.showAndWait().ifPresent(mode -> startGame(GameSession.forCustom(mode)));
    }

    private CustomGameMode parseCustomMode(TextField width, TextField height, TextField mines) {
        try {
            return new CustomGameMode(Integer.parseInt(width.getText().trim()),
                    Integer.parseInt(height.getText().trim()),
                    Integer.parseInt(mines.getText().trim()));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Enter whole numbers for width, height and mines.");
        }
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
