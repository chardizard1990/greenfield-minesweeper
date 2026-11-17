package com.minesweeper.gui.animation;

import javafx.animation.Timeline;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.Node;

/**
 * Tile-specific animation factory.
 * 
 * Creates animations for tile state transitions like revealing, flagging,
 * and explosion effects. These are higher-level animations built on
 * top of the basic AnimationUtil animations.
 */
public class TileAnimations {
    
    private TileAnimations() {
        // Factory class - don't instantiate
    }
    
    /**
     * Create a reveal animation for when a tile is revealed.
     * 
     * The animation fades in the tile and slightly scales it.
     * 
     * @param tileNode The tile node to animate
     * @return Timeline for the reveal animation
     */
    public static Timeline createRevealAnimation(Node tileNode) {
        // Reset to transparent and small
        tileNode.setOpacity(0);
        tileNode.setScaleX(0.8);
        tileNode.setScaleY(0.8);
        
        Timeline timeline = new Timeline();
        
        // Fade in and scale to normal
        timeline.getKeyFrames().addAll(
            new javafx.animation.KeyFrame(Duration.millis(0),
                new javafx.animation.KeyValue(tileNode.opacityProperty(), 0),
                new javafx.animation.KeyValue(tileNode.scaleXProperty(), 0.8),
                new javafx.animation.KeyValue(tileNode.scaleYProperty(), 0.8)),
            new javafx.animation.KeyFrame(Duration.millis(300),
                new javafx.animation.KeyValue(tileNode.opacityProperty(), 1,
                    javafx.animation.Interpolator.EASE_BOTH),
                new javafx.animation.KeyValue(tileNode.scaleXProperty(), 1,
                    javafx.animation.Interpolator.EASE_BOTH),
                new javafx.animation.KeyValue(tileNode.scaleYProperty(), 1,
                    javafx.animation.Interpolator.EASE_BOTH))
        );
        
        return timeline;
    }
    
    /**
     * Create a flag animation for when a tile is flagged.
     * 
     * The animation fades in and slightly scales the flag.
     * 
     * @param flagNode The flag node to animate
     * @return Timeline for the flag animation
     */
    public static Timeline createFlagAnimation(Node flagNode) {
        // Reset to transparent and small
        flagNode.setOpacity(0);
        flagNode.setScaleX(0.5);
        flagNode.setScaleY(0.5);
        
        Timeline timeline = new Timeline();
        
        // Fade in and scale to normal with slight bounce
        timeline.getKeyFrames().addAll(
            new javafx.animation.KeyFrame(Duration.millis(0),
                new javafx.animation.KeyValue(flagNode.opacityProperty(), 0),
                new javafx.animation.KeyValue(flagNode.scaleXProperty(), 0.5),
                new javafx.animation.KeyValue(flagNode.scaleYProperty(), 0.5)),
            new javafx.animation.KeyFrame(Duration.millis(150),
                new javafx.animation.KeyValue(flagNode.opacityProperty(), 1,
                    javafx.animation.Interpolator.EASE_BOTH),
                new javafx.animation.KeyValue(flagNode.scaleXProperty(), 1.1,
                    javafx.animation.Interpolator.EASE_BOTH),
                new javafx.animation.KeyValue(flagNode.scaleYProperty(), 1.1,
                    javafx.animation.Interpolator.EASE_BOTH)),
            new javafx.animation.KeyFrame(Duration.millis(200),
                new javafx.animation.KeyValue(flagNode.scaleXProperty(), 1),
                new javafx.animation.KeyValue(flagNode.scaleYProperty(), 1))
        );
        
        return timeline;
    }
    
