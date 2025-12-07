package com.comp2042.game.model;

import com.comp2042.game.util.MatrixOperations;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        // Copy matrix on construction to ensure immutability
        // This is the only place we need to copy - getter can return reference safely
        this.linesRemoved = linesRemoved;
        this.newMatrix = MatrixOperations.copy(newMatrix);
        this.scoreBonus = scoreBonus;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        // Return reference directly - ClearRow is immutable (final fields)
        // No need to copy on every getter call since the internal array never changes
        return newMatrix;
    }

    public int getScoreBonus() {
        return scoreBonus;
    }
}

