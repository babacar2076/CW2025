package com.comp2042;

import com.comp2042.game.util.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.IBrick;
import com.comp2042.logic.bricks.OBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    private BrickRotator rotator;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
    }

    @Test
    void testSetBrick_ResetsToFirstShape() {
        Brick brick = new IBrick();
        rotator.setBrick(brick);
        int[][] shape = rotator.getCurrentShape();
        // I-brick first shape is horizontal (row 1 has all 1s)
        assertEquals(1, shape[1][0]);
        assertEquals(1, shape[1][1]);
    }

    @Test
    void testGetCurrentShape_NotNull() {
        Brick brick = new IBrick();
        rotator.setBrick(brick);
        int[][] shape = rotator.getCurrentShape();
        assertNotNull(shape);
        assertTrue(shape.length > 0);
    }

    @Test
    void testGetNextShape_IBrick() {
        Brick brick = new IBrick();
        rotator.setBrick(brick);
        
        NextShapeInfo next = rotator.getNextShape();
        assertNotNull(next);
        assertEquals(1, next.getPosition());
        assertNotNull(next.getShape());
    }

    @Test
    void testGetNextShape_OBrick() {
        Brick brick = new OBrick();
        rotator.setBrick(brick);
        
        NextShapeInfo next = rotator.getNextShape();
        // O-brick has 1 rotation, so next should wrap to position 0
        assertEquals(0, next.getPosition());
    }

    @Test
    void testSetCurrentShape() {
        Brick brick = new IBrick();
        rotator.setBrick(brick);
        
        rotator.setCurrentShape(1);
        int[][] shape = rotator.getCurrentShape();
        assertNotNull(shape);
        
        // After setting to position 1, getNextShape should wrap to 0
        NextShapeInfo next = rotator.getNextShape();
        assertEquals(0, next.getPosition());
    }

    @Test
    void testRotation_WrapsAround() {
        Brick brick = new IBrick();
        rotator.setBrick(brick);
        
        // I-brick has 2 rotations (0 and 1)
        NextShapeInfo next1 = rotator.getNextShape();
        assertEquals(1, next1.getPosition());
        
        rotator.setCurrentShape(1);
        NextShapeInfo next2 = rotator.getNextShape();
        assertEquals(0, next2.getPosition()); // Wrapped around
    }

    @Test
    void testGetBrick() {
        Brick brick = new IBrick();
        rotator.setBrick(brick);
        assertEquals(brick, rotator.getBrick());
    }

    @Test
    void testSetBrick_MultipleBricks() {
        Brick brick1 = new IBrick();
        Brick brick2 = new OBrick();
        
        rotator.setBrick(brick1);
        assertEquals(brick1, rotator.getBrick());
        
        rotator.setBrick(brick2);
        assertEquals(brick2, rotator.getBrick());
    }

    @Test
    void testSetBrick_ResetsShape() {
        Brick brick = new IBrick();
        rotator.setBrick(brick);
        rotator.setCurrentShape(1);
        
        Brick newBrick = new IBrick();
        rotator.setBrick(newBrick);
        
        // Should reset to shape 0
        NextShapeInfo next = rotator.getNextShape();
        assertEquals(1, next.getPosition()); // Next after 0 is 1
    }
}

