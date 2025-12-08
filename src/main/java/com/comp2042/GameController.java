package com.comp2042;

import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import com.comp2042.game.model.ClearRow;
import com.comp2042.game.model.DownData;
import com.comp2042.game.model.Score;
import com.comp2042.game.model.ViewData;

/**
 * Main game controller that handles user input events and manages game logic.
 * Acts as a bridge between the GUI controller and the game board, processing all player actions.
 */
public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(20, 10); //change the width from 30 to 20
    private final GuiController viewGuiController;

    /**
     * Constructs a new GameController and connects it to the GUI controller.
     * @param c The GUI controller to connect to
     */
    public GameController(GuiController c) {
        viewGuiController = c;
        viewGuiController.setEventListener(this);
        // Don't start game immediately - wait for New Game button
    }
    
    /**
     * Starts a new game by creating the first brick and initializing the game view.
     */
    public void startGame() {
        board.createNewBrick();
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    /**
     * Handles the down movement event, moving the brick down and processing row clearing.
     * @param event The move event containing event type and source information
     * @return DownData containing information about cleared rows and updated view data
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handles the left movement event.
     * @param event The move event containing event type and source information
     * @return ViewData containing the updated brick position and shape
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Handles the right movement event.
     * @param event The move event containing event type and source information
     * @return ViewData containing the updated brick position and shape
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Handles the rotate event.
     * @param event The move event containing event type and source information
     * @return ViewData containing the updated brick position and rotated shape
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    /**
     * Creates a new game by resetting the board and creating the first brick.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        board.createNewBrick();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.refreshBrick(board.getViewData());
        viewGuiController.refreshHoldBrick();
    }
    
    /**
     * Resets the game board without creating a new brick.
     */
    public void resetGame() {
        board.newGame();
    }
    
    /**
     * Gets the ghost position (where the brick will land) for visual feedback.
     * @return ViewData containing the ghost brick position
     */
    public ViewData getGhostPosition() {
        if (board instanceof SimpleBoard) {
            return ((SimpleBoard) board).getGhostPosition();
        }
        return board.getViewData();
    }
    
    /**
     * Gets the current game score.
     * @return Score object containing the current score
     */
    public Score getScore() {
        return board.getScore();
    }
    
    /**
     * Holds or swaps the current brick with the held brick.
     * @return ViewData containing the new current brick after holding
     */
    public ViewData holdBrick() {
        return board.holdBrick();
    }
    
    /**
     * Gets the currently held brick.
     * @return The held Brick object, or null if no brick is held
     */
    public com.comp2042.logic.bricks.Brick getHeldBrick() {
        if (board instanceof SimpleBoard) {
            return ((SimpleBoard) board).getHeldBrick();
        }
        return null;
    }
}
