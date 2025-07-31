// name surname: Devin Isler
// student ID: 2023400063

/**
 * Entry point for the game. Initializes stages and starts the game.
 *
 * This class creates the list of stages, sets up StdDraw configurations,
 * and launches the Game instance to begin gameplay.
 *
 *
 * @author Devin Isler
 * @version 1.0
 */

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class DevinIsler {
    public static void main(String[] args) {
        boolean isGameFinished = false;
        final int NULL_BUTTON = -1;

        // Canvas dimensions
        final int CANVAS_WIDTH = 800;
        final int CANVAS_HEIGHT = 600;
        final int FRAME_DELAY = 15;

        // Given Stages
        Stage stage1 = new Stage(-0.45, 3.65, 10, 0, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP,
                "Arrow keys are required", "Arrow keys move player, press button and enter the second pipe");  // normal game

        Stage stage2 = new Stage(-0.45, 3.65, 10, 1, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP,
                "Not always straight forward", "Right and left buttons reversed");  // Reversed Buttons

        Stage stage3 = new Stage(-2, 3.65, 24, 2, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, NULL_BUTTON,
                "A bit bouncy here", "You jump constantly");  // bouncing

        Stage stage4 = new Stage(-0.45, 3.65, 10, 3, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP,
                "Never gonna give you up", "Press button 5 times");

        Stage stage5 = new Stage(-0.45, 3.65, 10, 4, KeyEvent.VK_H, KeyEvent.VK_F, KeyEvent.VK_T,
                "Center keyboard", "Use 'F', 'T', 'H' keys to move");


        // Add the stages to the arraylist
        ArrayList<Stage> stages = new ArrayList<Stage>();
        stages.add(stage1);
        stages.add(stage2);
        stages.add(stage3);
        stages.add(stage4);
        stages.add(stage5);

        Game game = new Game(stages);
        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        StdDraw.setXscale(0, CANVAS_WIDTH);
        StdDraw.setYscale(0, CANVAS_HEIGHT);

        while (!isGameFinished) {
            StdDraw.enableDoubleBuffering();
            StdDraw.clear();
            game.play();
            StdDraw.show();
            StdDraw.pause(FRAME_DELAY);
            isGameFinished = game.getEndGame();
        }
        System.exit(0);
    }
}