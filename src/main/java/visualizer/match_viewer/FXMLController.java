package visualizer.match_viewer;

import java.net.URL;
import java.text.FieldPosition;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.Node;

import java.util.Vector;

import components.Robot;
import components.Team;

public class FXMLController implements Initializable {

    private int TEAM_SIZE = 5;
    private int DIGITAL_ROBOT_MEASUREMENT_IN_PIXELS = 10;
    private Team teamA;
    private Team teamB;

    @FXML
    Pane fieldPane;  

    @FXML
    Pane matchStatsPane;

    public void updateScreenCoordinate(Robot robot, ImageView view){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(view);
        transition.setDuration(Duration.millis(100));
        Vector<Double> coordinate_ = robot.currentPosition.getCoordinate();
        transition.setToX(coordinate_.get(0));
        transition.setToY(coordinate_.get(1));
        transition.play();
    }

    private ImageView getImageViewFromPane(int index) {
        Node node = fieldPane.getChildren().get(index);
        if (node instanceof ImageView) {
            return (ImageView) node; // Safe to cast
        } else {
            throw new IllegalStateException("The requested node is not an ImageView");
        }
    }

    // Update everything
    // private void updateAllImageViews(Pane pane) {
    //     for (Node node : pane.getChildren()) {
    //         if (node instanceof ImageView) {
    //             ImageView imageView = (ImageView) node;
    //             // Perform operations on imageView here, e.g., set a new image, resize, etc.
    //         }
    //     }
    // }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        teamA = new Team("Los Angeles Bulls", TEAM_SIZE, false, "/media/robot.png", "/media/robot.png");
        teamB = new Team("Toronto Chimkems", TEAM_SIZE, false, "/media/robot.png", "/media/robot.png");

        for (int i = 0; i < TEAM_SIZE; i++){
            ImageView view = new ImageView(teamA.getPlayer(i).getIcon());
            view.setFitHeight(DIGITAL_ROBOT_MEASUREMENT_IN_PIXELS);
            view.setFitWidth(DIGITAL_ROBOT_MEASUREMENT_IN_PIXELS);
            fieldPane.getChildren().add(view);
        }

        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    teamA.getPlayer(1).currentPosition.setX(teamA.getPlayer(1).currentPosition.getX() + 20);
                    teamA.getPlayer(1).currentPosition.setY(teamA.getPlayer(1).currentPosition.getY() + 20);
                    updateScreenCoordinate(teamA.getPlayer(1), getImageViewFromPane(1));
                }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }
}
