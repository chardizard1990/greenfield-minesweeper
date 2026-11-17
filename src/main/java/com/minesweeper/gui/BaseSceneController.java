package com.minesweeper.gui;

import javafx.scene.Scene;
import com.minesweeper.gui.viewmodel.ViewModel;

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
 * ViewModel Support:
 * - Controllers can optionally use a ViewModel for state management
 * - ViewModels provide observable properties for data binding
 * - ViewModel.initialize() is called in onSceneShown()
 * - ViewModel.cleanup() is called in onSceneHidden()
 * 
 * This pattern enables:
 * - Clean separation of concerns (each scene manages itself)
 * - Easy data passing between scenes via onSceneShown(data)
 * - Proper resource cleanup via onSceneHidden()
 * - Consistent scene lifecycle management
 * - Observable properties for reactive UI updates
 */
public abstract class BaseSceneController {
    protected Scene scene;
    protected ViewModel viewModel;

    /**
     * Get the JavaFX Scene for this controller
     * 
     * @return The Scene to be displayed
     */
    public abstract Scene getScene();

    /**
     * Get the ViewModel associated with this controller (if any).
     * Override in subclasses to provide their specific ViewModel type.
     * 
     * @return The ViewModel, or null if this controller doesn't use one
     */
    public ViewModel getViewModel() {
        return viewModel;
    }

    /**
     * Set the ViewModel for this controller.
     * Called by scene management when creating/initializing the controller.
     * 
     * @param viewModel The ViewModel to use
     */
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Called when this scene is about to be shown to the user.
     * First initializes the ViewModel (if present), then subclass logic.
     * Override this to initialize scene state, load data, start timers, etc.
     * 
     * @param data Data passed from previous scene (may be null)
     */
    public void onSceneShown(Object data) {
        if (viewModel != null) {
            viewModel.initialize();
        }
        // Subclasses override to add their own logic
    }

    /**
     * Called when this scene is about to be hidden.
     * Calls cleanup on the ViewModel (if present), then subclass cleanup logic.
     * Override this to clean up resources, stop timers, save state, etc.
     */
    public void onSceneHidden() {
        if (viewModel != null) {
            viewModel.cleanup();
        }
        // Subclasses override to add their own logic
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
