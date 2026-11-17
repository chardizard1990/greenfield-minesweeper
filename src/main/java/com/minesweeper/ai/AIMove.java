package com.minesweeper.ai;

/**
 * Represents a single AI move decision.
 * 
 * Tracks:
 * - The coordinates and type of move (reveal, flag, unflag)
 * - The reasoning behind the decision
 * - Confidence level if applicable
 */
public class AIMove {

    private final int row;
    private final int column;
    private final MoveType moveType;
    private final String reasoning;
    private final double confidence;

    /**
     * Creates an AI move with full context.
     *
     * @param row the row coordinate
     * @param column the column coordinate
     * @param moveType the type of move (REVEAL, FLAG, etc.)
     * @param reasoning description of why this move was chosen
     * @param confidence confidence level (0.0 to 1.0)
     */
    public AIMove(int row, int column, MoveType moveType, String reasoning, double confidence) {
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("Confidence must be between 0.0 and 1.0");
        }
        this.row = row;
        this.column = column;
        this.moveType = moveType;
        this.reasoning = reasoning;
        this.confidence = confidence;
    }

    // Accessors
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public String getReasoning() {
        return reasoning;
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        return String.format("AIMove(%d,%d)[%s] %s (confidence: %.2f)",
                row, column, moveType, reasoning, confidence);
    }
}
