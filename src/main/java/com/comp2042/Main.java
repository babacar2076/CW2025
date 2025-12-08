package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main entry point for the Tetris game application.
 * Initializes the JavaFX application, loads the FXML layout, and creates the game controller.
 */
public class Main extends Application {

    private static MediaPlayer mediaPlayer;

    /**
     * Starts the JavaFX application by loading the FXML layout and initializing the game controller.
     * @param primaryStage The primary stage for the application
     * @throws Exception if there is an error loading the FXML file
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController c = fxmlLoader.getController();

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 450, 620); //used to be 510, 620 now
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); //edit
        primaryStage.show();
        new GameController(c);
        
        // Play background music with looping
        URL musicUrl = getClass().getClassLoader().getResource("Original Tetris theme (Tetris Soundtrack).mp3");
        if (musicUrl != null) {
            Media media = new Media(musicUrl.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(javafx.util.Duration.ZERO);
                mediaPlayer.play();
            });
            mediaPlayer.play();
        }
    }


    /**
     * Main method that launches the JavaFX application.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
