package com.minesweeper.gui.animation;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Utility class for creating common animations.
 * 
 * This provides reusable animation builders for smooth transitions
 * and effects in the game GUI.
 */
public class AnimationUtil {
    
    private AnimationUtil() {
        // Utility class - don't instantiate
    }
    
    /**
     * Create a fade-in animation.
     * 
     * @param node JavaFX node to animate
     * @param duration Duration in milliseconds
     * @return Timeline for the animation
     */
    public static Timeline createFadeIn(javafx.scene.Node node, int duration) {
        return createFade(node, 0, 1, duration);
    }
    
    /**
     * Create a fade-out animation.
     * 
     * @param node JavaFX node to animate
     * @param duration Duration in milliseconds
     * @return Timeline for the animation
     */
    public static Timeline createFadeOut(javafx.scene.Node node, int duration) {
        return createFade(node, 1, 0, duration);
    }
    
    /**
     * Create a fade animation with custom start/end opacity.
     * 
     * @param node JavaFX node to animate
     * @param startOpacity Starting opacity (0.0 - 1.0)
     * @param endOpacity Ending opacity (0.0 - 1.0)
     * @param duration Duration in milliseconds
     * @return Timeline for the animation
     */
    public static Timeline createFade(javafx.scene.Node node, double startOpacity, 
                                      double endOpacity, int duration) {
        node.setOpacity(startOpacity);
        
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(duration),
                new javafx.animation.KeyValue(node.opacityProperty(), endOpacity, 
                    Interpolator.EASE_BOTH))
        );
        
        return timeline;
    }
    
    /**
     * Create a scale animation (zoom in/out).
     * 
     * @param node JavaFX node to animate
     * @param startScale Starting scale (1.0 = normal)
     * @param endScale Ending scale
     * @param duration Duration in milliseconds
     * @return Timeline for the animation
     */
    public static Timeline createScale(javafx.scene.Node node, double startScale, 
                                       double endScale, int duration) {
        // Note: This is a simplified version. Full implementation would need
        // to handle the transform properly. For now, we just track the animation.
        
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        
        // Add callback for animation complete
        final double[] currentScale = {startScale};
        timeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(duration),
                event -> {
                    // Scale animation complete
                })
        );
        
        return timeline;
    }
    
    /**
     * Create a simple delay/pause animation.
     * 
     * @param duration Duration in milliseconds
     * @param callback Called when animation completes
     * @return Timeline for the animation
     */
    public static Timeline createDelay(int duration, EventHandler<ActionEvent> callback) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(duration), callback)
        );
        return timeline;
    }
    
    /**
     * Create a rotation animation (for effects like spinning).
     * 
     * @param node JavaFX node to animate
     * @param startAngle Starting rotation in degrees
     * @param endAngle Ending rotation in degrees
     * @param duration Duration in milliseconds
     * @return Timeline for the animation
     */
    public static Timeline createRotate(javafx.scene.Node node, double startAngle,
                                        double endAngle, int duration) {
        // Create rotation transform if needed
        javafx.scene.transform.Rotate rotate = new javafx.scene.transform.Rotate();
        rotate.setAngle(startAngle);
        node.getTransforms().add(rotate);
        
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(duration),
                new javafx.animation.KeyValue(rotate.angleProperty(), endAngle,
                    Interpolator.EASE_BOTH))
        );
        
        return timeline;
    }
    
    /**
     * Create a pulse animation (scale in and out).
     * 
     * @param node JavaFX node to animate
     * @param pulses Number of pulses
     * @param pulseDuration Duration of each pulse in milliseconds
     * @return Timeline for the animation
     */
    public static Timeline createPulse(javafx.scene.Node node, int pulses, int pulseDuration) {
        Timeline timeline = new Timeline();
        double scaleFrom = 1.0;
        double scaleTo = 1.2;
        
        for (int i = 0; i < pulses; i++) {
            timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(i * pulseDuration),
                    new javafx.animation.KeyValue(node.scaleXProperty(), scaleFrom),
                    new javafx.animation.KeyValue(node.scaleYProperty(), scaleFrom)),
                new KeyFrame(Duration.millis(i * pulseDuration + pulseDuration / 2),
                    new javafx.animation.KeyValue(node.scaleXProperty(), scaleTo,
                        Interpolator.EASE_BOTH),
                    new javafx.animation.KeyValue(node.scaleYProperty(), scaleTo,
                        Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis((i + 1) * pulseDuration),
                    new javafx.animation.KeyValue(node.scaleXProperty(), scaleFrom),
                    new javafx.animation.KeyValue(node.scaleYProperty(), scaleFrom))
            );
        }
        
        return timeline;
    }
    
    /**
     * Create a shake animation (useful for error effects).
     * 
     * @param node JavaFX node to animate
     * @param intensity How far to shake in pixels
     * @param duration Duration in milliseconds
     * @return Timeline for the animation
     */
    public static Timeline createShake(javafx.scene.Node node, double intensity, int duration) {
        Timeline timeline = new Timeline();
        double originalX = node.getLayoutX();
        
        int shakes = 4;
        int shakeDuration = duration / shakes;
        
        for (int i = 0; i < shakes; i++) {
            double offset = (i % 2 == 0) ? intensity : -intensity;
            timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(i * shakeDuration),
                    new javafx.animation.KeyValue(node.layoutXProperty(), 
                        originalX + offset))
            );
        }
        
        // Return to original position
        timeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(duration),
                new javafx.animation.KeyValue(node.layoutXProperty(), originalX))
        );
        
        return timeline;
    }
}
