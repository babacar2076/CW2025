package com.comp2042.ui.panel;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Levels selection panel UI component.
 * Displays buttons for selecting different game levels, with locked levels disabled.
 */
public class LevelsPanel extends BorderPane {
    
    private Button[] levelButtons = new Button[6];
    private Button backButton;
    
    /**
     * Constructs a new LevelsPanel with buttons for all 6 levels and a back button.
     */
    public LevelsPanel() {
        getStyleClass().add("mainMenuPanel");
        VBox vbox = new VBox(5);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        Label titleLabel = new Label("Levels");
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

