package components.controllers;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.beans.value.ChangeListener;


public class PaneController {
    private Pane node;
    private ChangeListener<Number> heightListener;
    private ChangeListener<Number> widthListener;

    public PaneController(){}

    public void bindPane(Pane node){
        this.node = node;
    }

    public void setScreenColor(String color){
        node.setStyle("-fx-background-color: " + color + ";");
    }

    public void bindScreenHeight(DoubleBinding bind){
        node.prefHeightProperty().unbind();
        node.prefHeightProperty().bind(bind);
    }

    public void bindScreenWidth(DoubleBinding bind){
        node.prefWidthProperty().unbind();
        node.prefWidthProperty().bind(bind);
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

    public Pane getNode(){
        return node;
    }
}