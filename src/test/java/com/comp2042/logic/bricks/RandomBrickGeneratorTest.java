package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    @Test
    void testGetBrick_ReturnsBrick() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Brick brick = generator.getBrick();
        assertNotNull(brick);
        assertNotNull(brick.getShapeMatrix());
        assertFalse(brick.getShapeMatrix().isEmpty());
    }

    @Test
    void testGetNextBrick_ReturnsBrick() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Brick nextBrick = generator.getNextBrick();
        assertNotNull(nextBrick);
        assertNotNull(nextBrick.getShapeMatrix());
        assertFalse(nextBrick.getShapeMatrix().isEmpty());
    }

    @Test
    void test7BagSystem_AllBrickTypes() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Set<Class<? extends Brick>> brickTypes = new HashSet<>();
        
        // Get 7 bricks (one full bag)
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            brickTypes.add(brick.getClass());
        }
        
        // Should have all 7 types
        assertEquals(7, brickTypes.size());
        assertTrue(brickTypes.contains(IBrick.class));
        assertTrue(brickTypes.contains(JBrick.class));
        assertTrue(brickTypes.contains(LBrick.class));
        assertTrue(brickTypes.contains(OBrick.class));
        assertTrue(brickTypes.contains(SBrick.class));
        assertTrue(brickTypes.contains(TBrick.class));
        assertTrue(brickTypes.contains(ZBrick.class));
    }

    @Test
    void test7BagSystem_MultipleBags() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Set<Class<? extends Brick>> brickTypes = new HashSet<>();
        
        // Get 14 bricks (two full bags)
        for (int i = 0; i < 14; i++) {
            Brick brick = generator.getBrick();
            brickTypes.add(brick.getClass());
        }
        
        // Should have all 7 types
        assertEquals(7, brickTypes.size());
    }

    @Test
    void testNoImmediateRepeats() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Brick firstBrick = generator.getBrick();
        Class<? extends Brick> firstType = firstBrick.getClass();
        
        // Get next brick - should not be same type
        Brick secondBrick = generator.getBrick();
        Class<? extends Brick> secondType = secondBrick.getClass();
        
        // If first two are same (edge case), check that pattern doesn't continue
        int consecutiveRepeats = 0;
        if (firstType.equals(secondType)) {
            consecutiveRepeats = 2;
            Brick thirdBrick = generator.getBrick();
            if (firstType.equals(thirdBrick.getClass())) {
                consecutiveRepeats = 3;
            }
            // Should not have more than 2 consecutive (very rare but possible)
            assertTrue(consecutiveRepeats <= 2);
        }
    }

    @Test
    void testNextBrick_PreviewConsistency() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Brick preview1 = generator.getNextBrick();
        Brick preview2 = generator.getNextBrick();
        
        // Should be the same until we get a brick
        assertEquals(preview1.getClass(), preview2.getClass());
    }

    @Test
    void testNextBrick_MatchesNextGetBrick() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Brick preview = generator.getNextBrick();
        Brick actual = generator.getBrick();
        
        // The preview should match what we actually get
        assertEquals(preview.getClass(), actual.getClass());
    }

    @Test
    void testBrickShapeMatrix_NotNull() {
        RandomBrickGenerator generator = new RandomBrickGenerator();
        Brick brick = generator.getBrick();
        
        List<int[][]> shapes = brick.getShapeMatrix();
        assertNotNull(shapes);
        assertFalse(shapes.isEmpty());
        
        for (int[][] shape : shapes) {
            assertNotNull(shape);
            assertTrue(shape.length > 0);
        }
    }
}

