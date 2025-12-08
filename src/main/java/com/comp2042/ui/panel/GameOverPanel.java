package com.comp2042.ui.panel;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Game over panel UI component displayed when the game ends.
 * Provides options to replay the game or return to the main menu.
 */
public class GameOverPanel extends BorderPane {

    private Button returnToMenuButton;
    private Button replayButton;

    /**
     * Constructs a new GameOverPanel with replay and return to menu buttons.
     */
    public GameOverPanel() {
        getStyleClass().add("gameOverBackground");
        VBox vbox = new VBox(20);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        final Label gameOverLabel = new Label("GAME OVER!");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        replayButton = new Button("Replay");
        replayButton.getStyleClass().add("newGameButton");
        returnToMenuButton = new Button("Return to Menu");
        returnToMenuButton.getStyleClass().add("newGameButton");
        vbox.getChildren().addAll(gameOverLabel, replayButton, returnToMenuButton);
        setCenter(vbox);
    }

    public void setOnReturnToMenu(javafx.event.EventHandler<ActionEvent> handler) {
        returnToMenuButton.setOnAction(handler);
    }

    public void setOnReplay(javafx.event.EventHandler<ActionEvent> handler) {
        replayButton.setOnAction(handler);
    }

}

