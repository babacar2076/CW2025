package com.comp2042.game.model;

import com.comp2042.game.util.MatrixOperations;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        // Copy arrays on construction to ensure immutability
        // This is the only place we need to copy - getters can return references safely
        this.brickData = MatrixOperations.copy(brickData);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = MatrixOperations.copy(nextBrickData);
    }

    public int[][] getBrickData() {
        // Return reference directly - ViewData is immutable (final fields)
        // No need to copy on every getter call since the internal array never changes
        return brickData;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        // Return reference directly - ViewData is immutable (final fields)
        // No need to copy on every getter call since the internal array never changes
        return nextBrickData;
    }
}

