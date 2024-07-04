package components.controllers;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Label;

/**
 * The LabelController class is responsible for managing the properties and bindings
 * of a Label node in a JavaFX application.
 */
public class LabelController {
    private Label node; // The Label node that this controller manages

    /**
     * Default constructor for LabelController.
     */
    public LabelController() {}

    /**
     * Binds a Label node to this controller.
     * 
     * @param node the Label node to be managed
     */
    public void bindLabel(Label node) {
        this.node = node;
    }

    /**
     * Sets the text of the bound Label node.
     * 
     * @param text the new text to be displayed on the label
     */
    public void setScreenText(String text) {
        node.setText(text);
    }

    /**
     * Binds the preferred height property of the bound Label node to a DoubleBinding.
     * 
     * @param bind the DoubleBinding to bind the preferred height property to
     */
    public void bindScreenHeight(DoubleBinding bind) {
        node.prefHeightProperty().unbind(); // Unbind any previous bindings
        node.prefHeightProperty().bind(bind); // Bind the preferred height property
    }

    /**
     * Binds the preferred width property of the bound Label node to a DoubleBinding.
     * 
     * @param bind the DoubleBinding to bind the preferred width property to
     */
    public void bindScreenWidth(DoubleBinding bind) {
        node.prefWidthProperty().unbind(); // Unbind any previous bindings
        node.prefWidthProperty().bind(bind); // Bind the preferred width property
    }

    /**
     * Gets the bound Label node.
     * 
     * @return the Label node managed by this controller
     */
    public Label getNode() {
        return node;
    }
}
