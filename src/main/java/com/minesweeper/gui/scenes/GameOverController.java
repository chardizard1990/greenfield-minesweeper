package com.minesweeper.gui.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.minesweeper.gui.BaseSceneController;
import com.minesweeper.gui.SceneManager;

/**
 * Game Over scene controller (Placeholder for Ticket 18).
 * 
 * Displays win/loss message and game statistics.
 */
public class GameOverController extends BaseSceneController {

    public GameOverController() {
        this.scene = createPlaceholderScene();
    }

    private Scene createPlaceholderScene() {
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #ffcccc; -fx-padding: 20;");
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Game Over");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label placeholderLabel = new Label("Game over screen coming in Ticket 18");
        placeholderLabel.setStyle("-fx-font-size: 14px;");

        Button menuButton = new Button("Main Menu");
        menuButton.setOnAction(e -> {
            SceneManager.getInstance().clearHistory();
            navigateTo(SceneManager.SceneType.MAIN_MENU);
        });

        root.getChildren().addAll(titleLabel, placeholderLabel, menuButton);
        return new Scene(root, 600, 400);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
