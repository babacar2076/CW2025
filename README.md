 
# Tetris Game - Coursework Project

## GitHub

https://github.com/babacar2076/CW2025.git

---

## Compilation Instructions

This project is a JavaFX application built with Maven. Follow these steps to compile and run the application:

### Prerequisites
- Java Development Kit (JDK) 23 or higher
- Maven 3.6 or higher (or use the included Maven Wrapper)

### Step-by-Step Compilation

1. **Navigate to the project directory:**
   ```bash
   cd CW2025
   ```

2. **Compile the project using Maven:**
   ```bash
   ./mvnw.cmd clean compile
   ```
   (On Windows use `.\mvnw.cmd`, on Unix/Linux/Mac use `./mvnw`)

3. **Run the application:**
   ```bash
   ./mvnw.cmd javafx:run
   ```

### Alternative: Using Maven directly
If you have Maven installed globally:
```bash
mvn clean compile
mvn javafx:run
```

### Dependencies
The project uses the following dependencies (automatically managed by Maven):
- JavaFX Controls 21.0.6
- JavaFX FXML 21.0.6
- JUnit 5.12.1 (for testing)

These dependencies will be automatically downloaded when you run Maven commands.

### Special Settings
- The project uses Java 23 as the source and target version
- The application window is set to 450x620 pixels and is non-resizable
- The game board dimensions are 20 columns by 10 rows

---

## Implemented and Working Properly

The following features have been successfully implemented and are functioning correctly:

1. **Core Tetris Gameplay**
   - All seven standard Tetris pieces (I, J, L, O, S, T, Z) with proper rotation states
   - Brick movement (left, right, down) with collision detection
   - Brick rotation (counter-clockwise) with wall-kick prevention
   - Automatic brick falling at increasing speeds based on level
   - Row clearing when rows are completely filled
   - Score calculation with bonuses (50 × lines² for cleared rows)

2. **Game Controls**
   - Arrow keys or WASD for movement and rotation
   - Spacebar for hard drop (instant drop to bottom)
   - H key for hold brick feature
   - P key to pause/unpause the game
   - N key for new game

3. **Visual Features**
   - Next brick preview showing the upcoming piece
   - Hold brick display showing the currently held piece
   - Ghost piece visualization (semi-transparent preview of landing position)
   - Animated score bonus notifications when rows are cleared
   - Color-coded bricks for each piece type

4. **Game Management**
   - Main menu with game title and navigation options
   - Level selection system (6 levels total, with Level 1 unlocked by default)
   - Pause menu with resume, restart, and return to menu options
   - Game over screen with replay and return to menu options
   - Controls information display in the main menu

5. **Scoring System**
   - Current score tracking
   - High score persistence across game sessions
   - Score bonuses for manual downward movement (1 point per move)
   - Score bonuses for row clearing (50 × lines cleared²)
   - Automatic high score updates and saving

6. **Level System**
   - Six difficulty levels with increasing speeds
   - Automatic level unlocking when reaching score threshold (200 points)
   - Final level (Level 6) with dynamic speed that increases based on score
   - Level persistence (unlocked levels and level scores saved)

7. **Data Persistence**
   - High score saved to `highscore.txt`
   - Level unlock status and scores saved to `levels.txt`
   - Automatic loading of saved data on game startup

8. **Brick Generation**
   - Random brick generation using a 7 bag system for fair distribution
   - Prevention of immediate brick type repeats for better gameplay

---

## Implemented but Not Working Properly

No major features are currently broken. Minor issues that may occur:

1. **Hold Brick Limitation**
   - The hold brick feature has a mechanism to prevent holding the same brick twice in a row, but this may sometimes be too restrictive. The implementation allows swapping between held and current brick, but the `canHold` flag resets only on new brick creation.

2. **Ghost Piece Edge Cases**
   - In rare edge cases with very fast-moving pieces, the ghost piece may briefly display incorrectly during rapid movements, but this does not affect gameplay.

---

## Features Not Implemented

The following features were not implemented:

1. **Multiplayer Mode**
   - No two-player or network multiplayer functionality was implemented, as it was outside the scope of this single-player Tetris implementation.

2. **Sound Effects and Background Music**
   - Audio features were not implemented. The game runs silently without sound effects or background music.

3. **Particle Effects or Advanced Animations**
   - While row clearing and score notifications are animated, no advanced particle effects or elaborate animations were added beyond the basic fade/translate transitions.

