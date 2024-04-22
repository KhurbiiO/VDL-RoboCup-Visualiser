package visualizer.match_viewer;
/*
Put header here


 */

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.util.Vector;
import components.Robot;

public class FXMLController implements Initializable {

    Random rand = new Random();

    private Robot robot;

    @FXML
    ImageView view;

    @FXML
    private void btnClickAction(ActionEvent event) {
        System.out.println("Hello world!");
    }

    public void updateScreenCoordinate(Robot robot){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(view);
        transition.setDuration(Duration.millis(100));
        Vector<Double> coordinate_ = robot.coordinate.getCoordinate();
        transition.setToX(coordinate_.get(0));
        transition.setToY(coordinate_.get(1));
        transition.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        robot = new Robot(1);
        view.setImage(robot.getIcon());
        // Add periodic ping
        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    robot.coordinate.setX(robot.coordinate.getX() + 20);
                    robot.coordinate.setY(robot.coordinate.getY() + 20);
                    System.out.println(robot.coordinate.getX());
                    updateScreenCoordinate(robot);
                }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }
}
