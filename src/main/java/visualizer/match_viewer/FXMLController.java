package visualizer.match_viewer;

import java.net.URL;
import java.util.ResourceBundle;

import components.Ball;
import components.CircleController;
import components.CoordsTransformation;
import components.RefClient;
import components.Robot;
import components.StopWatch;
import components.Team;
import components.WSClient;
import components.LabelController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * FXMLController is responsible for controlling the match viewer UI.
 * It initializes the components, handles data updates, and manages animations.
 */
public class FXMLController implements Initializable {

    @FXML
    Label scoreTeamA;

    @FXML
    Pane flagTeamA;

    @FXML
    AnchorPane logoTeamA;

    @FXML
    Circle connectionTeamA;

    
    @FXML
    Label scoreTeamB;

    @FXML
    Pane flagTeamB;

    @FXML
    AnchorPane logoTeamB;

    @FXML
    Circle connectionTeamB;

    @FXML
    Label refBoard;

    @FXML
    Label timer;

    @FXML
    Pane field;

    private int TEAM_SIZE = 6;

    Team teamA;
    Team teamB;
    Ball ball;
    StopWatch stopWatch;

    WSClient WSClient;
    RefClient refClient;

    CoordsTransformation transform;

    LabelController timerController;
    LabelController refBoardController;

    CircleController connACircleController;
    CircleController connBCircleController;

    private void initTeam(Team team, Pane logoDisplay, Pane flagDisplay, Label scoreDisplay, Circle connectionDisplay){
        for (int i = 0; i < team.getTeamSize(); i++) {
            Robot player = team.getPlayer(i);
            Circle circle = new Circle();
            player.bindCircle(circle);
            player.setScreenRadius(10);
            player.setScreenColor(team.getTeamColor(), "#ffffff", 1);
            player.setScreenOpacity(0.2);
            field.getChildren().add(player.getCircle());
        }
        team.bindProperties(logoDisplay, flagDisplay, scoreDisplay, connectionDisplay);
        team.flagDisplay.setScreenColor(team.getTeamColor());
    }

    private void initBall(Ball ball, Pane field){
        Circle circle = new Circle();
        ball.bindCircle(circle);
        ball.setScreenRadius(4);
        ball.setScreenColor("#a8ff05", "#ffffff", 1);
        ball.setScreenOpacity(1);
        field.getChildren().add(ball.getCircle());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        transform = new CoordsTransformation();

        teamA = new Team("Wolves", TEAM_SIZE, "#2481d7", "", false);
        initTeam(teamA, logoTeamA, flagTeamA, scoreTeamA, connectionTeamA);
        teamB = new Team("Skull", TEAM_SIZE, "#ed4848", "", true);
        initTeam(teamB, logoTeamB, flagTeamB, scoreTeamB, connectionTeamB);
        ball = new Ball();
        initBall(ball, field);

        WSClient = new WSClient(28094, "localhost", teamA, teamB, ball, field, transform);

        timerController = new LabelController();
        timerController.bindLabel(timer);

        stopWatch = new StopWatch(0, false, timerController);

        connACircleController = new CircleController();
        connACircleController.bindCircle(connectionTeamA);

        connBCircleController = new CircleController();
        connBCircleController.bindCircle(connectionTeamB);

        refBoardController = new LabelController();
        refBoardController.bindLabel(refBoard);

        // Start listening in a separate thread
        refClient = new RefClient(28097, "localhost", refBoardController, teamA, teamB, stopWatch);
        refClient.enableLogging(true);

        // System.out.println(refClient.getAddress());
        // System.out.println(refClient.getPort());
        System.out.println(refClient.isClosed());

        WSClient.listen(true);
        refClient.listen(true);

        Timeline updateUI = new Timeline(
                new KeyFrame(Duration.millis(50d), event -> {
                    stopWatch.updateStopWatch();
                }));
        updateUI.setCycleCount(Timeline.INDEFINITE);
        updateUI.play();
    }
}
