package com.minesweeper.gui.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Thread-safe observable property that notifies listeners of changes.
 * This is a simple implementation suitable for GUI data binding.
 * 
 * Example usage:
 * <pre>
 *   ObservableProperty<String> title = new ObservableProperty<>("Default Title");
 *   title.addListener((oldVal, newVal) -> System.out.println("Title changed: " + newVal));
 *   title.setValue("New Title"); // Triggers listener callback
 * </pre>
 * 
 * @param <T> The type of value held by this property
 */
public class ObservableProperty<T> {
    
    private T value;
    private final List<PropertyChangeListener<T>> listeners = new ArrayList<>();
    
    /**
     * Create an observable property with an initial value.
     * 
     * @param initialValue The initial value
     */
    public ObservableProperty(T initialValue) {
        this.value = initialValue;
    }
    
    /**
     * Create an observable property with null initial value.
     */
    public ObservableProperty() {
        this(null);
    }
    
    /**
     * Get the current value.
     * 
     * @return The current value
     */
    public T getValue() {
        return value;
    }
    
    /**
     * Set the value and notify all listeners if the value changed.
     * 
     * @param newValue The new value
     */
    public void setValue(T newValue) {
        // Only notify if the value actually changed
        if (!equals(value, newValue)) {
            T oldValue = value;
            this.value = newValue;
            notifyListeners(oldValue, newValue);
        }
    }
    
    /**
     * Add a listener to be notified when the value changes.
     * 
     * @param listener The listener to add
     */
    public void addListener(PropertyChangeListener<T> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    /**
     * Remove a listener so it no longer receives notifications.
     * 
     * @param listener The listener to remove
     */
    public void removeListener(PropertyChangeListener<T> listener) {
        listeners.remove(listener);
    }
    
    /**
     * Remove all listeners.
     */
    public void clearListeners() {
        listeners.clear();
    }
    
    /**
     * Notify all listeners of a value change.
     * 
     * @param oldValue The previous value
     * @param newValue The new value
     */
    private void notifyListeners(T oldValue, T newValue) {
        // Create a copy to avoid concurrent modification if listeners modify the list
        List<PropertyChangeListener<T>> listenersCopy = new ArrayList<>(listeners);
        for (PropertyChangeListener<T> listener : listenersCopy) {
            try {
                listener.onPropertyChanged(oldValue, newValue);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Null-safe equality check.
     */
    private static boolean equals(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
    
    @Override
    public String toString() {
        return "ObservableProperty{" + value + "}";
    }
}
