package com.minesweeper.gui.viewmodel;

import java.util.function.Function;

/**
 * Utilities for binding properties together.
 * Supports one-way and two-way binding with optional transformations.
 * 
 * Example usage:
 * <pre>
 *   ObservableProperty<Integer> count = new ObservableProperty<>(0);
 *   ObservableProperty<String> display = new ObservableProperty<>("Count: 0");
 *   
 *   // One-way binding: when count changes, display updates
 *   PropertyBinding.bindOneWay(
 *     count, 
 *     display, 
 *     val -> "Count: " + val
 *   );
 * </pre>
 */
public class PropertyBinding {
    
    /**
     * Create a one-way binding from source to target.
     * When source changes, target is updated using the transformer function.
     * 
     * @param source The source property
     * @param target The target property
     * @param transformer Function to transform source value to target value
     * @param <S> Source value type
     * @param <T> Target value type
     */
    public static <S, T> void bindOneWay(
            ObservableProperty<S> source,
            ObservableProperty<T> target,
            Function<S, T> transformer) {
        
        // Initial update
        target.setValue(transformer.apply(source.getValue()));
        
        // Listen for changes
        source.addListener((oldVal, newVal) -> {
            target.setValue(transformer.apply(newVal));
        });
    }
    
    /**
     * Create a one-way binding with no transformation (source type must equal target type).
     * 
     * @param source The source property
     * @param target The target property
     * @param <T> Value type
     */
    public static <T> void bindOneWay(
            ObservableProperty<T> source,
            ObservableProperty<T> target) {
        bindOneWay(source, target, Function.identity());
    }
    
    /**
     * Create a two-way binding between two properties.
     * Changes to either property are reflected in the other.
     * 
     * @param prop1 First property
     * @param prop2 Second property
     * @param <T> Value type
     */
    public static <T> void bindTwoWay(
            ObservableProperty<T> prop1,
            ObservableProperty<T> prop2) {
        
        // Track if we're already updating to avoid infinite loops
        final boolean[] updating = {false};
        
        // prop1 -> prop2
        prop1.addListener((oldVal, newVal) -> {
            if (!updating[0]) {
                updating[0] = true;
                prop2.setValue(newVal);
                updating[0] = false;
            }
        });
        
        // prop2 -> prop1
        prop2.addListener((oldVal, newVal) -> {
            if (!updating[0]) {
                updating[0] = true;
                prop1.setValue(newVal);
                updating[0] = false;
            }
        });
    }
}
