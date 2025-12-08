package com.comp2042.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LevelManagerTest {

    @Mock
    private FileManager mockFileManager;
    
    private LevelManager levelManager;

    @BeforeEach
    void setUp() {
        FileManager.LevelData levelData = new FileManager.LevelData(
            new boolean[]{true, false, false, false, false, false},
            new int[]{0, 0, 0, 0, 0, 0}
        );
        when(mockFileManager.loadLevels()).thenReturn(levelData);
        levelManager = new LevelManager(mockFileManager);
    }

    @Test
    void testGetCurrentLevel_DefaultIsOne() {
        assertEquals(1, levelManager.getCurrentLevel());
    }

    @Test
    void testSetCurrentLevel_ValidLevel() {
        levelManager.setCurrentLevel(3);
        assertEquals(3, levelManager.getCurrentLevel());
        
        levelManager.setCurrentLevel(6);
        assertEquals(6, levelManager.getCurrentLevel());
    }

    @Test
    void testSetCurrentLevel_BoundaryValues() {
        levelManager.setCurrentLevel(1);
        assertEquals(1, levelManager.getCurrentLevel());
        
        levelManager.setCurrentLevel(6);
        assertEquals(6, levelManager.getCurrentLevel());
    }

    @Test
    void testResetToFirstLevel() {
        levelManager.setCurrentLevel(5);
        levelManager.resetToFirstLevel();
        assertEquals(1, levelManager.getCurrentLevel());
    }

    @Test
    void testCalculateSpeed_Level1() {
        assertEquals(600, levelManager.calculateSpeed(0));
    }

    @Test
    void testCalculateSpeed_Level2() {
        levelManager.setCurrentLevel(2);
        assertEquals(500, levelManager.calculateSpeed(0));
    }

    @Test
    void testCalculateSpeed_Level3() {
        levelManager.setCurrentLevel(3);
        assertEquals(400, levelManager.calculateSpeed(0));
    }

    @Test
    void testCalculateSpeed_Level4() {
        levelManager.setCurrentLevel(4);
        assertEquals(300, levelManager.calculateSpeed(0));
    }

    @Test
    void testCalculateSpeed_Level5() {
        levelManager.setCurrentLevel(5);
        assertEquals(200, levelManager.calculateSpeed(0));
    }

    @Test
    void testCalculateSpeed_FinalLevel_LowScore() {
        levelManager.setCurrentLevel(6);
        int speed = levelManager.calculateSpeed(50);
        // Should be between 350-400ms for score 50
        assertTrue(speed >= 350 && speed <= 400);
    }

    @Test
    void testCalculateSpeed_FinalLevel_MediumScore() {
        levelManager.setCurrentLevel(6);
        int speed = levelManager.calculateSpeed(300);
        // Should be between 200-300ms for score 300
        assertTrue(speed >= 200 && speed <= 300);
    }

    @Test
    void testCalculateSpeed_FinalLevel_HighScore() {
        levelManager.setCurrentLevel(6);
        int speed = levelManager.calculateSpeed(500);
        // Should be between 200-250ms for score 500
        assertTrue(speed >= 150 && speed <= 250);
    }

    @Test
    void testCalculateSpeed_FinalLevel_VeryHighScore() {
        levelManager.setCurrentLevel(6);
        int speed = levelManager.calculateSpeed(1500);
        // Should be at minimum 100ms for score 1000+
        assertEquals(100, speed);
    }

    @Test
    void testIsFinalLevel() {
        assertFalse(levelManager.isFinalLevel());
        
        levelManager.setCurrentLevel(6);
        assertTrue(levelManager.isFinalLevel());
    }

    @Test
    void testIsLevelUnlocked_Level1() {
        assertTrue(levelManager.isLevelUnlocked(1));
    }

    @Test
    void testIsLevelUnlocked_Level2() {
        assertFalse(levelManager.isLevelUnlocked(2));
    }

    @Test
    void testIsLevelUnlocked_InvalidLevel() {
        assertFalse(levelManager.isLevelUnlocked(0));
        assertFalse(levelManager.isLevelUnlocked(7));
        assertFalse(levelManager.isLevelUnlocked(-1));
    }

    @Test
    void testCheckAndUnlockLevel_BelowThreshold() {
        boolean unlocked = levelManager.checkAndUnlockLevel(100);
        assertFalse(unlocked);
        assertFalse(levelManager.isLevelUnlocked(2));
    }

    @Test
    void testCheckAndUnlockLevel_AtThreshold() {
        boolean unlocked = levelManager.checkAndUnlockLevel(200);
        assertTrue(unlocked);
        assertTrue(levelManager.isLevelUnlocked(2));
        verify(mockFileManager, atLeastOnce()).saveLevels(any(), any());
    }

    @Test
    void testCheckAndUnlockLevel_AboveThreshold() {
        boolean unlocked = levelManager.checkAndUnlockLevel(500);
        assertTrue(unlocked);
        verify(mockFileManager, atLeastOnce()).saveLevels(any(), any());
    }

    @Test
    void testGetUnlockedLevels_ReturnsCopy() {
        boolean[] unlocked = levelManager.getUnlockedLevels();
        boolean[] unlocked2 = levelManager.getUnlockedLevels();
        
        assertNotSame(unlocked, unlocked2);
        assertArrayEquals(new boolean[]{true, false, false, false, false, false}, unlocked);
    }

    @Test
    void testGetLevelScores_ReturnsCopy() {
        int[] scores = levelManager.getLevelScores();
        int[] scores2 = levelManager.getLevelScores();
        
        assertNotSame(scores, scores2);
    }
}

