package com.minesweeper.gui.viewmodel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ObservableProperty.
 * Verifies that observable properties correctly notify listeners of changes.
 */
public class ObservablePropertyTest {
    
    private ObservableProperty<String> stringProperty;
    private ObservableProperty<Integer> intProperty;
    
    @BeforeEach
    public void setUp() {
        stringProperty = new ObservableProperty<>("initial");
        intProperty = new ObservableProperty<>(0);
    }
    
    @Test
    public void testGetValue() {
        assertEquals("initial", stringProperty.getValue());
        assertEquals(0, intProperty.getValue());
    }
    
    @Test
    public void testSetValue() {
        stringProperty.setValue("updated");
        assertEquals("updated", stringProperty.getValue());
        
        intProperty.setValue(42);
        assertEquals(42, intProperty.getValue());
    }
    
    @Test
    public void testListenerNotifiedOnChange() {
        // Track listener calls
        final String[] oldValue = {null};
        final String[] newValue = {null};
        
        stringProperty.addListener((old, newVal) -> {
            oldValue[0] = old;
            newValue[0] = newVal;
        });
        
        stringProperty.setValue("changed");
        
        assertEquals("initial", oldValue[0]);
        assertEquals("changed", newValue[0]);
    }
    
    @Test
    public void testMultipleListeners() {
        // Track calls from two different listeners
        final int[] listener1Calls = {0};
        final int[] listener2Calls = {0};
        
        stringProperty.addListener((old, newVal) -> listener1Calls[0]++);
        stringProperty.addListener((old, newVal) -> listener2Calls[0]++);
        
        stringProperty.setValue("first");
        stringProperty.setValue("second");
        
        assertEquals(2, listener1Calls[0]);
        assertEquals(2, listener2Calls[0]);
    }
    
    @Test
    public void testNoNotificationForSameValue() {
        final int[] listenerCalls = {0};
        
        stringProperty.addListener((old, newVal) -> listenerCalls[0]++);
        
        stringProperty.setValue("initial"); // Same value
        stringProperty.setValue("initial"); // Same value again
        stringProperty.setValue("updated"); // Different value
        
        // Should only be called once for the actual change
        assertEquals(1, listenerCalls[0]);
    }
    
    @Test
    public void testRemoveListener() {
        final int[] listenerCalls = {0};
        PropertyChangeListener<String> listener = (old, newVal) -> listenerCalls[0]++;
        
        stringProperty.addListener(listener);
        stringProperty.setValue("first");
        
        stringProperty.removeListener(listener);
        stringProperty.setValue("second");
        
        // Should only have been called once (for the first change)
        assertEquals(1, listenerCalls[0]);
    }
    
    @Test
    public void testClearListeners() {
        final int[] listener1Calls = {0};
        final int[] listener2Calls = {0};
        
        stringProperty.addListener((old, newVal) -> listener1Calls[0]++);
        stringProperty.addListener((old, newVal) -> listener2Calls[0]++);
        
        stringProperty.clearListeners();
        stringProperty.setValue("updated");
        
        // Neither listener should be called
        assertEquals(0, listener1Calls[0]);
        assertEquals(0, listener2Calls[0]);
    }
    
    @Test
    public void testNullValue() {
        ObservableProperty<String> nullable = new ObservableProperty<>(null);
        assertEquals(null, nullable.getValue());
        
        nullable.setValue("not null");
        assertEquals("not null", nullable.getValue());
        
        nullable.setValue(null);
        assertEquals(null, nullable.getValue());
    }
    
    @Test
    public void testInitialNull() {
        ObservableProperty<String> nullable = new ObservableProperty<>();
        assertEquals(null, nullable.getValue());
    }
    
    @Test
    public void testListenerExceptionHandling() {
        // Add a listener that throws an exception
        stringProperty.addListener((old, newVal) -> {
            throw new RuntimeException("Test exception");
        });
        
        // Add a second listener that should still be called
        final int[] listener2Calls = {0};
        stringProperty.addListener((old, newVal) -> listener2Calls[0]++);
        
        // This should not throw, just print error and continue
        stringProperty.setValue("updated");
        
        // Second listener should have been called despite first listener's exception
        assertEquals(1, listener2Calls[0]);
    }
}
