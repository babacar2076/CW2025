package com.comp2042.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoreManagerTest {

    @Mock
    private FileManager mockFileManager;
    
    private ScoreManager scoreManager;

    @BeforeEach
    void setUp() {
        when(mockFileManager.loadHighScore()).thenReturn(100);
        scoreManager = new ScoreManager(mockFileManager);
    }

    @Test
    void testGetCurrentScore_InitialValue() {
        assertEquals(0, scoreManager.getCurrentScore());
    }

    @Test
    void testGetHighScore_LoadedValue() {
        assertEquals(100, scoreManager.getHighScore());
    }

    @Test
    void testSetCurrentScore_BelowHighScore() {
        boolean isNewHigh = scoreManager.setCurrentScore(50);
        assertFalse(isNewHigh);
        assertEquals(50, scoreManager.getCurrentScore());
        assertEquals(100, scoreManager.getHighScore());
        verify(mockFileManager, never()).saveHighScore(anyInt());
    }

    @Test
    void testSetCurrentScore_EqualsHighScore() {
        boolean isNewHigh = scoreManager.setCurrentScore(100);
        assertFalse(isNewHigh);
        assertEquals(100, scoreManager.getCurrentScore());
        assertEquals(100, scoreManager.getHighScore());
    }

    @Test
    void testSetCurrentScore_NewHighScore() {
        boolean isNewHigh = scoreManager.setCurrentScore(150);
        assertTrue(isNewHigh);
        assertEquals(150, scoreManager.getCurrentScore());
        assertEquals(150, scoreManager.getHighScore());
        verify(mockFileManager).saveHighScore(150);
    }

    @Test
    void testSetCurrentScore_MultipleUpdates() {
        scoreManager.setCurrentScore(50);
        scoreManager.setCurrentScore(75);
        scoreManager.setCurrentScore(120);
        
        assertEquals(120, scoreManager.getCurrentScore());
        assertEquals(120, scoreManager.getHighScore());
        verify(mockFileManager, times(1)).saveHighScore(120);
    }

    @Test
    void testResetCurrentScore() {
        scoreManager.setCurrentScore(200);
        scoreManager.resetCurrentScore();
        assertEquals(0, scoreManager.getCurrentScore());
        // High score should remain unchanged
        assertEquals(200, scoreManager.getHighScore());
    }

    @Test
    void testCheckAndUpdateHighScore() {
        boolean updated = scoreManager.checkAndUpdateHighScore(150);
        assertTrue(updated);
        assertEquals(150, scoreManager.getCurrentScore());
        assertEquals(150, scoreManager.getHighScore());
    }

    @Test
    void testCheckAndUpdateHighScore_NoUpdate() {
        boolean updated = scoreManager.checkAndUpdateHighScore(50);
        assertFalse(updated);
        assertEquals(50, scoreManager.getCurrentScore());
        assertEquals(100, scoreManager.getHighScore());
    }

    @Test
    void testInitialHighScore_Zero() {
        when(mockFileManager.loadHighScore()).thenReturn(0);
        ScoreManager newScoreManager = new ScoreManager(mockFileManager);
        assertEquals(0, newScoreManager.getHighScore());
    }

    @Test
    void testScoreIncrement() {
        scoreManager.setCurrentScore(10);
        assertEquals(10, scoreManager.getCurrentScore());
        
        scoreManager.setCurrentScore(25);
        assertEquals(25, scoreManager.getCurrentScore());
    }
}

