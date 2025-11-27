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
import javafx.scene.effect.DropShadow;

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
    private GameOverPanel gameOverPanel;

    @FXML
    private MainMenuPanel mainMenuPanel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label currentScoreLabel;

    @FXML
    private Label highScoreLabel;

    @FXML
    private Label highScoreTextLabel;

    private int highScore = 0;

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
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });
        gameOverPanel.setVisible(false);
        gameOverPanel.setOnReturnToMenu(e -> { timeLine.stop(); gameOverPanel.setVisible(false); if (eventListener != null && eventListener instanceof GameController) { ((GameController) eventListener).resetGame(); } clearGameDisplay(); hideGameElements(); if (mainMenuPanel != null) mainMenuPanel.setVisible(true); isPause.setValue(false); isGameOver.setValue(false); });
        gameOverPanel.setOnReplay(e -> { timeLine.stop(); gameOverPanel.setVisible(false); eventListener.createNewGame(); gamePanel.requestFocus(); timeLine.play(); isPause.setValue(false); isGameOver.setValue(false); });
        
        mainMenuPanel.setVisible(true);
        mainMenuPanel.setOnNewGame(e -> startNewGame());
        mainMenuPanel.setOnLevels(e -> {});
        mainMenuPanel.getControlsButton().setOnAction(e -> mainMenuPanel.toggleControls());
        
        hideGameElements();

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


        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
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

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null && integerProperty != null) {
            scoreLabel.textProperty().bind(integerProperty.asString());
            integerProperty.addListener((obs, oldVal, newVal) -> {
                if (newVal.intValue() > highScore) {
                    highScore = newVal.intValue();
                    if (highScoreLabel != null) highScoreLabel.setText(String.valueOf(highScore));
                }
            });
        }
        if (highScoreLabel != null) highScoreLabel.setText(String.valueOf(highScore));
    }

    public void gameOver() {
        timeLine.stop();
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
    
    private void startNewGame() {
        if (mainMenuPanel != null) {
            mainMenuPanel.setVisible(false);
        }
        showGameElements();
        if (eventListener != null && eventListener instanceof GameController) {
            ((GameController) eventListener).startGame();
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
    }
    
    private void showGameElements() {
        if (gameBoard != null) gameBoard.setVisible(true);
        if (scoreLabel != null) scoreLabel.setVisible(true);
        if (currentScoreLabel != null) currentScoreLabel.setVisible(true);
        if (highScoreLabel != null) highScoreLabel.setVisible(true);
        if (highScoreTextLabel != null) highScoreTextLabel.setVisible(true);
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
    }
}