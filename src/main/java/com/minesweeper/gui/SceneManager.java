package com.minesweeper.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Scene management system for Greenfield Minesweeper GUI.
 * 
 * Manages transitions between different scenes (screens) in the application.
 * Supports:
 * - Scene caching to avoid reloading
 * - Navigation history for back button support
 * - Data passing between scenes
 * - Transition animations (future enhancement)
 * 
 * Usage:
 *   SceneManager.getInstance().initialize(primaryStage);
 *   SceneManager.getInstance().switchToScene(SceneType.GAME_BOARD, gameData);
 */
public class SceneManager {
    private static SceneManager instance;
    private final Map<SceneType, BaseSceneController> sceneControllers = new HashMap<>();
    private final Stack<SceneType> navigationStack = new Stack<>();
    private Stage primaryStage;
    private SceneType currentScene;

    private SceneManager() {
    }

    /**
     * Get singleton instance of SceneManager
     */
    public static synchronized SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Initialize SceneManager with the primary stage
     */
    public void initialize(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Register a scene controller for a scene type
     */
    public void registerScene(SceneType sceneType, BaseSceneController controller) {
        sceneControllers.put(sceneType, controller);
    }

    /**
     * Switch to a different scene with no data
     */
    public void switchToScene(SceneType sceneType) {
        switchToScene(sceneType, null);
    }

    /**
     * Switch to a different scene with data
     */
    public void switchToScene(SceneType sceneType, Object data) {
        if (primaryStage == null) {
            throw new IllegalStateException("SceneManager not initialized. Call initialize() first.");
        }

        BaseSceneController controller = sceneControllers.get(sceneType);
        if (controller == null) {
            throw new IllegalArgumentException("Scene not registered: " + sceneType);
        }

        // Notify current scene of exit
        if (currentScene != null) {
            BaseSceneController currentController = sceneControllers.get(currentScene);
            if (currentController != null) {
                currentController.onSceneHidden();
            }
        }

        // Add to navigation stack
        if (currentScene != null) {
            navigationStack.push(currentScene);
        }
        currentScene = sceneType;

        // Notify new scene of entry
        controller.onSceneShown(data);

        // Switch the scene
        Scene scene = controller.getScene();
        primaryStage.setScene(scene);
    }

    /**
     * Go back to previous scene
     */
    public boolean goBack() {
        if (navigationStack.isEmpty()) {
            return false;
        }

        SceneType previousScene = navigationStack.pop();
        
        // Notify current scene of exit
        BaseSceneController currentController = sceneControllers.get(currentScene);
        if (currentController != null) {
            currentController.onSceneHidden();
        }

        currentScene = previousScene;

        // Notify previous scene of re-entry
        BaseSceneController previousController = sceneControllers.get(previousScene);
        if (previousController != null) {
            previousController.onSceneShown(null);
        }

        // Switch the scene
        Scene scene = previousController.getScene();
        primaryStage.setScene(scene);

        return true;
    }

    /**
     * Get current scene type
     */
    public SceneType getCurrentScene() {
        return currentScene;
    }

    /**
     * Check if navigation stack is empty (can't go back)
     */
    public boolean canGoBack() {
        return !navigationStack.isEmpty();
    }

    /**
     * Clear navigation history
     */
    public void clearHistory() {
        navigationStack.clear();
    }

    /**
     * Close application
     */
    public void closeApplication() {
        if (primaryStage != null) {
            primaryStage.close();
        }
    }

    /**
     * Scene type enumeration for all application screens
     */
    public enum SceneType {
        MAIN_MENU("Main Menu"),
        MODE_SELECTION("Mode Selection"),
        GAME_BOARD("Game Board"),
        GAME_OVER("Game Over"),
        SETTINGS("Settings");

        private final String displayName;

        SceneType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
