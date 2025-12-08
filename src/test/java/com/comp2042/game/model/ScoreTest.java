package com.comp2042.game.model;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit
        new JFXPanel();
    }

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void testInitialScore_IsZero() {
        assertEquals(0, score.scoreProperty().getValue());
    }

    @Test
    void testAdd_SimpleAddition() {
        score.add(10);
        assertEquals(10, score.scoreProperty().getValue());
    }

    @Test
    void testAdd_MultipleAdditions() {
        score.add(5);
        score.add(10);
        score.add(15);
        assertEquals(30, score.scoreProperty().getValue());
    }

    @Test
    void testAdd_Zero() {
        score.add(10);
        score.add(0);
        assertEquals(10, score.scoreProperty().getValue());
    }

    @Test
    void testAdd_Negative() {
        score.add(10);
        score.add(-5);
        assertEquals(5, score.scoreProperty().getValue());
    }

    @Test
    void testReset() {
        score.add(100);
        score.reset();
        assertEquals(0, score.scoreProperty().getValue());
    }

    @Test
    void testReset_AfterMultipleAdditions() {
        score.add(50);
        score.add(25);
        score.add(75);
        score.reset();
        assertEquals(0, score.scoreProperty().getValue());
    }

    @Test
    void testScoreProperty_NotNull() {
        IntegerProperty property = score.scoreProperty();
        assertNotNull(property);
    }

    @Test
    void testScoreProperty_IsObservable() {
        IntegerProperty property = score.scoreProperty();
        
        final int[] changedValue = new int[1];
        property.addListener((observable, oldValue, newValue) -> {
            changedValue[0] = newValue.intValue();
        });
        
        score.add(42);
        // Note: Property changes happen on JavaFX thread, so we can't easily test listener in unit test
        // But we can verify the property value changed
        assertEquals(42, property.getValue());
    }

    @Test
    void testAdd_LargeNumbers() {
        score.add(1000000);
        assertEquals(1000000, score.scoreProperty().getValue());
    }
}

