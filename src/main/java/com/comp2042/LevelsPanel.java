package com.comp2042;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class LevelsPanel extends BorderPane {
    
    private Button[] levelButtons = new Button[6];
    private Button backButton;
    
    public LevelsPanel() {
        getStyleClass().add("mainMenuPanel");
        VBox vbox = new VBox(15);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        Label titleLabel = new Label("SELECT LEVEL");
        titleLabel.getStyleClass().add("menuTitle");
        
        for (int i = 0; i < 5; i++) {
            levelButtons[i] = new Button("Level " + (i + 1));
            levelButtons[i].getStyleClass().add("menuButton");
        }
        levelButtons[5] = new Button("Final");
        levelButtons[5].getStyleClass().add("menuButton");
        
        backButton = new Button("Back");
        backButton.getStyleClass().add("menuButton");
        
        vbox.getChildren().add(titleLabel);
        vbox.getChildren().addAll(levelButtons);
        vbox.getChildren().add(backButton);
        setCenter(vbox);
    }
    
    public void setLevelEnabled(int level, boolean enabled) {
        if (level >= 1 && level <= 6) {
            Button btn = levelButtons[level - 1];
            btn.setDisable(!enabled);
            if (!enabled) {
                btn.getStyleClass().removeAll("menuButton");
                if (!btn.getStyleClass().contains("lockedLevelButton")) {
                    btn.getStyleClass().add("lockedLevelButton");
                }
            } else {
                btn.getStyleClass().removeAll("lockedLevelButton");
                if (!btn.getStyleClass().contains("menuButton")) {
                    btn.getStyleClass().add("menuButton");
                }
            }
        }
    }
    
    public void setOnLevelSelected(int level, javafx.event.EventHandler<ActionEvent> handler) {
        if (level >= 1 && level <= 6) {
            levelButtons[level - 1].setOnAction(handler);
        }
    }
    
    public void setOnBack(javafx.event.EventHandler<ActionEvent> handler) {
        backButton.setOnAction(handler);
    }
}

