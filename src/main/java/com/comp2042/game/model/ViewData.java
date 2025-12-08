package com.comp2042.game.model;

import com.comp2042.game.util.MatrixOperations;

/**
 * Immutable data class containing view information for rendering.
 * Stores the current brick position, shape, and preview of the next brick.
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    /**
     * Constructs a new ViewData object with the specified brick information.
     * @param brickData 2D array representing the current brick shape
     * @param xPosition X coordinate of the brick on the board
     * @param yPosition Y coordinate of the brick on the board
     * @param nextBrickData 2D array representing the next brick shape for preview
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        // Copy arrays on construction to ensure immutability
        // This is the only place we need to copy - getters can return references safely
        this.brickData = MatrixOperations.copy(brickData);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = MatrixOperations.copy(nextBrickData);
    }

    /**
     * Gets the current brick shape data.
     * @return 2D array representing the brick shape matrix
     */
    public int[][] getBrickData() {
        // Return reference directly - ViewData is immutable (final fields)
        // No need to copy on every getter call since the internal array never changes
        return brickData;
    }

    /**
     * Gets the X coordinate of the brick.
     * @return The X position of the brick on the board
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the Y coordinate of the brick.
     * @return The Y position of the brick on the board
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets the next brick shape data for preview display.
     * @return 2D array representing the next brick shape matrix
     */
    public int[][] getNextBrickData() {
        // Return reference directly - ViewData is immutable (final fields)
        // No need to copy on every getter call since the internal array never changes
        return nextBrickData;
    }
}

