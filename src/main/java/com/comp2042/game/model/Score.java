package com.comp2042.game.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the game score with JavaFX property binding support.
 * Provides methods to add points and reset the score, with observable properties for UI updates.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Gets the score as a JavaFX property for UI binding.
     * @return IntegerProperty that can be bound to UI components
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds points to the current score.
     * @param i The number of points to add
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score.setValue(0);
    }
}

