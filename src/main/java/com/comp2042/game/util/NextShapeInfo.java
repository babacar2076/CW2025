package com.comp2042.game.util;

public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    public NextShapeInfo(final int[][] shape, final int position) {
        // Copy shape on construction to ensure immutability
        // This is the only place we need to copy - getter can return reference safely
        this.shape = MatrixOperations.copy(shape);
        this.position = position;
    }

    public int[][] getShape() {
        // Return reference directly - NextShapeInfo is immutable (final fields)
        // No need to copy on every getter call since the internal array never changes
        return shape;
    }

    public int getPosition() {
        return position;
    }
}

