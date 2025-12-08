package com.comp2042.logic.bricks;

/**
 * Interface for generating Tetris bricks.
 * Provides methods to get the current brick and preview the next brick.
 */
public interface BrickGenerator {

    /**
     * Gets the current brick to be placed on the board.
     * @return A Brick object representing the current tetromino
     */
    Brick getBrick();

    /**
     * Gets the next brick that will appear after the current one.
     * @return A Brick object representing the next tetromino
     */
    Brick getNextBrick();
}
