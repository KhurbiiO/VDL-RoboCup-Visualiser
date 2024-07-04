package components.controllers;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.beans.value.ChangeListener;

/**
 * The PaneController class is responsible for managing the properties and bindings
 * of a Pane node in a JavaFX application.
 */
public class PaneController {
    private Pane node; // The Pane node that this controller manages
    private ChangeListener<Number> heightListener; // Listener for height changes
    private ChangeListener<Number> widthListener;  // Listener for width changes

    /**
     * Default constructor for PaneController.
     */
    public PaneController() {}

    /**
     * Binds a Pane node to this controller.
     * 
     * @param node the Pane node to be managed
     */
    public void bindPane(Pane node) {
        this.node = node;
    }

    /**
     * Sets the background color of the bound Pane node.
     * 
     * @param color the new background color in hexadecimal format (e.g., "#FFFFFF" for white)
     */
    public void setScreenColor(String color) {
        node.setStyle("-fx-background-color: " + color + ";");
    }

    /**
     * Binds the preferred height property of the bound Pane node to a DoubleBinding.
     * 
     * @param bind the DoubleBinding to bind the preferred height property to
     */
    public void bindScreenHeight(DoubleBinding bind) {
        node.prefHeightProperty().unbind(); // Unbind any previous bindings
        node.prefHeightProperty().bind(bind); // Bind the preferred height property
    }

    /**
     * Binds the preferred width property of the bound Pane node to a DoubleBinding.
     * 
     * @param bind the DoubleBinding to bind the preferred width property to
     */
    public void bindScreenWidth(DoubleBinding bind) {
        node.prefWidthProperty().unbind(); // Unbind any previous bindings
        node.prefWidthProperty().bind(bind); // Bind the preferred width property
    }

    /**
     * Centers the bound Pane node vertically within its parent AnchorPane, with an optional offset.
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
     * Centers the bound Pane node horizontally within its parent AnchorPane, with an optional offset.
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
     * Gets the bound Pane node.
     * 
     * @return the Pane node managed by this controller
     */
    public Pane getNode() {
        return node;
    }
}