4. **Customizable Controls**
   - Control key bindings are hardcoded and cannot be customized through a settings menu. Users must use the default key mappings.

5. **Statistics Tracking**
   - No detailed statistics tracking (e.g., lines cleared per game, average score, play time) beyond the basic high score system.

6. **Themes or Visual Customization**
   - The game uses a fixed color scheme and visual style. No themes, color customization, or visual modes were implemented.

7. **Time Attack Mode**
   - A time attack mode with time constraints was not implemented due to earlier problems encountered while trying to implement time-based gameplay mechanics.

---

## New Java Classes

The following new Java classes were introduced for this assignment:

1. **`com.comp2042.service.FileManager`**
   - **Location:** `src/main/java/com/comp2042/service/FileManager.java`
   - **Purpose:** Manages file I/O operations for game data persistence. Handles saving and loading high scores and level data to/from text files. Includes a nested `LevelData` class to hold level information.

2. **`com.comp2042.service.LevelManager`**
   - **Location:** `src/main/java/com/comp2042/service/LevelManager.java`
   - **Purpose:** Manages game levels, including level selection, unlocking mechanism, and speed calculations. Handles level progression based on score and calculates appropriate game speed for each level.

3. **`com.comp2042.service.ScoreManager`**
   - **Location:** `src/main/java/com/comp2042/service/ScoreManager.java`
   - **Purpose:** Manages game scores including current score and high score tracking. Separates score-related business logic from UI concerns and coordinates with FileManager for persistence.

4. **`com.comp2042.ui.panel.MainMenuPanel`**
   - **Location:** `src/main/java/com/comp2042/ui/panel/MainMenuPanel.java`
   - **Purpose:** UI component for the main menu screen. Displays game title, navigation buttons (New Game, Levels, Controls), and togglable controls information.

5. **`com.comp2042.ui.panel.PausePanel`**
   - **Location:** `src/main/java/com/comp2042/ui/panel/PausePanel.java`
   - **Purpose:** UI component displayed when the game is paused. Provides buttons for resuming, restarting, or returning to the main menu.

6. **`com.comp2042.ui.panel.GameOverPanel`**
   - **Location:** `src/main/java/com/comp2042/ui/panel/GameOverPanel.java`
   - **Purpose:** UI component displayed when the game ends. Provides options to replay the game or return to the main menu.

7. **`com.comp2042.ui.panel.LevelsPanel`**
   - **Location:** `src/main/java/com/comp2042/ui/panel/LevelsPanel.java`
   - **Purpose:** UI component for level selection. Displays buttons for all 6 levels with locked/unlocked states and a back button to return to the main menu.

8. **`com.comp2042.ui.panel.NotificationPanel`**
   - **Location:** `src/main/java/com/comp2042/ui/panel/NotificationPanel.java`
   - **Purpose:** UI component for displaying temporary notifications (e.g., score bonuses). Shows animated text that fades out and moves up before being removed.

---

## Modified Java Classes

The following Java classes were modified from the provided codebase:

1. **`com.comp2042.SimpleBoard`**
   - **Changes Made:**
     - Added hold brick functionality (`holdBrick()` method, `heldBrick` field, `canHold` flag)
     - Added ghost piece position calculation (`getGhostPosition()` method)
     - Modified board dimensions from 30x10 to 20x10 for better gameplay balance
     - Fixed initial brick spawn position (changed Y coordinate from 10 to 0 to prevent premature game over)
     - Added `getHeldBrick()` and `setCanHold()` methods for hold brick management
   - **Reason:** To implement hold brick feature and ghost piece visualization, and to fix game over detection bugs.

2. **`com.comp2042.GameController`**
   - **Changes Made:**
     - Added `startGame()` method to initialize game separately from construction
     - Added `getGhostPosition()` method to retrieve ghost piece data
     - Added `holdBrick()` and `getHeldBrick()` methods for hold brick functionality
     - Modified constructor to wait for "New Game" button instead of starting immediately
   - **Reason:** To separate game initialization from controller creation, and to support new features like hold brick and ghost piece.

