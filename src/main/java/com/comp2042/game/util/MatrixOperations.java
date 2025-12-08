package com.comp2042.game.util;

import com.comp2042.game.model.ClearRow;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing static methods for matrix operations used in the game.
 * Handles intersection detection, matrix copying, merging, and row clearing logic.
 */
public class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

    /**
     * Checks if a brick intersects with the board at the specified position.
     * @param matrix The game board matrix
     * @param brick The brick shape matrix to check
     * @param x The X coordinate to check
     * @param y The Y coordinate to check
     * @return true if there is an intersection or out-of-bounds, false otherwise
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        boolean returnValue = true;
        if (targetX >= 0 && targetY < matrix.length && targetX < matrix[targetY].length) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Creates a deep copy of a 2D integer array.
     * @param original The original matrix to copy
     * @return A new 2D array with copied values
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges a brick into the board matrix at the specified position.
     * @param filledFields The current board matrix
     * @param brick The brick shape matrix to merge
     * @param x The X coordinate where the brick should be merged
     * @param y The Y coordinate where the brick should be merged
     * @return A new board matrix with the brick merged into it
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }

    /**
     * Checks the board for completed rows and removes them.
     * Calculates score bonus based on the number of rows cleared.
     * @param matrix The current board matrix
     * @return ClearRow object containing the number of cleared rows, updated matrix, and score bonus
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] tmp = new int[rows][cols];
        
        // First pass: mark which rows need to be cleared using boolean array (O(1) lookup)
        boolean[] shouldClear = new boolean[rows];
        int clearedCount = 0;
        for (int i = 0; i < rows; i++) {
            boolean rowToClear = true;
            // Early exit optimization: stop checking once we find an empty cell
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                    break; // Early exit - no need to check rest of row
                }
            }
            if (rowToClear) {
                shouldClear[i] = true;
                clearedCount++;
            }
        }
        
        // Second pass: build result matrix by copying non-cleared rows from bottom up
        // This avoids creating intermediate arrays and uses efficient System.arraycopy
        int targetRow = rows - 1; // Start from bottom
        for (int i = rows - 1; i >= 0; i--) {
            if (!shouldClear[i]) {
                // Direct array copy - faster than creating intermediate array
                System.arraycopy(matrix[i], 0, tmp[targetRow], 0, cols);
                targetRow--;
            }
        }
        
        // Fill cleared rows at the top with zeros (already initialized, but explicit for clarity)
        // Arrays are already initialized to 0, but this ensures correctness
        
        int scoreBonus = 50 * clearedCount * clearedCount;
        return new ClearRow(clearedCount, tmp, scoreBonus);
    }

    /**
     * Creates a deep copy of a list of 2D integer arrays.
     * @param list The list of matrices to copy
     * @return A new list containing deep copies of all matrices
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        // Use traditional loop instead of stream for small collections (bricks have 4 rotations)
        // This avoids stream overhead and is faster for small lists
        List<int[][]> result = new ArrayList<>(list.size());
        for (int[][] matrix : list) {
            result.add(copy(matrix));
        }
        return result;
    }

}

