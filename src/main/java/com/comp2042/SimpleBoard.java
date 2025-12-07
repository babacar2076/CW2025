package com.comp2042;

import com.comp2042.game.model.ClearRow;
import com.comp2042.game.model.Score;
import com.comp2042.game.model.ViewData;
import com.comp2042.game.util.MatrixOperations;
import com.comp2042.game.util.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick;
    private boolean canHold = true;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        // Use matrix directly - intersect doesn't modify it, so no copy needed
        // Calculate new position without creating new Point
        int newX = (int) currentOffset.getX();
        int newY = (int) currentOffset.getY() + 1;
        boolean conflict = MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), newX, newY);
        if (conflict) {
            return false;
        } else {
            // Reuse existing Point object by updating its coordinates
            currentOffset.setLocation(newX, newY);
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        // Use matrix directly - intersect doesn't modify it, so no copy needed
        int newX = (int) currentOffset.getX() - 1;
        int newY = (int) currentOffset.getY();
        boolean conflict = MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), newX, newY);
        if (conflict) {
            return false;
        } else {
            // Reuse existing Point object by updating its coordinates
            currentOffset.setLocation(newX, newY);
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        // Use matrix directly - intersect doesn't modify it, so no copy needed
        int newX = (int) currentOffset.getX() + 1;
        int newY = (int) currentOffset.getY();
        boolean conflict = MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), newX, newY);
        if (conflict) {
            return false;
        } else {
            // Reuse existing Point object by updating its coordinates
            currentOffset.setLocation(newX, newY);
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        // Use matrix directly - intersect doesn't modify it, so no copy needed
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentGameMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0);// changed y from 10 to 0 to fix when gameover kicks in
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
    }

    public ViewData getGhostPosition() {
        int[][] shape = brickRotator.getCurrentShape();
        int ghostY = (int) currentOffset.getY();
        int currentX = (int) currentOffset.getX();
        // Use matrix directly - intersect doesn't modify it, so no copy needed
        // This eliminates copying 200 ints in a potentially long loop
        while (!MatrixOperations.intersect(currentGameMatrix, shape, currentX, ghostY + 1)) {
            ghostY++;
        }
        return new ViewData(shape, currentX, ghostY, brickGenerator.getNextBrick().getShapeMatrix().get(0));
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null;
        canHold = true;
        createNewBrick();
    }
    
    @Override
    public ViewData holdBrick() {
        Brick currentBrick = brickRotator.getBrick();
        if (heldBrick == null) {
            if (!canHold) {
                return getViewData();
            }
            heldBrick = currentBrick;
            canHold = false;
            Brick newBrick = brickGenerator.getBrick();
            brickRotator.setBrick(newBrick);
            currentOffset = new Point(4, 0);
        } else {
            // Allow swapping even if canHold is false
            // Preserve current position when swapping
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            brickRotator.setBrick(temp);
            // Keep currentOffset at its current position instead of resetting to top
        }
        return getViewData();
    }
    
    public Brick getHeldBrick() {
        return heldBrick;
    }
    
    public void setCanHold(boolean canHold) {
        this.canHold = canHold;
    }
}
