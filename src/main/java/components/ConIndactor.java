package components;

import components.controllers.CircleController;

/**
 * The ConIndactor class extends CircleController to manage the visual representation
 * of a connection indicator in a JavaFX application.
 */
public class ConIndactor extends CircleController {

    private boolean connected; // Indicates whether the connection is active or not

    /**
     * Default constructor for ConIndactor.
     * Initializes the connected flag to false.
     */
    public ConIndactor() {
        super();
        connected = false;
    }

    /**
     * Toggles the connection status and updates the visual representation accordingly.
     * If connected, sets the color to green. If not connected, sets the color to red.
     */
    public void toggle() {
        connected = !connected; // Toggle the connection status
        if (connected) {
            setScreenColor("#0CFE03", "#000000", 1); // Green for connected
        } else {
            setScreenColor("#FE1203", "#000000", 1); // Red for not connected
        }
    }
}
