package com.minesweeper.ai;

/**
 * Represents the type of move decision made by the AI.
 */
public enum MoveType {
    REVEAL("Reveal"),
    FLAG("Flag"),
    UNFLAG("Unflag");

    private final String displayName;

    MoveType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
