package visualizer.match_viewer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import components.Ball;
import components.CoordsTransformation;
import components.Robot;
import components.Team;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * FXMLController is responsible for controlling the match viewer UI.
 * It initializes the components, handles data updates, and manages animations.
 */
public class FXMLController implements Initializable {

    // Constants
    private final int TEAM_SIZE = 5;
    private final int DIGITAL_MEASUREMENTS_RATIO = 15;
    private final double FIELD_WIDTH = 25d;
    private final double FIELD_HEIGHT = 25d;
    private final double NONE_FOCUS_OPACITY = 0.5;
    
    // Simulation flag
    private boolean isSimulated = true;
    private Boolean isFlipped = false;

    // Teams and ball
    private Team teamA;
    private Team teamB;
    private Ball ball;

    // Current robot view indices
    private int idAView = 0;
    private int idBView = 0;

    // Coordinate transformer
    CoordsTransformation transformer = new CoordsTransformation();

    // FXML UI elements
    @FXML
    Button shiftA;
    @FXML
    Label xPoseA;
    @FXML
    Label yPoseA;
    @FXML
    Label zPoseA;
    @FXML 
    Label intentA;
    @FXML 
    Label engagedA;
    @FXML
    Label batA;
    @FXML
    Label IDA;

    @FXML
    Button shiftB;
    @FXML
    Label xPoseB;
    @FXML
    Label yPoseB;
    @FXML
    Label zPoseB;
    @FXML 
    Label intentB;
    @FXML 
    Label engagedB;
    @FXML
    Label batB;
    @FXML
    Label IDB;

    @FXML
    ImageView logoA;
    @FXML
    ImageView logoB;
    @FXML
    Label scoreA;
    @FXML
    Label scoreB;
    @FXML
    Pane fieldPane;  
    @FXML
    GridPane matchStatsPane;
    @FXML
    AnchorPane gamePane;
    @FXML
    Label matchBoard;

    /**
     * Increments the given number by one and wraps around TEAM_SIZE.
     * @param x the number to increment.
     * @return the incremented number.
     */
    private int shiftNumber(int x) {
        return ++x % TEAM_SIZE;
    }

    /**
     * Handles the shift action for team A.
     * Updates the displayed robot information and highlights the current robot.
     */
    @FXML
    private void shiftAFunc() {
        ImageView last = getImageViewFromPane(idAView);
        last.setOpacity(NONE_FOCUS_OPACITY);

        idAView = shiftNumber(idAView);
        IDA.setText(Integer.toString(idAView + 1));

        ImageView next = getImageViewFromPane(idAView);
        next.setOpacity(1);
    }

    /**
     * Handles the shift action for team B.
     * Updates the displayed robot information and highlights the current robot.
     */
    @FXML
    private void shiftBFunc() {
        ImageView last = getImageViewFromPane(idBView + TEAM_SIZE);
        last.setOpacity(NONE_FOCUS_OPACITY);

        idBView = shiftNumber(idBView);
        IDB.setText(Integer.toString(idBView + 1));

        ImageView next = getImageViewFromPane(idBView + TEAM_SIZE);
        next.setOpacity(1);
    }


