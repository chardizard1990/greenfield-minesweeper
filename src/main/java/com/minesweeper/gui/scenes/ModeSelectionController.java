package com.minesweeper.gui.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.minesweeper.gui.BaseSceneController;
import com.minesweeper.gui.SceneManager;

/**
 * Mode Selection scene controller (Placeholder for Ticket 16).
 * 
 * Allows player to select game difficulty and start the game.
 */
public class ModeSelectionController extends BaseSceneController {

    public ModeSelectionController() {
        this.scene = createPlaceholderScene();
    }

    private Scene createPlaceholderScene() {
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #ffffff; -fx-padding: 20;");
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Mode Selection");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label placeholderLabel = new Label("Mode selection coming in Ticket 16");
        placeholderLabel.setStyle("-fx-font-size: 14px;");

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> goBack());

        root.getChildren().addAll(titleLabel, placeholderLabel, backButton);
        return new Scene(root, 600, 400);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
