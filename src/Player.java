// name surname: Devin Isler
// student ID: 2023400063

/**
 * Represents the elephant character controlled by the player.
 *
 * The Player class handles position, size, drawing, and respawning of the player
 * on the map. It tracks vertical velocity used in jumping and gravity mechanics.
 *
 * @author Devin Isler
 * @version 1.0
 */


/**
 * Represents the player character in the game.
 */
public class Player {
    // Position and dimensions
    private double x;
    private double y;
    private final double width = 20.0;
    private final double height = 20.0;

    // Movement properties
    private double velocityY = 0;

    // Visual representation
    private final String[] files = {"./misc/ElephantRight.png", "./misc/ElephantLeft.png"};
    private String file = files[0];  // Default to right-facing sprite

    /**
     * Constructs a player at the specified position.
     *
     * @param x The x-coordinate of the player
     * @param y The y-coordinate of the player
     */
    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the player's x-coordinate.
     *
     * @param x The new x-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the player's y-coordinate.
     *
     * @param y The new y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the player's x-coordinate.
     *
     * @return The player's x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the player's y-coordinate.
     *
     * @return The player's y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Resets the player's position to the specified spawn point.
     *
     * @param spawnPoints An array containing [x, y] coordinates
     */
    public void respawn(int[] spawnPoints) {
        setX(spawnPoints[0]);
        setY(spawnPoints[1]);
        velocityY = 0;
        playerDirection('R');
    }

    /**
     * Draws the player sprite at the current position.
     */
    public void draw() {
        StdDraw.picture(x, y, file, width, height);
    }

    /**
     * Updates the player's vertical position based on gravity.
     *
     * @param gravity The gravity value affecting the player
     */
    public void moveInY(double gravity) {
        setVelocityY(velocityY + gravity);
        setY(velocityY + y);
    }

    /**
     * Sets the player's facing direction.
     *
     * @param direction 'R' for right, 'L' for left
     */
    public void playerDirection(char direction) {
        if (direction == 'R') {
            file = files[0];
        }
        if (direction == 'L') {
            file = files[1];
        }
    }

    /**
     * Gets the player's vertical velocity.
     *
     * @return The player's vertical velocity
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Sets the player's vertical velocity.
     *
     * @param y The new vertical velocity
     */
    public void setVelocityY(double y) {
        velocityY = y;
    }
}