    private void centerObjectAnchor(AnchorPane parent, Pane node, boolean horizontal, boolean vertical) {
        if (horizontal) {
            AnchorPane.setLeftAnchor(node, (parent.getWidth() - node.getPrefWidth()) / 2);
            gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
                AnchorPane.setLeftAnchor(fieldPane, (newVal.doubleValue() - node.getPrefWidth()) / 2);
            });
        }
        if (vertical) {
            AnchorPane.setTopAnchor(node, (parent.getHeight() - node.getPrefHeight()) / 2);
            parent.heightProperty().addListener((obs, oldVal, newVal) -> {
                AnchorPane.setTopAnchor(node, (newVal.doubleValue() - node.getPrefHeight()) / 2);
            });
        }
    }
    

    private void scalePaneSize() {
        // Unbind
        fieldPane.prefHeightProperty().unbind();
        fieldPane.prefWidthProperty().unbind();
        
        if (isFlipped) {
            fieldPane.prefHeightProperty().bind(gamePane.widthProperty().multiply(0.90));
            fieldPane.prefWidthProperty().bind(gamePane.heightProperty().multiply(0.75));

            centerObjectAnchor(gamePane, fieldPane, true, true);


            // fieldPane.layoutYProperty().bind(gamePane.heightProperty().multiply(0.75));
            

            // centerObjectAnchor(gamePane, fieldPane, true, true);    

            // gamePane.setTopAnchor(fieldPane, (gamePane.getHeight() - fieldPane.getPrefHeight()) / 2);
            // gamePane.setLeftAnchor(fieldPane, (gamePane.getWidth() - fieldPane.getPrefWidth()) / 2);

            // gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
            //     AnchorPane.setTopAnchor(fieldPane, (newVal.doubleValue() - fieldPane.getHeight()) / 2);
            // });
            // gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            //     AnchorPane.setLeftAnchor(fieldPane, (newVal.doubleValue() - fieldPane.getWidth()) / 2);
            // });

        } else {
            fieldPane.prefWidthProperty().bind(gamePane.widthProperty().multiply(0.4));
            fieldPane.prefHeightProperty().bind(gamePane.heightProperty().multiply(0.75));

            centerObjectAnchor(gamePane, fieldPane, true, true);


            // gamePane.setTopAnchor(fieldPane, (gamePane.getHeight() - fieldPane.getPrefHeight()) / 2);
            // gamePane.setLeftAnchor(fieldPane, (gamePane.getWidth() - fieldPane.getPrefWidth()) / 2);

            // gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            //     AnchorPane.setTopAnchor(fieldPane, (newVal.doubleValue() - fieldPane.getHeight()) / 2);
            // });
            // gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
            //     AnchorPane.setLeftAnchor(fieldPane, (newVal.doubleValue() - fieldPane.getWidth()) / 2);
            // });
        }
    }

    // Assuming you have a method to flip the field
    @FXML
    private void flipField() {
        isFlipped = !isFlipped;
        fieldPane.setRotate(isFlipped ? -90 : 0);
        scalePaneSize();
    }

    /**
     * Updates the screen coordinates of the given ImageView to the new pose.
     * @param view the ImageView to update.
     * @param newPose the new pose coordinates.
     */
    public void updateScreenCoordinate(ImageView view, double[] newPose) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(view);
        transition.setDuration(Duration.millis(100));
        transition.setToX(newPose[0]);
        transition.setToY(newPose[1]);
        transition.play();
    }

    /**
     * Retrieves the ImageView at the specified index from the field pane.
     * @param index the index of the ImageView to retrieve.
     * @return the ImageView at the specified index.
     */
    private ImageView getImageViewFromPane(int index) {
        Node node = fieldPane.getChildren().get(index);
        if (node instanceof ImageView) {
            return (ImageView) node; // Safe to cast
        } else {
            throw new IllegalStateException("The requested node is not an ImageView");
        }
    }

    public static double calculateAngleBetweenPoints(double x1, double y1, double x2, double y2) {
        // Calculate the dot product
        double dotProduct = x1 * x2 + y1 * y2;
        
        // Calculate the magnitudes
        double magnitudeA = Math.sqrt(x1 * x1 + y1 * y1);
        double magnitudeB = Math.sqrt(x2 * x2 + y2 * y2);
        
        // Calculate the cosine of the angle
        double cosTheta = dotProduct / (magnitudeA * magnitudeB);
        
        // Calculate the angle in radians
        double angle = Math.acos(cosTheta);

        // Convert the angle to degrees
        double angleInDegrees = Math.toDegrees(angle);

        
        return angleInDegrees;
    }

    public void setTargetAngle(ImageView view, double targetAngle) {
        double currentAngle = view.getRotate();

        // Calculate the shortest path to the target angle
        double angleDifference = targetAngle - currentAngle;
        if (angleDifference > 180) {
            angleDifference -= 360;
        } else if (angleDifference < -180) {
            angleDifference += 360;
        }

        // Create and play the RotateTransition
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), view);
        rotateTransition.setByAngle(angleDifference);
        rotateTransition.setCycleCount(1);
        rotateTransition.play();
    }

    /**
     * Updates the visuals based on the provided data string.
     * @param data the JSON data string.
     * @param team the team to update.
     * @param indexShift the index shift for the team.
     */
    private void updateVisuals(String data, Team team, int indexShift) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(data);

            Iterator<JsonNode> robots = jsonNode.get("worldstate").get("robots").elements();

            for (int i = 0; i < TEAM_SIZE; i++) {
                JsonNode robot = robots.next();
                Robot player = team.getPlayer(i);

                player.setBallEngaged(robot.get("ballEngaged").asBoolean());
                player.setBatterPercentage(robot.get("batteryLevel").asDouble());
                player.setIntention(robot.get("intention").asText());

                Iterator<JsonNode> currentPose_ = robot.get("pose").elements();
                double currentPose[] = {currentPose_.next().asDouble(), currentPose_.next().asDouble(), currentPose_.next().asDouble()};
                if (indexShift > 0) {
                    currentPose = transformer.transformRobotCoords(currentPose);
                    if (i == idBView){
                        xPoseB.setText("X: "+player.currentPosition.getX().toString());
                        yPoseB.setText("Y: "+player.currentPosition.getY().toString());
                        zPoseB.setText("Z: "+player.currentPosition.getZ().toString());

                        intentB.setText("Intent: "+player.getIntention());
                        engagedB.setText("X: "+(player.getBallEngaged() ? "true" : "false"));
                        batB.setText("Bat: "+Double.toString(player.getBatterPercentage()));
                    }
                }
                else{
                    if (i == idAView){
                        xPoseA.setText("X: "+player.currentPosition.getX().toString());
                        yPoseA.setText("Y: "+player.currentPosition.getY().toString());
                        zPoseA.setText("Z: "+player.currentPosition.getZ().toString());

                        intentA.setText("Intent: "+player.getIntention());
                        engagedA.setText("X: "+(player.getBallEngaged() ? "true" : "false"));
                        batA.setText("Bat: "+Double.toString(player.getBatterPercentage()));
                    }
                }
                player.setCurrentPosition(currentPose[0], currentPose[1], currentPose[2]);

                Iterator<JsonNode> targetPose_ = robot.get("target pose").elements();
                double targetPose[] = {targetPose_.next().asDouble(), targetPose_.next().asDouble(), targetPose_.next().asDouble()};

                if (indexShift > 0) {
                    targetPose = transformer.transformRobotCoords(targetPose);
                }
                player.setTargetPosition(targetPose[0], targetPose[1], targetPose[2]);
        
                double screenPose[];
                if (isFlipped){ 
                    screenPose = player.currentPosition.getGridVVector(FIELD_WIDTH, FIELD_HEIGHT, fieldPane.getWidth(), fieldPane.getHeight());
                }
                else{
                    screenPose = player.currentPosition.getGridHVector(FIELD_WIDTH, FIELD_HEIGHT, fieldPane.getWidth(), fieldPane.getHeight());
                }

                ImageView view = getImageViewFromPane(i + indexShift);
                view.setFitHeight(fieldPane.getWidth() / DIGITAL_MEASUREMENTS_RATIO);
                view.setFitWidth(fieldPane.getWidth() / DIGITAL_MEASUREMENTS_RATIO);

                // Rotate rotate = new Rotate();
                // view.getTransforms().add(rotate);

                setTargetAngle(view, (calculateAngleBetweenPoints(player.currentPosition.getX(), player.currentPosition.getY(), player.targetPosition.getX(), player.targetPosition.getY())));
                updateScreenCoordinate(view, screenPose);
            }

            if (indexShift == 0) {
                try {
                    Iterator<JsonNode> balls = jsonNode.get("worldstate").get("balls").elements();
                    JsonNode ball_ = balls.next();
                    Iterator<JsonNode> currentPose_ = ball_.get("position").elements();

                    ball.currentPosition.setX(currentPose_.next().asDouble());
                    ball.currentPosition.setY(currentPose_.next().asDouble());
                    ball.currentPosition.setZ(currentPose_.next().asDouble());

                    ball.setConfidence(ball_.get("confidence").asDouble());

                    double currentPose[];
                    if (isFlipped){ 
                        currentPose = ball.currentPosition.getGridVVector(FIELD_WIDTH, FIELD_HEIGHT, fieldPane.getWidth(), fieldPane.getHeight());
                    }
                    else{
                        currentPose = ball.currentPosition.getGridHVector(FIELD_WIDTH, FIELD_HEIGHT, fieldPane.getWidth(), fieldPane.getHeight());
                    }

                    ImageView view = getImageViewFromPane(2 * TEAM_SIZE);
                    view.setFitHeight(fieldPane.getWidth() / DIGITAL_MEASUREMENTS_RATIO);
                    view.setFitWidth(fieldPane.getWidth() / DIGITAL_MEASUREMENTS_RATIO);
                    updateScreenCoordinate(view, currentPose);
                } catch (NoSuchElementException e) {
                    // Handle case where no ball is found
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     * Sets up teams, ball, and UI components. Loads and starts the data update threads.
     * @param url the location used to resolve relative paths for the root object.
     * @param rb the resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scalePaneSize();

        // Add listeners to keep the Node centered when the size of the AnchorPane changes
        gamePane.heightProperty().addListener((obs, oldVal, newVal) -> centerObjectAnchor(gamePane, fieldPane, false, true));
        gamePane.widthProperty().addListener((obs, oldVal, newVal) -> centerObjectAnchor(gamePane, fieldPane, true, false));

        teamA = new Team("Wolves", TEAM_SIZE, "/media/team_one.png", "/media/red_dot_arrow.png");
        teamB = new Team("Skull", TEAM_SIZE, "/media/team_two.png", "/media/blue_dot_arrow.png");

        IDA.setText(Integer.toString(1));
        IDB.setText(Integer.toString(1));

        ball = new Ball();

        matchBoard.setText(teamA.getTeamName() + " V.S " + teamB.getTeamName());

        logoA = new ImageView(teamA.getLogo());
        logoB = new ImageView(teamB.getLogo());

        for (int i = 0; i < TEAM_SIZE; i++) {
            ImageView view = new ImageView(teamA.getPlayer(i).getIcon());
            view.setOpacity(NONE_FOCUS_OPACITY);
            fieldPane.getChildren().add(view);
        }

        for (int i = 0; i < TEAM_SIZE; i++) {
            ImageView view = new ImageView(teamB.getPlayer(i).getIcon());
            view.setOpacity(NONE_FOCUS_OPACITY);
            fieldPane.getChildren().add(view);
        }

        ImageView view = new ImageView(ball.getIcon());
        fieldPane.getChildren().add(view);

        if (isSimulated) {
            String teamAPath = getClass().getResource("/scripts/CAMBADA-TUE/Team.A.msl").toExternalForm().substring(5);
            String teamBPath = getClass().getResource("/scripts/CAMBADA-TUE/Team.B.msl").toExternalForm().substring(5);

            try {
                String teamAString = Files.readString(Paths.get(teamAPath));
                Iterator<JsonNode> teamAData = new ObjectMapper().readTree(teamAString).elements();
                String teamBString = Files.readString(Paths.get(teamBPath));
                Iterator<JsonNode> teamBData = new ObjectMapper().readTree(teamBString).elements();

                if (teamAData.hasNext()) {
                    updateVisuals(teamAData.next().toString(), teamA, 0);
                }
                if (teamBData.hasNext()) {
                    updateVisuals(teamBData.next().toString(), teamB, TEAM_SIZE);
                }

                Timeline updateUI = new Timeline(
                        new KeyFrame(Duration.millis(50d), event -> {
                            if (teamAData.hasNext()) {
                                updateVisuals(teamAData.next().toString(), teamA, 0);
                            }
                            if (teamBData.hasNext()) {
                                updateVisuals(teamBData.next().toString(), teamB, TEAM_SIZE);
                            }
                        }));
                updateUI.setCycleCount(Timeline.INDEFINITE);
                updateUI.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
