package components;

import components.controllers.CircleController;

/**
 * The Robot class extends CircleController to manage the properties and visual representation
 * of a robot in a JavaFX application. It includes methods to get and set the robot's position,
 * velocity, battery percentage, ball engagement status, and intention.
 */
public class Robot extends CircleController {
    public Position currentPosition;  // The current position of the robot
    public Position targetPosition;   // The target position of the robot
    private double[] currentVelocity; // The current velocity of the robot
    private double batterPercentage;  // The battery percentage of the robot
    private boolean ballEngaged;      // Indicates if the robot has engaged with the ball
    private String intention;         // The robot's current intention or task

    /**
     * Default constructor for Robot.
     * Initializes the positions, velocity, battery percentage, ball engagement status, and intention.
     */
    public Robot() {
        super();
        currentPosition = new Position();
        targetPosition = new Position();
        currentVelocity = new double[3];
        batterPercentage = 100f;
        ballEngaged = false;
        intention = "NULL";
    }

    // Getter methods for robot properties
    public double[] getCurrentVelocity() {
        return currentVelocity;
    }

    public double getBatterPercentage() {
        return batterPercentage;
    }

    public String getIntention() {
        return intention;
    }

    public boolean getBallEngaged() {
        return ballEngaged;
    }

    // Setter methods for robot properties
    public void setCurrentPosition(double newX, double newY, double newZ, double realHeight, double realWidth, double digitalHeight, double digitalWidth) {
        currentPosition.setCoordinate(newX, newY, newZ);
        updateVisuals(realHeight, realWidth, digitalHeight, digitalWidth);
    }

    public void setTargetPosition(double newX, double newY, double newZ) {
        targetPosition.setCoordinate(newX, newY, newZ);
    }

    public void setCurrentVelocity(double[] currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public void setBatterPercentage(double batterPercentage) {
        this.batterPercentage = batterPercentage;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public void setBallEngaged(boolean ballEngaged) {
        this.ballEngaged = ballEngaged;
    }

    /**
     * Updates the visual representation of the robot based on its current position and field dimensions.
     * 
     * @param realHeight the real height of the field
     * @param realWidth the real width of the field
     * @param digitalHeight the digital height of the field
     * @param digitalWidth the digital width of the field
     */
    public void updateVisuals(double realHeight, double realWidth, double digitalHeight, double digitalWidth) {
        updateScreenCoordinate(currentPosition.getGridVVector(realWidth, realHeight, digitalWidth, digitalHeight), 1);
    }
}
