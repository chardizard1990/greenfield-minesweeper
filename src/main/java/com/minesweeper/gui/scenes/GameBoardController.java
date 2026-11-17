package com.minesweeper.gui.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.minesweeper.gui.BaseSceneController;
import com.minesweeper.gui.SceneManager;

/**
 * Game Board scene controller (Placeholder for Ticket 14).
 * 
 * Displays the game board with tiles and game controls.
 */
public class GameBoardController extends BaseSceneController {

    public GameBoardController() {
        this.scene = createPlaceholderScene();
    }

    private Scene createPlaceholderScene() {
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #cccccc; -fx-padding: 20;");
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Game Board");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label placeholderLabel = new Label("Game board coming in Ticket 14");
        placeholderLabel.setStyle("-fx-font-size: 14px;");

        Button menuButton = new Button("Back to Menu");
        menuButton.setOnAction(e -> goBack());

        root.getChildren().addAll(titleLabel, placeholderLabel, menuButton);
        return new Scene(root, 800, 600);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
