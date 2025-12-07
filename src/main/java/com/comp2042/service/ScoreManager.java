package com.comp2042.service;

/**
 * Manages game scores including current score and high score tracking.
 * Handles score-related business logic separately from UI concerns.
 */
public class ScoreManager {
    
    private int currentScore = 0;
    private int highScore = 0;
    private final FileManager fileManager;
    
    public ScoreManager(FileManager fileManager) {
        this.fileManager = fileManager;
        loadHighScore();
    }
    
    /**
     * Loads the high score from persistence.
     */
    private void loadHighScore() {
        highScore = fileManager.loadHighScore();
    }
    
    /**
     * Gets the current score.
     * @return The current score
     */
    public int getCurrentScore() {
        return currentScore;
    }
    
    /**
     * Sets the current score.
     * @param score The new score
     * @return true if this score is a new high score, false otherwise
     */
    public boolean setCurrentScore(int score) {
        currentScore = score;
        if (score > highScore) {
            highScore = score;
            fileManager.saveHighScore(highScore);
            return true; // New high score achieved
        }
        return false; // Not a new high score
    }
    
    /**
     * Gets the high score.
     * @return The high score
     */
    public int getHighScore() {
        return highScore;
    }
    
    /**
     * Resets the current score to 0 (for new game).
     */
    public void resetCurrentScore() {
        currentScore = 0;
    }
    
    /**
     * Updates the high score display if a new high score was achieved.
     * @return true if high score was updated, false otherwise
     */
    public boolean checkAndUpdateHighScore(int newScore) {
        return setCurrentScore(newScore);
    }
}

