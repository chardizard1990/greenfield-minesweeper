package com.minesweeper.gui.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.minesweeper.gui.BaseSceneController;
import com.minesweeper.gui.SceneManager;

/**
 * Settings scene controller (Placeholder for Ticket 21).
 * 
 * Allows players to configure game settings and preferences.
 */
public class SettingsController extends BaseSceneController {

    public SettingsController() {
        this.scene = createPlaceholderScene();
    }

    private Scene createPlaceholderScene() {
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #ccffcc; -fx-padding: 20;");
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Settings");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label placeholderLabel = new Label("Settings coming in Ticket 21");
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