3. **`com.comp2042.GuiController`**
   - **Changes Made:**
     - Added UI components for ghost piece display (`ghostPanel`, `ghostRectangles`)
     - Added UI components for hold brick display (`holdBrickPanel`, `holdBrickContainer`, `holdBrickRectangles`)
     - Implemented hard drop functionality (Space key)
     - Added hold brick key handler (H key)
     - Integrated level management and score management systems
     - Added main menu, pause menu, and game over screen handling
     - Added level selection panel integration
     - Implemented score bonus notification system
     - Added dynamic speed adjustment based on level and score
     - Modified initialization to show main menu first instead of starting game immediately
     - Added methods: `refreshHoldBrick()`, `holdBrick()`, `hardDrop()`, `togglePause()`, `startNewGame()`, `showGameElements()`, `hideGameElements()`, `clearHoldBlock()`
   - **Reason:** To add new UI features (ghost piece, hold brick, menus), integrate service classes, and improve game flow and user experience.

4. **`com.comp2042.Board` (Interface)**
   - **Changes Made:**
     - Added `holdBrick()` method to the interface
   - **Reason:** To standardize hold brick functionality across board implementations.

---

## Unexpected Problems

The following unexpected challenges were encountered during development:

1. **Game Over Detection Issue**
   - **Problem:** The game was incorrectly triggering game over immediately upon starting or when creating new bricks, even when the board had space.
   - **Cause:** The initial brick spawn position was set too low (Y=10), causing immediate intersection detection.
   - **Solution:** Changed the initial Y coordinate from 10 to 0 in `SimpleBoard.createNewBrick()` method. This ensures bricks spawn at the top of the board with proper spacing.

2. **Board Width Adjustment**
   - **Problem:** The original board width of 30 columns was too wide, making the game feel unbalanced and pieces difficult to manage.
   - **Solution:** Reduced board width from 30 to 20 columns in `GameController` and `SimpleBoard` for a more traditional Tetris experience.

3. **JavaFX Dependency Issues with Javadoc Generation**
   - **Problem:** When generating Javadoc documentation, the javadoc tool could not resolve JavaFX classes, causing compilation errors during documentation generation.
   - **Solution:** Configured Maven Javadoc plugin with `failOnError=false` and `-Xdoclint:none` options. Also ensured the project was compiled first before generating Javadoc, which allows Maven to properly include JavaFX dependencies in the classpath.

4. **Hold Brick State Management**
   - **Problem:** Initially, the hold brick feature allowed holding the same brick multiple times, which is not standard Tetris behavior.
   - **Solution:** Implemented a `canHold` flag that prevents holding the same brick consecutively. The flag resets when a new brick is created. However, swapping between held and current brick is still allowed for better gameplay.

5. **Ghost Piece Rendering Performance**
   - **Problem:** The ghost piece calculation and rendering initially caused slight performance issues during rapid brick movements.
   - **Solution:** Optimized the ghost position calculation in `SimpleBoard.getGhostPosition()` to use the board matrix directly without unnecessary copying, reducing memory allocations during gameplay.

6. **Level Persistence Data Format**
   - **Problem:** Initially, level data (unlocked levels and scores) was not being properly saved or loaded, causing levels to reset on game restart.
   - **Solution:** Implemented a structured file format in `FileManager.saveLevels()` and `FileManager.loadLevels()` that properly serializes boolean arrays and integer arrays with appropriate delimiters and error handling.

7. **Maven Configuration Issues**
   - **Problem:** Initially, the provided codebase could not be run due to Maven configuration problems. The project dependencies and build settings were not properly configured, preventing compilation and execution.
   - **Solution:** Had to troubleshoot and reconfigure the Maven `pom.xml` file, ensuring all dependencies (JavaFX, FXML) were correctly specified with proper versions and plugin configurations.

8. **Git Repository Commit and Push Problems**
   - **Problem:** Encountered issues with committing and pushing changes to the GitHub repository. Changes made to the code were not showing up on the remote repository, making it difficult to track progress and maintain version control.
   - **Solution:** Had to restart the project repository from scratch, ensuring proper Git configuration and commit/push workflows. This involved reinitializing the repository and carefully managing commits to ensure all changes were properly synchronized with the remote repository.

---

## Additional Notes

- The game uses a 7-bag randomization system for brick generation, ensuring fair distribution of all piece types.
- Score bonuses are calculated using the formula: `50 × (lines cleared)²`, providing exponential rewards for clearing multiple rows simultaneously.
- Level 6 (Final Level) uses dynamic speed that gradually increases as the score increases, creating an endless challenge mode.
- All UI panels use JavaFX BorderPane layout with consistent styling defined in `window_style.css`.
- The game maintains backward compatibility with saved data files; if files are missing or corrupted, default values are used.

