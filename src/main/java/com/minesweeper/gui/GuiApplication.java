package com.minesweeper.gui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.minesweeper.gui.scenes.*;

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

            // Initialize scene manager with primary stage
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.initialize(primaryStage);

            // Register all scene controllers
            sceneManager.registerScene(SceneManager.SceneType.MAIN_MENU, new MainMenuController());
            sceneManager.registerScene(SceneManager.SceneType.MODE_SELECTION, new ModeSelectionController());
            sceneManager.registerScene(SceneManager.SceneType.GAME_BOARD, new GameBoardController());
            sceneManager.registerScene(SceneManager.SceneType.GAME_OVER, new GameOverController());
            sceneManager.registerScene(SceneManager.SceneType.SETTINGS, new SettingsController());

            // Switch to main menu
            sceneManager.switchToScene(SceneManager.SceneType.MAIN_MENU);

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
