// name surname: Devin İşler
// student ID: 2023400063

/**
 * Represents the game environment, including obstacles, spikes, pipes, and the player.
 *
 * The Map class manages movement, drawing, button presses, collision detection,
 * and door animation. It acts as the central logic point for game interactions
 * during a stage.
 *
 * @author Devin Isler
 * @version 1.0
 */

import java.awt.*;

/**
 * Represents the game map with obstacles, interactive elements, and collision detection.
 */
public class Map {
    // Map elements and properties
    private final Stage stage;
    private final Player player;
    private boolean firstTouch = false;
    private int deathCount = 0;
    private int buttonPressNum = 0;
    private boolean isDoorOpen = false;
    private boolean isPressed = false;
    private boolean nowPressing = false;

    // File paths
    private final String file = "./misc/Spikes.png";

    // Calculation variables
    private double[] spikeFormatted;
    private double angle;
    private Color color;
    private double[] o;
    private double[] formattedDoor;

    // Obstacles List (format is int[] = {xLeftDown, yLeftDown, xRightUp, yRightUp}
    private final int[][] obstacles = {
            new int[]{0, 120, 120, 270}, new int[]{0, 270, 168, 330},
            new int[]{0, 330, 30, 480}, new int[]{0, 480, 180, 600},
            new int[]{180, 570, 680, 600}, new int[]{270, 540, 300, 570},
            new int[]{590, 540, 620, 570}, new int[]{680, 510, 800, 600},
            new int[]{710, 450, 800, 510}, new int[]{740, 420, 800, 450},
            new int[]{770, 300, 800, 420}, new int[]{680, 240, 800, 300},
            new int[]{680, 300, 710, 330}, new int[]{770, 180, 800, 240},
            new int[]{0, 120, 800, 150}, new int[]{560, 150, 800, 180},
            new int[]{530, 180, 590, 210}, new int[]{530, 210, 560, 240},
            new int[]{320, 150, 440, 210}, new int[]{350, 210, 440, 270},
            new int[]{220, 270, 310, 300}, new int[]{360, 360, 480, 390},
            new int[]{530, 310, 590, 340}, new int[]{560, 400, 620, 430}};;

    // Button Coordinates
    private final int[] button = {400, 390, 470, 410};

    // Button Floor Coordinates
    private final int[] buttonFloor = {400, 390, 470, 400};
    private final int[][] buttonFloorArray = {{400, 390, 470, 400}};

    // Start Pipe Coordinates for Drawing
    private final int[][] startPipe = {
            {115, 450, 145, 480},
            {110, 430, 150, 450}
    };

    // Exit Pipe Coordinates for Drawing
    private final int[][] exitPipe = {
            {720, 175, 740, 215},
            {740, 180, 770, 210}
    };

    // Coordinates of spike areas
    private final int[][] spikes = {
            new int[]{30, 333, 50, 423}, new int[]{121, 150, 207, 170},
            new int[]{441, 150, 557, 170}, new int[]{591, 180, 621, 200},
            new int[]{752, 301, 771, 419}, new int[]{680, 490, 710, 510},
            new int[]{401, 550, 521, 570}};;

    // Door Coordinates
    private final int[] door = {685, 180, 700, 240};

    /**
     * Constructs a Map with the specified stage and player.
     *
     * @param stage The game stage containing game mechanics and properties
     * @param player The player object
     */
    public Map(Stage stage, Player player) {
        this.stage = stage;
        this.player = player;
        this.color = stage.getColor();
        this.formattedDoor = setFormat(door);
    }

    /**
     * Moves the player in the specified direction with collision detection.
     *
     * @param direction Character indicating direction: 'R' for right, 'L' for left, 'U' for up
     */
    public void movePlayer(char direction) {
        if (!firstTouch) {
            return;
        }

        double curX = getPlayer().getX();
        double curY = getPlayer().getY();
        double velocityX = stage.getVelocityX();
        double velocityY = stage.getVelocityY();
        boolean canMove = true;

        if (direction == 'R') {
            handleRightMovement(curX, curY, velocityX, canMove);
        } else if (direction == 'L') {
            handleLeftMovement(curX, curY, velocityX, canMove);
        } else if (direction == 'U') {
            handleJump(velocityY);
        }
    }

