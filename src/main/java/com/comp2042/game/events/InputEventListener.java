package com.comp2042.game.events;

import com.comp2042.game.model.DownData;
import com.comp2042.game.model.ViewData;

/**
 * Interface for handling user input events in the game.
 * Defines callback methods for different types of player actions.
 */
public interface InputEventListener {

    /**
     * Handles a down movement event.
     * @param event The move event containing event details
     * @return DownData containing information about the movement and any cleared rows
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles a left movement event.
     * @param event The move event containing event details
     * @return ViewData containing the updated brick position and shape
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles a right movement event.
     * @param event The move event containing event details
     * @return ViewData containing the updated brick position and shape
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles a rotate event.
     * @param event The move event containing event details
     * @return ViewData containing the updated brick position and rotated shape
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Creates a new game by resetting the board and creating the first brick.
     */
    void createNewGame();
}

