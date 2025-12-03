package com.comp2042;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainMenuPanel extends BorderPane {

    private Button newGameButton;
    private Button levelsButton;
    private Button controlsButton;
    private VBox controlsInfo;
    private boolean controlsVisible = false;

    public MainMenuPanel() {
        getStyleClass().add("mainMenuPanel");
        VBox vbox = new VBox(20);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        
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
        
        Label leftRight = new Label("← → or A D : Move Left/Right");
        leftRight.getStyleClass().add("controlsText");
        
        Label rotate = new Label("↑ or W : Rotate");
        rotate.getStyleClass().add("controlsText");
        
        Label down = new Label("↓ or S : Move Down");
        down.getStyleClass().add("controlsText");
        
        Label space = new Label("SPACE : Hard Drop");
        space.getStyleClass().add("controlsText");
        
        Label pause = new Label("P : Pause");
        pause.getStyleClass().add("controlsText");
        
        Label hold = new Label("H : Hold");
        hold.getStyleClass().add("controlsText");
        
        Label newGameKey = new Label("N : New Game");
        newGameKey.getStyleClass().add("controlsText");
        
        controlsInfo.getChildren().addAll(controlsTitle, leftRight, rotate, down, space, pause, hold, newGameKey);
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

