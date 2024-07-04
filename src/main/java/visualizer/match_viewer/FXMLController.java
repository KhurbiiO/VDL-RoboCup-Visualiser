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
import components.communication.BroadCast;
import components.communication.RefClient;
import components.communication.WSClient;
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
    private Label scoreTeamA;

    @FXML
    private Pane flagTeamA;

    @FXML
    private AnchorPane logoTeamA;

    @FXML
    private Circle connectionTeamA;

    @FXML
    private HBox statsTeamA;

    @FXML
    private Label scoreTeamB;

    @FXML
    private Pane flagTeamB;

    @FXML
    private AnchorPane logoTeamB;

    @FXML
    private Circle connectionTeamB;

    @FXML
    private HBox statsTeamB;

    @FXML
    private Label refBoard;

    @FXML
    private Label timer;

    @FXML
    private Pane field;

    @FXML
    private VBox matchStats;

    private Team teamA;
    private Team teamB;
    private Ball ball;
    private StopWatch stopWatch;

    // private WSClient WSTeamAClient;
    // private WSClient WSTeamBClient;
    private BroadCast broadCast;
    // private RefClient refClient;

    private CoordsTransformation transform;

    private VBoxController matchStatsController;

    private LabelController refBoardController;

    private ConIndactor connACircleController;
    private ConIndactor connBCircleController;

    private Config config;
    private Field fieldController;

    private double realWidth;
    private double realHeight;
    private double rotation = 0;
    private boolean isLaid = false;

    /**
     * Rotates the field by 90 degrees.
     */
    @FXML
    void rotateField() {
        rotation = (rotation + 90) % 360;
        field.setRotate(rotation);
        isLaid = !isLaid;
        resizeComponents(config.getLoaded());        
    }

    /**
     * Loads the configuration file and initializes the components.
     */
    @FXML
    void loadConfig() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            config.readConfig(selectedFile.getAbsolutePath());

            // Update field information
            updateFieldInformation();

            int n = field.getChildren().size();
            if (n > 1) {
                fieldController.removePlayers(--n);
            }
            initTeam(teamA, logoTeamA, flagTeamA, scoreTeamA, connectionTeamA);
            initTeam(teamB, logoTeamB, flagTeamB, scoreTeamB, connectionTeamB);
            initBall(ball, field);

            // Update team A info
            updateTeamInfo(teamA, config.getTeamAFlagcolor(), config.getTeamASmallname(), config.getTeamAFullname(), config.getTeamASize(), config.getTeamALogoPath());

            // Update team B info
            updateTeamInfo(teamB, config.getTeamBFlagcolor(), config.getTeamBSmallname(), config.getTeamBFullname(), config.getTeamBSize(), config.getTeamBLogoPath());

            ball.setScreenColor(config.getBallColor(), config.getBallStroke(), config.getBallStrokeWidth());

            // Update field measurements
            realWidth = fieldController.getB() + 2 * fieldController.getL() + 2 * fieldController.getM();
            realHeight = fieldController.getA() + 2 * fieldController.getL();

            // initWSClientA();
            // initWSClientB();
            initBroadcast();
            // initRefClient();

            fieldController.drawField(config.getTeamAFlagcolor(), config.getTeamBFlagcolor(), config.getFieldColor());
            resizeComponents(config.getLoaded());
        }
    }

    /**
     * Updates the field information from the configuration.
     */
    private void updateFieldInformation() {
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
        fieldController.setGoalwidth(config.getGoalWidth());
        fieldController.setGoaldepth(config.getGoalDepth());
    }

    /**
     * Updates the information for a given team from the configuration.
     */
    private void updateTeamInfo(Team team, String flagColor, String smallName, String fullName, int teamSize, String logoPath) {
        team.setTeamColor(flagColor, config.getPlayerStroke(), config.getPlayerStrokeWidth());
        team.setSmallName(smallName);
        team.setTeamName(fullName);
        team.setTeamSize(teamSize);
        team.logoDisplay.setImage(logoPath);
    }

    /**
     * Initializes the WebSocket client for Broadcast.
     */
    private void initBroadcast() {
        if (broadCast == null) {
            broadCast = new BroadCast(config.getBroadCastPort(), config.getBroadCastIP(), teamA, teamB, ball, fieldController, transform, stopWatch, refBoardController);
            broadCast.listen(true);
            if (broadCast.isListening()) {
                connACircleController.toggle();
                connBCircleController.toggle();
            }
        } else {
            broadCast.close();
            connACircleController.toggle();
            connBCircleController.toggle();
            broadCast = new BroadCast(config.getBroadCastPort(), config.getBroadCastIP(), teamA, teamB, ball, fieldController, transform, stopWatch, refBoardController);
            broadCast.listen(true);
            if (broadCast.isListening()) {
                connACircleController.toggle();
                connBCircleController.toggle();
            }
        }
        // broadCast.enableLogging(true);
    }

    // /**
    //  * Initializes the WebSocket client for Broadcast.
    //  */
    // private void initWSClientA() {
    //     if (WSTeamAClient == null) {
    //         WSTeamAClient = new WSClient(config.getTeamAPort(), config.getTeamAIP(), teamA, ball, fieldController, null);
    //         WSTeamAClient.listen(true);
    //         if (WSTeamAClient.isJoined()) {
    //             connACircleController.toggle();
    //         }
    //     } else {
    //         WSTeamAClient.close();
    //         connACircleController.toggle();
    //         WSTeamAClient = new WSClient(config.getTeamAPort(), config.getTeamAIP(), teamA, ball, fieldController, null);
    //         WSTeamAClient.listen(true);
    //         if (WSTeamAClient.isJoined()) {
    //             connACircleController.toggle();
    //         }
    //     }
    // }

    /**
     * Initializes the WebSocket client for Team B.
     */
    // private void initWSClientB() {
    //     if (WSTeamBClient == null) {
    //         WSTeamBClient = new WSClient(config.getTeamBPort(), config.getTeamBIP(), teamB, ball, fieldController, transform);
    //         WSTeamBClient.listen(true);
    //         if (WSTeamBClient.isJoined()) {
    //             connBCircleController.toggle();
    //         }
    //     } else {
    //         WSTeamBClient.close();
    //         connBCircleController.toggle();
    //         WSTeamBClient = new WSClient(config.getTeamBPort(), config.getTeamBIP(), teamB, ball, fieldController, transform);
    //         WSTeamBClient.listen(true);
    //         if (WSTeamBClient.isJoined()) {
    //             connBCircleController.toggle();
    //         }
    //     }
    // }

    // /**
    //  * Initializes the RefClient.
    //  */
    // private void initRefClient() {
    //     if (refClient == null) {
    //         refClient = new RefClient(config.getRefboxPort(), config.getRefboxIP(), refBoardController, teamA, teamB, stopWatch);
    //         refClient.listen(true);
    //         refClient.listen(1000);
    //     } else if (refClient != null && refClient.isListening()) {
    //         refClient.close();
    //         refClient = new RefClient(config.getRefboxPort(), config.getRefboxIP(), refBoardController, teamA, teamB, stopWatch);
    //         refClient.listen(1000);
    //     }
    // }

    /**
     * Resizes the components of the UI based on the window size.
     * 
     * @param fieldExists indicates whether the field exists
     */
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

        if (fieldExists) {
            updateVisualsForComponents();
        }
    }

    /**
     * Updates the visuals for the components (robots and ball) based on the field dimensions.
     */
    private void updateVisualsForComponents() {
        for (int i = 0; i < teamA.getTeamSize(); i++) {
            Robot player = teamA.getPlayer(i);
            player.setScreenRadius((config.getPlayerRadius() / realWidth) * field.getWidth());
            player.updateVisuals(realHeight, realWidth, field.getPrefHeight(), field.getPrefWidth());
        }

        for (int i = 0; i < teamB.getTeamSize(); i++) {
            Robot player = teamB.getPlayer(i);
            player.setScreenRadius((config.getPlayerRadius() / realWidth) * field.getWidth());
            player.updateVisuals(realHeight, realWidth, field.getPrefHeight(), field.getPrefWidth());
        }

        ball.setScreenRadius((config.getBallRadius() / realWidth) * field.getWidth());
        ball.updateVisuals(realHeight, realWidth, field.getPrefHeight(), field.getPrefWidth());
    }

    /**
     * Adjusts the font sizes of the components based on the window size.
     * 
     * @param width the width of the window
     * @param height the height of the window
     */
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

    /**
     * Fits a rectangle within the given dimensions while maintaining the aspect ratio.
     * 
     * @param widthA the width of the containing area
     * @param heightA the height of the containing area
     * @param aspectRatioB the aspect ratio of the rectangle to fit
     * @return an array containing the new width and height of the rectangle
     */
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

    /**
     * Initializes the robots for a team and binds their properties.
     * 
     * @param team the team to initialize
     * @param logoDisplay the Pane for the team logo
     * @param flagDisplay the Pane for the team flag
     * @param scoreDisplay the Label for the team score
     * @param connectionDisplay the Circle for the connection indicator
     */
    private void initTeam(Team team, Pane logoDisplay, Pane flagDisplay, Label scoreDisplay, Circle connectionDisplay) {
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

    /**
     * Initializes the ball and binds its properties.
     * 
     * @param ball the ball to initialize
     * @param field the Pane for the field
     */
    private void initBall(Ball ball, Pane field) {
        Circle circle = new Circle();
        ball.bindCircle(circle);
        ball.currentPosition.setCoordinate(config.getFieldB()/2, config.getFieldA()/2, 0);;
        ball.setScreenRadius(config.getBallRadius());
        ball.setScreenColor(config.getBallColor(), config.getBallStroke(), config.getBallStrokeWidth());
        ball.setScreenOpacity(1);
        field.getChildren().add(circle);
    }

    /**
     * Initializes the controller and sets up bindings and listeners.
     */
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
