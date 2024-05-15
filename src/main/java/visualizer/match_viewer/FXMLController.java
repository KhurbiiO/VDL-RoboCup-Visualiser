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
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class FXMLController implements Initializable {

    private final int TEAM_SIZE = 5;
    private final int DIGITAL_MEASUREMENTS_RATIO = 15;
    private final double FIELD_WIDTH = 25d;
    private final double FIELD_HEIGHT = 25d;
    private boolean isSimulated = true;
    private Team teamA;
    private Team teamB;
    private Ball ball;

    CoordsTransformation transformer = new CoordsTransformation();

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
    Label matchBoard;

    public void updateScreenCoordinate(ImageView view, double[] newPose){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(view);
        transition.setDuration(Duration.millis(100));
        transition.setToX(newPose[0]);
        transition.setToY(newPose[1]);
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

    private void updateVisuals(String data, Team team, int indexShift){
        try{
            ObjectMapper objectMapper = new ObjectMapper(); 
            JsonNode jsonNode = objectMapper.readTree(data);

            Iterator<JsonNode> robots = jsonNode.get("worldstate").get("robots").elements();

            // TODO: Add ball position estimation
            for (int i = 0; i < TEAM_SIZE; i++) {
                JsonNode robot = robots.next();
                Robot player = team.getPlayer(i);
                
                player.setBallEngaged(robot.get("ballEngaged").asBoolean());
                player.setBatterPercentage(robot.get("batteryLevel").asDouble());
                player.setIntention(robot.get("intention").asText());
                
                Iterator<JsonNode> currentPose_ = robot.get("pose").elements();
                double currentPose[] = {currentPose_.next().asDouble(), currentPose_.next().asDouble(), currentPose_.next().asDouble()};
                if (indexShift > 0){
                    currentPose = transformer.transformRobotCoords(currentPose);
                }
                player.setCurrentPosition(currentPose[0], currentPose[1], currentPose[2]);

                Iterator<JsonNode> targetPose_ = robot.get("target pose").elements();
                double targetPose[] = {targetPose_.next().asDouble(), targetPose_.next().asDouble(), targetPose_.next().asDouble()};

                if (indexShift > 0){
                    currentPose = transformer.transformRobotCoords(targetPose);
                }
                player.setTargetPosition(targetPose[0], targetPose[1], targetPose[2]); //TODO: Look into the necesity of a fluctuations counter
                // TODO: Add velocity reader

                double screenPose[] = player.currentPosition.getGridVVector(FIELD_WIDTH, FIELD_HEIGHT, fieldPane.getWidth(), fieldPane.getHeight());
                
                ImageView view = getImageViewFromPane(i + indexShift);
                view.setFitHeight(fieldPane.getWidth()/DIGITAL_MEASUREMENTS_RATIO);
                view.setFitWidth(fieldPane.getWidth()/DIGITAL_MEASUREMENTS_RATIO);
                updateScreenCoordinate(view, screenPose);
            }

            // Semi hard coded
            if (indexShift == 0){
                try{
                    Iterator<JsonNode> balls = jsonNode.get("worldstate").get("balls").elements();
                    JsonNode ball_ = balls.next();
                    Iterator<JsonNode> currentPose_ = ball_.get("position").elements();

                    ball.currentPosition.setX(currentPose_.next().asDouble());
                    ball.currentPosition.setY(currentPose_.next().asDouble());
                    ball.currentPosition.setZ(currentPose_.next().asDouble());

                    ball.setConfidence(ball_.get("confidence").asDouble());

                    double currentPose[] = ball.currentPosition.getGridVVector(FIELD_WIDTH, FIELD_HEIGHT, fieldPane.getWidth(), fieldPane.getHeight());
                    

                    ImageView view = getImageViewFromPane(2*TEAM_SIZE);
                    view.setFitHeight(fieldPane.getWidth()/DIGITAL_MEASUREMENTS_RATIO);
                    view.setFitWidth(fieldPane.getWidth()/DIGITAL_MEASUREMENTS_RATIO);
                    updateScreenCoordinate(view, currentPose);
                }

                catch(NoSuchElementException e){}
            }

        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        teamA = new Team("Wolves", TEAM_SIZE, "/media/team_one.png", "/media/red_dot.png");
        teamB = new Team("Skull", TEAM_SIZE, "/media/team_two.png", "/media/blue_dot.png");

        ball = new Ball();

        matchBoard.setText(teamA.getTeamName()+" V.S "+teamB.getTeamName());

        logoA = new ImageView(teamA.getLogo());
        logoB = new ImageView(teamB.getLogo());

        for (int i = 0; i < TEAM_SIZE; i++){
            ImageView view = new ImageView(teamA.getPlayer(i).getIcon());            
            fieldPane.getChildren().add(view);
        }

        for (int i = 0; i < TEAM_SIZE; i++){
            ImageView view = new ImageView(teamB.getPlayer(i).getIcon());            
            fieldPane.getChildren().add(view);
        }

        ImageView view = new ImageView(ball.getIcon());
        fieldPane.getChildren().add(view);

        // TODO: Start update data stream threads

        
        if (isSimulated){
            // String teamAPath = "/home/khurbii/Documents/Fontys_S6/RoboCup/UX_Prototypes/Components/20180621_142627_CAMBADA-TUE/20180621_142627.A.msl";
            String teamAPath = getClass().getResource("/scripts/CAMBADA-TUE/Team.A.msl").toExternalForm().substring(5); //TODO: Remove hardcode substring
            // String teamBPath = "/home/khurbii/Documents/Fontys_S6/RoboCup/UX_Prototypes/Components/20180621_142627_CAMBADA-TUE/20180621_142627.B.msl";
            String teamBPath = getClass().getResource("/scripts/CAMBADA-TUE/Team.B.msl").toExternalForm().substring(5);

            try {
                String teamAString = Files.readString(Paths.get(teamAPath));
                Iterator<JsonNode> teamAData = new ObjectMapper().readTree(teamAString).elements();
                String teamBString = Files.readString(Paths.get(teamBPath));
                Iterator<JsonNode> teamBData = new ObjectMapper().readTree(teamBString).elements();

                Timeline updateUI = new Timeline(
                    new KeyFrame(Duration.millis(10d), event -> {
                        if(teamAData.hasNext()){
                            updateVisuals(teamAData.next().toString(), teamA, 0);
                        }
                        if(teamBData.hasNext()){
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
