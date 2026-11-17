package com.minesweeper.gui.scenes;

import com.minesweeper.gui.viewmodel.ViewModel;
import com.minesweeper.gui.viewmodel.ObservableProperty;

/**
 * ViewModel for the Main Menu scene.
 * Demonstrates the ViewModel pattern with observable properties.
 * 
 * This separates UI state from UI logic, making the controller cleaner
 * and the state testable without JavaFX dependencies.
 * 
 * Example of how to use:
 * - Bind UI elements to properties in the ViewModel
 * - When user clicks button, controller calls ViewModel method
 * - ViewModel updates properties, which triggers UI updates
 */
public class MainMenuViewModel extends ViewModel {
    
    // Observable properties for the UI to bind to
    private final ObservableProperty<String> appTitle;
    private final ObservableProperty<Boolean> newGameEnabled;
    private final ObservableProperty<Boolean> settingsEnabled;
    
    public MainMenuViewModel() {
        this.appTitle = new ObservableProperty<>("Greenfield Minesweeper");
        this.newGameEnabled = new ObservableProperty<>(true);
        this.settingsEnabled = new ObservableProperty<>(true);
    }
    
    @Override
    public void initialize() {
        // Called when MainMenuController shows this scene
        // Load any initial data here
    }
    
    @Override
    public void cleanup() {
        // Called when MainMenuController hides this scene
        // Clean up resources if needed
    }
    
    // Properties with getters
    
    public ObservableProperty<String> appTitleProperty() {
        return appTitle;
    }
    
    public String getAppTitle() {
        return appTitle.getValue();
    }
    
    public void setAppTitle(String title) {
        appTitle.setValue(title);
    }
    
    public ObservableProperty<Boolean> newGameEnabledProperty() {
        return newGameEnabled;
    }
    
    public Boolean isNewGameEnabled() {
        return newGameEnabled.getValue();
    }
    
    public void setNewGameEnabled(boolean enabled) {
        newGameEnabled.setValue(enabled);
    }
    
    public ObservableProperty<Boolean> settingsEnabledProperty() {
        return settingsEnabled;
    }
    
    public Boolean isSettingsEnabled() {
        return settingsEnabled.getValue();
    }
    
    public void setSettingsEnabled(boolean enabled) {
        settingsEnabled.setValue(enabled);
    }
}
