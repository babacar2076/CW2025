package com.comp2042.service;

/**
 * Manages game levels, including level selection, unlocking, and speed calculations.
 * Handles all level-related logic separately from UI concerns.
 */
public class LevelManager {
    
    private static final int[] LEVEL_SPEEDS = {600, 500, 400, 300, 200, 0};
    private static final int NUM_LEVELS = 6;
    private static final int FINAL_LEVEL = 6;
    private static final int UNLOCK_SCORE_THRESHOLD = 200;
    
    private int currentLevel = 1;
    private boolean[] unlockedLevels = new boolean[NUM_LEVELS];
    private int[] levelScores = new int[NUM_LEVELS];
    private final FileManager fileManager;
    
    public LevelManager(FileManager fileManager) {
        this.fileManager = fileManager;
        initializeLevels();
    }
    
    /**
     * Initializes level data from persistence and ensures Level 1 is unlocked.
     */
    private void initializeLevels() {
        FileManager.LevelData levelData = fileManager.loadLevels();
        unlockedLevels = levelData.getUnlockedLevels();
        levelScores = levelData.getLevelScores();
        
        // Ensure Level 1 is always unlocked
        if (!unlockedLevels[0]) {
            unlockedLevels[0] = true;
            fileManager.saveLevels(unlockedLevels, levelScores);
        }
    }
    
    /**
     * Gets the current level.
     * @return The current level (1-6)
     */
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    /**
     * Sets the current level.
     * @param level The level to set (1-6)
     */
    public void setCurrentLevel(int level) {
        if (level >= 1 && level <= NUM_LEVELS) {
            this.currentLevel = level;
        }
    }
    
    /**
     * Resets to Level 1 (for new game).
     */
    public void resetToFirstLevel() {
        currentLevel = 1;
    }
    
    /**
     * Calculates the game speed for the current level.
     * For Final level (6), speed is dynamically calculated based on score.
     * @param currentScore The current game score
     * @return The speed in milliseconds
     */
    public int calculateSpeed(int currentScore) {
        if (currentLevel == FINAL_LEVEL) {
            return calculateFinalLevelSpeed(currentScore);
        } else {
            return LEVEL_SPEEDS[currentLevel - 1];
        }
    }
    
    /**
     * Calculates speed for the Final level based on score.
     * Speed decreases (game gets faster) as score increases.
     * @param score The current score
     * @return The speed in milliseconds
     */
    private int calculateFinalLevelSpeed(int score) {
        if (score < 100) {
            // From score 0 to 100: decrease from 400ms to 300ms
            return 400 - ((score * 100) / 100);
        } else if (score < 500) {
            // From score 100 to 500: decrease from 300ms to 200ms
            return 300 - ((score - 100) * 100 / 400);
        } else if (score < 1000) {
            // From score 500 to 1000: decrease from 200ms to 100ms
            return 200 - ((score - 500) * 100 / 500);
        } else {
            // From score 1000+: stay at 100ms
            return 100;
        }
    }
    
    /**
     * Checks if a level should be unlocked based on score and unlocks it.
     * @param score The current score
     * @return true if a new level was unlocked, false otherwise
     */
    public boolean checkAndUnlockLevel(int score) {
        if (score >= UNLOCK_SCORE_THRESHOLD && currentLevel < FINAL_LEVEL) {
            int nextLevelIndex = currentLevel; // Next level to unlock
            if (!unlockedLevels[nextLevelIndex]) {
                unlockedLevels[nextLevelIndex] = true;
                levelScores[currentLevel - 1] = score;
                fileManager.saveLevels(unlockedLevels, levelScores);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the current level is the Final level and needs dynamic speed updates.
     * @return true if current level is Final level (6)
     */
    public boolean isFinalLevel() {
        return currentLevel == FINAL_LEVEL;
    }
    
    /**
     * Gets the unlocked status of all levels.
     * @return Array of booleans indicating which levels are unlocked
     */
    public boolean[] getUnlockedLevels() {
        return unlockedLevels.clone(); // Return copy to prevent external modification
    }
    
    /**
     * Gets whether a specific level is unlocked.
     * @param level The level to check (1-6)
     * @return true if the level is unlocked
     */
    public boolean isLevelUnlocked(int level) {
        if (level >= 1 && level <= NUM_LEVELS) {
            return unlockedLevels[level - 1];
        }
        return false;
    }
    
    /**
     * Gets the level scores.
     * @return Array of scores for each level
     */
    public int[] getLevelScores() {
        return levelScores.clone(); // Return copy to prevent external modification
    }
}

