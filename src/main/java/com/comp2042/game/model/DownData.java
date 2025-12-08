package com.comp2042.game.model;

/**
 * Immutable data class containing information from a down movement event.
 * Stores the result of row clearing (if any) and the updated view data.
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Constructs a new DownData object with the movement result information.
     * @param clearRow Information about cleared rows (can be null if no rows were cleared)
     * @param viewData The updated view data after the movement
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets information about cleared rows from this movement.
     * @return ClearRow object containing cleared row details, or null if no rows were cleared
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the updated view data after the down movement.
     * @return ViewData object containing the current brick position and shape
     */
    public ViewData getViewData() {
        return viewData;
    }
}

