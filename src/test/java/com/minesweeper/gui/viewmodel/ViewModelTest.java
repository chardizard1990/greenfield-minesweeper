package com.minesweeper.gui.viewmodel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ViewModel base class and ViewModel lifecycle.
 */
public class ViewModelTest {
    
    private TestViewModel viewModel;
    
    @BeforeEach
    public void setUp() {
        viewModel = new TestViewModel();
    }
    
    @Test
    public void testInitializeMethod() {
        assertFalse(viewModel.initialized);
        viewModel.initialize();
        assertTrue(viewModel.initialized);
    }
    
    @Test
    public void testCleanupMethod() {
        assertFalse(viewModel.cleaned);
        viewModel.cleanup();
        assertTrue(viewModel.cleaned);
    }
    
    @Test
    public void testMultipleInitializeCalls() {
        viewModel.initialize();
        viewModel.initialize();
        // Should handle multiple calls without error
        assertTrue(viewModel.initialized);
    }
    
    @Test
    public void testLifecycleSequence() {
        // Initialize
        viewModel.initialize();
        assertTrue(viewModel.initialized);
        assertFalse(viewModel.cleaned);
        
        // Then cleanup
        viewModel.cleanup();
        assertTrue(viewModel.initialized);
        assertTrue(viewModel.cleaned);
    }
    
    /**
     * Test implementation of ViewModel for testing purposes.
     */
    private static class TestViewModel extends ViewModel {
        boolean initialized = false;
        boolean cleaned = false;
        
        @Override
        public void initialize() {
            initialized = true;
        }
        
        @Override
        public void cleanup() {
            cleaned = true;
        }
    }
}
