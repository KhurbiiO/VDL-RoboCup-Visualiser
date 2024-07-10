package visualizer.match_viewer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import components.Ball;
import components.ConIndactor;
import components.UIConfig;
import components.CoordsTransformation;
import components.Robot;
import components.StopWatch;
import components.Team;
import components.Field;
import components.communication.BroadCast;
import components.controllers.LabelController;
import components.controllers.VBoxController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @FXML 
    private VBox configMenu;

    @FXML 
    private ComboBox<String> teamAChoice;

    @FXML 
    private ComboBox<String> teamBChoice;

    @FXML
    private TextField broadCastIPConfig;
    
    @FXML
    private TextField broadCastPortConfig;

    @FXML
    private CheckBox teamAAway;

    @FXML
    private CheckBox teamBAway;
    

    private Team teamA;
    private Team teamB;
    private Ball ball;
    private StopWatch stopWatch;

    private BroadCast broadCast;

    private CoordsTransformation transform;

    private VBoxController matchStatsController;

    private LabelController refBoardController;

    private ConIndactor connACircleController;
    private ConIndactor connBCircleController;

    private UIConfig config;
    private Field fieldController;
    private double rotation = 0;
    private boolean isLaid = true;
    private boolean configIsActive = false;
    private Map<String, String> teamsDict;

    /**
     * Rotates the field by 90 degrees.
     */
    @FXML
    void rotateField() {
        rotation = (rotation + 90) % 360;
        field.setRotate(rotation);
        isLaid = !isLaid;
        resizeComponents((broadCast != null));        
    }

    /**
     * Loads the configuration file and initializes the components.
     */
    @FXML
    void applyConfig() {
        try{
            teamA.updateFromJson(getResource(teamsDict.get(teamAChoice.getValue())), !teamAAway.isSelected());
            teamB.updateFromJson(getResource(teamsDict.get(teamBChoice.getValue())), !teamBAway.isSelected());

            int n = field.getChildren().size();
            if (n > 1) {
                fieldController.removePlayers(--n);
            }

            initTeam(teamA, logoTeamA, flagTeamA, scoreTeamA, connectionTeamA);
            initTeam(teamB, logoTeamB, flagTeamB, scoreTeamB, connectionTeamB);
            initBall(ball, field);

            teamA.loadTeam(config.getPlayerStroke(), config.getPlayerStrokeWidth());
            teamB.loadTeam(config.getPlayerStroke(), config.getPlayerStrokeWidth());

            ball.setScreenColor(config.getBallColor(), config.getBallStroke(), config.getBallStrokeWidth());

            fieldController.drawField(teamA.getTeamColor(), teamB.getTeamColor());
            resizeComponents((broadCast != null));

            initBroadcast(broadCastIPConfig.getText(), Integer.parseInt(broadCastPortConfig.getText()), false);
            configMenu.setVisible(false); // Close on succesfull configuration
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleConfig(){
        configIsActive = !configIsActive;
        configMenu.setVisible(configIsActive);
    }

    private Map<String, String> getTeamsList(String filePath){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read JSON file and convert it to a Map
            Map<String, String> map = objectMapper.readValue(new File(filePath), Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initializes the WebSocket client for Broadcast.
     */
    private void initBroadcast(String ip, int port, boolean debug) {
        if (broadCast == null) {
            broadCast = new BroadCast(port, ip, teamA, teamB, ball, fieldController, transform, stopWatch, refBoardController);
            broadCast.listen(true);
        } else {
            broadCast.listen(false);
            broadCast.close();
            connACircleController.toggle();
            connBCircleController.toggle();
            broadCast = new BroadCast(port, ip, teamA, teamB, ball, fieldController, transform, stopWatch, refBoardController);
            broadCast.listen(true);
        }
        if (debug) broadCast.enableLogging(true);
    }

    /**
     * Resizes the components of the UI based on the window size.
     * 
     * @param fieldExists indicates whether the field exists
     */
    private void resizeComponents(boolean fieldExists) {
        double width = root.getWidth();
        double height = root.getHeight();

        matchStats.setPrefWidth(width * config.getRelativeWidthRatio());
        matchStats.setPrefHeight(height * config.getRelativeHeightRatio());

        statsTeamA.setPrefHeight(matchStats.getPrefHeight() * config.getRelativeTeamASectionHeightRatio());
        statsTeamB.setPrefHeight(matchStats.getPrefHeight() * config.getRelativeTeamBSectionHeightRatio());

        scoreTeamA.setPrefHeight(matchStats.getPrefHeight() * config.getRelativeTeamASectionHeightRatio());
        scoreTeamA.setPrefWidth(matchStats.getPrefWidth() * config.getRelativeTeamAScoreboardWidthRatio());
        flagTeamA.setPrefWidth(matchStats.getPrefWidth() * config.getRelativeTeamAFlagWidthRatio());
        logoTeamA.setPrefWidth(matchStats.getPrefWidth() * config.getRelativeTeamALogoWidthRatio());

        scoreTeamB.setPrefHeight(matchStats.getPrefHeight() * config.getRelativeTeamBSectionHeightRatio());
        scoreTeamB.setPrefWidth(matchStats.getPrefWidth() * config.getRelativeTeamBScoreboardWidthRatio());
        flagTeamB.setPrefWidth(matchStats.getPrefWidth() * config.getRelativeTeamBFlagWidthRatio());
        logoTeamB.setPrefWidth(matchStats.getPrefWidth() * config.getRelativeTeamBLogoWidthRatio());

        timer.setPrefHeight(matchStats.getPrefHeight() * config.getRelativeTimerboardHeightRatio());
        timer.setPrefWidth(matchStats.getPrefWidth() * config.getRelativeTimerboardWidthRatio());
        refBoard.setPrefHeight(matchStats.getPrefHeight() * config.getRelativeRefboardHeightRatio());
        refBoard.setPrefWidth(matchStats.getPrefWidth() * config.getRelativeRefboardWidthRatio());

        adjustFontSizes(width, height);

        double fieldWidth;
        double fieldHeight;
        if (isLaid){
            fieldWidth = (width - matchStats.getPrefWidth() - config.getBorderSpaceWidth()*2); 
            fieldHeight = (height - config.getBorderSpaceWidth()*2);

            double measurements[] = fitRectangleWithin(fieldWidth, fieldHeight, (fieldController.getWidth()/fieldController.getHeight()));

            fieldWidth = measurements[0];
            fieldHeight = measurements[1];
            AnchorPane.setTopAnchor(field, config.getBorderSpaceWidth());   
        }
        else{

            fieldHeight = (width - matchStats.getPrefWidth() - config.getBorderSpaceWidth()*2);
            fieldWidth = (height - config.getBorderSpaceWidth()*2);

            double measurements[] = fitRectangleWithin(fieldWidth, fieldHeight, (fieldController.getWidth()/fieldController.getHeight()));

            fieldWidth = measurements[0];
            fieldHeight = measurements[1];
            AnchorPane.setTopAnchor(field, fieldWidth/2 - fieldHeight/2 + config.getBorderSpaceWidth());
        }  

        field.setPrefHeight(fieldHeight);
        field.setPrefWidth(fieldWidth);
        AnchorPane.setLeftAnchor(field, (((width - field.getPrefWidth()) / 2) + -(fieldWidth/2 - fieldHeight/2 + config.getBorderSpaceWidth())));
        
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
            player.setScreenRadius((config.getPlayerRadius() / fieldController.getWidth()) * field.getWidth());
            player.updateVisuals(fieldController.getHeight(), fieldController.getWidth(), field.getPrefHeight(), field.getPrefWidth());
        }

        for (int i = 0; i < teamB.getTeamSize(); i++) {
            Robot player = teamB.getPlayer(i);
            player.setScreenRadius((config.getPlayerRadius() / fieldController.getWidth()) * field.getWidth());
            player.updateVisuals(fieldController.getHeight(), fieldController.getWidth(), field.getPrefHeight(), field.getPrefWidth());
        }

        ball.setScreenRadius((config.getBallRadius() / fieldController.getWidth()) * field.getWidth());
        ball.updateVisuals(fieldController.getHeight(), fieldController.getWidth(), field.getPrefHeight(), field.getPrefWidth());
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
     * @param width the width of the containing area
     * @param height the height of the containing area
     * @param ratio the aspect ratio of the rectangle to fit
     * @return an array containing the new width and height of the rectangle
     */
    public static double[] fitRectangleWithin(double width, double height, double ratio) {
        double desiredHeight = ratio * width;
        if (desiredHeight <= height) {
            return new double[]{width, desiredHeight};
        } else {
            double scalingFactor = height / desiredHeight;
            double newWidth = width * scalingFactor;
            double newHeight = desiredHeight * scalingFactor;
            return new double[]{newWidth, newHeight};
        }
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
        ball.currentPosition.setCoordinate(0, 0, 0);;
        ball.setScreenRadius(config.getBallRadius());
        ball.setScreenColor(config.getBallColor(), config.getBallStroke(), config.getBallStrokeWidth());
        ball.setScreenOpacity(1);
        field.getChildren().add(circle);
    }

    private String getResource(String path){
        // Get the resource URL
        URL resource = getClass().getResource(path);
        if (resource != null) {
            File file = new File(resource.getFile());
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * Initializes the controller and sets up bindings and listeners.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Bind components to the rootPane size for scalability
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            resizeComponents((broadCast != null));
        });
        root.heightProperty().addListener((obs, oldVal, newVal) -> {
            resizeComponents((broadCast != null));
        });

        fieldController = new Field();
        fieldController.bindPane(field);

        try{
            config = new UIConfig(getResource("/config/ui.json"));
            teamsDict = getTeamsList(getResource("/config/teams.json"));
            fieldController.loadConfigFromJson(getResource("/config/field.json"));
            
            Set<String> keys = teamsDict.keySet();
            ObservableList<String> items = FXCollections.observableArrayList(keys);
            teamAChoice.setItems(items);
            teamBChoice.setItems(items);

            fieldController.drawField("#000000", "#000000");

        }catch (Exception e){
            e.printStackTrace();
        }
        transform = new CoordsTransformation();

        teamA = new Team(false);
        teamB = new Team(true);
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

        resizeComponents((broadCast != null));

        Timeline updateUI = new Timeline(
            new KeyFrame(Duration.millis(50d), event -> {
                stopWatch.updateStopWatch();
            }));
        updateUI.setCycleCount(Timeline.INDEFINITE);
        updateUI.play();
    }
}
