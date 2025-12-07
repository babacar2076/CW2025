package com.comp2042;

import com.comp2042.game.model.ClearRow;
import com.comp2042.game.model.Score;
import com.comp2042.game.model.ViewData;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();
    
    ViewData holdBrick();
}
