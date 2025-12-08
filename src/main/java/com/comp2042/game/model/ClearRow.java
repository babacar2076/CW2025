package com.comp2042.game.model;

import com.comp2042.game.util.MatrixOperations;

/**
 * Immutable data class containing information about cleared rows.
 * Stores the number of rows cleared, the updated board matrix, and the score bonus earned.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Constructs a new ClearRow object with the cleared row information.
     * @param linesRemoved The number of rows that were cleared
     * @param newMatrix The updated board matrix after clearing rows
     * @param scoreBonus The score bonus points awarded for clearing the rows
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        // Copy matrix on construction to ensure immutability
        // This is the only place we need to copy - getter can return reference safely
        this.linesRemoved = linesRemoved;
        this.newMatrix = MatrixOperations.copy(newMatrix);
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of rows that were cleared.
     * @return The count of cleared rows
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets the updated board matrix after clearing rows.
     * @return 2D array representing the board state after row clearing
     */
    public int[][] getNewMatrix() {
        // Return reference directly - ClearRow is immutable (final fields)
        // No need to copy on every getter call since the internal array never changes
        return newMatrix;
    }

    /**
     * Gets the score bonus awarded for clearing the rows.
     * @return The score bonus points (calculated as 50 * linesRemoved^2)
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}

