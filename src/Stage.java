// name surname: Devin Isler
// student ID: 2023400063

/**
 * Represents a game stage with unique movement mechanics and visuals.
 *
 * A stage defines gravity, player velocity, control keys, clues, and help messages.
 * Each stage may alter the game rules (e.g., key reversals or auto-jumping).
 *
 * @author Devin Isler
 * @version 1.0
 */

import java.awt.Color;
import java.util.Random;

/**
 * Represents a stage in the game with specific properties and behaviors.
 */
public class Stage {
    private final Random randomGenerator = new Random();
    private final int stageNumber;
    private final double gravity;
    private final double velocityX;
    private final double velocityY;
    private final int rightCode;
    private final int leftCode;
    private final int upCode;
    private final String clue;
    private final String help;
    private final Color color;

    /**
     * Constructs a new Stage with the specified parameters.
     *
     * @param gravity The gravity value for this stage
     * @param velocityX The horizontal velocity for this stage
     * @param velocityY The vertical velocity for this stage
     * @param stageNumber The stage identifier number
     * @param rightCode The key code for right movement
     * @param leftCode The key code for left movement
     * @param upCode The key code for jumping/up movement
     * @param clue The clue text for this stage
     * @param help The help text for this stage
     */
    public Stage(double gravity, double velocityX, double velocityY, int stageNumber,
                 int rightCode, int leftCode, int upCode, String clue, String help) {
        this.gravity = gravity;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.stageNumber = stageNumber;
        this.rightCode = rightCode;
        this.leftCode = leftCode;
        this.upCode = upCode;
        this.clue = clue;
        this.help = help;
        this.color = generateColor();
    }

    /**
     * @return The stage number
     */
    public int getStageNumber() {
        return stageNumber;
    }

    /**
     * @return The gravity value for this stage
     */
    public double getGravity() {
        return gravity;
    }

    /**
     * @return The horizontal velocity for this stage
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * @return The vertical velocity for this stage
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * @return An array containing right, left, and up key codes
     */
    public int[] getKeyCodes() {
        return new int[]{rightCode, leftCode, upCode};
    }

    /**
     * @return The clue text for this stage
     */
    public String getClue() {
        return clue;
    }

    /**
     * @return The help text for this stage
     */
    public String getHelp() {
        return help;
    }

    /**
     * Generates a random color.
     *
     * @return A randomly generated color
     */
    private Color generateColor() {
        int red = randomGenerator.nextInt(255);
        int green = randomGenerator.nextInt(255);
        int blue = randomGenerator.nextInt(255);

        return new Color(red, green, blue);
    }

    /**
     * Determines how many button presses are needed based on the stage number.
     *
     * @return The number of button presses required
     */
    public int getNeededPress() {
        if (stageNumber == 1 || stageNumber == 2 || stageNumber == 0) {
            return 1;
        }
        if (stageNumber == 3) {
            return 5;
        }
        return 0;
    }

    /**
     * @return The color associated with this stage
     */
    public Color getColor() {
        return color;
    }
}