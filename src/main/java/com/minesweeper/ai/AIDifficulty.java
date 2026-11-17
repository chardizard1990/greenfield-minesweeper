package com.minesweeper.ai;

/**
 * Enum representing the AI difficulty levels.
 * 
 * Different difficulty levels use different strategies:
 * - EASY: Random moves among safe unrevealed tiles
 * - NORMAL: Strategic analysis of board state to identify safe and mine tiles
 * - HARD: Optimal solver using constraint satisfaction and pattern matching
 */
public enum AIDifficulty {
    EASY,    // Random strategy
    NORMAL,  // Strategic analysis
    HARD     // Optimal solving
}
