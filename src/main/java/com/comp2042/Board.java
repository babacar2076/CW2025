package com.comp2042;

import com.comp2042.game.model.ClearRow;
import com.comp2042.game.model.Score;
import com.comp2042.game.model.ViewData;

/**
 * Interface representing the game board for Tetris.
 * Defines the core operations for managing bricks, movements, and game state.
 */
public interface Board {

    /**
     * Moves the current brick down one position.
     * @return true if the move was successful, false if the brick cannot move down
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick left one position.
     * @return true if the move was successful, false if the brick cannot move left
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick right one position.
     * @return true if the move was successful, false if the brick cannot move right
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick counter-clockwise.
     * @return true if the rotation was successful, false if the brick cannot rotate
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick at the top of the board.
     * @return true if a new brick was successfully created, false if game over (board is full)
     */
    boolean createNewBrick();

    /**
     * Gets the current state of the game board matrix.
     * @return a 2D array representing the board state, where 0 is empty and other values represent brick colors
     */
    int[][] getBoardMatrix();

    /**
     * Gets the view data for the current brick including its position and shape.
     * @return ViewData object containing the current brick information
     */
    ViewData getViewData();

    /**
     * Merges the current brick into the background board.
     * This is called when a brick can no longer move down.
     */
    void mergeBrickToBackground();

    /**
     * Clears completed rows from the board.
     * @return ClearRow object containing information about cleared rows and score bonus
     */
    ClearRow clearRows();

    /**
     * Gets the score object for tracking game score.
     * @return Score object containing the current game score
     */
    Score getScore();

    /**
     * Resets the board to start a new game.
     */
    void newGame();
    
    /**
     * Holds or swaps the current brick with the held brick.
     * @return ViewData object for the new current brick after holding
     */
    ViewData holdBrick();
}
