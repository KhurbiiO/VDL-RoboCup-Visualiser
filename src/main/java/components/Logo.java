package components;

import java.io.File;
import java.net.URL;

import components.controllers.PaneController;

/**
 * The Logo class extends PaneController to manage the visual representation
 * of a logo in a JavaFX application. It includes methods to set and get the
 * logo's image path.
 */
public class Logo extends PaneController {

    private String path; // The path to the logo image

    /**
     * Default constructor for Logo.
     * Calls the superclass constructor.
     */
    public Logo() {
        super();
    }

    /**
     * Sets the image of the logo by specifying the path to the image file.
     * Updates the visual representation of the logo with the new image.
     * 
     * @param path the path to the image file
     */
    public void setImage(String path) {
        // Convert the resource path to a URL format
        this.path = getResource(path);
        // Set the background image of the Pane node
        this.getNode().setStyle("-fx-background-image: url(file://" + this.path + ");");
    }

    /**
     * Gets the path to the logo image.
     * 
     * @return the path to the logo image
     */
    public String getPath() {
        return path;
    }

    private String getResource(String path){
        // Get the resource URL
        URL resource = Logo.class.getResource(path);
        if (resource != null) {
            File file = new File(resource.getFile());
            return file.getAbsolutePath();
        }
        return null;
    }
}