    /**
     * Handles right movement with collision detection.
     *
     * @param curX Current X position
     * @param curY Current Y position
     * @param velocityX Horizontal velocity
     * @param canMove Whether movement is possible
     */
    private void handleRightMovement(double curX, double curY, double velocityX, boolean canMove) {
        boolean canMoveRight = canMove;

        for (int[] obstacle : obstacles) {
            if (checkCollision(curX + velocityX, curY, obstacle)) {
                canMoveRight = false;
                break;
            }
        }

        if (checkCollision(curX + velocityX, curY, door) && !isDoorOpen) {
            canMoveRight = false;
        }

        if (checkCollision(curX + velocityX, curY, buttonFloor)) {
            canMoveRight = false;
        }

        if (canMoveRight) {
            getPlayer().setX(curX + velocityX);
            getPlayer().playerDirection('R');
            if (getStage().getStageNumber() == 1) {
                getPlayer().playerDirection('L');
            }
        }
    }

    /**
     * Handles left movement with collision detection.
     *
     * @param curX Current X position
     * @param curY Current Y position
     * @param velocityX Horizontal velocity
     * @param canMove Whether movement is possible
     */
    private void handleLeftMovement(double curX, double curY, double velocityX, boolean canMove) {
        boolean canMoveLeft = canMove;

        for (int[] obstacle : obstacles) {
            if (checkCollision(curX - velocityX, curY, obstacle)) {
                canMoveLeft = false;
                break;
            }
        }

        if (checkCollision(curX - velocityX, curY, door) && !isDoorOpen) {
            canMoveLeft = false;
        }

        if (checkCollision(curX - velocityX, curY, buttonFloor)) {
            canMoveLeft = false;
        }

        if (canMoveLeft) {
            getPlayer().setX(curX - velocityX);
            getPlayer().playerDirection('L');
            if (getStage().getStageNumber() == 1) {
                getPlayer().playerDirection('R');
            }
        }
    }

    /**
     * Handles jump action.
     *
     * @param velocityY Vertical velocity
     */
    private void handleJump(double velocityY) {
        boolean isTouchingGround = checkTouchGround(obstacles) || checkTouchGround(buttonFloorArray);

        if (isTouchingGround) {
            getPlayer().setVelocityY(velocityY);
        }
    }

    /**
     * Checks collisions with obstacles.
     *
     * @param nextX Next X position
     * @param nextY Next Y position
     * @param obstacle Obstacle coordinates
     * @return true if collision occurs, false otherwise
     */
    private boolean checkCollision(double nextX, double nextY, int[] obstacle) {
        return (nextX + 10 > obstacle[0] && nextX < obstacle[2] + 10 &&
                nextY + 10 > obstacle[1] && nextY < obstacle[3] + 10);
    }

    /**
     * Checks if the player is touching the ground.
     *
     * @param obstacles Array of obstacles to check against
     * @return true if player is touching ground, false otherwise
     */
    public boolean checkTouchGround(int[][] obstacles) {
        boolean isTouchingGround = false;

        for (int[] obstacle : obstacles) {
            o = setFormat(obstacle);
            if (getPlayer().getY() + getStage().getGravity() - o[1] > 0 &&
                    getPlayer().getY() + getStage().getGravity() - o[1] <= o[3] + 10 &&
                    getPlayer().getX() + 10 > obstacle[0] && getPlayer().getX() < obstacle[2] + 10) {
                isTouchingGround = true;
                break;
            }
        }

        if (isTouchingGround) {
            getPlayer().setY(o[1] + o[3] + 10);
            return true;
        }

        return false;
    }

