package components;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public abstract class Visual {
    private Pane view;

    Visual(Pane view){
        this.view = view;
    }

    public void updateScreenCoordinate(double[] newPose, int duration) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(view);
        transition.setDuration(Duration.millis(duration));
        transition.setToX(newPose[0]);
        transition.setToY(newPose[1]);
        transition.play();
    }

    public void updateScreenAngle(double degrees, int duration){
        RotateTransition transition = new RotateTransition(Duration.millis(duration), view);
        transition.setByAngle(degrees);
        transition.setCycleCount(1);
        transition.play();
    }

    public void setOpacity(double percentage){
        view.setOpacity(percentage);
    }

    public void bindHeight(DoubleBinding bind){
        view.prefHeightProperty().bind(bind);
    }

    public void bindWidth(DoubleBinding bind){
        view.prefWidthProperty().bind(bind);
    }

    private void centerPaneHorizontalAnchor(AnchorPane parent, Pane pane) {
        AnchorPane.setLeftAnchor(pane, (parent.getWidth() - pane.getPrefWidth()) / 2);
        parent.widthProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setLeftAnchor(pane, (newVal.doubleValue() - pane.getPrefWidth()) / 2);
        });
    }
    
    private void centerPaneVerticalAnchor(AnchorPane parent, Pane pane) {
        AnchorPane.setTopAnchor(pane, (parent.getHeight() - pane.getPrefHeight()) / 2);
        parent.heightProperty().addListener((obs, oldVal, newVal) -> {
            AnchorPane.setTopAnchor(pane, (newVal.doubleValue() - pane.getPrefHeight()) / 2);
        });
    }
}