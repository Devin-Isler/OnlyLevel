// name surname: Devin Isler
// student ID: 2023400063

/**
 * Manages the full game loop and handles stage progression, input, and state tracking.
 *
 * The Game class holds a list of stages, tracks deaths and game time, and handles
 * user interactions like resetting the game or progressing to the next stage.
 *
 * @author DevinIsler
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Represents the main game controller that manages game state, player interactions,
 * and transitions between stages.
 */
public class Game {
    // Game state properties
    private int stageIndex = 0;
    private final ArrayList<Stage> stages;
    private int deathNumber = 0;
    private double gameTime = 0.0;
    private boolean resetGame;
    private double resetTime;
    private boolean isHelpPressed = false;
    private double startTime = System.currentTimeMillis();
    private int durationTime = 0;
    private boolean isGameFinished = false;
    private double minutes;
    private double seconds;
    private double milliseconds;
    private boolean endGame;
    private boolean endPressed = false;

    // Game objects
    private Map map;
    private final Player player = new Player(131, 465);

    /**
     * Constructs a game with given stages.
     *
     * @param stages List of game stages
     */
    public Game(ArrayList<Stage> stages) {
        this.stages = stages;
        this.map = new Map(this.stages.get(stageIndex), player);
    }

    /**
     * Starts and runs the game, handling the main game loop.
     */
    public void play() {
        handleInput(map);
        map.draw();
        updateGameTime();
        drawGameInterface();

        if (resetGame) {
            handleGameReset();
        }

        if (map.changeStage()) {
            advanceToNextStage();
        }

        if (isGameFinished) {
            showGameCompletionScreen();
        }
    }

    /**
     * Updates the game timer.
     */
    private void updateGameTime() {
        double currentTime = System.currentTimeMillis();
        gameTime = currentTime - startTime - durationTime;
        minutes = Math.floor((gameTime / 1000) / 60);
        seconds = Math.floor((gameTime / 1000) % 60);
        milliseconds = gameTime % 100;
    }

    /**
     * Draws the game interface elements including timer, buttons, and status information.
     */
    private void drawGameInterface() {
        StdDraw.setFont(new Font("Ariel", Font.PLAIN, 15));
        String timeString = String.format("%02.0f:%02.0f:%02.0f", minutes, seconds, milliseconds);

        // Draw timer area
        StdDraw.setPenColor(new Color(56, 93, 172));
        StdDraw.filledRectangle(400, 60, 400, 60);

        // Draw buttons and text
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(250, 85, "Help");
        StdDraw.rectangle(250, 85, 40, 15); // Help button
        StdDraw.text(550, 85, "Restart");
        StdDraw.rectangle(550, 85, 40, 15); // Restart button
        StdDraw.text(400, 20, "RESET THE GAME");
        StdDraw.rectangle(400, 20, 80, 15); // Reset button

        // Draw game status
        deathNumber = map.getDeathCount();
        StdDraw.text(700, 75, "Deaths: " + deathNumber);
        StdDraw.text(700, 50, "Stage: " + (getStageIndex() + 1));
        StdDraw.text(100, 50, timeString);
        StdDraw.text(100, 75, "Level: 1");
        StdDraw.text(400, 85, "Clue:");

        // Show clue or help text
        String clueText = getCurrentStage().getClue();
        if (isHelpPressed) {
            clueText = getCurrentStage().getHelp();
        }
        StdDraw.text(400, 55, clueText);
    }

    /**
     * Handles the game reset process.
     */
    private void handleGameReset() {
        resetGame = false;
        greenBanner("RESETTING THE GAME...", "", "");
        StdDraw.show();
        StdDraw.pause(2000);
        resetGame();
        isHelpPressed = false;
    }

    /**
     * Advances to the next stage or finishes the game if all stages are completed.
     */
    private void advanceToNextStage() {
        stageIndex++;
        if (getStageIndex() == stages.size()) {
            isGameFinished = true;
        } else {
            player.respawn(new int[]{131, 465});
            greenBanner("You passed the stage", "But is the level over?!", "");
            StdDraw.pause(2000);
            durationTime += 2000;
            map = new Map(getCurrentStage(), player);
            map.setDeathCount(deathNumber);
            isHelpPressed = false;
        }
    }

