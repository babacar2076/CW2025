package com.comp2042;

import com.comp2042.game.util.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;

/**
 * Manages the rotation state of a brick.
 * Handles rotating through the different orientations of a brick shape.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Gets the next shape in the rotation sequence.
     * @return NextShapeInfo containing the next shape matrix and its position index
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Gets the current shape matrix of the brick.
     * @return 2D array representing the current brick shape
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation position of the brick.
     * @param currentShape The index of the rotation position (0-based)
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick to rotate and resets to the first rotation position.
     * @param brick The Brick object to manage rotations for
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }
    
    /**
     * Gets the current brick being rotated.
     * @return The Brick object currently being managed
     */
    public Brick getBrick() {
        return brick;
    }
}
