package components;

import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class CircleController {
    private Circle node;

    public CircleController(){}

    public void bindCircle(Circle node){
        this.node = node;
    }

    public void updateScreenCoordinate(double[] newPose, int duration) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(node);
        transition.setDuration(Duration.millis(duration));
        transition.setToX(newPose[0]);
        transition.setToY(newPose[1]);
        transition.play();
    }

    public void setScreenRadius(double radius){
        node.setRadius(radius);
    }
    public void setScreenOpacity(double percentage){
        node.setOpacity(percentage);
    }
    public void setScreenColor(String fillColor, String strokeColor, int strokeWidth){
        node.setFill(Color.web(fillColor));
        node.setStroke(Color.web(strokeColor));
        node.setStrokeWidth(strokeWidth);
    }

    public Circle getCircle() {
        return node;
    }
}
