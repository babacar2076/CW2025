package com.comp2042;
import javafx.scene.effect.DropShadow;
import javafx.animation.KeyFrame;
import javafx.scene.layout.BorderPane;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private javafx.scene.layout.BorderPane gameBoard;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane ghostPanel;

    @FXML
    private GridPane nextBrickPanel;

    @FXML
    private javafx.scene.layout.BorderPane nextBrickContainer;
    
    @FXML
    private GridPane holdBrickPanel;
    
    @FXML
    private javafx.scene.layout.BorderPane holdBrickContainer;

    private Rectangle[][] ghostRectangles;
    private Rectangle[][] nextBrickRectangles;
    private Rectangle[][] holdBrickRectangles;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private PausePanel pausePanel;

    @FXML
    private MainMenuPanel mainMenuPanel;
    
    @FXML
    private LevelsPanel levelsPanel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label currentScoreLabel;

    @FXML
    private Label highScoreLabel;

    @FXML
    private Label highScoreTextLabel;

    private int highScore = 0;
    private static final String HIGH_SCORE_FILE = "highscore.txt";
    private static final String LEVELS_FILE = "levels.txt";
    private int currentLevel = 1;
    private boolean[] unlockedLevels = new boolean[6];
    private int[] levelScores = new int[6];
    private static final int[] LEVEL_SPEEDS = {600, 500, 400, 300, 200, 0};
    private int currentScore = 0;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        hardDrop();
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.H) {
                        holdBrick();
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.P) {
                    togglePause();
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });
        gameOverPanel.setVisible(false);
        gameOverPanel.setOnReturnToMenu(e -> { timeLine.stop(); gameOverPanel.setVisible(false); if (eventListener != null && eventListener instanceof GameController) { ((GameController) eventListener).resetGame(); } clearGameDisplay(); hideGameElements(); if (mainMenuPanel != null) mainMenuPanel.setVisible(true); isPause.setValue(false); isGameOver.setValue(false); });
        gameOverPanel.setOnReplay(e -> { timeLine.stop(); gameOverPanel.setVisible(false); clearNextBlock(); clearHoldBlock(); if (currentScoreLabel != null) currentScoreLabel.setVisible(true); if (scoreLabel != null) scoreLabel.setVisible(true); if (highScoreTextLabel != null) highScoreTextLabel.setVisible(true); if (highScoreLabel != null) highScoreLabel.setVisible(true); if (nextBrickContainer != null) nextBrickContainer.setVisible(true); if (holdBrickContainer != null) holdBrickContainer.setVisible(true); eventListener.createNewGame(); refreshHoldBrick(); gamePanel.requestFocus(); timeLine.play(); isPause.setValue(false); isGameOver.setValue(false); });
        
        pausePanel.setVisible(false);
        pausePanel.setOnResume(e -> { pausePanel.setVisible(false); timeLine.play(); isPause.setValue(false); gamePanel.requestFocus(); });
        pausePanel.setOnRestart(e -> { pausePanel.setVisible(false); if (eventListener != null && eventListener instanceof GameController) { ((GameController) eventListener).resetGame(); ((GameController) eventListener).getScore().reset(); } clearGameDisplay(); clearNextBlock(); clearHoldBlock(); showGameElements(); if (eventListener != null) eventListener.createNewGame(); refreshHoldBrick(); gamePanel.requestFocus(); timeLine.play(); isPause.setValue(false); isGameOver.setValue(false); });
        pausePanel.setOnReturnToMenu(e -> { timeLine.stop(); pausePanel.setVisible(false); if (eventListener != null && eventListener instanceof GameController) { ((GameController) eventListener).resetGame(); } clearGameDisplay(); hideGameElements(); if (mainMenuPanel != null) mainMenuPanel.setVisible(true); isPause.setValue(false); isGameOver.setValue(false); });
        
        mainMenuPanel.setVisible(true);
        mainMenuPanel.setOnNewGame(e -> { currentLevel = 1; startNewGame(); });
        mainMenuPanel.setOnLevels(e -> { mainMenuPanel.setVisible(false); levelsPanel.setVisible(true); updateLevelsPanel(); });
        
        levelsPanel.setVisible(false);
        for (int i = 1; i <= 6; i++) {
            final int level = i;
            levelsPanel.setOnLevelSelected(level, e -> { currentLevel = level; levelsPanel.setVisible(false); startNewGame(); });
        }
        levelsPanel.setOnBack(e -> { levelsPanel.setVisible(false); mainMenuPanel.setVisible(true); });
        mainMenuPanel.getControlsButton().setOnAction(e -> mainMenuPanel.toggleControls());
        
        hideGameElements();
        
        loadHighScore();
        loadLevels();
        if (!unlockedLevels[0]) {
            unlockedLevels[0] = true;
            saveLevels();
        }

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
        
        if (ghostPanel != null) {
            ghostPanel.getChildren().clear();
        }
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                ghostRectangles[i][j] = rectangle;
                ghostPanel.add(rectangle, j, i);
            }
        }
        
        if (nextBrickPanel != null) {
            nextBrickPanel.getChildren().clear();
        }
        nextBrickRectangles = new Rectangle[brick.getNextBrickData().length][brick.getNextBrickData()[0].length];
        for (int i = 0; i < brick.getNextBrickData().length; i++) {
            for (int j = 0; j < brick.getNextBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setRectangleData(brick.getNextBrickData()[i][j], rectangle);
                nextBrickRectangles[i][j] = rectangle;
                nextBrickPanel.add(rectangle, j, i);
            }
        }

        int speed;
        if (currentLevel == 6) {
            speed = calculateFinalLevelSpeed(currentScore);
        } else {
            speed = LEVEL_SPEEDS[currentLevel - 1];
        }
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(speed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
        refreshHoldBrick();
    }

    private int calculateFinalLevelSpeed(int score) {
        // Tiered speed system: speed decreases (game gets faster) as score increases
        if (score < 100) {
            // From score 0 to 100: decrease from 400ms to 300ms
            return 400 - ((score * 100) / 100);
        } else if (score < 500) {
            // From score 100 to 500: decrease from 300ms to 200ms
            return 300 - ((score - 100) * 100 / 400);
        } else if (score < 1000) {
            // From score 500 to 1000: decrease from 200ms to 100ms
            return 200 - ((score - 500) * 100 / 500);
        } else {
            // From score 1000+: stay at 100ms
            return 100;
        }
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }


    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            if (eventListener instanceof GameController && ghostPanel != null) {
                ViewData ghost = ((GameController) eventListener).getGhostPosition();
                if (ghostRectangles == null || ghostRectangles.length != ghost.getBrickData().length || (ghostRectangles.length > 0 && ghostRectangles[0].length != ghost.getBrickData()[0].length)) {
                    ghostPanel.getChildren().clear();
                    ghostRectangles = new Rectangle[ghost.getBrickData().length][ghost.getBrickData()[0].length];
                    for (int i = 0; i < ghost.getBrickData().length; i++) {
                        for (int j = 0; j < ghost.getBrickData()[i].length; j++) {
                            Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                            rectangle.setFill(Color.TRANSPARENT);
                            ghostRectangles[i][j] = rectangle;
                            ghostPanel.add(rectangle, j, i);
                        }
                    }
                }
                ghostPanel.setLayoutX(gamePanel.getLayoutX() + ghost.getxPosition() * ghostPanel.getVgap() + ghost.getxPosition() * BRICK_SIZE);
                ghostPanel.setLayoutY(-42 + gamePanel.getLayoutY() + ghost.getyPosition() * ghostPanel.getHgap() + ghost.getyPosition() * BRICK_SIZE);
                for (int i = 0; i < ghost.getBrickData().length; i++) {
                    for (int j = 0; j < ghost.getBrickData()[i].length; j++) {
                        int color = ghost.getBrickData()[i][j];
                        if (color != 0) {
                            Color c = (Color) getFillColor(color);
                            ghostRectangles[i][j].setFill(new Color(c.getRed(), c.getGreen(), c.getBlue(), 0.3));
                            ghostRectangles[i][j].setArcHeight(9);
                            ghostRectangles[i][j].setArcWidth(9);
                        } else {
                            ghostRectangles[i][j].setFill(Color.TRANSPARENT);
                        }
                    }
                }
            }
            if (nextBrickRectangles == null || nextBrickRectangles.length != brick.getNextBrickData().length || (nextBrickRectangles.length > 0 && nextBrickRectangles[0].length != brick.getNextBrickData()[0].length)) {
                if (nextBrickPanel != null) {
                    nextBrickPanel.getChildren().clear();
                }
                nextBrickRectangles = new Rectangle[brick.getNextBrickData().length][brick.getNextBrickData()[0].length];
                for (int i = 0; i < brick.getNextBrickData().length; i++) {
                    for (int j = 0; j < brick.getNextBrickData()[i].length; j++) {
                        Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                        setRectangleData(brick.getNextBrickData()[i][j], rectangle);
                        nextBrickRectangles[i][j] = rectangle;
                        nextBrickPanel.add(rectangle, j, i);
                    }
                }
            } else {
                for (int i = 0; i < brick.getNextBrickData().length; i++) {
                    for (int j = 0; j < brick.getNextBrickData()[i].length; j++) {
                        setRectangleData(brick.getNextBrickData()[i][j], nextBrickRectangles[i][j]);
                    }
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
        if (color != 0) {
            DropShadow glow = new DropShadow(8, (Color) getFillColor(color));
            glow.setSpread(0.1);
            rectangle.setEffect(glow);
        }
    }
    

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }
    
    private void hardDrop() {
        if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE && eventListener != null) {
            DownData downData = null;
            ViewData previousViewData = null;
            boolean blockPlaced = false;
            
            while (!blockPlaced) {
                downData = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
                ViewData currentViewData = downData.getViewData();
                
                if (currentViewData == null) {
                    blockPlaced = true;
                    break;
                }
                
                if (previousViewData != null) {
                    if (currentViewData.getyPosition() == previousViewData.getyPosition()) {
                        blockPlaced = true;
                        break;
                    }
                }
                
                if (downData.getClearRow() != null) {
                    blockPlaced = true;
                    if (downData.getClearRow().getLinesRemoved() > 0) {
                        NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                        groupNotification.getChildren().add(notificationPanel);
                        notificationPanel.showScore(groupNotification.getChildren());
                    }
                    break;
                }
                
                previousViewData = currentViewData;
                refreshBrick(currentViewData);
            }
            
            if (downData != null && downData.getViewData() != null) {
                refreshBrick(downData.getViewData());
            }
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null && integerProperty != null) {
            scoreLabel.textProperty().bind(integerProperty.asString());
            integerProperty.addListener((obs, oldVal, newVal) -> {
                int score = newVal.intValue();
                currentScore = score;
                if (score > highScore) {
                    highScore = score;
                    if (highScoreLabel != null) highScoreLabel.setText(String.valueOf(highScore));
                    saveHighScore();
                }
                if (score >= 200 && currentLevel < 6) {
                    int nextLevelIndex = currentLevel;
                    if (!unlockedLevels[nextLevelIndex]) {
                        unlockedLevels[nextLevelIndex] = true;
                        levelScores[currentLevel - 1] = score;
                        saveLevels();
                    }
                }
                if (currentLevel == 6 && timeLine != null) {
                    int newSpeed = calculateFinalLevelSpeed(score);
                    timeLine.stop();
                    timeLine = new Timeline(new KeyFrame(
                            Duration.millis(newSpeed),
                            ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
                    ));
                    timeLine.setCycleCount(Timeline.INDEFINITE);
                    timeLine.play();
                }
            });
        }
        if (highScoreLabel != null) highScoreLabel.setText(String.valueOf(highScore));
    }

    public void gameOver() {
        timeLine.stop();
        if (currentScoreLabel != null) currentScoreLabel.setVisible(false);
        if (scoreLabel != null) scoreLabel.setVisible(false);
        if (highScoreTextLabel != null) highScoreTextLabel.setVisible(false);
        if (highScoreLabel != null) highScoreLabel.setVisible(false);
        if (nextBrickContainer != null) nextBrickContainer.setVisible(false);
        if (holdBrickContainer != null) holdBrickContainer.setVisible(false);
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }
    

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }
    
    private void togglePause() {
        if (isGameOver.getValue() == Boolean.FALSE) {
            if (isPause.getValue() == Boolean.FALSE) {
                timeLine.stop();
                if (currentScoreLabel != null) currentScoreLabel.setVisible(false);
                if (scoreLabel != null) scoreLabel.setVisible(false);
                if (highScoreTextLabel != null) highScoreTextLabel.setVisible(false);
                if (highScoreLabel != null) highScoreLabel.setVisible(false);
                if (nextBrickContainer != null) nextBrickContainer.setVisible(false);
                if (holdBrickContainer != null) holdBrickContainer.setVisible(false);
                pausePanel.setVisible(true);
                isPause.setValue(true);
            } else {
                pausePanel.setVisible(false);
                if (currentScoreLabel != null) currentScoreLabel.setVisible(true);
                if (scoreLabel != null) scoreLabel.setVisible(true);
                if (highScoreTextLabel != null) highScoreTextLabel.setVisible(true);
                if (highScoreLabel != null) highScoreLabel.setVisible(true);
                if (nextBrickContainer != null) nextBrickContainer.setVisible(true);
                timeLine.play();
                isPause.setValue(false);
                gamePanel.requestFocus();
            }
        }
    }
    
    private void holdBrick() {
        if (eventListener != null && eventListener instanceof GameController) {
            ViewData newViewData = ((GameController) eventListener).holdBrick();
            refreshBrick(newViewData);
            refreshHoldBrick();
        }
    }
    
    public void refreshHoldBrick() {
        if (holdBrickPanel == null || eventListener == null || !(eventListener instanceof GameController)) {
            return;
        }
        
        com.comp2042.logic.bricks.Brick heldBrick = ((GameController) eventListener).getHeldBrick();
        if (heldBrick == null) {
            if (holdBrickPanel != null) {
                holdBrickPanel.getChildren().clear();
            }
            holdBrickRectangles = null;
            return;
        }
        
        if (holdBrickPanel != null) {
            holdBrickPanel.getChildren().clear();
        }
        
        int[][] holdShape = heldBrick.getShapeMatrix().get(0);
        holdBrickRectangles = new Rectangle[holdShape.length][holdShape[0].length];
        for (int i = 0; i < holdShape.length; i++) {
            for (int j = 0; j < holdShape[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setRectangleData(holdShape[i][j], rectangle);
                holdBrickRectangles[i][j] = rectangle;
                holdBrickPanel.add(rectangle, j, i);
            }
        }
    }
    
    private void startNewGame() {
        if (mainMenuPanel != null) {
            mainMenuPanel.setVisible(false);
        }
        currentScore = 0;
        showGameElements();
        if (currentScoreLabel != null) currentScoreLabel.setVisible(true);
        if (scoreLabel != null) scoreLabel.setVisible(true);
        if (highScoreTextLabel != null) highScoreTextLabel.setVisible(true);
        if (highScoreLabel != null) highScoreLabel.setVisible(true);
        if (nextBrickContainer != null) nextBrickContainer.setVisible(true);
        if (holdBrickContainer != null) holdBrickContainer.setVisible(true);
        if (eventListener != null && eventListener instanceof GameController) {
            ((GameController) eventListener).startGame();
            refreshHoldBrick();
        }
        gamePanel.requestFocus();
        if (timeLine != null) {
            timeLine.play();
        }
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }
    
    private void hideGameElements() {
        if (gameBoard != null) {
            gameBoard.setVisible(false);
        }
        if (scoreLabel != null) {
            scoreLabel.setVisible(false);
        }
        if (currentScoreLabel != null) {
            currentScoreLabel.setVisible(false);
        }
        if (highScoreLabel != null) highScoreLabel.setVisible(false);
        if (highScoreTextLabel != null) highScoreTextLabel.setVisible(false);
        if (nextBrickContainer != null) nextBrickContainer.setVisible(false);
        if (holdBrickContainer != null) holdBrickContainer.setVisible(false);
    }
    
    private void showGameElements() {
        if (gameBoard != null) gameBoard.setVisible(true);
        if (scoreLabel != null) scoreLabel.setVisible(true);
        if (currentScoreLabel != null) currentScoreLabel.setVisible(true);
        if (highScoreLabel != null) highScoreLabel.setVisible(true);
        if (highScoreTextLabel != null) highScoreTextLabel.setVisible(true);
        if (nextBrickContainer != null) nextBrickContainer.setVisible(true);
        if (holdBrickContainer != null) holdBrickContainer.setVisible(true);
    }
    
    private void clearGameDisplay() {
        if (displayMatrix != null) {
            for (int i = 2; i < displayMatrix.length; i++) {
                for (int j = 0; j < displayMatrix[i].length; j++) {
                    if (displayMatrix[i][j] != null) {
                        setRectangleData(0, displayMatrix[i][j]);
                    }
                }
            }
        }
        if (rectangles != null) {
            for (int i = 0; i < rectangles.length; i++) {
                for (int j = 0; j < rectangles[i].length; j++) {
                    if (rectangles[i][j] != null) {
                        setRectangleData(0, rectangles[i][j]);
                    }
                }
            }
        }
        if (nextBrickRectangles != null) {
            for (int i = 0; i < nextBrickRectangles.length; i++) {
                for (int j = 0; j < nextBrickRectangles[i].length; j++) {
                    if (nextBrickRectangles[i][j] != null) {
                        setRectangleData(0, nextBrickRectangles[i][j]);
                    }
                }
            }
        }
    }
    
    private void clearNextBlock() {
        if (nextBrickPanel != null) {
            nextBrickPanel.getChildren().clear();
        }
        nextBrickRectangles = null;
    }
    
    private void clearHoldBlock() {
        if (holdBrickPanel != null) {
            holdBrickPanel.getChildren().clear();
        }
        holdBrickRectangles = null;
    }
    
    private void loadHighScore() {
        try {
            File file = new File(HIGH_SCORE_FILE);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextInt()) {
                    highScore = scanner.nextInt();
                    if (highScoreLabel != null) {
                        highScoreLabel.setText(String.valueOf(highScore));
                    }
                }
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, use default highScore = 0
        }
    }
    
    private void saveHighScore() {
        try {
            FileWriter writer = new FileWriter(HIGH_SCORE_FILE);
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (IOException e) {
            // Failed to save, ignore
        }
    }
    
    private void loadLevels() {
        try {
            File file = new File(LEVELS_FILE);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                for (int i = 0; i < 6 && scanner.hasNextBoolean(); i++) {
                    unlockedLevels[i] = scanner.nextBoolean();
                }
                for (int i = 0; i < 6 && scanner.hasNextInt(); i++) {
                    levelScores[i] = scanner.nextInt();
                }
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, use defaults
        }
    }
    
    private void saveLevels() {
        try {
            FileWriter writer = new FileWriter(LEVELS_FILE);
            for (int i = 0; i < 6; i++) {
                writer.write(String.valueOf(unlockedLevels[i]) + " ");
            }
            writer.write("\n");
            for (int i = 0; i < 6; i++) {
                writer.write(String.valueOf(levelScores[i]) + " ");
            }
            writer.close();
        } catch (IOException e) {
            // Failed to save, ignore
        }
    }
    
    private void updateLevelsPanel() {
        for (int i = 1; i <= 6; i++) {
            levelsPanel.setLevelEnabled(i, unlockedLevels[i - 1]);
        }
    }
}