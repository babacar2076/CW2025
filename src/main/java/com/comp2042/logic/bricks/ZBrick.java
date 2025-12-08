package com.comp2042.logic.bricks;

import com.comp2042.game.util.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Z-shaped brick (Z-brick) implementation.
 * A zigzag brick that can be rotated to form a mirror image (opposite of S-brick).
 */
final class ZBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a Z-brick with its two rotation states.
     */
    public ZBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
