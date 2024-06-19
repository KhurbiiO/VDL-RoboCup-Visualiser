package components;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Label;

public class LabelController {
    private Label node;
    public LabelController(){}

    public void bindLabel(Label node){
        this.node = node;
    }

    public void setScreenText(String text){
        node.setText(text);
    }

    public void bindScreenHeight(DoubleBinding bind){
        node.prefHeightProperty().unbind();
        node.prefHeightProperty().bind(bind);
    }

    public void bindScreenWidth(DoubleBinding bind){
        node.prefWidthProperty().unbind();
        node.prefWidthProperty().bind(bind);
    }
}
