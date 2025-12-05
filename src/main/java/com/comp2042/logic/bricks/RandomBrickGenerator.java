package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private final List<Brick> bag = new ArrayList<>();
    private Brick lastBrick = null;

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());
        fillBag();
        nextBricks.add(getBrickFromBag());
        nextBricks.add(getBrickFromBag());
    }

    private void fillBag() {
        bag.clear();
        bag.addAll(brickList);
        Collections.shuffle(bag);
    }

    private Brick getBrickFromBag() {
        if (bag.isEmpty()) {
            fillBag();
        }
        Brick selectedBrick = bag.remove(0);
        
        // Simple if statement: avoid immediate repeats
        if (lastBrick != null && selectedBrick.getClass().equals(lastBrick.getClass())) {
            if (!bag.isEmpty()) {
                bag.add(selectedBrick);
                selectedBrick = bag.remove(0);
            } else {
                // If bag only has one brick and it's a repeat, shuffle and try again
                fillBag();
                if (bag.size() > 0) {
                    selectedBrick = bag.remove(0);
                }
            }
        }
        
        return selectedBrick;
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(getBrickFromBag());
        }
        Brick brick = nextBricks.poll();
        lastBrick = brick;
        return brick;
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}
