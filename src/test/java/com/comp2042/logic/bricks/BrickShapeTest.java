package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BrickShapeTest {

    @Test
    void testIBrick_ShapeMatrix() {
        IBrick brick = new IBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertEquals(2, shapes.size()); // I-brick has 2 rotations
        
        // Check first shape (horizontal)
        int[][] shape1 = shapes.get(0);
        assertEquals(1, shape1[1][0]);
        assertEquals(1, shape1[1][1]);
        assertEquals(1, shape1[1][2]);
        assertEquals(1, shape1[1][3]);
        
        // Check second shape (vertical)
        int[][] shape2 = shapes.get(1);
        assertEquals(1, shape2[0][1]);
        assertEquals(1, shape2[1][1]);
        assertEquals(1, shape2[2][1]);
        assertEquals(1, shape2[3][1]);
    }

    @Test
    void testOBrick_ShapeMatrix() {
        OBrick brick = new OBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertEquals(1, shapes.size()); // O-brick has 1 rotation (square)
        
        int[][] shape = shapes.get(0);
        assertEquals(4, shape[1][1]);
        assertEquals(4, shape[1][2]);
        assertEquals(4, shape[2][1]);
        assertEquals(4, shape[2][2]);
    }

    @Test
    void testJBrick_ShapeMatrix() {
        JBrick brick = new JBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertEquals(4, shapes.size()); // J-brick has 4 rotations
        
        for (int[][] shape : shapes) {
            assertNotNull(shape);
            assertTrue(shape.length > 0);
        }
    }

    @Test
    void testLBrick_ShapeMatrix() {
        LBrick brick = new LBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertEquals(4, shapes.size()); // L-brick has 4 rotations
    }

    @Test
    void testSBrick_ShapeMatrix() {
        SBrick brick = new SBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertEquals(2, shapes.size()); // S-brick has 2 rotations
    }

    @Test
    void testTBrick_ShapeMatrix() {
        TBrick brick = new TBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertEquals(4, shapes.size()); // T-brick has 4 rotations
    }

    @Test
    void testZBrick_ShapeMatrix() {
        ZBrick brick = new ZBrick();
        List<int[][]> shapes = brick.getShapeMatrix();
        
        assertNotNull(shapes);
        assertEquals(2, shapes.size()); // Z-brick has 2 rotations
    }

    @Test
    void testBrickShapes_AreDeepCopies() {
        IBrick brick = new IBrick();
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();
        
        // Should be different instances
        assertNotSame(shapes1, shapes2);
        assertNotSame(shapes1.get(0), shapes2.get(0));
        
        // Modify one
        shapes1.get(0)[1][0] = 99;
        // Other should be unchanged
        assertNotEquals(99, shapes2.get(0)[1][0]);
    }

    @Test
    void testBrickColorValues() {
        IBrick iBrick = new IBrick();
        JBrick jBrick = new JBrick();
        LBrick lBrick = new LBrick();
        OBrick oBrick = new OBrick();
        SBrick sBrick = new SBrick();
        TBrick tBrick = new TBrick();
        ZBrick zBrick = new ZBrick();
        
        // Check that each brick has a distinct color value in its shapes
        assertTrue(hasColorValue(iBrick, 1));
        assertTrue(hasColorValue(jBrick, 2));
        assertTrue(hasColorValue(lBrick, 3));
        assertTrue(hasColorValue(oBrick, 4));
        assertTrue(hasColorValue(sBrick, 5));
        assertTrue(hasColorValue(tBrick, 6));
        assertTrue(hasColorValue(zBrick, 7));
    }
    
    private boolean hasColorValue(Brick brick, int colorValue) {
        List<int[][]> shapes = brick.getShapeMatrix();
        for (int[][] shape : shapes) {
            for (int[] row : shape) {
                for (int cell : row) {
                    if (cell == colorValue) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

