package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Interface representing a Tetris brick (tetromino).
 * Each brick can have multiple rotation states defined by shape matrices.
 */
public interface Brick {

    /**
     * Gets all rotation states of the brick as a list of 2D matrices.
     * Each matrix represents one rotation orientation of the brick.
     * @return List of 2D integer arrays, where each array represents one rotation state
     */
    List<int[][]> getShapeMatrix();
}
