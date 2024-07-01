package components.controllers;

import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class VBoxController {
    private VBox node;
    private ChangeListener<Number> heightListener;
    private ChangeListener<Number> widthListener;

    public VBoxController(){}

    public void bindVBox(VBox node){
        this.node = node;
    }

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

    private void removeVListeners(AnchorPane parent) {
        if (heightListener != null) {
            parent.heightProperty().removeListener(heightListener);
            heightListener = null;
        }
    }

    private void removeHListeners(AnchorPane parent) {
        if (widthListener != null) {
            parent.widthProperty().removeListener(widthListener);
            widthListener = null;
        }
    }

    public VBox getNode(){
        return node;
    }
}
