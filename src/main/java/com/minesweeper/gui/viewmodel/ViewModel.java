package com.minesweeper.gui.viewmodel;

/**
 * Base class for all ViewModels in the GUI layer.
 * Handles lifecycle management and provides access to observable properties.
 * 
 * ViewModels bridge the gap between the Model (game logic) and the View (JavaFX controllers).
 * They provide:
 * - Observable properties that UI can bind to
 * - State management independent of UI framework
 * - Testability (can test logic without JavaFX)
 * - Clean separation of concerns
 */
public abstract class ViewModel {
    
    /**
     * Called when the ViewModel is first created and bound to a view.
     * Override this to perform initialization (e.g., load data, start listeners).
     */
    public void initialize() {
        // Override in subclasses as needed
    }
    
    /**
     * Called when the view is about to be hidden or destroyed.
     * Override this to clean up resources (e.g., stop listeners, save state).
     */
    public void cleanup() {
        // Override in subclasses as needed
    }
}
