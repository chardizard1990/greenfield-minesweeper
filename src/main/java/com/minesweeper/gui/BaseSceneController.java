package com.minesweeper.gui;

import javafx.scene.Scene;

/**
 * Base class for all scene controllers in the Greenfield Minesweeper GUI.
 * 
 * Each scene (screen) in the application should extend this class to handle
 * its own UI logic and scene lifecycle events.
 * 
 * Key methods:
 * - getScene(): Returns the JavaFX Scene to display
 * - onSceneShown(): Called when scene is about to be displayed
 * - onSceneHidden(): Called when scene is about to be hidden
 * 
 * This pattern enables:
 * - Clean separation of concerns (each scene manages itself)
 * - Easy data passing between scenes via onSceneShown(data)
 * - Proper resource cleanup via onSceneHidden()
 * - Consistent scene lifecycle management
 */
public abstract class BaseSceneController {
    protected Scene scene;

    /**
     * Get the JavaFX Scene for this controller
     * 
     * @return The Scene to be displayed
     */
    public abstract Scene getScene();

    /**
     * Called when this scene is about to be shown to the user.
     * Override this to initialize scene state, load data, start timers, etc.
     * 
     * @param data Data passed from previous scene (may be null)
     */
    public void onSceneShown(Object data) {
        // Override in subclasses as needed
    }

    /**
     * Called when this scene is about to be hidden.
     * Override this to clean up resources, stop timers, save state, etc.
     */
    public void onSceneHidden() {
        // Override in subclasses as needed
    }

    /**
     * Navigate to another scene
     * 
     * @param sceneType Target scene
     */
    protected void navigateTo(SceneManager.SceneType sceneType) {
        SceneManager.getInstance().switchToScene(sceneType);
    }

    /**
     * Navigate to another scene with data
     * 
     * @param sceneType Target scene
     * @param data Data to pass to target scene
     */
    protected void navigateTo(SceneManager.SceneType sceneType, Object data) {
        SceneManager.getInstance().switchToScene(sceneType, data);
    }

    /**
     * Go back to previous scene
     */
    protected boolean goBack() {
        return SceneManager.getInstance().goBack();
    }

    /**
     * Check if back navigation is available
     */
    protected boolean canGoBack() {
        return SceneManager.getInstance().canGoBack();
    }

    /**
     * Close the application
     */
    protected void closeApplication() {
        SceneManager.getInstance().closeApplication();
    }
}
