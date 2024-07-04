package components;

import components.controllers.PaneController;

/**
 * The Flag class extends PaneController to manage the visual representation
 * of a flag in a JavaFX application. It includes methods to set and get the
 * flag's color.
 */
public class Flag extends PaneController {

    private String color; // The color of the flag

    /**
     * Default constructor for Flag.
     * Calls the superclass constructor.
     */
    public Flag() {
        super();
    }

    /**
     * Sets the color of the flag and updates the visual representation.
     * 
     * @param color the new color of the flag in hexadecimal format (e.g., "#FFFFFF" for white)
     */
    @Override
    public void setScreenColor(String color) {
        this.color = color;
        super.setScreenColor(color); // Call the superclass method to update the visual representation
    }

    /**
     * Gets the color of the flag.
     * 
     * @return the color of the flag in hexadecimal format
     */
    public String getColor() {
        return color;
    }
}
