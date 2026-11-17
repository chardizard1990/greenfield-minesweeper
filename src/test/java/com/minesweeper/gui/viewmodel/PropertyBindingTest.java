package com.minesweeper.gui.viewmodel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PropertyBinding utilities.
 */
public class PropertyBindingTest {
    
    private ObservableProperty<String> source;
    private ObservableProperty<String> target;
    
    @BeforeEach
    public void setUp() {
        source = new ObservableProperty<>("source");
        target = new ObservableProperty<>("target");
    }
    
    @Test
    public void testOneWayBindingNoTransform() {
        // Bind source to target (no transformation)
        PropertyBinding.bindOneWay(source, target);
        
        // Initial update should happen
        assertEquals("source", target.getValue());
        
        // Changes to source should update target
        source.setValue("updated");
        assertEquals("updated", target.getValue());
        
        // Changes to target should NOT update source
        target.setValue("manual");
        assertEquals("updated", source.getValue());
    }
    
    @Test
    public void testOneWayBindingWithTransform() {
        ObservableProperty<Integer> intSource = new ObservableProperty<>(5);
        ObservableProperty<String> stringTarget = new ObservableProperty<>("");
        
        // Bind with transformation (int to string)
        PropertyBinding.bindOneWay(
            intSource,
            stringTarget,
            value -> "Value: " + value
        );
        
        // Initial update with transformation
        assertEquals("Value: 5", stringTarget.getValue());
        
        // Changes should use transformation
        intSource.setValue(10);
        assertEquals("Value: 10", stringTarget.getValue());
    }
    
    @Test
    public void testTwoWayBinding() {
        // Create two-way binding
        PropertyBinding.bindTwoWay(source, target);
        
        // Change source
        source.setValue("from_source");
        assertEquals("from_source", target.getValue());
        
        // Change target
        target.setValue("from_target");
        assertEquals("from_target", source.getValue());
        
        // Change source again
        source.setValue("back_to_source");
        assertEquals("back_to_source", target.getValue());
    }
    
    @Test
    public void testTwoWayBindingNoInfiniteLoop() {
        // This should not cause infinite loop
        PropertyBinding.bindTwoWay(source, target);
        
        source.setValue("value1");
        target.setValue("value2");
        source.setValue("value3");
        
        // Should complete without hanging
        assertEquals("value3", source.getValue());
        assertEquals("value3", target.getValue());
    }
}
