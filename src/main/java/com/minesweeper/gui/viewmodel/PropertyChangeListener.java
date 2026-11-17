package com.minesweeper.gui.viewmodel;

/**
 * Simple listener interface for property change events.
 * Called whenever an observable property value changes.
 * 
 * @param <T> The type of value that changed
 */
@FunctionalInterface
public interface PropertyChangeListener<T> {
    
    /**
     * Called when a property value changes.
     * 
     * @param oldValue The previous value
     * @param newValue The new value
     */
    void onPropertyChanged(T oldValue, T newValue);
}
