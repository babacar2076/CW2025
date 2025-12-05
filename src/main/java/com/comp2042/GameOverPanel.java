package com.comp2042;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class GameOverPanel extends BorderPane {

    private Button returnToMenuButton;
    private Button replayButton;

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
