package components.controllers;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * The VBoxController class is responsible for managing the properties and bindings
 * of a VBox node in a JavaFX application.
 */
public class VBoxController {
    private VBox node; // The VBox node that this controller manages
    private ChangeListener<Number> heightListener; // Listener for height changes
    private ChangeListener<Number> widthListener;  // Listener for width changes

    /**
     * Default constructor for VBoxController.
     */
    public VBoxController() {}

    /**
     * Binds a VBox node to this controller.
     * 
     * @param node the VBox node to be managed
     */
    public void bindVBox(VBox node) {
        this.node = node;
    }

    /**
     * Centers the bound VBox node vertically within its parent AnchorPane, with an optional offset.
     * 
     * @param offset the offset to apply to the top anchor
     */
    public void centerVScreenAnchor(double offset) {
        try {
            AnchorPane parent = (AnchorPane) node.getParent();
            AnchorPane.setTopAnchor(node, ((parent.getHeight() - node.getPrefHeight()) / 2) + offset);
            removeVListeners(parent);

            if (heightListener == null) {
                heightListener = (obs, oldVal, newVal) -> {
                    AnchorPane.setTopAnchor(node, ((newVal.doubleValue() - node.getPrefHeight()) / 2) + offset);
                };
                parent.heightProperty().addListener(heightListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Centers the bound VBox node horizontally within its parent AnchorPane, with an optional offset.
     * 
     * @param offset the offset to apply to the left anchor
     */
    public void centerHScreenAnchor(double offset) {
        try {
            AnchorPane parent = (AnchorPane) node.getParent();
            AnchorPane.setLeftAnchor(node, ((parent.getWidth() - node.getPrefWidth()) / 2) + offset);
            removeHListeners(parent);

            if (widthListener == null) {
                widthListener = (obs, oldVal, newVal) -> {
                    AnchorPane.setLeftAnchor(node, ((newVal.doubleValue() - node.getPrefWidth()) / 2) + offset);
                };
                parent.widthProperty().addListener(widthListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the vertical listeners from the parent AnchorPane.
     * 
     * @param parent the parent AnchorPane
     */
    private void removeVListeners(AnchorPane parent) {
        if (heightListener != null) {
            parent.heightProperty().removeListener(heightListener);
            heightListener = null;
        }
    }

    /**
     * Removes the horizontal listeners from the parent AnchorPane.
     * 
     * @param parent the parent AnchorPane
     */
    private void removeHListeners(AnchorPane parent) {
        if (widthListener != null) {
            parent.widthProperty().removeListener(widthListener);
            widthListener = null;
        }
    }

    /**
     * Gets the bound VBox node.
     * 
     * @return the VBox node managed by this controller
     */
    public VBox getNode() {
        return node;
    }
}
