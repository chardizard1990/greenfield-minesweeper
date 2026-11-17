package com.minesweeper.gui.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import com.minesweeper.gui.BaseSceneController;
import com.minesweeper.gui.SceneManager;

/**
 * Main Menu scene controller.
 * 
 * This is the first scene displayed when the application starts.
 * It provides options to:
 * - Start a new game (navigate to mode selection)
 * - View settings
 * - Exit application
 */
public class MainMenuController extends BaseSceneController {
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_SPACING = 20;

    public MainMenuController() {
        this.scene = createScene();
    }

    private Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Title
        Label titleLabel = new Label("Greenfield Minesweeper");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Buttons
        Button newGameButton = new Button("New Game");
        newGameButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        newGameButton.setStyle("-fx-font-size: 14px;");
        newGameButton.setOnAction(e -> startNewGame());

        Button settingsButton = new Button("Settings");
        settingsButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton.setStyle("-fx-font-size: 14px;");
        settingsButton.setOnAction(e -> openSettings());

        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setStyle("-fx-font-size: 14px;");
        exitButton.setOnAction(e -> closeApplication());

        // Layout
        VBox centerBox = new VBox(BUTTON_SPACING);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(titleLabel, newGameButton, settingsButton, exitButton);
        centerBox.setPadding(new Insets(50));

        root.setCenter(centerBox);

        return new Scene(root, 600, 400);
    }

    private void startNewGame() {
        navigateTo(SceneManager.SceneType.MODE_SELECTION);
    }

    private void openSettings() {
        navigateTo(SceneManager.SceneType.SETTINGS);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
