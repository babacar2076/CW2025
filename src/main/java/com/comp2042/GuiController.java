package com.comp2042;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import com.comp2042.game.model.ClearRow;
import com.comp2042.game.model.DownData;
import com.comp2042.game.model.ViewData;
import com.comp2042.service.FileManager;
import com.comp2042.service.LevelManager;
import com.comp2042.service.ScoreManager;
import com.comp2042.ui.panel.GameOverPanel;
import com.comp2042.ui.panel.LevelsPanel;
import com.comp2042.ui.panel.MainMenuPanel;
import com.comp2042.ui.panel.NotificationPanel;
import com.comp2042.ui.panel.PausePanel;
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

import java.net.URL;
import java.util.ResourceBundle;

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

    private final FileManager fileManager = new FileManager();
    private final LevelManager levelManager = new LevelManager(fileManager);
    private final ScoreManager scoreManager = new ScoreManager(fileManager);

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
        mainMenuPanel.setOnNewGame(e -> { levelManager.resetToFirstLevel(); startNewGame(); });
        mainMenuPanel.setOnLevels(e -> { mainMenuPanel.setVisible(false); levelsPanel.setVisible(true); updateLevelsPanel(); });
        
        levelsPanel.setVisible(false);
        for (int i = 1; i <= 6; i++) {
            final int level = i;
            levelsPanel.setOnLevelSelected(level, e -> { levelManager.setCurrentLevel(level); levelsPanel.setVisible(false); startNewGame(); });
        }
        levelsPanel.setOnBack(e -> { levelsPanel.setVisible(false); mainMenuPanel.setVisible(true); });
        mainMenuPanel.getControlsButton().setOnAction(e -> mainMenuPanel.toggleControls());
        
        hideGameElements();
        
        // Initialize high score display
        if (highScoreLabel != null) {
            highScoreLabel.setText(String.valueOf(scoreManager.getHighScore()));
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

        int speed = levelManager.calculateSpeed(scoreManager.getCurrentScore());
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(speed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
        refreshHoldBrick();
    }


    // Static array for color lookup - faster than switch statement
    // Index directly corresponds to brick color value
    private static final Paint[] COLOR_LOOKUP = {
        Color.TRANSPARENT,  // 0
        Color.AQUA,         // 1
        Color.BLUEVIOLET,   // 2
        Color.DARKGREEN,    // 3
        Color.YELLOW,       // 4
        Color.RED,          // 5
        Color.BEIGE,        // 6
        Color.BURLYWOOD     // 7
    };
    
    private Paint getFillColor(int i) {
        // Array lookup is faster than switch for small, dense value sets
        // Bounds check to prevent ArrayIndexOutOfBoundsException
        if (i >= 0 && i < COLOR_LOOKUP.length) {
            return COLOR_LOOKUP[i];
        }
        return Color.WHITE; // Default for out-of-bounds values
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
                boolean isNewHighScore = scoreManager.setCurrentScore(score);
                if (isNewHighScore && highScoreLabel != null) {
                    highScoreLabel.setText(String.valueOf(scoreManager.getHighScore()));
                }
                levelManager.checkAndUnlockLevel(score);
                if (levelManager.isFinalLevel() && timeLine != null) {
                    int newSpeed = levelManager.calculateSpeed(score);
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
        if (highScoreLabel != null) highScoreLabel.setText(String.valueOf(scoreManager.getHighScore()));
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
        scoreManager.resetCurrentScore();
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
    
    
    private void updateLevelsPanel() {
        for (int i = 1; i <= 6; i++) {
            levelsPanel.setLevelEnabled(i, levelManager.isLevelUnlocked(i));
        }
    }
}