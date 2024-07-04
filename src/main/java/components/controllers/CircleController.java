package components.controllers;

import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * The CircleController class is responsible for managing the visual properties and animations
 * of a Circle node in a JavaFX application.
 */
public class CircleController {
    private Circle node; // The Circle node that this controller manages

    /**
     * Default constructor for CircleController.
     */
    public CircleController() {}

    /**
     * Binds a Circle node to this controller.
     * 
     * @param node the Circle node to be managed
     */
    public void bindCircle(Circle node) {
        this.node = node;
    }

    /**
     * Updates the screen coordinates of the bound Circle node with an animation transition.
     * 
     * @param newPose an array containing the new X and Y coordinates
     * @param duration the duration of the transition in milliseconds
     */
    public void updateScreenCoordinate(double[] newPose, int duration) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(node);
        transition.setDuration(Duration.millis(duration));
        transition.setToX(newPose[0]);
        transition.setToY(newPose[1]);
        transition.play();
    }

    /**
     * Sets the radius of the bound Circle node.
     * 
     * @param radius the new radius of the circle
     */
    public void setScreenRadius(double radius) {
        node.setRadius(radius);
    }

    /**
     * Sets the opacity of the bound Circle node.
     * 
     * @param percentage the new opacity value (0.0 to 1.0)
     */
    public void setScreenOpacity(double percentage) {
        node.setOpacity(percentage);
    }

    /**
     * Sets the fill color, stroke color, and stroke width of the bound Circle node.
     * 
     * @param fillColor the new fill color in hexadecimal format (e.g., "#FFFFFF" for white)
     * @param strokeColor the new stroke color in hexadecimal format
     * @param strokeWidth the new stroke width in pixels
     */
    public void setScreenColor(String fillColor, String strokeColor, int strokeWidth) {
        node.setFill(Color.web(fillColor));
        node.setStroke(Color.web(strokeColor));
        node.setStrokeWidth(strokeWidth);
    }

    /**
     * Gets the bound Circle node.
     * 
     * @return the Circle node managed by this controller
     */
    public Circle getNode() {
        return node;
    }
}