    /**
     * Checks if the player is touching a ceiling.
     *
     * @param obstacles Array of obstacles to check against
     * @return true if player is touching ceiling, false otherwise
     */
    public boolean checkTouchCeiling(int[][] obstacles) {
        boolean isTouchingCeiling = false;

        for (int[] obstacle : obstacles) {
            o = setFormat(obstacle);
            if (o[1] - getPlayer().getY() + getStage().getGravity() > 0 &&
                    o[1] - getPlayer().getY() + getStage().getGravity() <= o[3] + 10 &&
                    getPlayer().getX() + 10 > obstacle[0] && getPlayer().getX() < obstacle[2] + 10) {
                isTouchingCeiling = true;
                break;
            }
        }

        if (isTouchingCeiling) {
            getPlayer().setY(o[1] - o[3] - 10);
            return true;
        }
        return false;
    }

    /**
     * Checks if the player has collided with spikes.
     *
     * @param spikes Array of spike coordinates
     * @return true if collision with spikes occurs, false otherwise
     */
    public boolean checkSpikeCollision(int[][] spikes) {
        for (int[] spike : spikes) {
            if (getPlayer().getX() + 10 >= spike[0] && getPlayer().getX() - 10 <= spike[2]
                    && getPlayer().getY() >= spike[1] && getPlayer().getY() <= spike[3]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player has collided with a button.
     *
     * @param button Button coordinates
     * @return true if collision with button occurs, false otherwise
     */
    public boolean checkButtonCollision(int[] button) {
        return getPlayer().getX() + 10 >= button[0] && getPlayer().getX() - 10 <= button[2]
                && getPlayer().getY() >= button[1] && getPlayer().getY() <= button[3];
    }

    /**
     * Checks if the player has reached the exit to change stage.
     *
     * @return true if player has reached exit, false otherwise
     */
    public boolean changeStage() {
        double[] formattedPipe = setFormat(exitPipe[1]);
        return Math.abs(getPlayer().getX() - formattedPipe[0] ) <= 20
                && Math.abs(getPlayer().getY() - formattedPipe[1]) <= 7;
    }

    /**
     * Presses the button and increases buttonPressNum.
     * Opens the door if enough presses have occurred.
     */
    public void pressButton() {
        buttonPressNum += 1;
        if (buttonPressNum >= getStage().getNeededPress()) {
            isDoorOpen = true;
        }
    }

    /**
     * Restarts the stage, resetting player position and game state.
     */
    public void restartStage() {
        player.respawn(new int[]{131, 460});
        buttonPressNum = 0;
        firstTouch = false;
        isDoorOpen = false;
    }

    /**
     * Converts rectangle format from [xLeftDown, yLeftDown, xRightUp, yRightUp]
     * to [centerX, centerY, halfWidth, halfHeight].
     *
     * @param format Rectangle in [xLeftDown, yLeftDown, xRightUp, yRightUp] format
     * @return Converted format as [centerX, centerY, halfWidth, halfHeight]
     */
    private double[] setFormat(int[] format) {
        double xLeftDown = format[0];
        double yLeftDown = format[1];
        double xRightUp = format[2];
        double yRightUp = format[3];
        return new double[]{
                (xRightUp + xLeftDown) / 2, (yRightUp + yLeftDown) / 2,
                (xRightUp - xLeftDown) / 2, (yRightUp - yLeftDown) / 2
        };
    }

    /**
     * Draws all elements of the map.
     */
    public void draw() {
        updatePlayerPhysics();
        drawDoor();
        drawObstacles();
        drawSpikes();
        drawButton();
        drawButtonFloor();
        checkPlayerDeath();
        drawPlayer();
        drawPipes();
    }

    /**
     * Updates player physics including gravity and collision detection.
     */
    private void updatePlayerPhysics() {
        getPlayer().moveInY(getStage().getGravity());

        if (checkTouchGround(obstacles) || checkTouchGround(buttonFloorArray)) {
            getPlayer().setVelocityY(getStage().getGravity());
            firstTouch = true;
            if (getStage().getStageNumber() == 2) {
                getPlayer().setVelocityY(getStage().getVelocityY());
            }
        }

        if (checkTouchCeiling(obstacles)) {
            getPlayer().setVelocityY(getStage().getGravity());
            firstTouch = true;
        }
    }

    /**
     * Draws the door, changing position if open.
     */
    private void drawDoor() {
        StdDraw.setPenColor(StdDraw.GREEN);
        if (!isDoorOpen) {
            formattedDoor = setFormat(door);
            StdDraw.filledRectangle(formattedDoor[0], formattedDoor[1], formattedDoor[2], formattedDoor[3]);
        } else {
            formattedDoor[1] = formattedDoor[1] - 3;
            StdDraw.filledRectangle(formattedDoor[0], formattedDoor[1], formattedDoor[2], formattedDoor[3]);
        }
    }

    /**
     * Draws all obstacles.
     */
    private void drawObstacles() {
        StdDraw.setPenColor(stage.getColor());
        for (int[] obstacle : obstacles) {
            double[] obstaclesFormatted = setFormat(obstacle);
            StdDraw.filledRectangle(obstaclesFormatted[0], obstaclesFormatted[1],
                    obstaclesFormatted[2], obstaclesFormatted[3]);
        }
    }

    /**
     * Draws all spikes with appropriate orientation.
     */
    private void drawSpikes() {
        for (int i = 0; i < spikes.length; i++) {
            spikeFormatted = setFormat(spikes[i]);
            angle = 0.0;

            // Set spike rotation angle based on position
            if (i == 1 || i == 2 || i == 3) {
                angle = 180.0;
            } else if (i == 0) {
                angle = 90.0;
            } else if (i == 4) {
                angle = 270.0;
            }

            // Draw spike with appropriate dimensions and rotation
            if (i == 0 || i == 4) {
                StdDraw.picture(spikeFormatted[0], spikeFormatted[1], file,
                        spikeFormatted[3] * 2, spikeFormatted[2] * 2, angle);
            } else {
                StdDraw.picture(spikeFormatted[0], spikeFormatted[1], file,
                        spikeFormatted[2] * 2, spikeFormatted[3] * 2, angle);
            }
        }
    }

    /**
     * Draws the button and handles its state.
     */
    private void drawButton() {
        if (!checkButtonCollision(button)) {
            isPressed = true;
            nowPressing = true;
            StdDraw.setPenColor(StdDraw.RED);
            double[] formattedButton = setFormat(button);
            StdDraw.filledRectangle(formattedButton[0], formattedButton[1],
                    formattedButton[2], formattedButton[3]);
        } else {
            nowPressing = false;
        }

        if (isPressed && !nowPressing) {
            isPressed = false;
            pressButton();
        }
    }

    /**
     * Draws the button floor.
     */
    private void drawButtonFloor() {
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        double[] formattedButtonFloor = setFormat(buttonFloor);
        StdDraw.filledRectangle(formattedButtonFloor[0], formattedButtonFloor[1],
                formattedButtonFloor[2], formattedButtonFloor[3]);
    }

    /**
     * Checks if player died from spikes and handles respawn.
     */
    private void checkPlayerDeath() {
        if (checkSpikeCollision(spikes)) {
            player.respawn(new int[]{131, 465});
            deathCount++;
            firstTouch = false;
            isDoorOpen = false;
            buttonPressNum = 0;
        }
    }

    /**
     * Draws the player.
     */
    private void drawPlayer() {
        player.draw();
    }

    /**
     * Draws the start and exit pipes.
     */
    private void drawPipes() {
        StdDraw.setPenColor(Color.ORANGE);
        for (int[] pipe : startPipe) {
            double[] formattedPipe = setFormat(pipe);
            StdDraw.filledRectangle(formattedPipe[0], formattedPipe[1],
                    formattedPipe[2], formattedPipe[3]);
        }

        for (int[] exit : exitPipe) {
            double[] formattedExit = setFormat(exit);
            StdDraw.filledRectangle(formattedExit[0], formattedExit[1],
                    formattedExit[2], formattedExit[3]);
        }
    }

    /**
     * Returns the stage of the map.
     *
     * @return The current stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Returns the player.
     *
     * @return The player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the death count.
     *
     * @return Number of deaths
     */
    public int getDeathCount() {
        return deathCount;
    }

    /**
     * Sets the death count.
     *
     * @param death New death count
     */
    public void setDeathCount(int death) {
        this.deathCount = death;
    }
}