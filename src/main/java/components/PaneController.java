package components;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class PaneController {
    private Pane node;

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

    public void centerHScreenAnchor(AnchorPane parent, Pane pane, int offset) {
        AnchorPane.setLeftAnchor(pane, ((parent.getWidth() - pane.getPrefWidth()) / 2) + offset);
        parent.widthProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setLeftAnchor(pane, ((newVal.doubleValue() - pane.getPrefWidth()) / 2) + offset);
        });
    }
    
    public void centerVScreenAnchor(AnchorPane parent, Pane pane, int offset) {
        AnchorPane.setTopAnchor(pane, ((parent.getHeight() - pane.getPrefHeight()) / 2) + offset);
        parent.heightProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setTopAnchor(pane, ((newVal.doubleValue() - pane.getPrefHeight()) / 2) + offset);
        });
    }
}