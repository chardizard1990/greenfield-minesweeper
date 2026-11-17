package com.minesweeper.game;

/**
 * Represents game statistics tracked during a game session.
 * 
 * Tracks:
 * - Time elapsed since game start
 * - Moves made by player
 * - Accuracy (correct moves / total moves)
 * - Flags placed
 */
public class GameStatistics {

    private final long startTime;
    private long endTime;
    private int totalMoves;
    private int flagsPlaced;
    private int flagsCorrect;
    private int revealsMade;
    private int revealsCorrect;

    /**
     * Creates game statistics, starting the timer.
     */
    public GameStatistics() {
        this.startTime = System.currentTimeMillis();
        this.endTime = 0;
        this.totalMoves = 0;
        this.flagsPlaced = 0;
        this.flagsCorrect = 0;
        this.revealsMade = 0;
        this.revealsCorrect = 0;
    }

    // Accessors
    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public int getFlagsPlaced() {
        return flagsPlaced;
    }

    public int getFlagsCorrect() {
        return flagsCorrect;
    }

    public int getRevealsMade() {
        return revealsMade;
    }

    public int getRevealsCorrect() {
        return revealsCorrect;
    }

    /**
     * Gets elapsed time in milliseconds.
     * If game is not finished, returns current elapsed time.
     * 
     * @return elapsed time in milliseconds
     */
    public long getElapsedTimeMs() {
        long end = endTime > 0 ? endTime : System.currentTimeMillis();
        return end - startTime;
    }

    /**
     * Gets elapsed time in seconds (rounded).
     * 
     * @return elapsed time in seconds
     */
    public long getElapsedTimeSeconds() {
        return getElapsedTimeMs() / 1000;
    }

    /**
     * Records a flag action.
     * 
     * @param flagged whether tile is now flagged
     * @param isMine whether the flagged tile actually contains a mine
     */
    public void recordFlagAction(boolean flagged, boolean isMine) {
        if (flagged) {
            flagsPlaced++;
            if (isMine) {
                flagsCorrect++;
            }
        }
        totalMoves++;
    }

    /**
     * Records a reveal action.
     * 
     * @param isMine whether the revealed tile contains a mine (hit mine = incorrect)
     */
    public void recordRevealAction(boolean isMine) {
        revealsMade++;
        if (!isMine) {
            revealsCorrect++;
        }
        totalMoves++;
    }

    /**
     * Marks game as finished.
     */
    public void finishGame() {
        this.endTime = System.currentTimeMillis();
    }

    /**
     * Gets overall accuracy as percentage.
     * Accuracy = correct moves / total moves * 100
     * 
     * @return accuracy percentage, 0 if no moves made
     */
    public double getAccuracy() {
        if (totalMoves == 0) {
            return 0.0;
        }
        int correctMoves = flagsCorrect + revealsCorrect;
        return (double) correctMoves / totalMoves * 100.0;
    }

    @Override
    public String toString() {
        return String.format(
                "GameStats(moves=%d, flags=%d/%d, reveals=%d/%d, time=%ds, accuracy=%.1f%%)",
                totalMoves, flagsCorrect, flagsPlaced, revealsCorrect, revealsMade,
                getElapsedTimeSeconds(), getAccuracy()
        );
    }
}