    /**
     * Create an unflag animation for when a tile is unflagged.
     * 
     * The reverse of the flag animation.
     * 
     * @param flagNode The flag node to animate
     * @return Timeline for the unflag animation
     */
    public static Timeline createUnflagAnimation(Node flagNode) {
        Timeline timeline = new Timeline();
        
        // Fade out and scale down
        timeline.getKeyFrames().addAll(
            new javafx.animation.KeyFrame(Duration.millis(0),
                new javafx.animation.KeyValue(flagNode.opacityProperty(), 1),
                new javafx.animation.KeyValue(flagNode.scaleXProperty(), 1),
                new javafx.animation.KeyValue(flagNode.scaleYProperty(), 1)),
            new javafx.animation.KeyFrame(Duration.millis(150),
                new javafx.animation.KeyValue(flagNode.opacityProperty(), 0,
                    javafx.animation.Interpolator.EASE_IN),
                new javafx.animation.KeyValue(flagNode.scaleXProperty(), 0.5,
                    javafx.animation.Interpolator.EASE_IN),
                new javafx.animation.KeyValue(flagNode.scaleYProperty(), 0.5,
                    javafx.animation.Interpolator.EASE_IN))
        );
        
        timeline.setOnFinished(e -> flagNode.setOpacity(0));
        return timeline;
    }
    
    /**
     * Create an explosion animation for when a mine is hit.
     * 
     * Shows a brief visual effect indicating a mine explosion.
     * 
     * @param tileNode The tile node to animate
     * @return Timeline for the explosion animation
     */
    public static Timeline createExplosionAnimation(Node tileNode) {
        Timeline timeline = new Timeline();
        
        // Bright flash followed by fade
        timeline.getKeyFrames().addAll(
            new javafx.animation.KeyFrame(Duration.millis(0),
                new javafx.animation.KeyValue(tileNode.opacityProperty(), 1)),
            new javafx.animation.KeyFrame(Duration.millis(100),
                new javafx.animation.KeyValue(tileNode.opacityProperty(), 1)),
            new javafx.animation.KeyFrame(Duration.millis(300),
                new javafx.animation.KeyValue(tileNode.opacityProperty(), 0.5,
                    javafx.animation.Interpolator.EASE_OUT)),
            new javafx.animation.KeyFrame(Duration.millis(500),
                new javafx.animation.KeyValue(tileNode.opacityProperty(), 1))
        );
        
        return timeline;
    }
    
    /**
     * Create a cascade delay animation.
     * 
     * When a tile with 0 adjacent mines is revealed, surrounding tiles
     * are revealed with a staggered delay to create a cascade effect.
     * 
     * @param distanceFromOrigin Distance from the original tile (determines delay)
     * @return Timeline that completes after the cascade delay
     */
    public static Timeline createCascadeDelay(int distanceFromOrigin) {
        // Each tile in the cascade gets revealed with increasing delay
        // Distance 0 = no delay, distance 1 = 50ms, distance 2 = 100ms, etc.
        int delay = distanceFromOrigin * 50;
        
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
            new javafx.animation.KeyFrame(Duration.millis(delay))
        );
        
        return timeline;
    }
    
    /**
     * Create a "pulsing" highlight animation.
     * 
     * Useful for drawing attention to a tile (e.g., during cascade reveals).
     * 
     * @param tileNode The tile node to animate
     * @return Timeline for the pulse animation
     */
    public static Timeline createPulseAnimation(Node tileNode) {
        return AnimationUtil.createPulse(tileNode, 2, 150);
    }
    
    /**
     * Create a correct flag animation.
     * 
     * Shows when a correctly-flagged tile is revealed at game end.
     * 
     * @param tileNode The tile node to animate
     * @return Timeline for the animation
     */
    public static Timeline createCorrectFlagAnimation(Node tileNode) {
        // Green glow/highlight briefly
        Timeline timeline = new Timeline();
        
        timeline.getKeyFrames().addAll(
            new javafx.animation.KeyFrame(Duration.millis(0),
                new javafx.animation.KeyValue(tileNode.opacityProperty(), 1)),
            new javafx.animation.KeyFrame(Duration.millis(300),
                new javafx.animation.KeyValue(tileNode.opacityProperty(), 1)),
            new javafx.animation.KeyFrame(Duration.millis(600),
                new javafx.animation.KeyValue(tileNode.opacityProperty(), 0.7))
        );
        
        return timeline;
    }
}
