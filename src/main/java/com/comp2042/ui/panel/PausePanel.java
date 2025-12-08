package com.comp2042.ui.panel;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Pause panel UI component displayed when the game is paused.
 * Provides options to resume, restart, or return to the main menu.
 */
public class PausePanel extends BorderPane {

    private Button resumeButton;
    private Button restartButton;
    private Button returnToMenuButton;

    /**
     * Constructs a new PausePanel with resume, restart, and return to menu buttons.
     */
    public PausePanel() {
        getStyleClass().add("gameOverBackground");
        VBox vbox = new VBox(20);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        final Label pauseLabel = new Label("PAUSED");
        pauseLabel.getStyleClass().add("gameOverStyle");
        resumeButton = new Button("Resume");
        resumeButton.getStyleClass().add("newGameButton");
        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("newGameButton");
        returnToMenuButton = new Button("Return to Menu");
        returnToMenuButton.getStyleClass().add("newGameButton");
        vbox.getChildren().addAll(pauseLabel, resumeButton, restartButton, returnToMenuButton);
        setCenter(vbox);
    }

    public void setOnResume(javafx.event.EventHandler<ActionEvent> handler) {
        resumeButton.setOnAction(handler);
    }

    public void setOnRestart(javafx.event.EventHandler<ActionEvent> handler) {
        restartButton.setOnAction(handler);
    }

    public void setOnReturnToMenu(javafx.event.EventHandler<ActionEvent> handler) {
        returnToMenuButton.setOnAction(handler);
    }
}

