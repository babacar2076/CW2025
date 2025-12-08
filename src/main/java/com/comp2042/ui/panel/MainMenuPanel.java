package com.comp2042.ui.panel;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Main menu panel UI component displaying game title and navigation buttons.
 * Provides options to start a new game, select levels, and view controls.
 */
public class MainMenuPanel extends BorderPane {

    private Button newGameButton;
    private Button levelsButton;
    private Button controlsButton;
    private VBox controlsInfo;
    private boolean controlsVisible = false;

    /**
     * Constructs a new MainMenuPanel with all menu buttons and controls information.
     */
    public MainMenuPanel() {
        getStyleClass().add("mainMenuPanel");
        VBox vbox = new VBox(20);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox.setPadding(new javafx.geometry.Insets(110, 0, 0, 0));
        
        Label titleLabel = new Label("TETRIS");
        titleLabel.getStyleClass().add("menuTitle");
        
        newGameButton = new Button("New Game");
        newGameButton.getStyleClass().add("menuButton");
        
        levelsButton = new Button("Levels");
        levelsButton.getStyleClass().add("menuButton");
        
        controlsButton = new Button("Controls");
        controlsButton.getStyleClass().add("menuButton");
        
        controlsInfo = new VBox(10);
        controlsInfo.setAlignment(javafx.geometry.Pos.CENTER);
        controlsInfo.setVisible(false);
        createControlsInfo();
        
        vbox.getChildren().addAll(titleLabel, newGameButton, levelsButton, controlsButton, controlsInfo);
        setCenter(vbox);
    }

    private void createControlsInfo() {
        Label controlsTitle = new Label("CONTROLS");
        controlsTitle.getStyleClass().add("controlsTitle");
        
        // Left column controls
        VBox leftControls = new VBox(10);
        leftControls.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label leftRight = new Label("← → or A D : Move Left/Right");
        leftRight.getStyleClass().add("controlsText");
        
        Label rotate = new Label("↑ or W : Rotate");
        rotate.getStyleClass().add("controlsText");
        
        Label down = new Label("↓ or S : Move Down");
        down.getStyleClass().add("controlsText");
        
        Label space = new Label("SPACE : Hard Drop");
        space.getStyleClass().add("controlsText");
        
        leftControls.getChildren().addAll(leftRight, rotate, down, space);
        
        // Right column controls
        VBox rightControls = new VBox(10);
        rightControls.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label pause = new Label("P : Pause");
        pause.getStyleClass().add("controlsText");
        
        Label hold = new Label("H : Hold");
        hold.getStyleClass().add("controlsText");
        
        Label newGameKey = new Label("N : New Game");
        newGameKey.getStyleClass().add("controlsText");
        
        rightControls.getChildren().addAll(pause, hold, newGameKey);
        
        // Arrange left and right columns horizontally
        HBox controlsColumns = new HBox(40);
        controlsColumns.setAlignment(javafx.geometry.Pos.CENTER);
        controlsColumns.getChildren().addAll(leftControls, rightControls);
        
        controlsInfo.getChildren().addAll(controlsTitle, controlsColumns);
    }

    public void setOnNewGame(javafx.event.EventHandler<ActionEvent> handler) {
        newGameButton.setOnAction(handler);
    }

    public void setOnLevels(javafx.event.EventHandler<ActionEvent> handler) {
        levelsButton.setOnAction(handler);
    }

    public void toggleControls() {
        controlsVisible = !controlsVisible;
        controlsInfo.setVisible(controlsVisible);
    }
    
    public Button getControlsButton() {
        return controlsButton;
    }

}

