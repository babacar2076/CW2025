package com.comp2042.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Manages file I/O operations for game data.
 * Handles saving and loading high scores and level data.
 */
public class FileManager {
    
    private static final String HIGH_SCORE_FILE = "highscore.txt";
    private static final String LEVELS_FILE = "levels.txt";
    private static final int NUM_LEVELS = 6;
    
    /**
     * Loads the high score from the persistence file.
     * @return The high score, or 0 if the file doesn't exist or is invalid
     */
    public int loadHighScore() {
        try {
            File file = new File(HIGH_SCORE_FILE);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    if (scanner.hasNextInt()) {
                        return scanner.nextInt();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, use default highScore = 0
        }
        return 0;
    }
    
    /**
     * Saves the high score to the persistence file.
     * @param highScore The high score to save
     * @return true if the save was successful, false otherwise
     */
    public boolean saveHighScore(int highScore) {
        try (FileWriter writer = new FileWriter(HIGH_SCORE_FILE)) {
            writer.write(String.valueOf(highScore));
            return true;
        } catch (IOException e) {
            // Failed to save, ignore
            return false;
        }
    }
    
    /**
     * Loads level data (unlocked levels and scores) from the persistence file.
     * @return A LevelData object containing the loaded data, or default values if file doesn't exist
     */
    public LevelData loadLevels() {
        boolean[] unlockedLevels = new boolean[NUM_LEVELS];
        int[] levelScores = new int[NUM_LEVELS];
        
        try {
            File file = new File(LEVELS_FILE);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    for (int i = 0; i < NUM_LEVELS && scanner.hasNextBoolean(); i++) {
                        unlockedLevels[i] = scanner.nextBoolean();
                    }
                    for (int i = 0; i < NUM_LEVELS && scanner.hasNextInt(); i++) {
                        levelScores[i] = scanner.nextInt();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, use defaults
        }
        
        return new LevelData(unlockedLevels, levelScores);
    }
    
    /**
     * Saves level data (unlocked levels and scores) to the persistence file.
     * @param unlockedLevels Array of booleans indicating which levels are unlocked
     * @param levelScores Array of scores for each level
     * @return true if the save was successful, false otherwise
     */
    public boolean saveLevels(boolean[] unlockedLevels, int[] levelScores) {
        try (FileWriter writer = new FileWriter(LEVELS_FILE)) {
            for (int i = 0; i < NUM_LEVELS; i++) {
                writer.write(String.valueOf(unlockedLevels[i]) + " ");
            }
            writer.write("\n");
            for (int i = 0; i < NUM_LEVELS; i++) {
                writer.write(String.valueOf(levelScores[i]) + " ");
            }
            return true;
        } catch (IOException e) {
            // Failed to save, ignore
            return false;
        }
    }
    
    /**
     * Data class to hold level information.
     */
    public static class LevelData {
        private final boolean[] unlockedLevels;
        private final int[] levelScores;
        
        public LevelData(boolean[] unlockedLevels, int[] levelScores) {
            this.unlockedLevels = unlockedLevels;
            this.levelScores = levelScores;
        }
        
        public boolean[] getUnlockedLevels() {
            return unlockedLevels;
        }
        
        public int[] getLevelScores() {
            return levelScores;
        }
    }
}