    /**
     * Shows the game completion screen and handles end-game inputs.
     */
    private void showGameCompletionScreen() {
        StdDraw.clear();
        greenBanner("CONGRATULATIONS YOU FINISHED THE LEVEL", "PRESS 'A' TO PLAY AGAIN!",
                "You finished with " + deathNumber + " deaths in " +
                        String.format("%02.0f:%02.0f:%02.0f", minutes, seconds, milliseconds));
        while (!endPressed) {
            handleInput(map);
        }
        if (!endGame) {
            resetGame();
            isHelpPressed = false;
        }
    }

    /**
     * Handles player input and interactions (keyboard and mouse).
     *
     * @param map The current game map
     */
    private void handleInput(Map map) {
        if (!isGameFinished) {
            handleMovementInput(map);
        } else {
            handleEndGameInput();
        }
        handleMouseInput(map);
    }

    /**
     * Handles keyboard input for player movement.
     *
     * @param map The current game map
     */
    private void handleMovementInput(Map map) {

        int[] keyCodes = getCurrentStage().getKeyCodes();
        if (StdDraw.isKeyPressed(keyCodes[0])) {
            map.movePlayer('R');
        }
        if (StdDraw.isKeyPressed(keyCodes[1])) {
            map.movePlayer('L');
        }
        if (StdDraw.isKeyPressed(keyCodes[2])) {
            map.movePlayer('U');
        }
    }

    /**
     * Handles keyboard input at the end of the game.
     */
    private void handleEndGameInput() {
        if (StdDraw.isKeyPressed(KeyEvent.VK_A)) {
            endPressed = true;
            endGame = false;
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
            endGame = true;
            endPressed = true;
        }
    }

    /**
     * Handles mouse input for UI interaction.
     *
     * @param map The current game map
     */
    private void handleMouseInput(Map map) {
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();

        if (StdDraw.isMousePressed()) {
            if (mouseX < 290 && mouseX > 210 && mouseY < 100 && mouseY > 70) {
                isHelpPressed = true;
            }
            if (mouseX < 590 && mouseX > 510 && mouseY < 100 && mouseY > 70) {
                map.restartStage();
                map.setDeathCount(++deathNumber);
            }
            if (mouseX < 480 && mouseX > 320 && mouseY < 35 && mouseY > 5) {
                resetGame = true;
            }
            StdDraw.pause(80);
        }
    }

    /**
     * Returns the current stage index.
     *
     * @return The current stage index
     */
    public int getStageIndex() {
        return stageIndex;
    }

    /**
     * Returns the current game stage object.
     *
     * @return The current Stage object
     */
    public Stage getCurrentStage() {
        return stages.get(stageIndex);
    }

    /**
     * Returns whether the game should end completely.
     *
     * @return true if the game should end, false otherwise
     */
    public boolean getEndGame() {
        return endGame;
    }

    /**
     * Resets the game to its initial state.
     */
    public void resetGame() {
        stageIndex = 0;
        map = new Map(stages.getFirst(), player);
        map.restartStage();
        map.setDeathCount(0);
        deathNumber = 0;
        gameTime = 0;
        startTime = System.currentTimeMillis();
        durationTime = 0;
        isGameFinished = false;
        endGame = false;
        endPressed = false;
    }

    /**
     * Displays a green banner with text information.
     *
     * @param text1 Primary text to display
     * @param text2 Secondary text to display (optional)
     * @param text3 Tertiary text to display (optional)
     */
    public void greenBanner(String text1, String text2, String text3) {
        if (text2.isEmpty() && text3.isEmpty()) {
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.filledRectangle(400, 320, 400, 70);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new Font("Courier New", Font.BOLD, 35));
            StdDraw.text(400, 320, text1);
        } else if (text3.isEmpty()) {
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.filledRectangle(400, 270, 400, 70);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new Font("Courier New", Font.BOLD, 25));
            StdDraw.text(400, 290, text1);
            StdDraw.text(400, 250, text2);
        } else {
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.filledRectangle(400, 270, 400, 70);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new Font("Courier New", Font.BOLD, 30));
            StdDraw.text(400, 300, text1);
            StdDraw.text(400, 260, text2);
            StdDraw.setFont(new Font("Courier New", Font.BOLD, 15));
            StdDraw.text(400, 230, text3);
        }
        StdDraw.show();
    }
}