package com.minesweeper.gui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Main JavaFX Application class for Greenfield Minesweeper GUI.
 * 
 * This application uses a layered architecture where all game logic is reused from
 * the MVP console version. The GUI layer only handles presentation and user interaction.
 * 
 * Architecture:
 * - Game Logic Layer: Board, GameRules, GameController, AI strategies (100% reused)
 * - GUI Layer: Scenes, components, styling, animations (new for final version)
 * 
 * @author Copilot
 */
public class GuiApplication extends Application {
    private static final String APPLICATION_TITLE = "Greenfield Minesweeper";
    private static final int INITIAL_WINDOW_WIDTH = 800;
    private static final int INITIAL_WINDOW_HEIGHT = 600;

    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Set up primary stage
            primaryStage.setTitle(APPLICATION_TITLE);
            primaryStage.setWidth(INITIAL_WINDOW_WIDTH);
            primaryStage.setHeight(INITIAL_WINDOW_HEIGHT);
            primaryStage.setResizable(true);
            primaryStage.setOnCloseRequest(e -> onApplicationClose());

            // Create and set initial scene (temporary placeholder)
            Scene scene = createPlaceholderScene();
            primaryStage.setScene(scene);

            // Center window on screen
            centerWindowOnScreen(primaryStage);

            // Show the application
            primaryStage.show();

            System.out.println("Greenfield Minesweeper GUI launched successfully!");

        } catch (Exception e) {
            System.err.println("Error starting GUI application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a placeholder scene while full GUI is being developed.
     * This will be replaced with proper scene management system.
     */
    private Scene createPlaceholderScene() {
        StackPane root = new StackPane();
        Label welcomeLabel = new Label("Welcome to Greenfield Minesweeper!\nGUI coming soon...");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-text-alignment: center;");
        root.getChildren().add(welcomeLabel);
        
        return new Scene(root, INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT);
    }

    /**
     * Center the window on the screen.
     */
    private void centerWindowOnScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2 + screenBounds.getMinX();
        double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2 + screenBounds.getMinY();
        stage.setX(centerX);
        stage.setY(centerY);
    }

    /**
     * Handle application close event.
     */
    private void onApplicationClose() {
        // Clean up resources if needed
        System.out.println("Greenfield Minesweeper GUI closing...");
    }
}
