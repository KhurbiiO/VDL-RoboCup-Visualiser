package components;

import components.controllers.CircleController;

/**
 * The Ball class extends CircleController to manage the properties and visual representation
 * of a ball in a JavaFX application.
 */
public class Ball extends CircleController {
    public Position currentPosition; // The current position of the ball
    private double velocity;         // The velocity of the ball
    private double confidence;       // The confidence level of the ball's position

    /**
     * Default constructor for Ball.
     * Initializes the current position, velocity, and confidence.
     */
    public Ball() {
        super();
        currentPosition = new Position();
        velocity = 0d;
        confidence = 0d;
    }

    /**
     * Gets the velocity of the ball.
     * 
     * @return the velocity of the ball
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Gets the confidence level of the ball's position.
     * 
     * @return the confidence level of the ball's position
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * Sets the velocity of the ball.
     * 
     * @param velocity the new velocity of the ball
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Sets the confidence level of the ball's position.
     * 
     * @param confidence the new confidence level of the ball's position
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    /**
     * Sets the current position of the ball and updates its visual representation.
     * 
     * @param newX the new X coordinate
     * @param newY the new Y coordinate
     * @param newZ the new Z coordinate
     * @param realHeight the real height of the field
     * @param realWidth the real width of the field
     * @param digitalHeight the digital height of the field
     * @param digitalWidth the digital width of the field
     */
    public void setCurrentPosition(double newX, double newY, double newZ, double realHeight, double realWidth, double digitalHeight, double digitalWidth) {
        currentPosition.setCoordinate(newX, newY, newZ);
        updateVisuals(realHeight, realWidth, digitalHeight, digitalWidth);
    }

    /**
     * Updates the visual representation of the ball based on its current position and field dimensions.
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
