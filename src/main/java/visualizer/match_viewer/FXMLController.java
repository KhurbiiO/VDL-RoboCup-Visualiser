package visualizer.match_viewer;

import java.net.URL;
import java.util.ResourceBundle;

import components.Ball;
import components.CoordsTransformation;
import components.Robot;
import components.StopWatch;
import components.Team;
import components.Field;
import components.communication.RefClient;
import components.communication.WSClient;
import components.controllers.CircleController;
import components.controllers.LabelController;
import components.controllers.VBoxController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import javafx.scene.image.ImageView;

/**
 * FXMLController is responsible for controlling the match viewer UI.
 * It initializes the components, handles data updates, and manages animations.
 */
public class FXMLController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    Label scoreTeamA;

    @FXML
    Pane flagTeamA;

    @FXML
    AnchorPane logoTeamA;

    @FXML
    Circle connectionTeamA;

    @FXML
    HBox statsTeamA;

    
    @FXML
    Label scoreTeamB;

    @FXML
    Pane flagTeamB;

    @FXML
    AnchorPane logoTeamB;

    @FXML
    Circle connectionTeamB;

    @FXML
    private HBox statsTeamB;

    @FXML
    Label refBoard;

    @FXML
    Label timer;

    @FXML
    Pane field;

    @FXML
    VBox matchStats;

    private int TEAM_SIZE = 6;

    Team teamA;
    Team teamB;
    Ball ball;
    StopWatch stopWatch;

    WSClient WSClient;
    RefClient refClient;

    CoordsTransformation transform;

    VBoxController matchStatsController;

    LabelController refBoardController;

    CircleController connACircleController;
    CircleController connBCircleController;


    double realWidth;
    double realHeight;

    Field fieldController;

    double rotation = 0;
    boolean isLaid = false;

    @FXML
    void rotateField(){
        rotation = (rotation+90)%360;
        field.setRotate(rotation);
        isLaid = !isLaid;
        resizeComponents();
    }

    private void resizeComponents() {
        double width = root.getWidth();
        double height = root.getHeight();

        matchStats.setPrefWidth(width * 0.20);
        matchStats.setPrefHeight(height * 0.20);

        statsTeamA.setPrefHeight(matchStats.getPrefHeight() * 0.2);
        statsTeamB.setPrefHeight(matchStats.getPrefHeight() * 0.2);

        scoreTeamA.setPrefHeight(matchStats.getPrefHeight() * 0.2);
        scoreTeamA.setPrefWidth(matchStats.getPrefWidth() * 0.4);
        flagTeamA.setPrefWidth(matchStats.getPrefWidth() * 0.2);
        logoTeamA.setPrefWidth(matchStats.getPrefWidth() * 0.4);

        scoreTeamB.setPrefHeight(matchStats.getPrefHeight() * 0.2);
        scoreTeamB.setPrefWidth(matchStats.getPrefWidth() * 0.4);
        flagTeamB.setPrefWidth(matchStats.getPrefWidth() * 0.2);
        logoTeamB.setPrefWidth(matchStats.getPrefWidth() * 0.4);

        timer.setPrefHeight(matchStats.getPrefHeight() * 0.15);
        timer.setPrefWidth(matchStats.getPrefWidth() * 1.0);
        refBoard.setPrefHeight(matchStats.getPrefHeight() * 0.1);
        refBoard.setPrefWidth(matchStats.getPrefWidth() * 1.0);

        adjustFontSizes(width, height);

        double fieldWidth; 
        double fieldHeight;

        if (isLaid){

            fieldWidth = (height - 40);
            fieldHeight = (width - matchStats.getPrefWidth() - 40);
            double measurements[] = fitRectangleWithin(fieldHeight, fieldWidth, (9d/11d));


            fieldWidth = measurements[0];
            fieldHeight = measurements[1];

            AnchorPane.setTopAnchor(field, fieldWidth/2 - fieldHeight/2 + 20);

        }
        else{

            fieldWidth = (width - matchStats.getPrefWidth() - 40);
            fieldHeight = (height - 40);
            double measurements[] = fitRectangleWithin(fieldWidth, fieldHeight, (9d/11d));

            fieldWidth = measurements[0];
            fieldHeight = measurements[1];

            AnchorPane.setTopAnchor(field, 20d);            
        }   

        fieldController.centerHScreenAnchor(-(matchStats.getPrefWidth() - 40));

        field.setPrefHeight(fieldHeight);
        field.setPrefWidth(fieldWidth);

        for(int i = 0; i < teamA.getTeamSize(); i++){
            teamA.getPlayer(i).updateVisuals(realHeight, realWidth, field.getPrefHeight(), field.getPrefWidth());
        }

        for(int i = 0; i < teamB.getTeamSize(); i++){
            teamB.getPlayer(i).updateVisuals(realHeight, realWidth, field.getPrefHeight(), field.getPrefWidth());
        }

        ball.updateVisuals(realHeight, realWidth, field.getPrefHeight(), field.getPrefWidth());

    }

    public void adjustFontSizes(double width, double height) {
        double min = Math.min(width, height);
        double refFontSize = min / 30;
        double timerFontSize = min / 20;
        double scoreFontSize = min / 10;
        if (refFontSize > 0) {
            Font newBaseFont = new Font("Digital-7", refFontSize);
            Font newScoreFont = new Font("Digital-7", scoreFontSize);
            Font newTimerFont = new Font("Digital-7", timerFontSize);

            scoreTeamA.setFont(newScoreFont);
            scoreTeamB.setFont(newScoreFont);
            timer.setFont(newTimerFont);
            refBoard.setFont(newBaseFont);
        }
    }

    public static double[] fitRectangleWithin(double widthA, double heightA, double aspectRatioB) {
        double newWidthB;
        double newHeightB;

        if (aspectRatioB > (widthA / heightA)) {
            newWidthB = widthA;
            newHeightB = widthA / aspectRatioB;
        } else {
            newHeightB = heightA;
            newWidthB = heightA * aspectRatioB;
        }

        if (newWidthB > widthA) {
            newWidthB = widthA;
            newHeightB = newWidthB / aspectRatioB;
        }
        if (newHeightB > heightA) {
            newHeightB = heightA;
            newWidthB = newHeightB * aspectRatioB;
        }

        return new double[]{newWidthB, newHeightB};
    }


    private void initTeam(Team team, Pane logoDisplay, Pane flagDisplay, Label scoreDisplay, Circle connectionDisplay){
        for (int i = 0; i < team.getTeamSize(); i++) {
            Robot player = team.getPlayer(i);
            Circle circle = new Circle();
            player.bindCircle(circle);
            player.setScreenRadius(10);
            player.setScreenColor(team.getTeamColor(), "#ffffff", 1);
            player.setScreenOpacity(0.2);
            field.getChildren().add(circle);
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
        field.getChildren().add(circle);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         // Bind components to the rootPane size for scalability
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            resizeComponents();
        });
        root.heightProperty().addListener((obs, oldVal, newVal) -> {
            resizeComponents();
        });

        transform = new CoordsTransformation();

        fieldController = new Field();
        fieldController.bindPane(field);

        // Init field sizes
        realWidth = fieldController.getB() + 2*fieldController.getL() + 2*fieldController.getM();
        realHeight = fieldController.getA() + 2*fieldController.getL();

        teamA = new Team("Wolves", TEAM_SIZE, "#2481d7", "", false);
        initTeam(teamA, logoTeamA, flagTeamA, scoreTeamA, connectionTeamA);

        teamB = new Team("Skull", TEAM_SIZE, "#ed4848", "", true);
        initTeam(teamB, logoTeamB, flagTeamB, scoreTeamB, connectionTeamB);

        fieldController.drawField("#2481d7", "#ed4848", "#A8DC7F");

        ball = new Ball();
        initBall(ball, field);
        // ball.setCurrentPosition(0, 0, 0, fieldHeight, fieldWidth, root.getPrefHeight(), root.getPrefWidth());

        WSClient = new WSClient(28094, "localhost", teamA, teamB, ball, fieldController, transform);

        stopWatch = new StopWatch(50);
        stopWatch.bindLabel(timer);

        matchStatsController = new VBoxController();
        matchStatsController.bindVBox(matchStats);

        connACircleController = new CircleController();
        connACircleController.bindCircle(connectionTeamA);

        connBCircleController = new CircleController();
        connBCircleController.bindCircle(connectionTeamB);

        refBoardController = new LabelController();
        refBoardController.bindLabel(refBoard);

        resizeComponents();

        // Start listening in a separate thread
        refClient = new RefClient(28097, "localhost", refBoardController, teamA, teamB, stopWatch);
        refClient.enableLogging(true);

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
