package com.minesweeper.gui.animation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AnimationUtil.
 * 
 * These tests verify that animation utilities are properly created
 * and have correct properties for timing and interpolation.
 */
@DisplayName("AnimationUtil Tests")
public class AnimationUtilTest {
    
    @Test
    @DisplayName("createFadeIn should return valid timeline")
    public void testCreateFadeIn() {
        javafx.scene.layout.StackPane node = new javafx.scene.layout.StackPane();
        javafx.animation.Timeline timeline = AnimationUtil.createFadeIn(node, 300);
        
        assertNotNull(timeline, "Timeline should not be null");
        assertFalse(timeline.getKeyFrames().isEmpty(), "Timeline should have key frames");
        assertEquals(1, timeline.getKeyFrames().size(), "Fade in should have one key frame");
    }
    
    @Test
    @DisplayName("createFadeOut should return valid timeline")
    public void testCreateFadeOut() {
        javafx.scene.layout.StackPane node = new javafx.scene.layout.StackPane();
        javafx.animation.Timeline timeline = AnimationUtil.createFadeOut(node, 300);
        
        assertNotNull(timeline, "Timeline should not be null");
        assertFalse(timeline.getKeyFrames().isEmpty(), "Timeline should have key frames");
    }
    
    @Test
    @DisplayName("createFade should set correct opacity values")
    public void testCreateFade() {
        javafx.scene.layout.StackPane node = new javafx.scene.layout.StackPane();
        javafx.animation.Timeline timeline = AnimationUtil.createFade(node, 0.3, 0.8, 500);
        
        assertNotNull(timeline, "Timeline should not be null");
        // Initial opacity should be set to start value
        assertEquals(0.3, node.getOpacity(), 0.01, "Initial opacity should be 0.3");
    }
    
    @Test
    @DisplayName("createRotate should return valid timeline")
    public void testCreateRotate() {
        javafx.scene.layout.StackPane node = new javafx.scene.layout.StackPane();
        javafx.animation.Timeline timeline = AnimationUtil.createRotate(node, 0, 360, 1000);
        
        assertNotNull(timeline, "Timeline should not be null");
        assertFalse(timeline.getKeyFrames().isEmpty(), "Timeline should have key frames");
        assertEquals(1, timeline.getKeyFrames().size(), "Rotate animation should have one key frame");
    }
    
    @Test
    @DisplayName("createScale should return valid timeline")
    public void testCreateScale() {
        javafx.scene.layout.StackPane node = new javafx.scene.layout.StackPane();
        javafx.animation.Timeline timeline = AnimationUtil.createScale(node, 1.0, 1.5, 300);
        
        assertNotNull(timeline, "Timeline should not be null");
        assertFalse(timeline.getKeyFrames().isEmpty(), "Timeline should have key frames");
    }
    
    @Test
    @DisplayName("createDelay should return valid timeline")
    public void testCreateDelay() {
        javafx.animation.Timeline timeline = AnimationUtil.createDelay(500, e -> {});
        
        assertNotNull(timeline, "Timeline should not be null");
        assertFalse(timeline.getKeyFrames().isEmpty(), "Timeline should have key frames");
        assertEquals(1, timeline.getKeyFrames().size(), "Delay should have one key frame");
    }
    
    @Test
    @DisplayName("createPulse should return valid timeline")
    public void testCreatePulse() {
        javafx.scene.layout.StackPane node = new javafx.scene.layout.StackPane();
        javafx.animation.Timeline timeline = AnimationUtil.createPulse(node, 2, 150);
        
        assertNotNull(timeline, "Timeline should not be null");
        // Pulse animation should have multiple key frames (start, peak, return) * pulses
        assertTrue(timeline.getKeyFrames().size() >= 3, "Pulse should have multiple key frames");
    }
    
    @Test
    @DisplayName("createShake should return valid timeline")
    public void testCreateShake() {
        javafx.scene.layout.StackPane node = new javafx.scene.layout.StackPane();
        javafx.animation.Timeline timeline = AnimationUtil.createShake(node, 5.0, 400);
        
        assertNotNull(timeline, "Timeline should not be null");
        // Shake animation should have multiple key frames for each shake
        assertTrue(timeline.getKeyFrames().size() > 1, "Shake should have multiple key frames");
    }
}
