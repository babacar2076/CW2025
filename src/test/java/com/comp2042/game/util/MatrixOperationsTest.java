package com.comp2042.game.util;

import com.comp2042.game.model.ClearRow;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testIntersect_NoCollision() {
        int[][] matrix = new int[10][10];
        int[][] brick = {{1, 1}, {1, 0}};
        assertFalse(MatrixOperations.intersect(matrix, brick, 0, 0));
    }

    @Test
    void testIntersect_WithCollision() {
        int[][] matrix = new int[10][10];
        matrix[1][1] = 1;
        int[][] brick = {{1, 1}, {1, 0}};
        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 1));
    }

    @Test
    void testIntersect_OutOfBoundsLeft() {
        int[][] matrix = new int[10][10];
        int[][] brick = {{1, 1}};
        assertTrue(MatrixOperations.intersect(matrix, brick, -1, 0));
    }

    @Test
    void testIntersect_OutOfBoundsTop() {
        int[][] matrix = new int[10][10];
        int[][] brick = {{1, 1}};
        assertTrue(MatrixOperations.intersect(matrix, brick, 0, -1));
    }

    @Test
    void testIntersect_OutOfBoundsRight() {
        int[][] matrix = new int[10][10];
        int[][] brick = {{1, 1}};
        assertTrue(MatrixOperations.intersect(matrix, brick, 10, 0));
    }

    @Test
    void testIntersect_EmptyBrick() {
        int[][] matrix = new int[10][10];
        int[][] brick = {{0, 0}, {0, 0}};
        assertFalse(MatrixOperations.intersect(matrix, brick, 0, 0));
    }

    @Test
    void testCopy_DeepCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        int[][] copy = MatrixOperations.copy(original);
        assertNotSame(original, copy);
        assertArrayEquals(original, copy);
        
        copy[0][0] = 99;
        assertNotEquals(original[0][0], copy[0][0]);
    }

    @Test
    void testCopy_DifferentSizedArrays() {
        int[][] original = {{1, 2}, {3, 4, 5}};
        int[][] copy = MatrixOperations.copy(original);
        assertArrayEquals(original, copy);
    }

    @Test
    void testMerge() {
        int[][] board = new int[5][5];
        int[][] brick = {{1, 1}, {1, 0}};
        int[][] result = MatrixOperations.merge(board, brick, 1, 1);
        
        assertEquals(1, result[1][1]);
        assertEquals(1, result[1][2]);
        assertEquals(1, result[2][1]);
        assertEquals(0, result[2][2]);
        assertEquals(0, result[0][0]); // Original board unchanged
    }

    @Test
    void testMerge_DoesNotModifyOriginal() {
        int[][] board = new int[5][5];
        board[0][0] = 5;
        int[][] brick = {{1, 1}};
        int[][] result = MatrixOperations.merge(board, brick, 0, 0);
        
        assertEquals(5, board[0][0]); // Original unchanged
        assertEquals(1, result[0][0]); // Copy modified
    }

    @Test
    void testCheckRemoving_NoRowsToClear() {
        int[][] matrix = new int[5][5];
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());
    }

    @Test
    void testCheckRemoving_SingleRowCleared() {
        int[][] matrix = new int[5][5];
        for (int j = 0; j < 5; j++) {
            matrix[2][j] = 1;
        }
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        assertEquals(1, result.getLinesRemoved());
        assertEquals(50, result.getScoreBonus());
        
        // Check cleared row is empty in result
        for (int j = 0; j < 5; j++) {
            assertEquals(0, result.getNewMatrix()[2][j]);
        }
    }

    @Test
    void testCheckRemoving_MultipleRowsCleared() {
        int[][] matrix = new int[5][5];
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = 1;
            }
        }
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        assertEquals(2, result.getLinesRemoved());
        assertEquals(200, result.getScoreBonus()); // 50 * 2^2
    }

    @Test
    void testCheckRemoving_AllRowsCleared() {
        int[][] matrix = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = 1;
            }
        }
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        assertEquals(3, result.getLinesRemoved());
        assertEquals(450, result.getScoreBonus()); // 50 * 3^2
    }

    @Test
    void testCheckRemoving_PartialRowNotCleared() {
        int[][] matrix = new int[5][5];
        matrix[2][0] = 1;
        matrix[2][1] = 1;
        // Row 2 is not fully filled
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());
    }

    @Test
    void testDeepCopyList() {
        List<int[][]> original = new ArrayList<>();
        original.add(new int[][]{{1, 2}});
        original.add(new int[][]{{3, 4}});
        
        List<int[][]> copy = MatrixOperations.deepCopyList(original);
        assertNotSame(original, copy);
        assertEquals(original.size(), copy.size());
        
        copy.get(0)[0][0] = 99;
        assertNotEquals(original.get(0)[0][0], copy.get(0)[0][0]);
    }

    @Test
    void testDeepCopyList_EmptyList() {
        List<int[][]> original = new ArrayList<>();
        List<int[][]> copy = MatrixOperations.deepCopyList(original);
        assertTrue(copy.isEmpty());
    }
}

