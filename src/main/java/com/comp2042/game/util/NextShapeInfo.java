package com.comp2042.game.util;

/**
 * Immutable data class containing information about the next rotation shape.
 * Stores the shape matrix and its position index in the rotation sequence.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a new NextShapeInfo object with the specified shape and position.
     * @param shape The 2D array representing the shape matrix
     * @param position The index position of this shape in the rotation sequence
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        // Copy shape on construction to ensure immutability
        // This is the only place we need to copy - getter can return reference safely
        this.shape = MatrixOperations.copy(shape);
        this.position = position;
    }

    /**
     * Gets the shape matrix.
     * @return 2D array representing the brick shape
     */
    public int[][] getShape() {
        // Return reference directly - NextShapeInfo is immutable (final fields)
        // No need to copy on every getter call since the internal array never changes
        return shape;
    }

    /**
     * Gets the position index in the rotation sequence.
     * @return The index of this rotation state (0-based)
     */
    public int getPosition() {
        return position;
    }
}

