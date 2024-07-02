package visualizer.match_viewer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import components.Ball;
import components.ConIndactor;
import components.Config;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

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

    Team teamA;
    Team teamB;
    Ball ball;
    StopWatch stopWatch;

    WSClient WSTeamAClient;
    WSClient WSTeamBClient;
    RefClient refClient;

    CoordsTransformation transform;

    VBoxController matchStatsController;

    LabelController refBoardController;

    ConIndactor connACircleController;
    ConIndactor connBCircleController;

    Config config;
    Field fieldController;

    double realWidth;
    double realHeight;
    double rotation = 0;
    boolean isLaid = false;

    @FXML
    void rotateField(){
        rotation = (rotation+90)%360;
        field.setRotate(rotation);
        isLaid = !isLaid;
        resizeComponents(config.getLoaded());
    }

    @FXML
    void loadConfig(){
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            config.readConfig(selectedFile.getAbsolutePath());

            // Update field information
            fieldController.setA(config.getFieldA());
            fieldController.setB(config.getFieldB());
            fieldController.setC(config.getFieldC());
            fieldController.setD(config.getFieldD());
            fieldController.setE(config.getFieldE());
            fieldController.setF(config.getFieldF());
            fieldController.setG(config.getFieldG());
            fieldController.setH(config.getFieldH());
            fieldController.setI(config.getFieldI());
            fieldController.setJ(config.getFieldJ());
            fieldController.setK(config.getFieldK());
            fieldController.setL(config.getFieldL());
            fieldController.setM(config.getFieldM());
            fieldController.setN(config.getFieldN());
            fieldController.setO(config.getFieldO());
            fieldController.setP(config.getFieldP());
            fieldController.setQ(config.getFieldQ());

            int n = field.getChildren().size();
            if(n > 1){
                fieldController.removePlayers(--n);
            }
            initTeam(teamA, logoTeamA, flagTeamA, scoreTeamA, connectionTeamA);
            initTeam(teamB, logoTeamB, flagTeamB, scoreTeamB, connectionTeamB);
            initBall(ball, field);

            //Update team A info
            teamA.setTeamColor(config.getTeamAFlagcolor(), config.getPlayerStroke(), config.getPlayerStrokeWidth());
            teamA.setSmallName(config.getTeamASmallname());
            teamA.setTeamName(config.getTeamAFullname());
            teamA.setTeamSize(config.getTeamASize());
            teamA.logoDisplay.setImage(config.getTeamALogoPath());

            //Update team A info
            teamB.setTeamColor(config.getTeamBFlagcolor(), config.getPlayerStroke(), config.getPlayerStrokeWidth());
            teamB.setSmallName(config.getTeamBSmallname());
            teamB.setTeamName(config.getTeamBFullname());
            teamB.setTeamSize(config.getTeamBSize());
            teamB.logoDisplay.setImage(config.getTeamBLogoPath());

            ball.setScreenColor(config.getBallColor(), config.getBallStroke(), config.getBallStrokeWidth());

            // Update field Measurements
            realWidth = fieldController.getB() + 2*fieldController.getL() + 2*fieldController.getM();
            realHeight = fieldController.getA() + 2*fieldController.getL();

            if (WSTeamAClient == null){
                WSTeamAClient = new WSClient(config.getTeamAPort(), config.getTeamAIP(), teamA, ball, fieldController, null);
                WSTeamAClient.listen(true);
                if (WSTeamAClient.isJoined()){
                    connACircleController.toggle();
                }
            }
            else{
                WSTeamAClient.close();
                connACircleController.toggle();
                WSTeamAClient = new WSClient(config.getTeamAPort(), config.getTeamAIP(), teamA, ball, fieldController, null);
                WSTeamAClient.listen(true);
                if (WSTeamAClient.isJoined()){
                    connACircleController.toggle();
                }
            }

            if (WSTeamBClient == null){
                WSTeamBClient = new WSClient(config.getTeamBPort(), config.getTeamBIP(), teamB, ball, fieldController, transform);
                WSTeamBClient.listen(true);
                if (WSTeamBClient.isJoined()){
                    connBCircleController.toggle();
                }
            }
            else{
                WSTeamBClient.close();
                connBCircleController.toggle();
                WSTeamBClient = new WSClient(config.getTeamBPort(), config.getTeamBIP(), teamB, ball, fieldController, transform);
                WSTeamBClient.listen(true);
                if (WSTeamBClient.isJoined()){
                    connBCircleController.toggle();
                }
            }


            // Start listening in a separate thread
            if (refClient == null){
                refClient = new RefClient(config.getRefboxPort(), config.getRefboxIP(), refBoardController, teamA, teamB, stopWatch);
                refClient.listen(true);
            }
            else if(refClient != null && refClient.isListening()){
                refClient.close();
                refClient = new RefClient(config.getRefboxPort(), config.getRefboxIP(), refBoardController, teamA, teamB, stopWatch);
                refClient.listen(true);
            }

            fieldController.drawField(config.getTeamAFlagcolor(), config.getTeamBFlagcolor(), config.getFieldColor());
            resizeComponents(config.getLoaded());
        }
    }

    private void resizeComponents(boolean fieldExists) {
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

        if (isLaid && rotation == 90){

            fieldWidth = (height - 40);
            fieldHeight = (width - matchStats.getPrefWidth() - 40);
            double measurements[] = fitRectangleWithin(fieldHeight, fieldWidth, (9d/11d));


            fieldWidth = measurements[0];
            fieldHeight = measurements[1];

            AnchorPane.setTopAnchor(field, fieldWidth/2 - fieldHeight/2 + 20);

        }
        else if(isLaid && rotation == 270){
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

        if (fieldExists){
            for(int i = 0; i < teamA.getTeamSize(); i++){
                Robot player = teamA.getPlayer(i);
                player.setScreenRadius((config.getPlayerRadius()/realWidth * fieldWidth));
                player.updateVisuals(realHeight, realWidth, field.getPrefHeight(), field.getPrefWidth());
            }

            for(int i = 0; i < teamB.getTeamSize(); i++){
                Robot player = teamB.getPlayer(i);
                player.setScreenRadius((config.getPlayerRadius()/realWidth * fieldWidth));
                player.updateVisuals(realHeight, realWidth, field.getPrefHeight(), field.getPrefWidth());
            }

            ball.setScreenRadius((config.getBallRadius()/realWidth * fieldWidth));
            ball.updateVisuals(realHeight, realWidth, field.getPrefHeight(), field.getPrefWidth());
        }
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
            player.setScreenRadius(config.getPlayerRadius());
            player.setScreenColor(team.getTeamColor(), config.getPlayerStroke(), config.getPlayerStrokeWidth());
            player.setScreenOpacity(0.0);
            field.getChildren().add(circle);
        }
        team.bindProperties(logoDisplay, flagDisplay, scoreDisplay, connectionDisplay);
        team.flagDisplay.setScreenColor(team.getTeamColor());
    }

    private void initBall(Ball ball, Pane field){
        Circle circle = new Circle();
        ball.bindCircle(circle);
        ball.setScreenRadius(config.getBallRadius());
        ball.setScreenColor(config.getBallColor(), config.getBallStroke(), config.getBallStrokeWidth());
        ball.setScreenOpacity(1);
        field.getChildren().add(circle);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         // Bind components to the rootPane size for scalability
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            resizeComponents(config.getLoaded());
        });
        root.heightProperty().addListener((obs, oldVal, newVal) -> {
            resizeComponents(config.getLoaded());
        });

        config = new Config();

        transform = new CoordsTransformation();

        fieldController = new Field();
        fieldController.bindPane(field);

        teamA = new Team("TeamA", "A", 5, "#2481d7", "", false);
        teamB = new Team("TeamB", "B", 5, "#ed4848", "", true);
        ball = new Ball();

        // WSClient = new WSClient(28094, "localhost", teamA, teamB, ball, fieldController, transform);

        stopWatch = new StopWatch(50);
        stopWatch.bindLabel(timer);

        matchStatsController = new VBoxController();
        matchStatsController.bindVBox(matchStats);

        connACircleController = new ConIndactor();
        connACircleController.bindCircle(connectionTeamA);

        connBCircleController = new ConIndactor();
        connBCircleController.bindCircle(connectionTeamB);

        refBoardController = new LabelController();
        refBoardController.bindLabel(refBoard);

        resizeComponents(config.getLoaded());

        Timeline updateUI = new Timeline(
                new KeyFrame(Duration.millis(50d), event -> {
                    stopWatch.updateStopWatch();
                }));
        updateUI.setCycleCount(Timeline.INDEFINITE);
        updateUI.play();
    }
}
